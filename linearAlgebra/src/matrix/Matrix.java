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
