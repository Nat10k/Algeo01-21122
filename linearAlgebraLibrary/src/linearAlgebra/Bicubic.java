package linearAlgebra;

import java.util.Scanner;

public class Bicubic {
	public static void bicubic() {
		// Melakukan interpolasi bicubic dengan menerima matrix hasil f(x,y) berukuran 4x4
		int x,y,pangkatX,pangkatY; // x = integer [-1..2], y = integer [-1..2], pangkatX = integer [0..3], pangkatY = integer [0..3]
		Matrix xMat = new Matrix(); // Matriks koefisien X
		Matrix inputMtrx = new Matrix(); // Array nilai f(i,j), i = integer[-1..2], j = integer[-1..2]
		Matrix nilaiF = new Matrix();
		double[] nilaiAB = new double[2]; // Array penampung nilai A,B yang ingin dicari hasil f(A,B) nya
		Matrix arrayA = new Matrix(); // Array penampung nilai aij
		String fileName;
		Scanner input = new Scanner(System.in);
		
		// Menerima nama file dan membaca array inputMtrx dari file
		System.out.println("Masukkan path file");
		fileName = input.next();
		inputMtrx.readMatrix(fileName);
		input.close();
		
		// Menyimpan nilai a,b yang ingin dicari ke array nilaiAB
		for (int i=0; i<nilaiAB.length; i++) {
			nilaiAB[i] = inputMtrx.getElmt(inputMtrx.getRow()-1, i);
		}
		inputMtrx.mtrx.get(inputMtrx.getRow()-1).clear();
		inputMtrx.row--;
		
		// Memasukkan nilai f(x,y) dari inputMtrx ke matrix nilaiF 
		while (nilaiF.row < 16) {
			nilaiF.addRow();
			nilaiF.row++;
		}
		System.out.println(nilaiF.getRow());
		nilaiF.setCol(1);
		int newRowNilaiF = 0;
		for (int i=0; i<4; i++) {
			for (int j=0; j<4; j++) {
				nilaiF.addElmt(newRowNilaiF, inputMtrx.getElmt(i, j));
				newRowNilaiF++;
			}
		}
		
		
		// Inisialisasi matrix X
		xMat.setRow(16);
		xMat.setCol(16);
		// Inisialisasi nilai x dan y 
		x = -1;
		y = -1;
		// Mengisi matriks koefisien x
		for (int i=0; i<xMat.getRow(); i++) {
			xMat.addRow();
			pangkatX = 0;
			pangkatY = 0;
			for (int j=0; j<xMat.getCol(); j++) {
				xMat.addElmt(i,Math.pow(x,(pangkatX))*Math.pow(y,(pangkatY)));
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
		xMat.displayMatrix();
		
		// Mengisi matriks a dari hasil perkalian matrix X dengan matrix inputMtrx
		// Matrix.inverse(xMat);
//		arrayA = multiplyMatrix(inverse(xMat),nilaiF);
//		arrayA.displayMatrix();
	}
}
