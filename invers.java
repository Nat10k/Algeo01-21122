package com.company;

import java.util.Scanner;
import Matriks.java; 

public class Inverse {
    public static double[][] inverseMatriks(double[][] arr){
        double det = Matriks.detMatriksEK(arr);
        double[][] mNew = Matriks.matriksAdjoint(arr);
        for (int i = 0; i < mNew.length; ++i){
            for (int j = 0; j < mNew.length; ++j){
                mNew[i][j] /= det;
            }
        }
        return mNew;
    }

    public static double[][] solveSPLInverse(double[][] arr){
        int brs = arr.length;
        int kol = arr[0].length;
        double[][] A = new double[brs][kol - 1];
        double[][] B = new double[brs][1];
        for (int i = 0; i < brs; ++i){
            for (int j = 0; j < kol - 1; ++j){
                A[i][j] = arr[i][j];
            }
        }

        for (int i = 0; i < brs; ++i){
            B[i][0] = arr[i][kol - 1];
        }
    }
    public static void main(String[] args) {
        solveSPLInverse();
    }
}