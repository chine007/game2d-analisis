package ar.edu.unicen.exa.genie.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import ar.edu.unicen.exa.genie.utils.AbstractGenieUtils;
import ar.edu.unicen.exa.genie.utils.IGenieConstants;


/**
 * Clase encargada de reemplazar los valores faltantes
 * 
 * @author Juan
 * 
 */
public class GenieDataSetReplacer {


	/**
	 * Reemplaza los missing values
	 * 
	 * @param data Datos
	 */
	public void replaceMissingValues(List<GenieData> data) {
		for (Entry<String, Float> entry : data.get(0).getProperties().entrySet()) {
			String prop = entry.getKey();
			
			if (hasMissingValues(data, prop)) {
				replace(data, prop);
			}
		}
	}

	/**
	 * Remplaza los missing values de la propiedad
	 *  
	 * @param data Datos
	 * @param prop Propiedad
	 */
	private void replace(List<GenieData> data, String prop) {
		for (int i = 1; i < IGenieConstants.FELDER_INTERVALS.length; i++) {
			// Obtiene rango de Felder
			int lowerFelder = IGenieConstants.FELDER_INTERVALS[i - 1];
			int upperFelder = IGenieConstants.FELDER_INTERVALS[i];
		
			// Obtiene los registros que caen en el intervalo de Felder
			List<GenieData> dataFelder = new ArrayList<GenieData>();
			for (GenieData row : data) {
				int rowFelder = row.getFelder();
				
				if (lowerFelder <= rowFelder && rowFelder < upperFelder) {
					dataFelder.add(row);
				}
			}
			
			// Acumula los valores que no son missing values
			SummaryStatistics ss = new SummaryStatistics();
			for (GenieData row : dataFelder) {
				float rowValue = row.get(prop);
				
				if (!AbstractGenieUtils.isMissingValue(rowValue)) {
					ss.addValue(rowValue);
				}
			}

			// Analiza si realiza el reemplazo
			float porc = 0.2f;
			if (ss.getN()/(float)dataFelder.size() >= porc &&
				ss.getStandardDeviation() != 0) {
				// Crea un random generator
				RandomGenerator rg = new JDKRandomGenerator();
				rg.setSeed(17399225432l);  // Fixed seed means same results every time

				// Crea la distribucion normal
				NormalDistribution nd = new NormalDistribution(ss.getMean(), ss.getStandardDeviation());
				
				// Reemplaza los valores
				for (GenieData row : dataFelder) {
					float rowValue = row.get(prop);
					
					if (AbstractGenieUtils.isMissingValue(rowValue)) {
						row.put(prop, (float)nd.sample());
//						row.put(prop, (float)ss.getMean());
					}
				}
			}
		}
	}

	/**
	 * Retorna true si la propiedad posee missing values
	 * 
	 * @param data Datos
	 * @param prop Propiedad a analizar
	 * @return
	 */
	private boolean hasMissingValues(List<GenieData> data, String prop) {
		for (GenieData row : data) {
			if (AbstractGenieUtils.isMissingValue(row.get(prop))) {
				return true;
			}
		}
		return false;
	}

}