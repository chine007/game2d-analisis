package ar.edu.unicen.exa.genie.data;

import java.util.Arrays;

import org.apache.log4j.Logger;

import smile.SMILEException;
import smile.learning.DataSet;
import ar.edu.unicen.exa.genie.utils.IGenieConstants;


/**
 * Clase encargada de discretizar el data set
 * 
 * @author Juan
 * 
 */
public class GenieDataSetDiscretizer {
	private static final Logger logger = Logger.getLogger(GenieDataSetDiscretizer.class);

	
	/**
	 * Discretiza el data set pasada como parametro
	 * 
	 * @param dataSet
	 * @return
	 */
	public DataSet discretize(DataSet dataSet) {
		// variable a inferir (no usar discretizador uniforme porque usa los valores limites de la variable a discretizar segun el dataset)
		discretize(dataSet, 0, IGenieConstants.FELDER_INTERVALS);

		// variables observables
		for (int i = 1; i < dataSet.getVariableCount(); i++) {
			discretize(dataSet, DataSet.DiscretizationAlgorithmType.UniformCount, i, IGenieConstants.LEVEL2_NODES_VALUES.length);
		}
		
		return dataSet;
	}
	
	private void discretize(DataSet dataSet, int type, int varIdx, int binsNumber) {
		String var = "";
		try {
			var = dataSet.getVariableId(varIdx);
			double bins[] = dataSet.discretize(varIdx, type, binsNumber, null);
			logger.debug(var + ": " + Arrays.toString(bins));
		} catch (SMILEException se) {
			logger.error("Error al discretizar la variable '" + var + "'");
		}
	}

	private void discretize(DataSet dataSet, int varIdx, int[] lowerLimits) {
		for (int i = 0; i < dataSet.getRecordCount(); i++) {
			// obtiene el valor a discretizar
			int value = dataSet.getInt(varIdx, i);
			
			// discretiza el valor
			int discValue = 0;
			for (int j = 1; j < lowerLimits.length; j++) {
				if (value < lowerLimits[j]) {
					discValue = j - 1;
					break;
				}
			}

			// setea valor discretizado
			dataSet.setInt(varIdx, i, discValue);
		}
	}
	
}