package linearAlgebra;

import java.util.Scanner;

public class MultiLinearRegression {
	public static void multiRegression() {
		// Melakukan regresi linear berganda
		
		// Menerima banyak variabel dan jumlah sample
		int n,m;
		double sum, hasilK;
		double[] target;
		boolean fromFile, isResultZero;
		Matrix data = new Matrix();
		
		Scanner input = new Scanner(System.in);
		System.out.println("Masukan dari file ?");
		fromFile = input.nextBoolean();
		if (fromFile) { // Menerima masukan dari file, nilai Xk yang ingin dicari diasumsikan ada di baris terakhir
			System.out.println("Masukkan path file");
			String fileName = input.next();
			data.readMatrix(fileName);
			n = data.getCol()-1;
			m = data.getRow();
			// Menerima masukan target
			target = new double[n];
			System.out.println("Masukkan nilai-nilai peubah yang ingin dicari hasilnya secara berurutan");
			for (int i = 0; i<n; i++) {
				target[i] = input.nextDouble();
			}
		} 
		else { // Menerima masukan data dari keyboard
			System.out.println("Masukkan banyak peubah");
			n = input.nextInt();
			System.out.println("Masukkan banyak sample");
			m = input.nextInt();
			// Menerima masukan target
			target = new double[n];
			System.out.println("Masukkan nilai-nilai peubah yang ingin dicari hasilnya secara berurutan");
			for (int i = 0; i<n; i++) {
				target[i] = input.nextDouble();
			}
			System.out.println("Masukkan semua data per sample, kolom terakhir sebagai hasilnya");
			data.readMatrix(m, n+1);
		}
		input.close();
		
		// Membuat matriks SPL regresi
		Matrix equations = new Matrix(data.getCol(), data.getCol()+1);
		
		for (int i=0;i<equations.getRow();i++) {
			for (int j=0; j<equations.getCol();j++) {
				if (i == 0 && j == 0) { // Koefisien B0 pertama
					equations.setElmt(i, j, (double) data.getRow());
				} 
				else if (i==0) { // SPL baris pertama
					sum = 0;
					for (int k=0; k<data.getRow();k++) {
						sum += data.getElmt(k,j-1);
					}
					equations.setElmt(i, j, sum);
				}
				else if (j==0) { // SPL kolom pertama
					sum = 0;
					for (int k=0; k<data.getRow();k++) {
						sum += data.getElmt(k,i-1);
					}
					equations.setElmt(i, j, sum);
				}
				else {
					sum = 0;
					for (int k=0; k<data.getRow();k++) {
						sum += data.getElmt(k, i-1) * data.getElmt(k,j-1);
					}
					equations.setElmt(i, j, sum);
				}
			}
		}
		
		// Mencetak persamaan hasil
		double[] result = GaussElimination.gaussElim(equations);
		// Mengecek apakah ada hasil/tidak
		isResultZero = true;
		for (int i=0; i<result.length; i++) {
			if(result[i] != 0) {
				isResultZero = false;
				break;
			}
		}
		if (!isResultZero) {
			String persamaan = "f(x) = " + result[0];
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
			System.out.println(persamaan);
			hasilK = result[0];
			String asked = ""+target[0];
			for (int i=0;i<target.length;i++) {
				hasilK += target[i]*result[i+1];
				if (i > 0) {
					asked += "," + target[i];
				}
			}
			System.out.println("f(" + asked + ") = " + hasilK);
		}
	}
}
