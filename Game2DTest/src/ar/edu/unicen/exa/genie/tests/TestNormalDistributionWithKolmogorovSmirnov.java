package ar.edu.unicen.exa.genie.tests;

import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.commons.math3.stat.inference.KolmogorovSmirnovTest;

import ar.edu.unicen.exa.games.loader.DataLoader;
import ar.edu.unicen.exa.genie.data.GenieData;
import ar.edu.unicen.exa.genie.utils.IGenieConstants;

/**
 * Analiza si las variables del dataset siguen una distribucion normal 
 * 
 * HO: la distribucion es normal
 * H1: la distribucion NO es normal
 * 
 * Si el valor de retorno de {@code KolmogorovSmirnovTest#kolmogorovSmirnovTest} es true se rechaza H0 con un nivel de confianza de 1 - alpha
 * 
 * @author juan
 *
 */
public class TestNormalDistributionWithKolmogorovSmirnov {

	public static void main(String[] args) {
		// Chequea que el test funciona bien
//		NormalDistribution nd = new NormalDistribution(5, 2);
//		SummaryStatistics ss = new SummaryStatistics();
//		double[] n = new double[1000];
//		for (int i = 0; i < 1000; i++) {
//			n[i] = nd.sample();
//			ss.addValue(n[i]);
//		}
//		System.out.println(new KolmogorovSmirnovTest().kolmogorovSmirnovTest(new NormalDistribution(ss.getMean(), ss.getStandardDeviation()), n, 0.01));

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
					.distinct()	// R dice que el test de KS no debe tener valores repetidos
					.sorted()
					.collect(Collectors.toList());
			
			// Copia los valores
			SummaryStatistics ss = new SummaryStatistics();
			values.stream().forEach(ss::addValue);
			
			double[] array = new double[values.size()];
			Arrays.setAll(array, idx -> values.get(idx));
			
			// Realiza el test
			if (array.length > 0) {
				// si p-value < alpha --> rechaza H0
				KolmogorovSmirnovTest kst = new KolmogorovSmirnovTest();
				NormalDistribution nd = new NormalDistribution(ss.getMean(), ss.getStandardDeviation());
				double alpha = 0.01;
				System.out.printf("Variable %-30s - Valor %-5b - p-value %f%n", prop, kst.kolmogorovSmirnovTest(nd, array, alpha), kst.kolmogorovSmirnovTest(nd, array));

//				KolmogorovSmirnov1Sample instance = new KolmogorovSmirnov1Sample(
//						array, new com.numericalmethod.suanshu.stats.distribution.univariate.NormalDistribution(ss.getMean(), ss.getStandardDeviation()),
//						KolmogorovSmirnov.Side.LESS);
//
//				System.out.printf("p-value = %f; test stats = %f%n",
//						instance.pValue(), instance.statistics());
			}
		}
	}
	
	

}
