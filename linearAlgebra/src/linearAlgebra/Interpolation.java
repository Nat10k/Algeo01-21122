package linearAlgebra;

import java.util.Scanner;
import spl.*;
import matrix.Matrix;

public class Interpolation {
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
            	m.setElmt(i, j, Math.pow(m.getElmt(i, 1), j));
            }
        }
        for(int i= 0; i<n; i++){
            System.out.println("Masukkan y" + (i+1) +"= ");
            m.setElmt(i, n, input.nextDouble());
        }
        m.displayMatrix();
        a = GaussElimination.gaussElim(m, false);
        y = a[0];
        for (int i=1; i<n; i++){
            y += a[i]*Math.pow(x, i);
        }
        System.out.println(y);
        input.close();
    }
}
