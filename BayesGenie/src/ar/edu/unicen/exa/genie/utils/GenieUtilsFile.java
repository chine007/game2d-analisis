package ar.edu.unicen.exa.genie.utils;

import java.io.File;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Clase de utilidades para el manejo de archivos
 * 
 * @author Juan
 *
 */
public abstract class GenieUtilsFile {

	/**
	 * Crea un directorio
	 * 
	 * @param dir Nombre del directorio a crear
	 */
	public static void createDir(String dir) {
		new File(dir).mkdir();
	}
	
	/**
	 * Borra un directorio y todo su contenido
	 * 
	 * @param dir Directorio a borrar
	 */
	public static void deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				deleteDir(new File(dir, children[i]));
			}
		}
		
		dir.delete();
	}

	/**
	 * Lee el contenido de un file
	 * 
	 * @param fileName Nombre del file
	 * @return Contenido de un file
	 * @throws Exception
	 */
	public static String readFile(InputStream fileName) throws Exception {
		StringBuilder text = new StringBuilder();
		Scanner scanner = new Scanner(fileName);

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
