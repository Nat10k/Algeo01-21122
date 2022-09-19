package matrix;
import java.util.*;

public class Matrix {
	// Atribut
	private double[][] mtrx; // Bagian matrix
	private int row,col; // Banyak baris dan kolom matrix
	private final int IDX_UNDEF = -1;
	
	public Matrix() {
		// Inisialisasi Matrix kosong
		row = 0;
		col = 0;
		mtrx = new double[row][col];
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
		return this.mtrx[i][j];
	}
	
	public void setRow(int nRow) {
		this.row = nRow;
	}
	
	public void setCol(int nCol) {
		this.col = nCol;
	}
	
	public void setElmt(int i, int j, double val) {
		// Mengubah elemen matriks di indeks i,j dengan nilai val, diasumsikan indeks valid
		this.mtrx[i][j] = val;
	}
	
	public void readMatrix(int nRow, int nCol){
		// Membaca seluruh elemen baru matriks
        this.setRow(nRow);
        this.setCol(nCol);
        this.mtrx = new double[nRow][nCol];
        Scanner input = new Scanner(System.in);
		for(int i=0; i<nRow; i++){
			System.out.println("Baris " + (i+1));
            for(int j=0; j<nCol; j++){
                this.setElmt(i,j,input.nextDouble());
            }
        }
		input.close();
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
    
    public static double[] gaussJordan (Matrix m){
    	// Fungsi eliminasi Gauss Jordan
        int i,j,k;
        double t;
        for(j=0; j<m.getCol(); j++){
            for(i=0; i<m.getRow(); i++)
            {
                if(i!=j)
                {
                    t=m.getElmt(i,j)/m.getElmt(j,j);
                    for(k=0; k<m.getCol(); k++)
                    {
                        m.setElmt(i,k,m.getElmt(i,k)-t*m.getElmt(j,k));
                    }
                }
            }
        }
        double[] a = new double[m.getRow()];
        for(i=0; i<m.getRow(); i++) 
        {
            a[i]=m.getElmt(i,m.getCol())/m.getElmt(i,i);
            // System.out.println("a"+ (i) + "= " + a[i]);
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
        m.mtrx = new double[n][n+1];
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
        a = gaussJordan(m);
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
		
		for (int k=0;k<m.getRow()-1;k++) {
	        // Mencari nilai terbesar dari tiap baris pada kolom yang bersangkutan
	        row_max = k;
	        max = m.getElmt(row_max, k);
	        for (int i=k+1;i<m.getRow();i++) {
	            if(Math.abs(m.getElmt(i, k)) > max) {
	                row_max = i;
	                max = m.getElmt(i, k);
	            }
	        }

	        if (max != 0) { // Jika kolom seluruhnya berisi 0, jangan lakukan pembagian
	        	if (row_max != k){ // Elemen terbesar ada di baris selain k
		            swap(m,row_max,k);
		        }
	        	
	        	// Ubah elemen pertama baris maksimal menjadi 1 utama, bagi seluruh baris dengan elemen pertama
	        	for (int j=0;j<m.getCol();j++) {
	        		m.setElmt(row_max, j, (m.getElmt(row_max, j)/m.getElmt(row_max, k)));
	        	}
	
		        // Kurangi tiap baris di bawah baris k
		        for (int i=k+1;i<m.getRow();i++) {
		            factor = m.getElmt(i, k)/m.getElmt(k, k);
		            for (int j=k;j<m.getCol();j++) {
		            	m.setElmt(i, j, (m.getElmt(i, j)-(m.getElmt(k,j)*factor)));
		            }
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
	
	public static void multiRegression() {
		// Melakukan regresi linear berganda
		
		// Menerima banyak variabel dan jumlah sample
		int n,m;
		double sum;
		
		Scanner input = new Scanner(System.in);
		System.out.println("Masukkan banyak peubah x");
		n = input.nextInt();
		System.out.println("Masukkan banyak sample");
		m = input.nextInt();
		
		// Menerima masukan data
		Matrix data = new Matrix();
		System.out.println("Masukkan semua data per sample, kolom terakhir sebagai hasilnya");
		data.readMatrix(m, n+1);
		
		// Membuat matriks SPL regresi
		Matrix equations = new Matrix();
		equations.setRow(n+1);
		equations.setCol(n+2);
		equations.mtrx = new double[n+1][n+2];
		for (int i=0; i<equations.getRow();i++) {
			for (int j=0; j<equations.getCol();j++) {
				if (i == 0 && j == 0) { // Koefisien B0 pertama
					equations.setElmt(i, j, m);
				} 
				else if (i==0) { // SPL baris pertama
					sum = 0;
					for (int k=0; k<data.getRow();k++) {
						sum += data.getElmt(k,j-1);
					}
					equations.setElmt(i, j, sum);
				}
				else if (j==0) { // SPL kolom pertama
					sum = 0;
					for (int k=0; k<data.getRow();k++) {
						sum += data.getElmt(k,i-1);
					}
					equations.setElmt(i, j, sum);
				}
				else {
					sum = 0;
					for (int k=0; k<data.getRow();k++) {
						sum += data.getElmt(k, i-1) * data.getElmt(k,j-1);
					}
					equations.setElmt(i, j, sum);
				}
			}
		}
		equations.displayMatrix();
		System.out.println(gaussJordan(equations));
	}

	public static void main(String[] args) {
//        Matrix m = new Matrix();
//        m.readMatrix(3, 4);
//        forwardElimination(m);
//        m.displayMatrix();
		multiRegression();
	}

}
