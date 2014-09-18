package ar.edu.unicen.exa.genie.tests;

import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import ar.edu.unicen.exa.games.loader.DataLoader;
import ar.edu.unicen.exa.genie.data.GenieData;
import ar.edu.unicen.exa.genie.utils.IGenieConstants;

import com.numericalmethod.suanshu.stats.test.distribution.normality.ShapiroWilk;

/**
 * Analiza si las variables del dataset siguen una distribucion normal 
 * 
 * HO: la distribucion es normal
 * H1: la distribucion NO es normal
 * 
 * 
 * @author juan
 *
 */
public class TestNormalDistributionWithShapiro {

	public static void main(String[] args) {
		// Obtiene los datos
		List<GenieData> data = new DataLoader().loadData();

		// Analiza cada propiedad  
		for (Entry<String, Float> entry : data.get(0).getProperties().entrySet()) {
			// Obtiene la propiedad
			String prop = entry.getKey();
			
			// Obtiene los valores de la propiedad que no son missing values
			List<Float> values = data
					.stream()
					.map(row -> row.get(prop))
					.filter(val -> val.intValue() != IGenieConstants.DATASET_MISSING_VALUE)
					.sorted()
					.collect(Collectors.toList());
			
			// Copia los valores
			double[] array = new double[values.size()];
			Arrays.setAll(array, idx -> values.get(idx));
			
			// Realiza el test
			double alpha = 0.05;
			if (array.length > 0) {
				ShapiroWilk sw = new ShapiroWilk(array);
				System.out.printf("Variable %-30s - p-value = %f - reject H0 = %s%n", prop, sw.pValue(), sw.isNullRejected(alpha));
			}
		}
	}

}
