package linearAlgebra;

import java.util.Scanner;

public class Bicubic {
	/** Class persoalan interpolasi bicubic */
	static Matrix inputFBicubic(Scanner input) {
		/** Membaca matriks nilai Fij dari file */
		String fileName;
		Matrix inputMtrx = new Matrix();
		System.out.println("Masukkan path file");
		fileName = input.next();
		inputMtrx.readMatrix(fileName);
		return inputMtrx;
	}
	
	public static void bicubic(Scanner input) {
		/** Membaca matriks nilai Fij dari file lalu mengembalikan hasil interpolasi bicubicnya */
		Matrix inputMtrx = inputFBicubic(input);
		System.out.println("Simpan hasil ke dalam file ? Y/N");
		if (input.next().equals("Y")) { // Hasil disimpan ke file
			System.out.println("Masukkan nama file output");
			String outputFile = input.next();
			bicubic(inputMtrx,outputFile);
		}
		else { // Hasil hanya ditampilkan ke layar
			bicubic(inputMtrx,null);
		}
	}
	
	public static void bicubic(Matrix inputMtrx, String outputFile) {
		/** Melakukan interpolasi bicubic dengan menerima matrix hasil f(x,y) (inputMtrx) berukuran 4x4 */
		int x,y,pangkatX,pangkatY; // x = integer [-1..2], y = integer [-1..2], pangkatX = integer [0..3], pangkatY = integer [0..3]
		Matrix xMat = new Matrix(16,16); // Matriks koefisien X
		Matrix nilaiF = new Matrix(16,1);
		Matrix inputTranspose;
		double[] nilaiAB = new double[2]; // Array penampung nilai A,B yang ingin dicari hasil f(A,B) nya
		Matrix arrayA; // Array penampung nilai aij
		double result;
		
		// Menyimpan nilai a,b yang ingin dicari ke array nilaiAB
		for (int i=0; i<nilaiAB.length; i++) {
			nilaiAB[i] = inputMtrx.getElmt(inputMtrx.getRow()-1, i);
		}
		inputMtrx.mtrx.get(inputMtrx.getRow()-1).clear();
		inputMtrx.row--;
		
		// Memasukkan nilai f(x,y) dari inputMtrx ke matrix nilaiF 
		inputTranspose = Matrix.transpose(inputMtrx);
		int newRowNilaiF = 0;
		for (int i=0; i<4; i++) {
			for (int j=0; j<4; j++) {
				nilaiF.setElmt(newRowNilaiF,0,inputTranspose.getElmt(i, j));
				newRowNilaiF++;
			}
		}
		
		// Inisialisasi nilai x dan y 
		x = -1;
		y = -1;
		// Mengisi matriks koefisien x
		for (int i=0; i<xMat.getRow(); i++) {
			pangkatX = 0;
			pangkatY = 0;
			for (int j=0; j<xMat.getCol(); j++) {
				xMat.setElmt(i,j,Math.pow(x,(pangkatX))*Math.pow(y,(pangkatY)));
				if (pangkatX == 3) {
					pangkatX = 0;
					pangkatY++;
				} else {
					pangkatX++;
				}
			}
			// Ubah nilai x
			if (x == 2) {
				x = -1;
			} else {
				x++;
			}
			// Ubah nilai y
			if (i % 4 == 3) { // Sudah baris kelipatan 4
				y++;
			}
		}
		// Mengisi matriks a dari hasil perkalian matrix X dengan matrix inputMtrx
		arrayA = Matrix.multiplyMatrix(Inverse.inverseGaussJordan(xMat),nilaiF);
		result = 0;
		for (int i=0; i<arrayA.getRow(); i++) {
			result += arrayA.getElmt(i, 0)*Math.pow(nilaiAB[0],i%4)*Math.pow(nilaiAB[1],Math.floorDiv(i, 4));
		}
		System.out.println("f("+nilaiAB[0]+","+nilaiAB[1]+") = "+result);
		if (outputFile != null) {
			FileOutput.printFile(outputFile, "f("+nilaiAB[0]+","+nilaiAB[1]+") = "+result);
		}
	}
}
