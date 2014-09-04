package ar.edu.unicen.exa.games.utils.writers;

import java.io.PrintWriter;
import java.util.List;

import org.apache.log4j.Logger;

import smile.learning.DataSet;
import ar.edu.unicen.exa.genie.data.GenieData;
import ar.edu.unicen.exa.genie.data.GenieDataSetBuilder;
import ar.edu.unicen.exa.genie.utils.IGenieConstants;

/**
 * Clase encargada de generar el TXT
 * 
 * @author juan
 *
 */
public class WriterTxt {
	private static final Logger logger = Logger.getLogger(WriterTxt.class);

	
	/**
	 * Genera el TXT
	 * 
	 * @param dataSet Datos
	 * @param fileName Nombre del file a escribir
	 */
	public void write(DataSet dataSet, String fileName) {
		try {
			PrintWriter pw = new PrintWriter(fileName);
			
			// Escribe el nombre de las variables
			for (int i = 0; i < dataSet.getVariableCount(); i++) {
				String variable = dataSet.getVariableId(i); 
				pw.printf("%s ", variable);
			}
			pw.println();

			// Escribe los valores de las variables
			for (int record = 0; record < dataSet.getRecordCount(); record++) {
				for (int var = 0; var < dataSet.getVariableCount(); var++) {
					int value = dataSet.getInt(var, record);
					if (value == IGenieConstants.DATASET_MISSING_VALUE) {
						pw.printf("%8s", "*");
					} else {
						pw.printf("%8d", value);
					}
				}
				pw.println();
			}
			
			pw.close();
		} catch (Exception e) {
			logger.error("Error escribiendo el TXT", e);	
		}
	}
	

	/**
	 * Genera el TXT
	 * 
	 * @param data Datos
	 * @param fileName Nombre del file a escribir
	 */
	public void write(List<GenieData> data, String fileName) {
		write(new GenieDataSetBuilder().build(data), fileName);
	}
	
}
