package linearAlgebra;

import java.util.Scanner;

public class Interpolation {
	public static void interpolasi(){
    	// Fungsi interpolasi polinom
        int n;
        double x,y; // x yang ingin dicari hasil y nya
        double[] a;
        Matrix m;
        Matrix titik = new Matrix();
        boolean fromFile;
        Scanner input = new Scanner(System.in);
        
        // Menerima masukan
        System.out.println("Masukan dari file ?");
		fromFile = input.nextBoolean();
		if (fromFile) { // Menerima masukan dari file
			System.out.println("Masukkan path file");
			String fileName = input.next();
			titik.readMatrix(fileName);
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
		} 
		else { // Menerima masukan data dari keyboard
			System.out.println("Masukkan jumlah titik: ");
	        n = input.nextInt();
	        
	        // Menerima masukan nilai x,y dan membuat matriks persamaan
	        m = new Matrix(n,n+1);
	        a = new double[n];
	        for (int i = 0; i<n; i++){
	            m.setElmt(i,0,1);
	        }
	        for (int i = 0; i<n; i++){
	            System.out.println("Masukkan x" +(i+1));
	            m.setElmt(i,1, input.nextDouble());
	            for(int j=2; j<n; j++){
	            	m.setElmt(i, j, Math.pow(m.getElmt(i, 1), j));
	            }
	            System.out.println("Masukkan y" + (i+1) +"= ");
	            m.setElmt(i, n, input.nextDouble());
	        }
		}
		// Menerima masukan nilai x yang ingin dicari hasilnya
		System.out.println("Masukkan nilai x yang ingin dicari hasilnya");
		x = input.nextDouble();
		input.close();
		
        a = GaussElimination.gaussElim(m);
        y = a[0];
        for (int i=1; i<n; i++){
            y += a[i]*Math.pow(x, i);
        }
        System.out.print("f(x) = ");
        for(int i = m.getRow()-1; i>=0; i--){
            if (a[i] > 0) {
                System.out.print("+");
            }
            System.out.print(a[i]+"x^"+ (i));
        }
        System.out.println();
        System.out.println("f("+x+") = "+ (y) );
        input.close();
    }
}
