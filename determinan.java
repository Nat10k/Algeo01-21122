package com.company;

import java.util.*;
public class determinan{
    static void cofactor(double M[][],int N, double temp[][], int a, int b){
        int i =0;
        int j = 0;
        for (int k = 0 ; k < N ; k++){
            for (int l = 0 ; l < N; l++){
                if ((k != a) && (l!=b)){
                    temp[i][j++] = M[k][l];
                    if (j == N-1){
                        j = 0;
                        i++;
                    }
                }
            }
        }
    }

    public static double determinanCofactor(double M[][], int N){
        if (N==2){
            return ((M[0][0] * M[1][1]) - (M[0][1] * M[1][0])) ;
        }
        else{
            double det = 0;
            double temp[][] = new double[N-1][N-1];
            int tanda = 1;
            for (int k=0; k < N; k++){
                cofactor(M,N,temp,0,k);
                det += tanda * M[0][k] * determinanCofactor(temp,N-1);
                tanda = tanda * (-1);
            }
            return det;
        }

    }

    public static double determinanReduction(double M[][], int N){
        double temp[][] = new double[N][N];
        copyMatrix(M,temp,N);
        int count = rowReduction(M,N);
        double det = M[0][0];
        for (int i = 1; i < N; i++){
            det *= M[i][i];
        }
        copyMatrix(temp,M,N);
        if (count % 2 == 0){
            return det;
        }else{
            return -det;
        }
    }

    static int rowReduction(double M[][],int N){
        int i = 0, l = 0, idx;
        double pengurang;
        int count = 0;
        for (int j = 1; j < N; j++){
            for (int h = j; h < N; h++){
                idx = i+1;
                while ((M[i][l] == 0) && (idx < N)){
                    tukerRow(M,N,i,idx);
                    count++;
                    idx++;
                }
                if (idx == N){
                    continue;}
                pengurang = M[h][l] / M[i][l];
                for (int k = l; k < N; k++){
                    M[h][k] -= M[i][k] * pengurang;
                }
            }

            i++;
            l++;
        }
        return count;
    }

    static void tukerRow(double M[][], int N, int p, int q){
        double temp[] = new double[N];
        for (int j = 0; j < N; j++){
            temp[j] = M[p][j];
            M[p][j] = M[q][j];
            M[q][j] = temp[j];
        }
    }

    public static void copyMatrix(double M[][], double temp[][], int N){
        for (int row = 0 ; row < N ; row++){
            for (int col = 0; col < N; col++){
                temp[row][col] = M[row][col];
            }
        }
    }

    public static void cetakMatrix(double M[][], int N){
        for (int i =0; i < N; i ++){
            for (int j = 0; j < N; j++){
                System.out.print(M[i][j]);
                System.out.print(" ");
            }
            System.out.print("\n");
        }
    }
    public static void main(String[] args) {
        double[][] m = new double[2][2];
        Scanner input = new Scanner(System.in);
        for(int i = 0; i<2; i++){
            for(int j = 0; j<2; j++){
                m[i][j] = input.nextDouble();
            }
        }
        System.out.println(determinanReduction(m,2));
    }
}