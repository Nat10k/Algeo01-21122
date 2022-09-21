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
	public static boolean isIdxValid(Matrix m, int i, int j) {
		// Menentukan apakah indeks i,j valid untuk matriks
		return (i >= 0 && i < m.row && j >= 0 && j < m.col);
	}
	
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
				this.mtrx.add(new ArrayList<>());
	            for(int j=0; j<nCol; j++){
	            	this.mtrx.get(i).add(input.nextDouble());
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
					this.mtrx.add(new ArrayList<>());
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
    
    public static double[] gaussElim (Matrix m){
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
                	System.out.println("x" + (i+1) + " = " + a[i]);
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
        a = gaussElim(m);
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
		double sum;
		double[] target;
		boolean fromFile;
		Matrix data = new Matrix();
		
		Scanner input = new Scanner(System.in);
		System.out.println("Masukan dari file ?");
		fromFile = input.nextBoolean();
		if (fromFile) { // Menerima masukan dari file, nilai Xk yang ingin dicari diasumsikan ada di baris terakhir
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
		equations.setRow(data.getCol()+1);
		equations.setCol(data.getCol()+2);
		for (int i=0; i<equations.getRow();i++) {
			equations.mtrx.add(new ArrayList<>());
			for (int j=0; j<equations.getCol();j++) {
				if (i == 0 && j == 0) { // Koefisien B0 pertama
					equations.mtrx.get(i).add(j, (double) data.getRow());
				} 
				else if (i==0) { // SPL baris pertama
					sum = 0;
					for (int k=0; k<data.getRow();k++) {
						sum += data.getElmt(k,j-1);
					}
					equations.mtrx.get(i).add(j, sum);
				}
				else if (j==0) { // SPL kolom pertama
					sum = 0;
					for (int k=0; k<data.getRow();k++) {
						sum += data.getElmt(k,i-1);
					}
					equations.mtrx.get(i).add(j, sum);
				}
				else {
					sum = 0;
					for (int k=0; k<data.getRow();k++) {
						sum += data.getElmt(k, i-1) * data.getElmt(k,j-1);
					}
					equations.mtrx.get(i).add(j, sum);
				}
			}
		}
		equations.displayMatrix();
		double[] result = gaussElim(equations);
		String persamaan = "y = ";
		for (int i=0; i<result.length; i++) {
			persamaan += result[i] + "x" + (i+1) + " + ";
		}
		System.out.println(persamaan);
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
//        m.readMatrix("test.txt");
//        m.displayMatrix();
//        forwardElimination(m);
//        m.displayMatrix();
//        gaussElim(m);
		multiRegression();
	}

}
