package linearAlgebra;
import java.util.*;

public class Main {
	public static void main(String[] args) {
		// Menu
		int choice;
		boolean exit = false;
		Scanner input = new Scanner(System.in);
		while (!exit) {
			System.out.println("MENU :");
			System.out.println("1. SPL");
			System.out.println("2. Determinan");
			System.out.println("3. Matriks invers");
			System.out.println("4. Interpolasi polinom");
			System.out.println("5. Interpolasi bicubic");
			System.out.println("6. Regresi linier berganda");
			System.out.println("7. Exit");
			choice = input.nextInt();
			
			if (choice == 1) {
				int splChoice;
				System.out.println("1. Metode eliminasi Gauss");
				System.out.println("2. Metode eliminasi Gauss-Jordan");
				System.out.println("3. Metode matriks invers");
				System.out.println("4. Kaidah Cramer");
				splChoice = input.nextInt();
				if (splChoice == 1) {
					SPL.gaussElim(input);
				}
				else if (splChoice == 2) {
					SPL.gaussjordanElim(input);
				}
				else if (splChoice == 3) {
					SPL.solveSPLInverse(input);
				}
			}
			else if (choice == 2) {
				int detChoice;
				System.out.println("1. Metode eliminasi Gauss-Jordan");
				System.out.println("2. Metode kofaktor");
				detChoice = input.nextInt();
			}
			else if (choice == 3) {
				int invChoice;
				System.out.println("1. Metode Gauss-Jordan");
				System.out.println("2. Metode adjoint");
				invChoice = input.nextInt();
			}
			else if (choice == 4) {
				Interpolation.interpolasiPolinom(input);
			}
			else if (choice == 5) {
				Bicubic.bicubic(input);
			}
			else if (choice == 6) {
				MultiLinearRegression.multiRegression(input);
			}
			else {
				exit = true;
			}
		}
		input.close();
	}
		
		// Uji coba
//	    Matrix m = new Matrix();
//	    Matrix m2 = new Matrix();
//	    m.readMatrix("./test/testInverse.txt");
//	    GaussJordan.inverseOBE(m).displayMatrix();
//	    m2.readMatrix("./test/testMultiply.txt");
//	    Matrix.multiplyMatrix(m,m2).displayMatrix();
		
//	    m.displayMatrix();
//	    GaussElimination.gaussElim();
//		MultiLinearRegression.multiRegression();
//		Interpolation.interpolasi();
		
//		Bicubic.bicubic();
//		Matrix m = new Matrix();
////	m.displayMatrix();
//		GaussJordan.gaussjordanElim();
		
//		Matrix m = new Matrix();
//		m.readMatrix("./test/testDet.txt");
//		System.out.println(Determinant.determinanCofactor(m));
//		System.out.println(Determinant.determinanReduction(m));
//		Matrix mInverse = Inverse.inverseGaussJordan(m);
//		if (mInverse != null) {
//			mInverse.displayMatrix();
//		}
//		mInverse = Inverse.inverseAdjoint(m);
//		if (mInverse != null) {
//			mInverse.displayMatrix();
//		}
		
//		Inverse.solveSPLInverse();
}

