package ar.edu.unicen.exa.genie.data;

import java.util.Arrays;

import org.apache.log4j.Logger;

import smile.SMILEException;
import smile.learning.DataSet;
import ar.edu.unicen.exa.genie.utils.IGenieConstants;


/**
 * Clase encargada de crear y manipular el data set
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
		// variable a inferir
		discretize(dataSet, DataSet.DiscretizationAlgorithmType.UniformWidth, 0, IGenieConstants.FELDER_VALUES.length);

		// variables observables
		for (int i = 1; i < dataSet.getVariableCount(); i++) {
			discretize(dataSet, DataSet.DiscretizationAlgorithmType.UniformWidth, i, IGenieConstants.LEVEL2_NODES_VALUES.length);
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
	
}