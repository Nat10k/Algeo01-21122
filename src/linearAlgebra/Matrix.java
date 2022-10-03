package linearAlgebra;
import java.util.*;
import java.io.*;

// ADT Matrix
public class Matrix {
	/** Class ADT Matrix */
	// Atribut
	public ArrayList<ArrayList<Double>> mtrx = new ArrayList<>(); /** ArrayList sebagai matriks */
	public int row,col; /** Banyak baris dan kolom matrix */
	
	public Matrix() { 
		/** Inisialisasi matriks yang belum diketahui ukurannya */
		row =0;
		col = 0;
	}
	
	public Matrix(int nRow, int nCol) {
		/** Inisialisasi Matrix kosong berukuran nRow x nCol */
		if (nRow > 0 && nCol > 0) {
			setSize(nRow, nCol);
		}
	}
	
	// Selektor
	public int getRow() {
		/** Mengembalikan banyak baris matriks */
		return this.row;
	}
	
	public int getCol() {
		/** Mengembalikan banyak kolom matriks */
		return this.col;
	}
	
	public double getElmt(int i, int j) {
		/** Mengembalikan elemen matriks di indeks i,j, diasumsikan indeks valid */
		return this.mtrx.get(i).get(j);
	}
	
	public void setSize(int nRow, int nCol) {
		/** Membuat matrix "kosong" (hanya berisi 0) berukuran nRow x nCol */
		this.mtrx.clear();
		for (int i=0; i<nRow; i++) {
			this.addRow();
			for (int j=0; j<nCol; j++) {
				this.addElmt(i, 0);
			}
		}
	}
	
	public void setElmt(int i, int j, double val) {
		/** Mengubah elemen matriks di indeks i,j dengan nilai val, diasumsikan indeks valid */
		this.mtrx.get(i).set(j, val);
	}
	
	public void addRow() {
		/** Menambahkan baris baru ke matrix */
		this.mtrx.add(new ArrayList <>());
		this.row++;
	}
	
	public void addElmt(int i, double val) {
		/** Menambahkan kolom baru beserta isinya ke baris i */
		this.mtrx.get(i).add(val);
		if (this.mtrx.get(i).size() > this.col) {
			this.col = this.mtrx.get(i).size();
		}
	}
	
	// Input Matrix dari file/keyboard
	public void readMatrix(int nRow, int nCol, Scanner input) {
		/** Membaca isi matriks berukuran nRow x nCol */
		readMatrix(nRow, nCol, null, input);
	}
	
	public void readMatrix(String fileName) {
		/** Membaca isi matriks dari file */
		readMatrix(0, 0, fileName, null);
	}
	
	public void readMatrix(int nRow, int nCol, String fileName, Scanner input){
		/** Membaca isi matriks berukuran nRow x nCol atau membaca matriks dari file */ 
		if (fileName == null) {
			// Membaca elemen matriks dari keyboard
			this.setSize(nRow, nCol);
			for(int i=0; i<nRow; i++){
				System.out.println("Baris/Persamaan " + (i+1));
	            for(int j=0; j<nCol; j++){
	            	this.setElmt(i,j,input.nextDouble());
	            }
	        }
		} else { // Input matriks dari file
			try {
				File mtrxFile = new File(fileName);
				Scanner inputFile = new Scanner(mtrxFile);
				
				this.mtrx.clear(); // Kosongkan matriks sebelum diisi ulang
				this.row = 0;
				this.col = 0;
				
				while(inputFile.hasNextLine()) { // Mengisikan matriks
					this.addRow();
					Scanner elInput = new Scanner(inputFile.nextLine());
					while(elInput.hasNextDouble()) {
						this.addElmt(this.getRow()-1, elInput.nextDouble());
					}
					elInput.close();
				}
				inputFile.close();
			}
			catch (FileNotFoundException e) {
				System.out.println("File is not found");
			    e.printStackTrace();
			}
				
		}
    }
	
	// Output Matrix
	public void displayMatrix() {
		/** Menuliskan matriks ke layar saja */
		displayMatrix(null);
	}
	
    public void displayMatrix(String fileName){
    	/** Menuliskan matriks ke layar dan file (jika diberi fileName) */
    	String matrixOutput = "";
    	String newline = System.getProperty("line.separator");  
        for(int i=0; i<this.getRow(); i++){
            for(int j=0; j<this.getCol(); j++){
                matrixOutput += this.getElmt(i, j) + " ";
            }
            matrixOutput += newline;
        }
        System.out.print(matrixOutput);
        if (fileName != null) {
        	FileOutput.printFile(fileName, matrixOutput);
        }
    }
    
    // Operasi Matrix
    public static Matrix multiplyMatrix(Matrix m1, Matrix m2) {
    	/** Menghasilkan matriks hasil perkalian m1 dengan m2, diasumsikan m1.getCol() = m2.getRow() */
    	Matrix result = new Matrix(m1.getRow(),m2.getCol());
    	double sum;
    	
    	// Melakukan perkalian matriks
    	for(int i=0;i<result.getRow();i++) {
            for(int j=0;j<result.getCol();j++) {
                sum = 0;
                for(int k=0;k<m1.getCol();k++) {
                	sum += m1.getElmt(i, k) * m2.getElmt(k, j);
                }
                result.setElmt(i,j,sum);
            }
        }
    	return result;
    }
    
    public static Matrix transpose(Matrix m) {
    	/** Menghasillkan matriks transpose matriks m */
    	Matrix mTrans = new Matrix(m.getCol(),m.getRow());
    	for(int i=0;i<mTrans.getRow();i++) {
            for(int j=0;j<mTrans.getCol();j++) {
                mTrans.setElmt(i,j, m.getElmt(j, i));
            }
        }
    	return mTrans;
    }
    
    public static Matrix copyMatrix(Matrix m) {
    	/** Menghasilkan matriks salinan matriks m */
    	Matrix mCopy = new Matrix(m.getRow(),m.getCol());
    	for (int i=0; i<mCopy.getRow(); i++) {
    		for (int j=0; j<mCopy.getCol(); j++) {
    			mCopy.setElmt(i,j, m.getElmt(i, j));
    		}
    	}
    	return mCopy;
    }
    
    public static void swap(Matrix m, int row1, int row2) {
    	/** Menukar baris row1 dengan baris row2 */
    	if (row1 < 0 || row1 >= m.getRow() || row2 < 0 || row2 > m.getRow()){
    		System.out.println("Baris tidak valid");
    	}
    	else {
	    	double temp; // Penampung nilai sementara
	    	for (int j=0; j<m.getCol();j++) {
	    		temp = m.getElmt(row1, j);
	    		m.setElmt(row1, j, m.getElmt(row2, j));
	    		m.setElmt(row2, j, temp);
	    	}	
    	}
    }
}
