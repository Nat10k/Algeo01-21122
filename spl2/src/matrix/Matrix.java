package matrix;
import java.util.*;

public class Matrix {
	// Atribut
	float[][] mtrx; // Bagian matrix
	int row,col; // Banyak baris dan kolom matrix
	final int IDX_UNDEF = -1;
	
	public Matrix(int nRow, int nCol) {
		row = nRow;
		col = nCol;
		mtrx = new float[row][col];
		Scanner input = new Scanner(System.in);
		for (int i=0; i<row;i++) {
			System.out.println("Masukkan koefisien-koefisien persamaan ke-" + i);
			for (int j=0; j<col-1;j++) {
				mtrx[i][j] = input.nextFloat();
			}
		}
		
		for (int i=0; i<row; i++) {
			System.out.println("Masukkan hasil persamaan ke-" + i);
			mtrx[i][col-1] = input.nextFloat();
		}
		input.close();
	}
	
	public boolean isIdxValid(Matrix m, int i, int j) {
		// Menentukan apakah indeks i,j valid untuk matriks
		return (i >= 0 && i < m.row && j >= 0 && j < m.col);
	}
	
	public float getElmt(Matrix m, int i, int j) {
		// Mengembalikan elemen matriks di indeks i,j, diasumsikan indeks valid
		return m.mtrx[i][j];
	}
	
	public void setElmt(Matrix m, int i, int j, float val) {
		// Mengubah elemen matriks di indeks i,j dengan nilai val, diasumsikan indeks valid
		m.mtrx[i][j] = val;
	}
	
	private void MakeSatuUtama(Matrix m) {
		// Melakukan eliminasi Gauss pada Matrix m
		for (int i=0;i<m.row;i++) {
			m.mtrx[i][i] /= getElmt(m,i,i);
			for (int j=i+1;j<m.col;j++) {
				
			}
		}
	}

	public static void main(String[] args) {
		Matrix mat = new Matrix(3,3);
		for (int i=0; i<mat.row;i++) {
			for (int j=0; j<mat.col;j++) {
				System.out.print(mat.mtrx[i][j] + " ");
			}
			System.out.println();
		}

	}

}
