package r;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.log4j.Logger;

import r.db.DbFacade;
import r.utils.Discretizer;
import r.utils.WriterArff;
import features.utils.SessionManager;

/**
 * Genera un ARFF con todos los features de los juegos (timesPlayed, level, time, etc) y el valor
 * de preferencia seteado por los estudiantes en la encuesta. 
 * La preferencia se discretiza en base a la media. 
 * Los registros estan agrupados por usuario y por juego.
 * 
 * @author Juan
 *
 */
@SuppressWarnings("unused")
public class MainGameFeatures {
	private static Logger logger = Logger.getLogger(MainGameFeatures.class);
	private DbFacade db = new DbFacade();
	@SuppressWarnings("rawtypes")
	private Map cacheGameFeatures = new LinkedHashMap<>();
	
	
	public static void main(String[] args) {
		new MainGameFeatures().generateArff("c:/Temp/j-features.arff");
	}
	
//	@SuppressWarnings("unchecked")
	public void generateArff(String arffPath) {
		logger.info("================== Starting Java code ==================");
		SessionManager.beginTransaction();

		// result
		List<Map<String, Object>> result = new ArrayList<>(); 
				
		// get perception - user - game
		List<Map<?, ?>> data = db.getData();
		
		int row = 1;
		DescriptiveStatistics ds = new DescriptiveStatistics();
		for (Map<?, ?> record : data) {
			String game = (String) record.get("game");
			String username = (String) record.get("username");
			Integer perception = (Integer) record.get("perception");
			Integer processing = (Integer) record.get("processing");
			Integer input = (Integer) record.get("input");
			Integer understanding = (Integer) record.get("understanding");

			// get profile
//			Map<?, ?> profile = db.getProfile(game, username);
			
			// get preference 
//			Double preference = getPreferenceByFormula(profile, game);
			Double preference = db.getPreference(username, game);
			if (preference == null) {
				continue;
			}
			ds.addValue(preference);
			
			// get games features
			Map<String,Double> gameFeatures = db.getGameFeaturesByGameAndStudent(game, username);
			if (gameFeatures.isEmpty()) {
				continue;
			}
			
//			Map<String,Double> gameFeatures = (Map<String, Double>) cacheGameFeatures.get(game);
//			if (gameFeatures == null) {
//				gameFeatures = db.getGameFeaturesByGame(game);
//				cacheGameFeatures.put(game, gameFeatures);
//			}
			
			// add record
			Map<String, Object> entry = new LinkedHashMap<>();
			// columns to filter
//			entry.put("row", row++);
			entry.put("game", game);
			entry.put("username", username);
			entry.put("preference", preference);
			// columns learning styles 
//			entry.put("ls_perception", Discretizer.felder(perception));
//			entry.put("ls_processing", Discretizer.felder(processing));
//			entry.put("ls_input", Discretizer.felder(input));
//			entry.put("ls_understanding", Discretizer.felder(understanding));
			entry.put("ls_perception", perception);
			entry.put("ls_processing", processing);
			entry.put("ls_input", input);
			entry.put("ls_understanding", understanding);
			// columns features
			for (Entry<?, ?> feat : gameFeatures.entrySet()) {
				entry.put("f_" + feat.getKey(), feat.getValue());
			}
			
			// add row
			result.add(entry);
		}
		
		SessionManager.commitTransaction();
		
		// discretize preference
		Discretizer.addPreferenceDisc(result, ds);
		
		// generate arff
		new WriterArff().write(arffPath, result);
	}
	
	private Double getPreferenceByFormula(Map<?, ?> profile,
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

}
