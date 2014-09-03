package ar.edu.unicen.exa.genie.data;

import java.util.List;
import java.util.Map.Entry;

import ar.edu.unicen.exa.genie.utils.IGenieConstants;


/**
 * Clase encargada de reemplazar los valores faltantes
 * 
 * @author Juan
 * 
 */
public class GenieDataSetReplacer {


	/**
	 * Reemplaza los valores faltantes
	 * 
	 * @param data Datos
	 */
	public void replaceMissingValues(List<GenieData> data) {
		for (GenieData row : data) {
			int felder = row.getFelder();
			
			for (Entry<String, Float> entry : row.getProperties().entrySet()) {
				String prop = entry.getKey();
				float value = entry.getValue();
				
				if (value == IGenieConstants.DATASET_MISSING_VALUE) {
					float newValue = getAverage(felder, prop, data);
					row.put(prop, newValue);
				}
			}
		}
	}


	/**
	 * Calcula el valor promedio de la propiedad segun su valor de Felder (sensitivo, neutro o intuitivo)
	 * 
	 * @param felder Valor de Felder
	 * @param prop Propiedad a calcular su valor promedio
	 * @param data Datos
	 * @return
	 */
	private float getAverage(int felder, String prop, List<GenieData> data) {
		// Obtiene el rango en el cual cae el valor de felder del registro
		int lowerFelder = 0;
		int upperFelder = 0;
		for (int i = 1; i < IGenieConstants.FELDER_INTERVALS.length; i++) {
			if (felder < IGenieConstants.FELDER_INTERVALS[i]) {
				lowerFelder = IGenieConstants.FELDER_INTERVALS[i - 1];
				upperFelder = IGenieConstants.FELDER_INTERVALS[i];
				break;
			}
		}
		
		// Calcula el promedio de la propiedad teniendo en cuenta su valor de Felder
		float sum = 0;
		int quantity = 0;
		for (GenieData row : data) {
			int rowFelder = row.getFelder();
			
			if (lowerFelder <= rowFelder && rowFelder < upperFelder) {
				float rowValue = row.get(prop);
				
				if (rowValue != IGenieConstants.DATASET_MISSING_VALUE) {
					sum += rowValue;
					quantity++;
				}
			}
		}
		
		if (quantity > 2) {
			return sum/quantity;
		} else {
			return IGenieConstants.DATASET_MISSING_VALUE;
		}
	}
	
}