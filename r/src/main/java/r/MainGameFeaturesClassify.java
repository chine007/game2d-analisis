package r;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import r.db.DbFacade;
import r.utils.WriterArff;
import features.model.Game;
import features.utils.SessionManager;

/**
 * Genera un ARFF con los datos de un usuario del que se desea conocer (clasificar) su nivel de preferencia
 * a partir de un modelo (SVM, Bayes, etc) ya generado 
 * 
 * @author Juan
 *
 */
public class MainGameFeaturesClassify {
	private static final Logger logger = Logger.getLogger(MainGameFeaturesClassify.class);
	private static final String username = "246483";

	private DbFacade db = new DbFacade();
	
	
	public static void main(String[] args) {
		String fileName = String.format("c:/Temp/j-features-classify-%s.arff", username);
		new MainGameFeaturesClassify().generateArff(fileName);
	}
	
	public void generateArff(String arffPath) {
		logger.info("================== Starting Java code ==================");
		SessionManager.beginTransaction();

		// result
		List<Map<String, Object>> result = new ArrayList<>(); 
				
		// get LS
		Map<String, Integer> data = db.getLS(username);
		Integer perception = (Integer) data.get("perception");
		Integer processing = (Integer) data.get("processing");
		Integer input = (Integer) data.get("input");
		Integer understanding = (Integer) data.get("understanding");
		
		// get games
		List<Game> games = db.getGames();
		
		// get data
		for (Game game : games) {
			String gameId = game.getId();

			// get preference 
			Double preference = db.getPreference(username, gameId);
			if (preference == null) {
				preference = 0d;
			}
			
			// get games features
//			Map<String,Double> gameFeatures = db.getGameFeaturesByGameAndStudent(gameId, username);
//			if (gameFeatures.isEmpty()) {
//				continue;
//			}
			Map<String,Double> gameFeatures = (Map<String, Double>) db.getGameFeaturesByGame(gameId);
			if (gameFeatures.isEmpty()) {
				continue;
			}
			
			// add record
			Map<String, Object> entry = new LinkedHashMap<>();
			// columns to filter
			entry.put("game", gameId);
			entry.put("username", username);
			entry.put("preference", preference);
			// columns learning styles 
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
		
		// generate arff
		new WriterArff().write(arffPath, result);
	}
	
}
