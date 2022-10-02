package linearAlgebra;

import java.util.Scanner;

public class Interpolation {
	/** Class persoalan inerpolasi polinom */
	static String newline = System.getProperty("line.separator"); 
	
	static Matrix inputTitikInterpolasi(Scanner input) {
		/** Menerima input matriks titik untuk interpolasi */
		int n;
        Matrix titik = new Matrix();        
        // Menerima masukan
        System.out.println("Masukan dari file ? Y/N");
		if (input.next().equals("Y")) { // Menerima masukan dari file
			System.out.println("Masukkan path file");
			String fileName = input.next();
			titik.readMatrix(fileName);
		} 
		else { // Menerima masukan data dari keyboard
			System.out.println("Masukkan banyak titik: ");
	        n = input.nextInt();
	        
	        // Menerima masukan nilai x,y dan memasukkannya ke dalam Matrix titik
	        titik.setSize(n, 2);
	        System.out.println("Masukkan setiap titik xi yi per baris. Format : xi yi.");
	        for (int i=0; i<titik.getRow(); i++) {
	        	System.out.println("x" + (i+1) + " y" + (i+1));
	        	titik.setElmt(i, 0, input.nextDouble());
	        	titik.setElmt(i, 1, input.nextDouble());
	        }
		}
		return titik;
	}
	
	static double inputX(Scanner input) {
		/** Menerima masukan nilai x yang ingin dicari hasilnya */
		System.out.println("Masukkan nilai x yang ingin dicari hasilnya");
		double x = input.nextDouble();
		return x;
	}
	
	public static void interpolasiPolinom(Scanner input) {
		/** Menerima input matriks titik, x yang ingin dicari nilainya, dan mengembalikan persamaan dan hasilnya */
		Matrix titik = inputTitikInterpolasi(input);
		double x = inputX(input);
		System.out.println("Simpan hasil ke dalam file ? Y/N");
		if (input.next().equals("Y")) { // Hasil disimpan ke file
			System.out.println("Masukkan nama file output");
			String outputFile = input.next();
			interpolasiPolinom(titik,x,outputFile);
		}
		else { // Hasil ditampilkan ke layar saja
			interpolasiPolinom(titik,x,null);
		}
	}
	
	public static void interpolasiPolinom(Matrix titik, double x, String outputFile){
    	/** Melakukan interpolasi polinom berdasarkan matriks titik dan masukan nilai x */
        int n;
        double y; // hasil dari x yang dicari
        double[] a;
        String hasil="";
        Matrix m;
        
        n = titik.getRow();
		
		// Membuat matriks persamaan
		m = new Matrix(n,n+1);
        a = new double[n];
        for (int i = 0; i<n; i++){
            m.setElmt(i,0,1);
        }
		for (int i = 0; i<n; i++){
            m.setElmt(i,1,titik.getElmt(i, 0));
            for(int j=2; j<n; j++){
            	m.setElmt(i,j,Math.pow(m.getElmt(i, 1), j));
            }
            m.setElmt(i,n, titik.getElmt(i,1));
        }
		
        a = SPL.gaussElim(m,false);
        y = a[0];
        for (int i=1; i<n; i++){
            y += a[i]*Math.pow(x, i);
        }
        hasil+= "f(x) = ";
        for(int i = m.getRow()-1; i>=0; i--){
            if (a[i] > 0) {
                hasil += "+";
            }
            if (i > 0) {
            	hasil += a[i]+"x^"+ (i);
            }
            else {
            	hasil += a[i];
            }
        }
        hasil += newline;
        hasil += "f("+x+") = "+ (y);
        
        System.out.println(hasil);
        if(outputFile != null) {
        	FileOutput.printFile(outputFile,hasil);
        }
    }
}
