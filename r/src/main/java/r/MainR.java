package r;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import r.db.DbFacade;
import r.utils.WritterArff;
import features.utils.SessionManager;

public class MainR {
	private DbFacade db = new DbFacade();

	
	public static void main(String[] args) {
		new MainR().generateArff("c:/Temp/r.arff");
	}
	
	public void generateArff(String arffPath) {
		SessionManager.beginTransaction();

		// result
		List<Map<String, Object>> result = new ArrayList<>(); 
				
		// get perception - user - game
		List<Map<?, ?>> data = db.getData();
		
		for (Map<?, ?> record : data) {
			String game = (String) record.get("game");
			String username = (String) record.get("username");
			Integer perception = (Integer) record.get("perception");

			// get profile
			Map<?, ?> profile = db.getProfile(game, username);
			
			// get preference 
			double preference = getPreference(profile, game);
			String preferenceDisc = getPreferenceDisc(preference);
			
			// get games features
			Map<String,Integer> gameFeatures = db.getGameFeatures(game);
			
			// generate output
			Map<String, Object> entry = new LinkedHashMap<>();
			entry.put("game", game);
			entry.put("username", username);
			entry.put("perception", perception);
			entry.putAll(gameFeatures);
			entry.put("preferenceDisc", preferenceDisc);
			entry.put("preference", preference);
			result.add(entry);
		}
		
		SessionManager.commitTransaction();
		
		new WritterArff().write(arffPath, result);
	}
	
	private double getPreference(Map<?, ?> profile,
			String game) {
		double maxTimesPlayed = db.getMaxTimesPlayedByGame(game);
		double maxTime = db.getMaxTimeByGame(game);
		double maxLevel = db.getMaxLevelByGame(game);
		
		double profileTimesPlayed = ((Number)profile.get("timesPlayed")).doubleValue();
		double profileTime = ((Number)profile.get("time")).doubleValue();
		double profileLevel = ((Number)profile.get("level")).doubleValue();
		return 0.33 * (profileTimesPlayed/maxTimesPlayed + 
				profileTime/maxTime + 
				profileLevel/maxLevel);
	}

	private String getPreferenceDisc(double preference) {
		if (preference <= .6) {
			return "B";
		} else {
			return "A";
		}
	}

}