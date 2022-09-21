package linearAlgebra;
import java.util.*;

public class Main {

	public static void main(String[] args) {
		// Menu
		int choice;
		System.out.println("MENU :");
		System.out.println("1. SPL");
		System.out.println("2. Determinan");
		System.out.println("3. Matriks invers");
		System.out.println("4. Interpolasi polinom");
		System.out.println("5. Interpolasi bicubic");
		System.out.println("6. Regresi linier berganda");
		System.out.println("7. Exit");
		Scanner input = new Scanner(System.in);
		choice = input.nextInt();
		
		if (choice == 1) {
			int splChoice;
			System.out.println("1. Metode eliminasi Gauss");
			System.out.println("2. Metode eliminasi Gauss-Jordan");
			System.out.println("3. Metode matriks invers");
			System.out.println("4. Kaidah Cramer");
			splChoice = input.nextInt();
		}
		else if (choice == 2) {
			int detChoice;
			System.out.println("1. Metode eliminasi Gauss-Jordan");
			System.out.println("2. Metode adjoin");
			detChoice = input.nextInt();
		}
		input.close();
	}

}
