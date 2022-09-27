public class Matriks {
        public static double[][] bacaMatriks(int brs, int kol){
            double arr[][] = new double[brs][kol];
            Scanner s = new Scanner(System.in);
            System.out.println("-----------------------");
            for (int i = 0; i < brs; ++i){
                for (int j = 0; j < kol; ++j){
                    arr[i][j] = s.nextDouble();
            }
        }
        System.out.println("-----------------------");
        return arr;
    }

    public static void tulisMatriks(double[][] arr){
        for (double[] brs : arr){
            for (double el : brs){
                System.out.print(el + "  ");
            }
            System.out.print("\n");
        }
    }

    public static double[][] transposeMatriks(double[][] arr){
        double[][] mNew = new double[arr.length][arr.length];
        for (int i = 0; i < arr.length; ++i){
            for (int j = 0; j < arr.length; ++j){
                mNew[i][j] = arr[j][i];
            }
        }
        return mNew;
    }

    public static double[][] kaliMatriks(double[][] m1, double[][] m2){
        //menghasilkan matriks m1 * m2
        //Prekondisi : jumlah kolom m1 = jumlah baris m2
        double mNew[][] = new double[m1.length][m2[0].length];
        for (int i = 0; i < mNew.length; ++i){
            for (int j = 0; j < mNew[0].length; ++j){
                mNew[i][j] = 0;
                for (int k = 0; k < m2.length; ++k){
                    mNew[i][j] += (m1[i][k] * m2[k][j]);
                }
            }
        }
        return mNew;

    }

    public static double detMatriksEK(double[][] arr) {
        //determinan Matriks dengan metode Ekspansi Kofaktor
        double det = 0;
        if (arr.length == 2) {
            det += ((arr[0][0] * arr[1][1]) - (arr[0][1] * arr[1][0]));
        } else if (arr.length == 1){
            det += arr[0][0];
        } else {
            int i = 0;
            for (int j = 0; j < arr.length; ++j){
                double temp[][] = new double[arr.length - 1][arr.length - 1];
                int iTemp = 0;
                int jTemp = 0;
                //Mengisi temp
                for (int brs = 0; brs < arr.length; ++brs){
                    for (int kol = 0; kol < arr.length; ++kol){
                        if ((brs != i) && (kol != j)){
                            temp[iTemp][jTemp] = arr[brs][kol];
                            jTemp += 1;
                            if (jTemp == temp.length){
                                jTemp = 0;
                                iTemp += 1;
                            }
                        }
                    }
                }
                det += (Math.pow(-1, (i + j)) * arr[i][j] *  detMatriksEK(temp));
            }
        }
        return det;
    }

    public static double[][] matriksKofaktor(double[][] arr){
        double[][] mNew = new double[arr.length][arr.length];
        for (int i = 0; i < arr.length; ++i){
            for (int j = 0; j < arr.length; ++j){
                double temp[][] = new double[arr.length - 1][arr.length - 1];
                int iTemp = 0;
                int jTemp = 0;
                //Mengisi temp
                for (int brs = 0; brs < arr.length; ++brs){
                    for (int kol = 0; kol < arr.length; ++kol){
                        if ((brs != i) && (kol != j)){
                            temp[iTemp][jTemp] = arr[brs][kol];
                            jTemp += 1;
                            if (jTemp == temp.length){
                                jTemp = 0;
                                iTemp += 1;
                            }
                        }
                    }
                }
                mNew[i][j] = (Math.pow(-1, (i + j)) *  detMatriksEK(temp));
            }
        }
        return mNew;
    }

    public static double[][] matriksAdjoint(double[][] arr){
        return transposeMatriks(matriksKofaktor(arr));
    }
        //SPL A . x = B -> x = inverse(A) . B
        double[][] inverseA = inverseMatriks(A);
        double[][] x = Matriks.kaliMatriks(inverseA, B);
        return x;
    }

    public static void main(String args[]) {
        solveSPLInverse();
    }
