package ar.edu.unicen.exa.genie.data;
import static ar.edu.unicen.exa.genie.utils.IGenieConstants.N0_ROOT;

import java.util.List;
import java.util.Map.Entry;

import smile.learning.DataSet;
import ar.edu.unicen.exa.genie.utils.IGenieConstants;


/**
 * Clase encargada de crear y manipular el data set
 * 
 * @author Juan
 * 
 */
public class GenieDataSetBuilder {

	
	/**
	 * Crea el data set
	 * 
	 * @param data Datos del data set
	 */
	public DataSet build(List<GenieData> data) {
		DataSet dataSet = new DataSet();
		
		// Crea los headers
		dataSet.addIntVariable(N0_ROOT, IGenieConstants.DATASET_MISSING_VALUE);
		for (String key : data.get(0).getProperties().keySet()) {
			dataSet.addIntVariable(key, IGenieConstants.DATASET_MISSING_VALUE);
		}
		
		// Crea el body
		int recordIdx = 0;
		for (GenieData dat : data) {
			int propIdx = 0;
			dataSet.addEmptyRecord();

			// variable a inferir
			dataSet.setInt(propIdx++, recordIdx, dat.getFelder());
			
			// variables observables
			for (Entry<String, Float> prop : dat.getProperties().entrySet()) {
				dataSet.setInt(propIdx++, recordIdx, prop.getValue().intValue());
			}
			recordIdx++;
		}

		return dataSet;
	}
}