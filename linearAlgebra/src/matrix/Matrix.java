package matrix;
import java.util.*;
import java.io.*;

// ADT Matrix
public class Matrix {
	// Atribut
	public ArrayList<ArrayList<Double>> mtrx = new ArrayList<>(); // Bagian matrix
	public int row,col; // Banyak baris dan kolom matrix
	
	public Matrix() {
		// Inisialisasi Matrix kosong
		row = 0;
		col = 0;
	}
	
	// Selektor
	public int getRow() {
		return this.row;
	}
	
	public int getCol() {
		return this.col;
	}
	
	public double getElmt(int i, int j) {
		// Mengembalikan elemen matriks di indeks i,j, diasumsikan indeks valid
		return this.mtrx.get(i).get(j);
	}
	
	public void setRow(int nRow) {
		this.row = nRow;
	}
	
	public void setCol(int nCol) {
		this.col = nCol;
	}
	
	public void setElmt(int i, int j, double val) {
		// Mengubah elemen matriks di indeks i,j dengan nilai val, diasumsikan indeks valid
		this.mtrx.get(i).set(j, val);
	}
	
	public void addRow() {
		// Menambahkan baris baru ke matrix
		this.mtrx.add(new ArrayList <>());
	}
	
	public void addElmt(int i, double val) {
		// Menambahkan kolom baru beserta elemennya ke baris i
		this.mtrx.get(i).add(val);
	}
	
	public void readMatrix(int nRow, int nCol) {
		readMatrix(nRow, nCol, null);
	}
	
	public void readMatrix(String fileName) {
		readMatrix(-1,-1, fileName);
	}
	
	public void readMatrix(int nRow, int nCol, String fileName){
		if (fileName == null) {
			// Membaca elemen matriks dari keyboard
			Scanner input = new Scanner(System.in);
			this.setRow(nRow);
	        this.setCol(nCol);
	        this.mtrx.clear(); // Kosongkan matriks sebelum diisi ulang
			for(int i=0; i<nRow; i++){
				System.out.println("Baris " + (i+1));
				this.addRow();
	            for(int j=0; j<nCol; j++){
	            	this.addElmt(i,input.nextDouble());
	            }
	        }
			input.close();
		} else { // Input matriks dari file
			try {
				File mtrxFile = new File(fileName);
				Scanner inputFile = new Scanner(mtrxFile);
				
				this.mtrx.clear(); // Kosongkan matriks sebelum diisi ulang
				this.setRow(0);
				this.setCol(0);
				
				while(inputFile.hasNextLine()) { // Mengisikan matriks
					this.addRow();
					Scanner elInput = new Scanner(inputFile.nextLine());
					while(elInput.hasNextDouble()) {
						this.mtrx.get(this.mtrx.size()-1).add(elInput.nextDouble());
					}
					elInput.close();
				}
				inputFile.close();
				this.setRow(this.mtrx.size());
				this.setCol(this.mtrx.get(0).size());
			}
			catch (FileNotFoundException e) {
				System.out.println("File is not found");
			    e.printStackTrace();
			}
				
		}
			
    }
	
    public void displayMatrix(){
    	// Menuliskan seluruh matrix ke layar
        for(int i=0; i<this.getRow(); i++){
            for(int j=0; j<this.getCol(); j++){
                System.out.print(this.getElmt(i, j) + " ");
            }
            System.out.println();
        }
    }
    
    public static Matrix multiplyMatrix(Matrix m1, Matrix m2) {
    	// Menghasilkan matriks hasil perkalian m1 dengan m2, diasumsikan m1.getCol() = m2.getRow()
    	Matrix result = new Matrix();
    	double sum;
    	
    	// Melakukan perkalian matriks
    	for(int i=0;i<m1.getRow();i++) {
    		result.addRow();
    		result.row++;
            for(int j=0;j<m2.getCol();j++) {
                sum = 0;
                for(int k=0;k<m1.getCol();k++) {
                	sum += m1.getElmt(i, k) * m2.getElmt(k, j);
                }
                result.addElmt(i,sum);
                if (i == 0) {
                	result.col++;
                }
            }
        }
    	return result;
    }
    
    public static double determinantCofactor(Matrix m) {
    	// Menghasilkan determinan matriks, diasumsikan m adalah matriks persegi
        Matrix minor;
        int row_cof;
        int sign;
        double det;

        /* ALGORITMA */
        // Mencari determinan dengan metode kofaktor secara rekursif
        if (m.getRow() == 1) { // Jika matriks hanya memiliki 1 elemen
            det = m.getElmt(0, 0);
        }
        else if (m.getRow()==2) { // Jika matriks ukuran 2x2
            det = (m.getElmt(0, 0)*m.getElmt(1, 1))-(m.getElmt(1, 0)*m.getElmt(0, 1));
        } else {
            sign = 1;
            det = 0;
            
            // Dipakai patokan baris pertama
            for(int k=0; k<m.getCol(); k++) {
                // Membuat matriks minor
            	// Inisialisasi ukuran matriks minor
            	minor = new Matrix();
                minor.setRow(m.getRow()-1);
                minor.setCol(m.getCol()-1);
                row_cof = 0;
                for (int i=1; i<m.getRow(); i++) {
                	minor.addRow();
                    for (int j=0; j<m.getCol(); j++) {
                        if (j != k) {
                            minor.addElmt(row_cof, m.getElmt(i,j));
                        }
                    }
                    row_cof++;
                }
                // Menghitung determinan
                det += sign*m.getElmt(0, k)*determinantCofactor(minor);
                sign *= -1;
            }
        }
        return det;
    }
    
