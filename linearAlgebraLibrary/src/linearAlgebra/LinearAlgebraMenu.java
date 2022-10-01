package linearAlgebra;
import java.util.*;

public class LinearAlgebraMenu {
	/** Class utama library linearAlgebra */
	public static void menu() {
		/** Menu utama library linearAlgebra */
		int choice;
		boolean exit = false;
		Scanner input = new Scanner(System.in);
		String outputFile;
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
				System.out.println("3. Metode matriks invers (khusus matriks persegi)");
				System.out.println("4. Kaidah Cramer (khusus matriks persegi)");
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
				else if (splChoice == 4) {
					SPL.cramer(input);
				}
			}
			else if (choice == 2) {
				int detChoice;
				System.out.println("1. Metode reduksi baris");
				System.out.println("2. Metode kofaktor");
				detChoice = input.nextInt();
				if (detChoice == 1) {
					double det = Determinant.determinanReduction(input);
					System.out.println("Tulis hasil ke file ? Y/N");
					if (input.next().equals("Y")) {
						System.out.println("Tuliskan nama file");
						outputFile = input.next();
						FileOutput.printFile(outputFile, "Determinan : " + det);
					}
					System.out.println("Determinan : " + det);
				}
				else if (detChoice == 2) {
					double det = Determinant.determinanCofactor(input);
					System.out.println("Tulis hasil ke file ? Y/N");
					if (input.next().equals("Y")) {
						System.out.println("Tuliskan nama file");
						outputFile = input.next();
						FileOutput.printFile(outputFile, "Determinan : " + det);
					}
					System.out.println("Determinan : " + det);
				}
			}
			else if (choice == 3) {
				int invChoice;
				System.out.println("1. Metode Gauss-Jordan");
				System.out.println("2. Metode adjoint");
				invChoice = input.nextInt();
				if (invChoice == 1) {
					Matrix inverse = Inverse.inverseGaussJordan(input);
					System.out.println("Tulis hasil ke file ? Y/N");
					if (input.next().equals("Y")) {
						System.out.println("Tuliskan nama file");
						outputFile = input.next();
						inverse.displayMatrix(outputFile);
					}
					else {
						inverse.displayMatrix();
					}
				}
				else if (invChoice == 2) {
					Matrix inverse = Inverse.inverseAdjoint(input);
					System.out.println("Tulis hasil ke file ? Y/N");
					if (input.next().equals("Y")) {
						System.out.println("Tuliskan nama file");
						outputFile = input.next();
						inverse.displayMatrix(outputFile);
					}
					else {
						inverse.displayMatrix();
					}
				}
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
			
			if (!exit) {
				System.out.println("Continue ? Y/N");
				if (input.next().equals("N")) {
					exit = true;
				}
			}
		}
		input.close();
	}
}