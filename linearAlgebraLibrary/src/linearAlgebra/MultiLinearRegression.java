package linearAlgebra;

import java.util.Scanner;

public class MultiLinearRegression {
	/** Class persoalan regresi linier berganda */
	static String newline = System.getProperty("line.separator"); 
	
	static Matrix inputDataRegresi(Scanner input) {
		/** Menerima input data untuk regresi */
		boolean fromFile;
		int n,m;
		double[] target;
		Matrix data = new Matrix();
		
		System.out.println("Masukan dari file ?");
		fromFile = input.nextBoolean();
		if (fromFile) { // Menerima masukan dari file, nilai Xk yang ingin dicari diasumsikan ada di baris terakhir
			System.out.println("Masukkan path file");
			String fileName = input.next();
			data.readMatrix(fileName);
			n = data.getCol()-1;
			m = data.getRow();
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
			data.readMatrix(m, n+1, input);
		}
		return data;
	}
	
	static double[] inputTargetRegresi(Matrix data, Scanner input) {
		/** Menerima masukan target regresi */
		int n = data.getCol()-1;
		double[] target = new double[n];
		System.out.println("Masukkan nilai-nilai peubah yang ingin dicari hasilnya secara berurutan");
		for (int i = 0; i<n; i++) {
			target[i] = input.nextDouble();
		}
		return target;
	}
	
	public static void multiRegression(Scanner input) {
		/** Menerima masukan data dan target regresi dan mengembalikan persamaan dan hasilnya */
		Matrix data = inputDataRegresi(input);
		double[] target = inputTargetRegresi(data,input);
		System.out.println("Simpan hasil ke dalam file ? Y/N");
		if (input.next().equals("Y")) { // Hasil disimpan ke file
			System.out.println("Masukkan nama file output");
			String outputFile = input.next();
		    multiRegression(data,target,outputFile);
		}
		else { // Hasil ditampilkan ke layar saja
			multiRegression(data,target,null);
		}
	}
	
	public static void multiRegression(Matrix data, double[] target, String outputFile) {
		/** Melakukan regresi linear berganda berdasarkan data dan target */
		double sum, hasilK;
		boolean isResultZero;
		String hasil = "";
		
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
		double[] result = SPL.gaussElim(equations,false);
		// Mengecek apakah ada hasil/tidak
		isResultZero = true;
		for (int i=0; i<result.length; i++) {
			if(result[i] != 0) {
				isResultZero = false; // Regresi memiliki hasil
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
			hasil += persamaan+newline;
			hasilK = result[0];
			String asked = ""+target[0];
			for (int i=0;i<target.length;i++) {
				hasilK += target[i]*result[i+1];
				if (i > 0) {
					asked += "," + target[i];
				}
			}
			System.out.println("f(" + asked + ") = " + hasilK);
			hasil += "f(" + asked + ") = " + hasilK;
			if (outputFile != null) {
				FileOutput.printFile(outputFile, hasil);
			}
		}
	}
}
