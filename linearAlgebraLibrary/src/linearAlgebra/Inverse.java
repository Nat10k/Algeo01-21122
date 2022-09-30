package linearAlgebra;

import java.util.Scanner;

public class Inverse {
	/** Class operasi inverse */
	static Matrix inputInverse(Scanner input) {
		/** Fungsi untuk input matriks persoalan inverse matriks */
		boolean fromFile;
    	Matrix m = new Matrix();
    	
    	// Menerima masukan jumlah persamaan, jumlah variabel, dan isi SPL
    	System.out.println("Masukan dari file ?");
    	fromFile = input.nextBoolean();
    	if (fromFile) {
    		String fileName;
    		System.out.println("Masukkan path file");
    		fileName = input.next();
    		m.readMatrix(fileName);
    	}
    	else {
    		System.out.println("Masukkan dimensi matriks");
        	int n = input.nextInt();
        	m.readMatrix(n, n, input);
        }
    	return m;
	}
	
	public static Matrix matrixCofactor(Matrix m){
		/** Membuat matriks kofaktor dari Matrix m */
		Matrix mNew = new Matrix(m.getRow(),m.getCol());
        for (int i = 0; i < m.getRow(); ++i){
            for (int j = 0; j < m.getCol(); ++j){
            	Matrix temp = new Matrix(m.getRow()-1,m.getCol()-1);
                int iTemp = 0;
                int jTemp = 0;
                //Mengisi temp
                for (int brs = 0; brs < m.getRow(); ++brs){
                    for (int kol = 0; kol < m.getCol(); ++kol){
                        if ((brs != i) && (kol != j)){
                        	temp.setElmt(iTemp, jTemp, m.getElmt(brs, kol));
                            jTemp += 1;
                            if (jTemp == temp.getRow()){
                                jTemp = 0;
                                iTemp += 1;
                            }
                        }
                    }
                }
                mNew.setElmt(i, j, (Math.pow(-1, (i + j)) *  Determinant.determinanReduction(temp)));
            }
        }
        return mNew;
    }

    public static Matrix adjoint(Matrix m){
    	/** Menghasilkan matriks adjoin dari matriks m */
        return Matrix.transpose(matrixCofactor(m));
    }
    
    public static Matrix inverseAdjoint(Scanner input) {
    	/** Menerima input matriks lalu menghasilkan inversenya dengan metode adjoin */
    	Matrix m = inputInverse(input);
    	return inverseAdjoint(m);
    }
    
	public static Matrix inverseAdjoint(Matrix m){
		/** Menghasilkan inverse matriks dengan metode adjoint, diasumsikan m matriks persegi */
        double det = Determinant.determinanReduction(m);
        if (det != 0) {
        	Matrix mNew = adjoint(m);
            for (int i = 0; i < mNew.getRow(); ++i){
                for (int j = 0; j < mNew.getCol(); ++j){
                	mNew.setElmt(i, j, mNew.getElmt(i, j)/det);
                }
            }
            return mNew;
        }
        else {
        	System.out.println("Matriks tidak memiliki inverse");
        	return null;
        }
    }
	
	public static Matrix inverseGaussJordan(Scanner input) {
		/** Menerima input matriks lalu menghasilkan inverse matriks dengan metode Gauss-Jordan */
		Matrix m = inputInverse(input);
    	return inverseGaussJordan(m);
	}
	
	public static Matrix inverseGaussJordan(Matrix m) {
    	/** Prosedur inverse memakai eliminasi Gauss-Jordan, diasumsikan m adalah matriks persegi */
    	Matrix addedIdentity = Matrix.copyMatrix(m);
    	Matrix inverseM;
    	boolean invertible = true;
    	
    	// Mengaugment matriks identitas pada matriks m
    	for (int i=0; i<m.getRow(); i++) {
    		for (int j=0; j<m.getCol(); j++) {
    			if (i==j) {
    				addedIdentity.addElmt(i, 1);
    			}
    			else {
    				addedIdentity.addElmt(i, 0);
    			}
    		}
    	}
    	
    	// Eliminasi Gauss-Jordan
    	SPL.forwardElimination(addedIdentity,true);
    	SPL.backwardElimination(addedIdentity);
    	
    	// Ada baris yang 0 (matriks tidak memiliki inverse
    	for (int i=addedIdentity.getRow()-1; i>=0; i--) {
    		if (SPL.zeroRow(addedIdentity,i,false,true)) {
    			System.out.println("Matriks tidak memiliki inverse");
    			invertible = false;
    			break;
    		}
    	}
    	
    	if (invertible) {
	    	// Membuat matriks inverse
    		inverseM = new Matrix(m.getRow(),m.getCol());
	    	for (int i=0; i<m.getRow(); i++) {
	    		for (int j=0; j<m.getCol(); j++) {
	    			inverseM.setElmt(i, j, addedIdentity.getElmt(i, j+m.getCol()));
	    		}
	    	}
	    	return inverseM;
    	}
    	else {
    		return null;
    	}
    }
}
