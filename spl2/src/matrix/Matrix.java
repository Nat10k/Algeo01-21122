package matrix;
import java.util.*;
import java.io.*;


public class Matrix {
	// Atribut
	private ArrayList<ArrayList<Double>> mtrx = new ArrayList<>(); // Bagian matrix
	private int row,col; // Banyak baris dan kolom matrix
	private final int IDX_UNDEF = -1;
	
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
    
    private static Matrix multiplyMatrix(Matrix m1, Matrix m2) {
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
    
    public static double determinant(Matrix m) {
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
                det += sign*m.getElmt(0, k)*determinant(minor);
                sign *= -1;
            }
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
    	
    	if (determinant(m) == 0) {
    		System.out.println("Matriks tidak memiliki inverse");
    		return null;
    	}
    	else {
	    	Matrix cofactor = new Matrix();
	    	Matrix adjoint = new Matrix();
	    	double detM = determinant(m);
	    	
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
	    			cofactor.addElmt(i, determinant(minor)*pangkat(-1,i+j));
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
    
    public static double[] gaussElim (Matrix m, boolean SPL){
    	// Fungsi eliminasi Gauss Jordan, m matrix augmented
        boolean noSolution, manySolution;
        int firstNonZero = -1;
//        for(j=0; j<m.getCol(); j++){
//            for(i=0; i<m.getRow(); i++)
//            {
//                if(i!=j)
//                {
//                    t=m.getElmt(i,j)/m.getElmt(j,j);
//                    for(k=0; k<m.getCol(); k++)
//                    {
//                        m.setElmt(i,k,(m.getElmt(i,k)-t*m.getElmt(j,k)));
//                    }
//                }
//            }
//        }
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
    
    public static double pangkat(double a, int b){
        int i;
        double hasil;
        hasil = 1;
        for (i=1; i<=b; i++){
            hasil *= a;
        }
        return hasil;
    }
    
    public static void interpolasi(double x){
    	// Fungsi interpolasi polinom
        int n;
        double y;
        Scanner input = new Scanner(System.in);
        
        System.out.println("Masukkan jumlah titik: ");
        n = input.nextInt();
        Matrix m = new Matrix();
        m.setRow(n);
        m.setCol(n+1);
        double[]a = new double[n];
        for (int i = 0; i<n; i++){
            m.setElmt(i,0,1);
        }
        for (int i = 0; i<n; i++){
            System.out.println("Masukkan x" +(i+1));
            m.setElmt(i, 1, input.nextDouble());
            for(int j=2; j<n; j++){
            	m.setElmt(i, j, pangkat(m.getElmt(i, 1), j));
            }
        }
        for(int i= 0; i<n; i++){
            System.out.println("Masukkan y" + (i+1) +"= ");
            m.setElmt(i, n, input.nextDouble());
        }
        m.displayMatrix();
        a = gaussElim(m, false);
        y = a[0];
        for (int i=1; i<n; i++){
            y += a[i]*pangkat(x, i);
        }
        System.out.println(y);
        input.close();
    }
    
    private static void swap(Matrix m, int row1, int row2) {
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
			            swap(m,row_max,k);
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
//		// Bagi baris terakhir menjadi memiliki 1 utama
//		for (int j=0;j<m.getCol();j++) {
//			if (m.getElmt(m.getRow()-1, j) != 0) {
//				for (int k=j;k<m.getCol();k++) {
//					m.setElmt(m.getRow()-1, k, (m.getElmt(m.getRow()-1, k)/m.getElmt(m.getRow()-1, j)));
//				}
//				break;
//			}
//		}
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
	
	public static void multiRegression() {
		// Melakukan regresi linear berganda
		
		// Menerima banyak variabel dan jumlah sample
		int n,m;
		double sum, hasilK;
		double[] target;
		boolean fromFile;
		Matrix data = new Matrix();
		
		Scanner input = new Scanner(System.in);
		System.out.println("Masukan dari file ?");
		fromFile = input.nextBoolean();
		if (fromFile) { // Menerima masukan dari file, nilai Xk yang ingin dicari diasumsikan ada di baris terakhir
			System.out.println("Masukkan nama file");
			String fileName = input.next();
			data.readMatrix(fileName);
			
			// Mengisi array penampung elemen yang ingin dicari
			target = new double[data.getCol()-1];
			for (int j=0; j<data.getCol()-1; j++) { 
				target[j] = data.getElmt(data.getRow()-1, j);
			}
			data.mtrx.get(data.getRow()-1).clear();
			data.row--;
		} 
		else { // Menerima masukan data dari keyboard
			System.out.println("Masukkan banyak peubah x");
			n = input.nextInt();
			System.out.println("Masukkan banyak sample");
			m = input.nextInt();
			
			// Menerima masukan data
			System.out.println("Masukkan semua data per sample, kolom terakhir sebagai hasilnya");
			data.readMatrix(m, n+1);
			System.out.println("Masukkan nilai-nilai peubah yang ingin dicari hasilnya secara berurutan");
			target = new double[n];
			for (int i = 0; i<n; i++) {
				target[i] = input.nextDouble();
			}
		}
		input.close();
		
		// Membuat matriks SPL regresi
		Matrix equations = new Matrix();
		equations.setRow(data.getCol());
		equations.setCol(data.getCol()+1);
		
		for (int i=0;i<equations.getRow();i++) {
			equations.addRow();
			for (int j=0; j<equations.getCol();j++) {
				if (i == 0 && j == 0) { // Koefisien B0 pertama
					equations.addElmt(i, (double) data.getRow());
				} 
				else if (i==0) { // SPL baris pertama
					sum = 0;
					for (int k=0; k<data.getRow();k++) {
						sum += data.getElmt(k,j-1);
					}
					equations.addElmt(i,sum);
				}
				else if (j==0) { // SPL kolom pertama
					sum = 0;
					for (int k=0; k<data.getRow();k++) {
						sum += data.getElmt(k,i-1);
					}
					equations.addElmt(i,sum);
				}
				else {
					sum = 0;
					for (int k=0; k<data.getRow();k++) {
						sum += data.getElmt(k, i-1) * data.getElmt(k,j-1);
					}
					equations.addElmt(i,sum);
				}
			}
		}
		System.out.println("SPL dari Normal Estimation Equation for Multiple Linear Regression");
		equations.displayMatrix();
		
		// Mencetak persamaan hasil
		double[] result = gaussElim(equations, false);
		String persamaan = "y = " + result[0];
		for (int i=1; i<result.length-1; i++) {
			if (result[i] > 0) {
				persamaan += " + ";
			}
			else {
				persamaan += " - ";
			}
			persamaan += Math.abs(result[i]) + "x" + i;
		}
		if (result[result.length-1] > 0) {
			persamaan += " + ";
		}
		else {
			persamaan += " - ";
		}
		persamaan += Math.abs(result[result.length-1]) + "x" + (result.length-1);
		System.out.println("Persamaan regresi : " + persamaan);
		hasilK = result[0];
		for (int i=0;i<target.length;i++) {
			hasilK += target[i]*result[i+1];
		}
		System.out.println("yk = " + hasilK);
	}
	
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
		System.out.println("Masukkan nama file");
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
				xMat.addElmt(i,pangkat(x,(pangkatX))*pangkat(y,(pangkatY)));
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
		
		// Mengisi matriks hasil a dari hasil perkalian matrix X dengan matrix inputMtrx
//		inverse(xMat).displayMatrix();
//		arrayA = multiplyMatrix(inverse(xMat),nilaiF);
//		arrayA.displayMatrix();
	}

	public static void main(String[] args) {
//		String test;
//		File testFile = new File("test.txt");
//		try {
//			Scanner stringTest = new Scanner(testFile);
//			test = stringTest.nextLine();
//			System.out.print(test);
//		}
//		catch (FileNotFoundException e) {
//			System.out.println("File is not found");
//		    e.printStackTrace();
//		}
		
//        Matrix m = new Matrix();
//        Matrix m2 = new Matrix();
//        m.readMatrix("testMultiply.txt");
//        m2.readMatrix("testMultiply.txt");
//        multiplyMatrix(m,m2).displayMatrix();
		
//        m.displayMatrix();
//        forwardElimination(m);
//        m.displayMatrix();
//        gaussElim(m, true);
//		multiRegression();
		
		bicubic();
//		Matrix m = new Matrix();
//		m.readMatrix("test.txt");
//		m.displayMatrix();
//		determinant(m);
//		transpose(m);
//		inverse(m);
	}

}
