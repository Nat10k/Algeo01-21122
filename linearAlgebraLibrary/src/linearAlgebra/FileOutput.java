package linearAlgebra;

import java.io.*;

// Source : https://www.w3schools.com/java/java_files_create.asp
//			https://www.geeksforgeeks.org/filewriter-class-in-java/
class FileOutput {
	static void printFile(String fileName, String output) {
		try {
			FileWriter writer = new FileWriter(fileName);
			writer.write(output);
			writer.close();
			System.out.println("Hasil sudah tersimpan di file " + fileName);
		}
		catch (IOException e) {
			System.out.println("Error occured");
			e.printStackTrace();
		}
	}
}
