package r;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.log4j.Logger;

import r.db.DbFacade;
import r.utils.WriterArff;
import features.utils.SessionManager;

public class MainR {
	private static Logger logger = Logger.getLogger(MainR.class);
	private DbFacade db = new DbFacade();
	
	public static void main(String[] args) {
		new MainR().generateArff("c:/Temp/r.arff");
	}
	
	public void generateArff(String arffPath) {
		logger.info("================== Starting Java code ==================");
		SessionManager.beginTransaction();

		// result
		List<Map<String, Object>> result = new ArrayList<>(); 
				
		// get perception - user - game
		List<Map<?, ?>> data = db.getData();
		
		DescriptiveStatistics ds = new DescriptiveStatistics();
		for (Map<?, ?> record : data) {
			String game = (String) record.get("game");
			String username = (String) record.get("username");
			Integer perception = (Integer) record.get("perception");

			// get profile
			Map<?, ?> profile = db.getProfile(game, username);
			
			// get preference 
			double preference = getPreference(profile, game);
			ds.addValue(preference);
			
			// get games features
			Map<String,Integer> gameFeatures = db.getGameFeatures(game);
			
			// add record
			Map<String, Object> entry = new LinkedHashMap<>();
			// columns to filter
			entry.put("game", game);
			entry.put("username", username);
			entry.put("preference", preference);
			// columns data
			entry.put("perception", perception);
			entry.putAll(gameFeatures);
			result.add(entry);
		}
		
		SessionManager.commitTransaction();
		
		// discretize preference
		discretizePreference(result, ds.getPercentile(50));
		
		// generate arff
		new WriterArff().write(arffPath, result);
	}
	
	private double getPreference(Map<?, ?> profile,
			String game) {
		// max values
		double maxTimesPlayed = getMax(db.getMaxTimesPlayedByGame(game));
		double maxTime = getMax(db.getMaxTimeByGame(game));
		double maxLevel = getMax(db.getMaxLevelByGame(game));
		double maxCorrectAnswers = getMax(db.getMaxCorrectAnswersByGame(game));
		double maxIncorrectAnswers = getMax(db.getMaxIncorrectAnswersByGame(game));
		
		// profile values
		double profileTimesPlayed = ((Number)profile.get("timesPlayed")).doubleValue();
		double profileTime = ((Number)profile.get("time")).doubleValue();
		double profileLevel = ((Number)profile.get("level")).doubleValue();
		double profileCorrectAnswers = ((Number)profile.get("correctAnswers")).doubleValue();
		double profileIncorrectAnswers = ((Number)profile.get("incorrectAnswers")).doubleValue();
		
		// factors
		int factorTimesPlayed = 100;
		int factorTime = 10;
		int factorLevel = 0;
		int factorCorrectAnswers = 0;
		int factorIncorrectAnswers = 0;
		int factorTotal = factorTimesPlayed + factorTime + factorLevel
				+ factorCorrectAnswers + factorIncorrectAnswers;
		
		// preference
		double val = (
				factorTimesPlayed * profileTimesPlayed/maxTimesPlayed + 
				factorTime * profileTime/maxTime + 
				factorLevel * profileLevel/maxLevel +
				factorCorrectAnswers * profileCorrectAnswers/maxCorrectAnswers +
				factorIncorrectAnswers * profileIncorrectAnswers/maxIncorrectAnswers
		) / factorTotal;
		
		// checking
		if (val > 1) {
			throw new AssertionError("Error al analizar el juego " + game);
		}
		
		return val;
	}

	private double getMax(Number value) {
		if (value.doubleValue() == 0) {
			return 1.0;
		}
		return value.doubleValue();
	}

	private void discretizePreference(List<Map<String, Object>> result, double median) {
		logger.info("Median: " + median);
		
		for (Map<String, Object> entry : result) {
			entry.put("preferenceDisc", "B");
			if (((Number)entry.get("preference")).doubleValue() > median) {
				entry.put("preferenceDisc", "A");
			}
		}
	}

}
