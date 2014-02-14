package r.utils;

import java.util.List;
import java.util.Map;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.log4j.Logger;

public class Discretizer {
	private static Logger logger = Logger.getLogger(Discretizer.class);


	public static void addPreferenceDisc(List<Map<String, Object>> result, DescriptiveStatistics ds) {
		double median = ds.getPercentile(50);
		logger.info("Median: " + median);
		
		for (Map<String, Object> entry : result) {
			entry.put("preferenceDisc", "B");
			if (((Number)entry.get("preference")).doubleValue() > median) {
				entry.put("preferenceDisc", "A");
			}

//			if (((Number)entry.get("preference")).doubleValue() >= ds.getPercentile(66)) {
//				entry.put("preferenceDisc", "A");
//			}
//			else if (((Number)entry.get("preference")).doubleValue() >= ds.getPercentile(33)) {
//				entry.put("preferenceDisc", "M");
//			}
//			else if (((Number)entry.get("preference")).doubleValue() >= 0) {
//				entry.put("preferenceDisc", "B");
//			}
		}
	}
	
	public static String discretizeFelderDimension(Integer value) {
		if (value > 3) {
			return "Positivo";
		} else if (value > -5) {
			return "Neutro";
		} else {
			return "Negativo";
		}
	}
	
}
