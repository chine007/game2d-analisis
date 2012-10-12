package ar.edu.unicen.exa.games.data.analizers.weka.utils;

import java.util.Set;

/**
 * Calcula la precision del modelo
 * 
 * @author Juan
 *
 */
public class HelperPredictionAccuracy {

	/**
	 * Calcula la precision del modelo
	 * 
	 * @param prediction Datos predichos
	 * @param game Juego analizado
	 * @return Precision del modelo
	 */
	public Accuracy getAccuracy(HelperPrediction prediction, String game) {
		Set<String> userNames = prediction.getAllUsernames();
		
		int counter = 0;
		int total = 0;
		for (String username : userNames) {
			HelperPredictionUser userPrediction = prediction.getUserPrediction(username, game);
			if (userPrediction.getUsername().length() > 0 && !userPrediction.getFinalClassification().equals("EQUALS")) {
				if (userPrediction.getRealClass().equals(userPrediction.getFinalClassification())) {
					counter++;
				}
				total++;
			}
		}

		return new Accuracy(total, counter);
	}
	
	/**
	 * Precision
	 * 
	 * @author Juan
	 *
	 */
	public static class Accuracy {
		private int total;
		private int counter;

		public Accuracy(int total, int counter) {
			this.total = total;
			this.counter = counter;
		}

		public float get() {
			return counter/(float)total*100;
		}

		public String getFraction() {
			return counter + "/" + total;
		}
		
	}
}
