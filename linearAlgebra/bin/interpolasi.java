import java.util.*;



public class Interpolasi{
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        interpolasi(input, 9.2);

    }

    public static void enterMatix(Scanner input, double[][] matrix, int nRow, int nCol){
        for(int i= 0; i<nRow; i++){
            for(int j = 0; j< nCol; j++){
                System.out.println("M[" + (i+1) + "]" + "[" +(j+1)+"] : ");
                matrix[i][j] = input.nextDouble();
            }
        }
    }
    public static void displayMatrix(double[][] matrix, int nRow, int nCol){
        for(int i= 0; i<nRow; i++){
            for(int j = 0; j< nCol; j++){
                System.out.print(matrix[i][j]+"\t");
            }
            System.out.println();
        }
    }

    

    public static double[] gaussjordan (Scanner input, double[][] m, int nRow, int nCol ){
        int i,j,k;
        double t;
        for(j=0; j<nCol; j++){
            for(i=0; i<nRow; i++)
            {
                if(i!=j)
                {
                    t=m[i][j]/m[j][j];
                    for(k=0; k<nCol+1; k++)
                    {
                        m[i][k]=m[i][k]-t*m[j][k];
                    }
                }
            }
        }
        double[] a = new double[nRow];
        for(i=0; i<nRow; i++) 
        {
            a[i]=m[i][nCol]/m[i][i];
            // System.out.println("a"+ (i) + "= " + a[i]);
        }
        return a;
    }
    public static double pangkat(double a, int b){
        int i;
        double hasil;
        hasil = 1;
        for (i=1; i<= b; i++){
            hasil *= a;
        }
        return hasil;
    }
    public static void interpolasi(Scanner input, double x){
        int n;
        double y;
        System.out.println("Masukkan jumlah titik: ");
        n = input.nextInt();
        double[][] m = new double [n][n+1];
        double[]a = new double[n];
        for (int i = 0; i<n; i++){
            m[i][0] = 1;
        }
        for (int i = 0; i<n; i++){
            System.out.println("Masukkan x" +i);
            m[i][1] = input.nextDouble();
            for(int j=2; j<n; j++){
                m[i][j] = pangkat(m[i][1], (j));
            }
        }
        for(int i= 0; i<n; i++){
            System.out.println("Masukkan y" +(i) +"= ");
            m[i][n] = input.nextDouble();
        }
        displayMatrix(m, n, n+1);
        a = gaussjordan(input, m, n, n);
        y = a[0];
        for (int i = 1; i<n; i++){
            y += a[i]*pangkat(x, i);
        }
        System.out.print("f(x) = ");
        for(int i= n-1; i>=0; i--){
            if (a[i] > 0) {
                System.out.print(" + ");
            }
            System.out.print(a[i]+"x^"+ (i));
        }
        System.out.println("");
        System.out.println("f("+(x)+") = "+ (y) );
        
        
    
    }
}