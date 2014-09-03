package ar.edu.unicen.exa.genie.data;

import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

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
		data.stream().forEach(row -> 
				row.getProperties().keySet()
					.stream()
					.filter(key -> row.get(key).intValue() == IGenieConstants.DATASET_MISSING_VALUE)
					.forEach(key -> row.put(key, getAverage(row.getFelder(), key, data)))
		);
	}


	/**
	 * Calcula el valor promedio de la propiedad segun su valor de Felder (sensitivo, neutro o intuitivo)
	 * 
	 * @param felder Valor de Felder
	 * @param prop Propiedad a calcular su valor promedio
	 * @param data Datos
	 * @return
	 */
	private float getAverage(final int felder, String prop, List<GenieData> data) {
		// Obtiene el rango en el cual cae el valor de felder del registro
		final int lowerFelder = Arrays.stream(IGenieConstants.FELDER_INTERVALS).filter(val -> felder >= val).max().getAsInt();
		final int upperFelder = Arrays.stream(IGenieConstants.FELDER_INTERVALS).filter(val -> felder < val).findFirst().getAsInt();
		
		// Calcula el promedio de la propiedad teniendo en cuenta su valor de Felder
		DoubleSummaryStatistics dss = data.stream()
				.filter(row -> lowerFelder <= row.getFelder())
				.filter(row -> row.getFelder() < upperFelder)
				.map(row -> row.get(prop))
				.filter(value -> value.intValue() != IGenieConstants.DATASET_MISSING_VALUE)
				.collect(Collectors.summarizingDouble(Float::doubleValue));

		long quantity = dss.getCount();
		float sum = (float)dss.getSum();
		
		if (quantity > 2) {
			return sum/quantity;
		} else {
			return IGenieConstants.DATASET_MISSING_VALUE;
		}
	}
	
}