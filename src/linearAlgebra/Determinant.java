package linearAlgebra;

import java.util.*;

public class Determinant {
	/** Class operasi determinan */
	static Matrix inputDeterminant(Scanner input) {
		/** Input matriks untuk persoalan determinan */
    	Matrix m = new Matrix();
    	
    	// Menerima masukan jumlah persamaan, jumlah variabel, dan isi SPL
    	System.out.println("Masukan dari file ? Y/N");
		if (input.next().equals("Y")) {
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
	
	static void minor(Matrix m,Matrix temp, int a, int b){
		/** Membuat matriks minor dari matriks m */
		int N = m.getRow();
        int i =0;
        int j = 0;
        for (int k = 0 ; k < N ; k++){
            for (int l = 0 ; l < N; l++){
                if ((k != a) && (l != b)){
                	temp.setElmt(i,j++, m.getElmt(k, l));
                    if (j == N-1){
                        j = 0;
                        i++;
                    }
                }
            }
        }
    }
	
	public static double determinanCofactor (Scanner input) {
		/** Menerima input matriks lalu mengembalikan hasil determinannya dengan metode kofaktor */
		Matrix m = inputDeterminant(input);
		return determinanCofactor(m);
	}

    public static double determinanCofactor(Matrix m){
    	/** Menghasilkan determinan matriks m dengan metode ekspansi kofaktor, diasumsikan m matriks persegi dan relatif kecil (ukuran kurang dari 10x10) */
    	int N = m.getRow();
    	if (N==1) {
    		return m.getElmt(0, 0);
    	}
    	else if (N==2){
            return ((m.getElmt(0, 0) * m.getElmt(1, 1)) - (m.getElmt(0, 1) * m.getElmt(1, 0))) ;
        }
        else{
            double det = 0;
            Matrix temp = new Matrix(N-1,N-1);
            for (int k=0; k < N; k++){
                minor(m,temp,0,k);
                det += Math.pow(-1, k) * m.getElmt(0, k) * determinanCofactor(temp);
            }
            return det;
        }

    }
    
    static int detRowReduction(Matrix m){
    	/** Mengubah matriks m menjadi matriks segitiga atas */
    	int N = m.getRow();
        int i = 0, l = 0, idx;
        double pengurang;
        int count = 0;
        for (int j = 1; j < N; j++){
            for (int h = j; h < N; h++){
                idx = i+1;
                while ((m.getElmt(i, l) == 0) && (idx < N)){
                    Matrix.swap(m,i,idx);
                    count++;
                    idx++;
                }
                if (idx == N){
                    continue;}
                pengurang = m.getElmt(h, l) / m.getElmt(i,l);
                for (int k = l; k < N; k++){
                	m.setElmt(h, k, m.getElmt(h, k)-m.getElmt(i,k) * pengurang);
                	if (Math.abs(m.getElmt(h, k)) < 1E-10) {
                		m.setElmt(h, k, 0);
                	}
                }
            }
            i++;
            l++;
        }
        return count;
    }
    
    public static double determinanReduction(Scanner input) {
    	/** Menerima input matriks lalu menghasilkan determinannya dengan metode reduksi baris */
    	Matrix m = inputDeterminant(input);
    	return determinanReduction(m);
    }
    
    public static double determinanReduction(Matrix m){
    	/** Menghasilkan determinan matrix m dengan metode reduksi baris, diasumsikan m matriks persegi */
    	Matrix temp = Matrix.copyMatrix(m);
    	int N = m.getRow();
        int count = detRowReduction(temp);
        double det = temp.getElmt(0, 0);
        for (int i = 1; i < N; i++){
            det *= temp.getElmt(i, i);
        }
        if (count % 2 == 0){
            return det;
        } else{
            return -det;
        }
    }
}
