package linearAlgebra;

import java.util.Scanner;

public class Inverse {
	// Class operasi inverse
	public static Matrix matrixCofactor(Matrix arr){
		// Membuat matriks kofaktor dari Matrix arr
		Matrix mNew = new Matrix(arr.getRow(),arr.getCol());
        for (int i = 0; i < arr.getRow(); ++i){
            for (int j = 0; j < arr.getCol(); ++j){
            	Matrix temp = new Matrix(arr.getRow()-1,arr.getCol()-1);
                int iTemp = 0;
                int jTemp = 0;
                //Mengisi temp
                for (int brs = 0; brs < arr.getRow(); ++brs){
                    for (int kol = 0; kol < arr.getCol(); ++kol){
                        if ((brs != i) && (kol != j)){
                        	temp.setElmt(iTemp, jTemp, arr.getElmt(brs, kol));
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

    public static Matrix adjoint(Matrix arr){
        return Matrix.transpose(matrixCofactor(arr));
    }
    
	public static Matrix inverseAdjoint(Matrix arr){
		// Menghasilkan inverse Matriks dengan metode adjoint
        double det = Determinant.determinanReduction(arr);
        if (det != 0) {
        	Matrix mNew = adjoint(arr);
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
	
	public static Matrix inverseGaussJordan(Matrix m) {
    	// Prosedur inverse memakai eliminasi Gauss-Jordan, diasumsikan m adalah matriks persegi
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
    	GaussElimination.forwardElimination(addedIdentity,true);
    	GaussJordan.backwardElimination(addedIdentity);
    	
    	// Ada baris yang 0 (matriks tidak memiliki inverse
    	for (int i=addedIdentity.getRow()-1; i>=0; i--) {
    		if (GaussElimination.zeroRow(addedIdentity,i,false,true)) {
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

    public static Matrix solveSPLInverse(){
    	// Menyelesaikan SPL memakai inverse matrix
    	// Menerima input matriks arr
    	Matrix arr = new Matrix();
    	boolean fromFile;
    	
    	Scanner input = new Scanner(System.in);
    	
    	// Menerima masukan jumlah persamaan, jumlah variabel, dan isi SPL
    	System.out.println("Masukan dari file ?");
    	fromFile = input.nextBoolean();
    	if (fromFile) {
    		String fileName;
    		System.out.println("Masukkan path file");
    		fileName = input.next();
    		arr.readMatrix(fileName);
    	}
    	else {
    		System.out.println("Masukkan banyak persamaan");
        	int jumPersamaan = input.nextInt();
        	System.out.println("Masukkan banyak variabel");
        	int jumVariabel = input.nextInt();
        	System.out.println("Masukan semua koefisien per persamaan diikuti hasil tiap persamaan di akhir");
        	arr.readMatrix(jumPersamaan,jumVariabel+1);
        }
    	input.close();
    	
        int brs = arr.getRow();
        int kol = arr.getCol();
        Matrix A = new Matrix(brs,kol-1);
        Matrix invA;
        Matrix B = new Matrix(brs,1);
        Matrix result;
        for (int i = 0; i < brs; ++i){
            for (int j = 0; j < kol - 1; ++j){
            	A.setElmt(i, j, arr.getElmt(i, j));
            }
        }

        for (int i = 0; i < brs; ++i){
        	B.setElmt(i, 0, arr.getElmt(i, kol-1));
        }
        
        invA = inverseGaussJordan(A);
        if (invA != null) {
        	result = Matrix.multiplyMatrix(invA, B);
        	for (int i=0; i<result.getRow(); i++) {
        		System.out.println("x"+(i+1)+" = "+result.getElmt(i, 0));
        	}
        	return result;
        }
        else {
        	return null;
        }
    }
}
