package ar.edu.unicen.exa.genie.utils;

import java.util.ArrayList;
import java.util.List;

import smile.learning.DataSet;
import ar.edu.unicen.exa.genie.data.GenieData;

/**
 * Clase de utilidades
 * 
 * @author Juan
 *
 */
public abstract class AbstractGenieUtils {
	/** Datos de los usuario */
	private static List<GenieData> data;
	

	/**
	 * Retorna un data set de training copiando el data set original menos
	 * el registro pasado como parametro
	 * 
	 * @param dataSet Data set original
	 * @param recordToDiscard Registro a descartar
	 * @return
	 */
	public static DataSet getTrainingSet(DataSet dataSet, int recordToDiscard) {
		DataSet result = new DataSet();
		
		// Crea el header
		for (int i = 0; i < dataSet.getVariableCount(); i++) {
			String nodeId = dataSet.getVariableId(i);
			result.addIntVariable(nodeId, IGenieConstants.DATASET_MISSING_VALUE);
		}
		
		// Crea el cuerpo
		int recordIndex = 0;
		for (int record = 0; record < dataSet.getRecordCount(); record++) {
			if (record != recordToDiscard) {
				result.addEmptyRecord();
				
				for (int variable = 0; variable < dataSet.getVariableCount(); variable++) {
					result.setInt(variable, recordIndex, dataSet.getInt(variable, record));
				}
				
				recordIndex++;
			}
		}
		
		return result;
	}

	/**
	 * Retorna un data set de testing
	 * 
	 * @param dataSet Data set original
	 * @param recordToCopy Registro de testing
	 * @return
	 */
	public static DataSet getTestSet(DataSet dataSet, int recordToCopy) {
		DataSet result = new DataSet();
		
		// Crea el header
		for (int i = 0; i < dataSet.getVariableCount(); i++) {
			String nodeId = dataSet.getVariableId(i);
			result.addIntVariable(nodeId, IGenieConstants.DATASET_MISSING_VALUE);
		}
		
		// Crea el cuerpo
		result.addEmptyRecord();
		for (int variable = 0; variable < dataSet.getVariableCount(); variable++) {
			result.setInt(variable, 0, dataSet.getInt(variable, recordToCopy));
		}

		return result;
	}

	/**
	 * Retorna los datos de un usuario
	 * 
	 * @param record
	 */
	public static GenieData getData(int record) {
		return data.get(record);
	}

	/**
	 * Setea los datos de los usuarios
	 * 
	 * @param data
	 */
	public static void setData(List<GenieData> data) {
		AbstractGenieUtils.data = new ArrayList<GenieData>(data);
	}
}
