package ar.edu.unicen.exa.games.utils.writers;

import java.io.FileWriter;
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
			FileWriter file = new FileWriter(fileName);
			
			// Escribe el nombre de las variables
			for (int i = 0; i < dataSet.getVariableCount(); i++) {
				String variable = dataSet.getVariableId(i); 
				file.write(String.format("%s ", variable));
			}
			file.write(String.format("%n"));

			// Escribe los valores de las variables
			for (int record = 0; record < dataSet.getRecordCount(); record++) {
				for (int var = 0; var < dataSet.getVariableCount(); var++) {
					int value = dataSet.getInt(var, record);
					if (value == IGenieConstants.DATASET_MISSING_VALUE) {
						file.write(String.format("%8s", "*"));
					} else {
						file.write(String.format("%8d", value));
					}
				}
				file.write(String.format("%n"));
			}
			
			file.close();
		} catch (Exception e) {
			logger.error("Error escribiendo el ARFF", e);	
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