    public static double determinantOBE(Matrix m) {
    	// Menghasilkan determinan matriks menggunakan OBE, diasumsikan m adalah matriks persegi
    	// Membuat matriks m menjadi matriks eselon baris
    	double det;
		int row_max;
		double max, factor;
		ArrayList<Double> kali = new ArrayList<> ();
		int pangkatSwap = 0;
		
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
			            swap(m,row_max,k);
			            pangkatSwap++;
			        }
		        	
		        	// Ubah elemen tidak nol pertama baris maksimal menjadi 1 utama, bagi seluruh baris dengan elemen pertama
		        	for (int j=r;j<m.getCol();j++) {
		        		m.setElmt(k, j, m.getElmt(k, j)/max);
		        	}
		        	kali.add(max);
		
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
		det = 1;
		for (int i=0; i<m.getRow(); i++) {
			det *= m.getElmt(i, i);
		}
		det *= Math.pow(-1,pangkatSwap);
		for (int i=0; i<kali.size(); i++) {
			det *= kali.get(i);
		}
        return det;
    }
    
    public static Matrix transpose(Matrix m) {
    	// Menghasillkan matrix transpose matrix m
    	Matrix mTrans = new Matrix();
    	mTrans.setRow(m.getCol());
    	mTrans.setCol(m.getRow());
    	for(int i=0;i<mTrans.getRow();i++) {
    		mTrans.addRow();
            for(int j=0;j<mTrans.getCol();j++) {
                mTrans.addElmt(i, m.getElmt(j, i));
            }
        }
    	return mTrans;
    }
    
    public static Matrix inverse(Matrix m) {
    	// Menghasilkan invers Matrix m, diasumsikan matriks m matriks persegi
    	
    	if (determinantOBE(m) == 0) {
    		System.out.println("Matriks tidak memiliki inverse");
    		return null;
    	}
    	else {
	    	Matrix cofactor = new Matrix();
	    	Matrix adjoint = new Matrix();
	    	double detM = determinantOBE(m);
	    	
	    	// Inisialisasi ukuran matrix cofactor dan adjoint
	    	cofactor.setRow(m.getRow());
	    	cofactor.setCol(m.getCol());
	    	adjoint.setRow(m.getRow());
	    	adjoint.setCol(m.getCol());
	    	
	    	// Membuat Matrix cofactor dari Matrix m
	    	for (int i=0; i<m.getRow(); i++) {
	    		cofactor.addRow();
	    		for (int j=0; j<m.getCol(); j++) {
	    			// Membuat matrix minor
	        		Matrix minor = new Matrix();
	        		minor.setRow(m.getRow()-1);
	        		minor.setCol(m.getCol()-1);
	        		int row_minor = 0;
	        		for (int r=0; r<minor.getRow(); r++) {
	        			minor.addRow();
	        		}
	    			for (int k=0; k<m.getRow();k++) {
	    				boolean addedElmt = false;
	    				for (int l=0; l<m.getCol(); l++) {
	    					if (k != i && l != j) {
	    						minor.addElmt(row_minor, m.getElmt(k, l));
	    						addedElmt = true;
	    					}
	    				}
	    				if (addedElmt) {
	    					row_minor++;
	    				}
	    			}
	    			// Menambahkan elemen matriks kofaktor Cij
	    			cofactor.addElmt(i, determinantOBE(minor)*Math.pow(-1,i+j));
	    		}
	    	}
	    	adjoint = transpose(cofactor);
	    	for (int i=0; i<adjoint.getRow(); i++) {
	    		for (int j=0; j<adjoint.getCol(); j++) {
	    			adjoint.setElmt(i, j, adjoint.getElmt(i, j)/detM);
	    		}
	    	}
	    	adjoint.displayMatrix();
	    	return adjoint;
	    }
    	
    }
    
    public static void swap(Matrix m, int row1, int row2) {
    	// Menukar baris row1 dengan baris row2
    	if (row1 < 0 || row1 >= m.getRow() || row2 < 0 || row2 > m.getRow()){
    		System.out.println("Baris tidak valid");
    	}
    	else {
	    	double temp; // Penampung nilai sementara
	    	for (int i=0; i<m.getCol();i++) {
	    		temp = m.getElmt(row1, i);
	    		m.setElmt(row1, i, m.getElmt(row2, i));
	    		m.setElmt(row2, i, temp);
	    	}	
    	}
    }
}
