package ar.edu.unicen.exa.games.utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Scanner;

/**
 * Clase de utilidades para el manejo de archivos
 * 
 * @author Juan
 *
 */
public abstract class UtilsFile {

	/**
	 * Borra un directorio y todo su contenido
	 * 
	 * @param dir Directorio a borrar
	 * @return True si se pudo borrar el directorio
	 */
	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}

		// The directory is now empty so delete it
		return dir.delete();
	}

	/**
	 * Lee el contenido de un file
	 * 
	 * @param fileName Nombre del file
	 * @return Contenido de un file
	 * @throws Exception
	 */
	public static String readFile(String fileName) throws Exception {
		StringBuilder text = new StringBuilder();
		Scanner scanner = new Scanner(new FileInputStream(fileName));

		try {
			while (scanner.hasNextLine()) {
				text.append(scanner.nextLine())
				.append(System.getProperty("line.separator"));
			}
			
			if (text.toString().endsWith(System.getProperty("line.separator"))) {
				text.delete(text.length() - 2, text.length()); //borrar ultimo salto de linea
			}
			
		} finally {
			scanner.close();
		}

		return text.toString();
	}
	
}
