package linearAlgebra;

import java.util.*;

public class GaussElimination {
	static void forwardElimination(Matrix m) {
		forwardElimination(m,false);
	}
	static void forwardElimination(Matrix m, boolean inv) {
		// Membuat matriks m menjadi matriks eselon baris
		int row_max, colLimit; // colLimit adalah pembatas kolom yang disearch untuk nilai max
		double max, factor;
		
		if (inv) {
			colLimit = m.getRow();
		}
		else {
			colLimit = m.getCol()-1;
		}
		
		for (int k=0;k<m.getRow();k++) {
		    for (int r=k; r<colLimit; r++) {
		    	// Mencari nilai terbesar dari tiap baris pada kolom r
				row_max = k;
		        max = m.getElmt(row_max, r);
		        for (int i=k+1;i<m.getRow();i++) {
		            if(Math.abs(m.getElmt(i, r)) > Math.abs(max)) {
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
	
	static boolean zeroRow(Matrix m, int rowIdx, boolean SPL) {
		return zeroRow(m, rowIdx, SPL, false);
	}
	
	static boolean zeroRow(Matrix m, int rowIdx, boolean SPL, boolean inv) {
		// Mengembalikan true jika baris rowIdx berisi 0 saja (untuk non-SPL) atau semua koefisien 0 (untuk SPL)
		boolean isZeroRow = true;
		int colLimit; // Batas kolom yang perlu dicari
		if (SPL) {
			colLimit = m.getCol()-1;
		} 
		else if (inv) { // Mengecek baris 0 untuk inverse matriks metode reduksi
			colLimit = m.getRow();
		}
		else {
			colLimit = m.getCol();
		}
		int j = 0;
		while (j < colLimit && isZeroRow) {
			if (m.getElmt(rowIdx, j) != 0) {
				isZeroRow = false;
			} else {
				j++;
			}
		}
		return isZeroRow;
	}
	
	public static double[] gaussElim () {
		// Overloader fungsi eliminasi Gauss
		return gaussElim(null);
	}
	
	public static double[] gaussElim (Matrix m){
    	// Fungsi eliminasi Gauss, m matrix augmented
        boolean noSolution, manySolution, SPL, fromFile;
        int firstNonZero = -1;
        
        // Jika tidak diberikan matrix m
        if (m == null) {
        	Scanner input = new Scanner(System.in);
        	SPL = true; // berarti diminta menyelesaikan permasalahan SPL dan mencetak hasilnya ke layar
        	m = new Matrix();
        	
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
        		System.out.println("Masukkan banyak persamaan");
	        	int jumPersamaan = input.nextInt();
	        	System.out.println("Masukkan banyak variabel");
	        	int jumVariabel = input.nextInt();
	        	System.out.println("Masukan semua koefisien per persamaan diikuti hasil tiap persamaan di akhir");
	        	m.readMatrix(jumPersamaan,jumVariabel+1);
	        }
        	input.close();
        }
        else {
        	SPL = false;
        }
        // Membuat matriks segitiga atas
        forwardElimination(m);
        
        // Array penampung hasil
        double[] a = new double[m.getCol()-1];
        
        // Memeriksa apakah SPL memiliki solusi unik, banyak, atau tidak memiliki solusi
        noSolution = false;
        manySolution = false;
        for (int i=0;i<m.getRow();i++) {
        	if (zeroRow(m,i,true) && m.getElmt(i, m.getCol()-1) != 0) {
        		noSolution = true;
        		break;
        	}
        	else if ((zeroRow(m,i,true) && m.getElmt(i, m.getCol()-1) == 0 && i < m.getCol()-1) || m.getCol() > m.getRow()+1) {
        		manySolution = true;
        	}
        }
        
        if (noSolution) { // SPL tidak memiliki solusi
        	System.out.println("SPL tidak memiliki solusi");
        }
        else {
        	if (manySolution) { // SPL memiliki banyak solusi
        		if (!SPL) {
        			System.out.println("SPL tidak memiliki solusi unik. Regresi/interpolasi tidak dapat diselesaikan");
        		}
        		else {
        			String hasil;
	        		Matrix parametrik = new Matrix(m.getCol()-1, m.getCol()); // Matrix penampung koefisien persamaan parametrik
	        		
	        		// Mengisi matrix persamaan parametrik
	        		for (int i=m.getRow()-1; i>=0;i--) {
	        			if(!zeroRow(m,i,true)) {
	        				// Cari elemen non-0 pertama pada baris
	        				for (int j=0; j<m.getCol()-1; j++) {
	        					if(m.getElmt(i, j) != 0) {
	        						firstNonZero = j;
	        						break;
	        					}
	        				}
	        				
	        				for (int j=m.getCol()-1; j>=0; j--) {
	        					parametrik.setElmt(firstNonZero, j, m.getElmt(i,j));
	        				}
	        			}
	        		}
	        		
	        		// Mencetak persamaan parametrik
	        		for (int i=parametrik.getRow()-1; i>=0;i--) {
	        			if(!zeroRow(parametrik,i,false)) {
	        				hasil = "x" + (i+1) + " = ";
	        				boolean addedHasil = false;
	        				for (int j=i+1; j<parametrik.getCol(); j++) {
	        					if (j == parametrik.getCol()-1) {
	        						if (parametrik.getElmt(i,j) == 0 && !addedHasil) {
	        							hasil += parametrik.getElmt(i, j);
        							}
	        						else {
	        							if (parametrik.getElmt(i,j) > 0 && addedHasil) {
	        								hasil += "+";
	        							}
	        							if (parametrik.getElmt(i, j) != 0) {
	        								hasil += parametrik.getElmt(i, j);
	        							}
	        						}
        						}
	        					else if (parametrik.getElmt(i, j) != 0) {
	        						// Substitusi persamaan parametrik yang sudah ada sebelumnya
	        						if (!zeroRow(parametrik,j,false)) {
	        							for (int k=parametrik.getCol()-1; k >= 0; k--) {
	        								if (k != j) {
	        									parametrik.setElmt(i, k, parametrik.getElmt(i, k) + (-1)*parametrik.getElmt(i, j)*parametrik.getElmt(j, k));
	        								}
	        	        				}
	        							parametrik.setElmt(i, j, 0);
	        						}
	        						else {
	        							if ((-1)*parametrik.getElmt(i, j) > 0 && addedHasil) {
	        								hasil += "+";
	        							}
	        							hasil += (-1)*parametrik.getElmt(i, j) + "" + (char)(j+97);
	        							addedHasil = true;
	        						}
	        					}
	        				}
	        				System.out.println(hasil);
	        			}
	        		}
	        		
	        		for (int i=0; i<parametrik.getRow();i++) { // Mengecek variabel x yang tidak memiliki nilai tertentu
	        			if(zeroRow(parametrik,i,false)) {
	        				System.out.println("x"+(i+1)+" = "+(char)(i+97));
	        			}
	        		}
        		}
        	}
        	else { // SPL memiliki solusi unik
        		// Backwards substitution
                for (int i = m.getCol()-2; i>=0; i--) {
                	if (i == m.getCol()-2) {
                		a[i] += m.getElmt(i, m.getCol()-1); 
                	}
                	else {
                		for (int j=i+1; j<m.getCol(); j++) {
                			if (j == m.getCol()-1) {
                				a[i] += m.getElmt(i, j);
                			} else {
                				a[i] -= m.getElmt(i, j) * (double) a[j];
                			}
                		}
                	}
                	if (Math.abs(a[i]) < 1E-10) {
                		a[i] = 0;
                	}
                	if (SPL) {
                		System.out.println("x" + (i+1) + " = " + a[i]);
                	}
                }
        	}
        }
        return a;
    }
}
