package ar.edu.unicen.exa.tests;

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

import com.numericalmethod.suanshu.stats.test.distribution.kolmogorov.KolmogorovSmirnov;
import com.numericalmethod.suanshu.stats.test.distribution.kolmogorov.KolmogorovSmirnov1Sample;

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
			
			double[] sample = new double[values.size()];
			Arrays.setAll(sample, idx -> values.get(idx));
			
			// Realiza el test
			double alpha = 0.01;
			if (sample.length > 0) {
				// si p-value < alpha --> rechaza H0
				KolmogorovSmirnovTest kst = new KolmogorovSmirnovTest();
				NormalDistribution nd = new NormalDistribution(ss.getMean(), ss.getStandardDeviation());
				System.out.printf("Variable %-30s - p-value = %f - rechaza H0 = %b%n", prop, kst.kolmogorovSmirnovTest(nd, sample), kst.kolmogorovSmirnovTest(nd, sample, alpha));

				// usa la biblioteca suanshu
				KolmogorovSmirnov1Sample kst2 = new KolmogorovSmirnov1Sample(
						sample, new com.numericalmethod.suanshu.stats.distribution.univariate.NormalDistribution(ss.getMean(), ss.getStandardDeviation()),
						KolmogorovSmirnov.Side.TWO_SIDED);
				System.out.printf("Variable %-30s - p-value = %f - rechaza H0 = %b%n", prop, kst2.pValue(), kst2.isNullRejected(alpha));
			}
		}
	}

}
