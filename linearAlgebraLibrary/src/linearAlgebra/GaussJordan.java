package linearAlgebra;
import java.util.*;

public class GaussJordan{
    static void backwardElimination(Matrix m) {
    	// Mengubah matriks eselon baris menjadi matriks eselon baris tereduksi
    	double factor;
    	for (int i=m.getRow()-1; i>0; i--) {
    		// Kurangi baris di atas baris i dengan baris i
    		if (i < m.getCol()) {
	    		for (int k=i-1; k>=0; k--) {
	    			factor = m.getElmt(k, i);
	    			for (int j=i; j<m.getCol(); j++) {
	    				m.setElmt(k, j, m.getElmt(k, j)-factor*m.getElmt(i, j));
	    				if (Math.abs(m.getElmt(k, j)) < 1E-10) {
		            		m.setElmt(k, j, 0);
		            	}
	    			}
	    		}
    		}
    	}
    }
	
	public static double[] gaussjordanElim () {
		// Overloader fungsi gaussjordanElim
		return gaussjordanElim(null);
	}
	
    public static double[] gaussjordanElim (Matrix m){
        // Fungsi eliminasi Gauss Jordan, m matrix augmented, bisa digunakan untuk mencari determinan juga
        boolean noSolution, manySolution, SPL, fromFile;
        int firstNonZero = -1;
        
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
        GaussElimination.forwardElimination(m);
        backwardElimination(m);
        
        // Array penampung hasil
        double[] a = new double[m.getCol()-1];
        
        // Memeriksa ada/tidak adanya baris matriks yang hanya terdiri dari 0
        noSolution = false;
        manySolution = false;
        for (int i=0; i<m.getRow();i++) {
        	if (GaussElimination.zeroRow(m,i,true) && m.getElmt(i, m.getCol()-1) != 0) {
        		noSolution = true;
        		break;
        	}
        	else if (GaussElimination.zeroRow(m,i,true) && m.getElmt(i, m.getCol()-1) == 0 || m.getCol() != m.getRow()+1) {
        		manySolution = true;
        	}
        }
        
        if (noSolution) { // SPL tidak memiliki solusi
        	if (SPL) {
        		System.out.println("SPL tidak memiliki solusi");
        	} else {
        		System.out.println("Matriks tidak memiliki invers");
        	}
        }
        else {
        	if (manySolution) { // SPL memiliki banyak solusi
        		if (!SPL) {
        			System.out.println("Matriks tidak memiliki invers");
        		}
        		else {
        			String hasil;
            		Matrix parametrik = new Matrix(m.getCol()-1,m.getCol()); // Matrix penampung koefisien persamaan parametrik
            		
            		// Mengisi matrix persamaan parametrik
            		for (int i=m.getRow()-1; i>=0;i--) {
            			if(!GaussElimination.zeroRow(m,i,true)) {
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
            			if(!GaussElimination.zeroRow(parametrik,i,false)) {
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
            						if (!GaussElimination.zeroRow(parametrik,j,false)) {
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
            			if(GaussElimination.zeroRow(parametrik,i,false)) {
            				System.out.println("x"+(i+1)+" = "+(char)(i+97));
            			}
            		}
        		}
        	}
        	else { // SPL memiliki solusi unik
                 for (int i=0; i<a.length; i++) {
                 	a[i] = m.getElmt(i, m.getCol()-1);
                 	if (Math.abs(a[i]) < 1E-10) {
                		a[i] = 0;
                	}
                 	if (SPL) {
                 		System.out.println("x"+(i+1)+ " = " + a[i]);
                 	}
                 }
        	}
        }
        return a;
    }
}
