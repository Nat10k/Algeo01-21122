package linearAlgebra;

import java.io.*;

class FileOutput {
	static void printFile(String fileName, String output) {
		/** Prosedur untuk menyimpan String output ke File fileName */
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
