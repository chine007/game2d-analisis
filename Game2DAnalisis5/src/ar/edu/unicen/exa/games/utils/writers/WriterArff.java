package ar.edu.unicen.exa.games.utils.writers;

import java.io.FileWriter;
import java.util.Arrays;

import org.apache.log4j.Logger;

import smile.learning.DataSet;
import ar.edu.unicen.exa.genie.utils.IGenieConstants;

/**
 * Clase encargada de generar el ARFF
 * 
 * @author juan
 *
 */
public class WriterArff {
	private static final Logger logger = Logger.getLogger(WriterArff.class);

	
	/**
	 * Genera el ARFF
	 * 
	 * @param dataSet Datos
	 */
	public void write(DataSet dataSet) {
		try {
			FileWriter file = new FileWriter(IGenieConstants.FILE_ARFF);
			
			// Escribe el header
			file.write("@relation Genie\n\n");
			
			// Escribe los atributos
			for (int i = 1; i < dataSet.getVariableCount(); i++) {
				String variable = dataSet.getVariableId(i); 
				file.write(String.format("@attribute %s numeric%n", variable));
			}
			
			// Escribe la clase
			String variable = dataSet.getVariableId(0);
			file.write(String.format("@attribute %s %s%n%n", variable, Arrays.toString(IGenieConstants.FELDER_VALUES).replace("[", "{").replace("]", "}"))); 
	
			// Escribe el data
			file.write("@data\n");
			
			// Escribe valores de los atributos
			for (int record = 0; record < dataSet.getRecordCount(); record++) {
				for (int var = 1; var < dataSet.getVariableCount(); var++) {
					int value = dataSet.getInt(var, record);
					if (value == IGenieConstants.DATASET_MISSING_VALUE) {
						file.write("?,");
					} else {
						file.write(value + ",");
					}
				}

				// Escribe valor de la clase
				int value = dataSet.getInt(0, record); 
				file.write(String.format("%s%n", IGenieConstants.FELDER_VALUES[value]));
			}

			file.close();
		} catch (Exception e) {
			logger.error("Error escribiendo el ARFF", e);	
		}
	}
	
}
