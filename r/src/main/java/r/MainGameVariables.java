package r;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.log4j.Logger;

import r.db.DbFacade;
import r.utils.Discretizer;
import r.utils.WriterArff;
import features.utils.SessionManager;


/**
 * Genera un ARFF con todas las variables de los juegos (timesPlayed, level, time, etc) y el valor
 * de preferencia seteado por los estudiantes en la encuesta.
 * Los registros estan agrupados por usuario y por juego.
 * 
 * @author Juan
 *
 */
public class MainGameVariables {
	private static Logger logger = Logger.getLogger(MainGameVariables.class);
	private DbFacade db = new DbFacade();
	
	
	public static void main(String[] args) {
		new MainGameVariables().generateArff("c:/Temp/j-variables.arff");
	}
	
	public void generateArff(String arffPath) {
		logger.info("================== Starting Java code ==================");
//		SessionManager.beginTransaction();
//
//		List<Map<String, Object>> result = db.getVariablesPreference();
//		
//		SessionManager.commitTransaction();
//		
//		// generate arff
//		new WriterArff().write(arffPath, result);

		SessionManager.beginTransaction();

		List<Map<String, Object>> result = new ArrayList<>();
		DescriptiveStatistics ds = new DescriptiveStatistics();
		
		List<Map<String, Object>> data = db.getVariablesPreference();
		
		for (Map<String, Object> record : data) {
			String game = (String) record.remove("game");
			String username = (String) record.remove("username");
			Integer preference = ((java.math.BigInteger) record.remove("preference")).intValue();
			Integer perception = (Integer) record.remove("perception");
			Integer processing = (Integer) record.remove("processing");
			Integer input = (Integer) record.remove("input");
			Integer understanding = (Integer) record.remove("understanding");
			
			ds.addValue(preference);

			// add record
			Map<String, Object> entry = new LinkedHashMap<>();
			// columns to filter
			entry.put("game", game);
			entry.put("username", username);
			entry.put("preference", preference);
			// columns data
			entry.put("ls_perception", perception);
			entry.put("ls_processing", processing);
			entry.put("ls_input", input);
			entry.put("ls_understanding", understanding);
			entry.putAll(record);
			result.add(entry);
		}
		
		SessionManager.commitTransaction();

		// discretize preference
		Discretizer.addPreferenceDisc(result, ds);
		
		// generate arff
		new WriterArff().write(arffPath, result);
	}

}
