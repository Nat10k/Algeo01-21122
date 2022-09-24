package linearAlgebra;

import java.util.Scanner;
import spl.*;
import matrix.Matrix;

public class MultiLinearRegression {
	public static void multiRegression() {
		// Melakukan regresi linear berganda
		
		// Menerima banyak variabel dan jumlah sample
		int n,m;
		double sum, hasilK;
		double[] target;
		boolean fromFile;
		Matrix data = new Matrix();
		
		Scanner input = new Scanner(System.in);
		System.out.println("Masukan dari file ?");
		fromFile = input.nextBoolean();
		if (fromFile) { // Menerima masukan dari file, nilai Xk yang ingin dicari diasumsikan ada di baris terakhir
			System.out.println("Masukkan nama file");
			String fileName = input.next();
			data.readMatrix(fileName);
			
			// Mengisi array penampung elemen yang ingin dicari
			target = new double[data.getCol()-1];
			for (int j=0; j<data.getCol()-1; j++) { 
				target[j] = data.getElmt(data.getRow()-1, j);
			}
			data.mtrx.get(data.getRow()-1).clear();
			data.row--;
		} 
		else { // Menerima masukan data dari keyboard
			System.out.println("Masukkan banyak peubah x");
			n = input.nextInt();
			System.out.println("Masukkan banyak sample");
			m = input.nextInt();
			
			// Menerima masukan data
			System.out.println("Masukkan semua data per sample, kolom terakhir sebagai hasilnya");
			data.readMatrix(m, n+1);
			System.out.println("Masukkan nilai-nilai peubah yang ingin dicari hasilnya secara berurutan");
			target = new double[n];
			for (int i = 0; i<n; i++) {
				target[i] = input.nextDouble();
			}
		}
		input.close();
		
		// Membuat matriks SPL regresi
		Matrix equations = new Matrix();
		equations.setRow(data.getCol());
		equations.setCol(data.getCol()+1);
		
		for (int i=0;i<equations.getRow();i++) {
			equations.addRow();
			for (int j=0; j<equations.getCol();j++) {
				if (i == 0 && j == 0) { // Koefisien B0 pertama
					equations.addElmt(i, (double) data.getRow());
				} 
				else if (i==0) { // SPL baris pertama
					sum = 0;
					for (int k=0; k<data.getRow();k++) {
						sum += data.getElmt(k,j-1);
					}
					equations.addElmt(i,sum);
				}
				else if (j==0) { // SPL kolom pertama
					sum = 0;
					for (int k=0; k<data.getRow();k++) {
						sum += data.getElmt(k,i-1);
					}
					equations.addElmt(i,sum);
				}
				else {
					sum = 0;
					for (int k=0; k<data.getRow();k++) {
						sum += data.getElmt(k, i-1) * data.getElmt(k,j-1);
					}
					equations.addElmt(i,sum);
				}
			}
		}
		System.out.println("SPL dari Normal Estimation Equation for Multiple Linear Regression");
		equations.displayMatrix();
		
		// Mencetak persamaan hasil
		double[] result = GaussElimination.gaussElim(equations, false);
		String persamaan = "y = " + result[0];
		for (int i=1; i<result.length-1; i++) {
			if (result[i] > 0) {
				persamaan += " + ";
			}
			else {
				persamaan += " - ";
			}
			persamaan += Math.abs(result[i]) + "x" + i;
		}
		if (result[result.length-1] > 0) {
			persamaan += " + ";
		}
		else {
			persamaan += " - ";
		}
		persamaan += Math.abs(result[result.length-1]) + "x" + (result.length-1);
		System.out.println("Persamaan regresi : " + persamaan);
		hasilK = result[0];
		for (int i=0;i<target.length;i++) {
			hasilK += target[i]*result[i+1];
		}
		System.out.println("yk = " + hasilK);
	}
}
