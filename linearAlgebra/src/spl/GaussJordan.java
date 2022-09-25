package spl;
import java.util.*;
import matrix.Matrix;

public class GaussJordan{
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        int row,col;
        row = input.nextInt();
        col = input.nextInt();
        Matrix m = new Matrix();
        double [] a = new double[row];
        for(int i= 0; i<row; i++){
            for(int j = 0; j<col; j++){
                m.setElmt(i, j,input.nextDouble());
            }
        }
        a = gaussjordanElim(m, true);
        for(int i= 0; i<row; i++){
            System.out.println(a[i]);
        }
        input.close();


    }

    private static void forwardElimination(Matrix m) {
		// Membuat matriks m menjadi matriks eselon baris
		int row_max;
		double max, factor;
		
		for (int k=0;k<m.getRow();k++) {
		    for (int r=k; r<m.getCol()-1; r++) {
		    	// Mencari nilai terbesar dari tiap baris pada kolom koefisien yang bersangkutan
				row_max = k;
		        max = m.getElmt(row_max, r);
		        for (int i=k+1;i<m.getRow();i++) {
		            if(Math.abs(m.getElmt(i, r)) > max) {
		                row_max = i;
		                max = m.getElmt(i, r);
		            }
		        }
		        
		        if (max != 0) { // Jika kolom seluruhnya berisi 0, jangan lakukan pembagian maupun pertukaran baris, lanjut baris berikutnya
		        	if (row_max != k){ // Elemen terbesar ada di baris selain k
			            Matrix.swap(m,row_max,k);
			        }
		        	
		        	// Ubah elemen tidak nol pertama baris maksimal menjadi 1 utama, bagi seluruh baris dengan elemen pertama
		        	for (int j=r;j<m.getCol();j++) {
		        		m.setElmt(k, j, m.getElmt(k, j)/max);
		        	}
		
			        // Kurangi tiap baris di bawah baris k
			        for (int i=k+1;i<m.getRow();i++) {
			        	factor = m.getElmt(i, r)/m.getElmt(k, r);
			            for (int j=r;j<m.getCol();j++) {
			            	m.setElmt(i, j, (m.getElmt(i, j)-(m.getElmt(k,j)*factor)));
			            	if (Math.abs(m.getElmt(i, j)) < 1E-10) {
			            		m.setElmt(i, j, 0);
			            	}
			            }
			        }
			        break;
		        }
		    }
		}
	}
	
	private static boolean zeroRow(Matrix m, int rowIdx, boolean SPL) {
		// Mengembalikan true jika baris rowIdx berisi 0 saja (untuk non-SPL) atau semua koefisien 0 (untuk SPL)
		if (SPL) {
			boolean coefZero = true;
			int j = 0;
			while (j < m.getCol()-1 && coefZero) {
				if (m.getElmt(rowIdx, j) != 0) {
					coefZero = false;
				} else {
					j++;
				}
			}
			return coefZero;
		} else {
			boolean allZero = true;
			int j = 0;
			while (j < m.getCol() && allZero) {
				if (m.getElmt(rowIdx, j) != 0) {
					allZero = false;
				} else {
					j++;
				}
			}
			return allZero;
		}
	}
	
    public static double[] gaussjordanElim (Matrix m, boolean SPL){
        // Fungsi eliminasi Gauss Jordan, m matrix augmented
        boolean noSolution, manySolution;
        int firstNonZero = -1;

        // Membuat matriks segitiga atas
        forwardElimination(m);
        
        // Array penampung hasil
        double[] a = new double[m.getCol()-1];
        
        // Memeriksa ada/tidak adanya baris matriks yang hanya terdiri dari 0
        noSolution = false;
        manySolution = false;
        for (int i=0; i<m.getRow();i++) {
        	if (zeroRow(m,i,true) && m.getElmt(i, m.getCol()-1) != 0) {
        		noSolution = true;
        		break;
        	}
        	else if (zeroRow(m,i,true) && m.getElmt(i, m.getCol()-1) == 0 || m.getCol() != m.getRow()+1) {
        		manySolution = true;
        	}
        }
        
        if (noSolution) { // SPL tidak memiliki solusi
        	System.out.println("SPL tidak memiliki solusi");
        }
        else {
        	// Inisialisasi array penampung hasil
        	for(int i=0; i < m.getCol()-1; i++){
                a[i]=0;
            }
        	
        	if (manySolution) { // SPL memiliki banyak solusi
        		String hasil;
        		for (int i=m.getRow()-1; i>=0;i--) {
        			if(!zeroRow(m,i,true)) {
        				// Cari elemen non-0 pertama pada baris
        				for (int j=0; j<m.getCol()-1; j++) {
        					if(m.getElmt(i, j) != 0) {
        						firstNonZero = j;
        						break;
        					}
        				}
        				
        				a[firstNonZero] = 1;
        				hasil = "x"+(firstNonZero+1) + " = ";
        				// Mencetak persamaan parametrik
        				for (int j=firstNonZero+1; j<m.getCol(); j++) {
        					if (j==m.getCol()-1) {
        						hasil += m.getElmt(i, j);
        					}
        					else if (m.getElmt(i, j) != 0){
        						hasil += (-1)*m.getElmt(i, j) + "x" + (j+1) + " + "; 
        					}
        				}
        				System.out.println(hasil);
        			}
        		}
        		
        		for (int i=0; i<m.getCol()-1;i++) { // Mengecek variabel x yang tidak memiliki nilai tertentu
        			if(a[i] == 0) {
        				System.out.println("x"+(i+1)+" = bilangan real");
        			}
        		}
        	}
        	else { // SPL memiliki solusi unik
        		// Backwards substitution
                double t;
                int i,j,k;
                for(j=0; j<m.getCol(); j++){
                    for(i=0; i<m.getRow(); i++)
                    {
                        if(i!=j)
                        {
                            t=m.getElmt(i, j)/m.getElmt(j, j);
                            for(k=0; k<m.getCol()+1; k++)
                            {
                                m.setElmt(i, k, (m.getElmt(i,k) - t*m.getElmt(j,k))); 
                            }
                        }
                    }
                }
                double[] hasil = new double[m.getRow()];
                for(i=0; i<m.getRow(); i++) 
                {
                    hasil[i]=m.getElmt(i, m.getCol())/m.getElmt(i, i);
                    // System.out.println("a"+ (i) + "= " + a[i]);
                }
                
                // for (int i = m.getCol()-2; i>=0; i--) {
                // 	if (i == m.getCol()-2) {
                // 		a[i] += m.getElmt(i, m.getCol()-1); 
                // 	}
                // 	else {
                // 		for (int j=i+1; j<m.getCol(); j++) {
                // 			if (j == m.getCol()-1) {
                // 				a[i] += m.getElmt(i, j);
                // 			} else {
                // 				a[i] -= m.getElmt(i, j) * (double) a[j];
                // 			}
                // 		}
                // 	}
                // 	if (Math.abs(a[i]) < 1E-10) {
                // 		a[i] = 0;
                // 	}
                // 	if (SPL) {
                // 		System.out.println("x" + (i+1) + " = " + a[i]);
                // 	}
                // }
        	}
        }
        return a;
    }
    }
