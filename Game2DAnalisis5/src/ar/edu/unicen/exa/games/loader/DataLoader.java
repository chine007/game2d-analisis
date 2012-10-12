package ar.edu.unicen.exa.games.loader;

import static ar.edu.unicen.exa.games.db.IDatabaseUtils.*;
import static ar.edu.unicen.exa.genie.utils.IGenieConstants.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import ar.edu.unicen.exa.games.db.Database;
import ar.edu.unicen.exa.games.utils.UtilsConfig;
import ar.edu.unicen.exa.genie.data.GenieData;

/**
 * Clase encargada de recuperar los datos del usuario
 * 
 * @author Juan
 *
 */
public class DataLoader {
	private static final Logger logger = Logger.getLogger(DataLoader.class);
	private Database db;
	
	
	/**
	 * Constructor
	 */
	public DataLoader() {
		db = new Database();
	}

	/**
	 * Carga los datos del usuario
	 * 
	 * @return
	 */
	public List<GenieData> loadData() {
		List<GenieData> result = new ArrayList<GenieData>();
		
		try {
			// Obtiene los nombres de todos los jugadores que jugaron
			List<String> users = db.getAllUsersPlayed();
			
			// Obtiene la dimension de Felde a analizar
			String felderDimension = UtilsConfig.get().getValue("felder.dimension");
			
			for (String username : users) {
				// Obtiene el valor de Felder del usuario
				Integer felderDimensionValue = db.getFelderDimensionValue(username, felderDimension);
				
				// Datos del usuario
				GenieData data = new GenieData(username, felderDimensionValue); 
				
				// S2_ABSTRACT_LEVELS_WON
				data.put(N2_ABSTRACT_LEVELS_WON, getPropertyValue(username, "*", CATEGORY_ABSTRACT, OPER_COUNT, RESULT_LEVEL_WON));
				data.put(N2_ABSTRACT_MAX_LEVEL, getPropertyValue(username, "level", CATEGORY_ABSTRACT, OPER_MAX, RESULT_LEVEL_WON));
				data.put(N2_ABSTRACT_MEAN_TIME_PLAYED, getPropertyValue(username, "time", CATEGORY_ABSTRACT, OPER_AVG, ""));

				// S2_CONCRETE_LEVELS_WON
				data.put(N2_CONCRETE_LEVELS_WON, getPropertyValue(username, "*", CATEGORY_CONCRETE, OPER_COUNT, RESULT_LEVEL_WON));
				data.put(N2_CONCRETE_TIME_PLAYED, getPropertyValue(username, "time", CATEGORY_CONCRETE, OPER_SUM, ""));
				data.put(N2_CONCRETE_TURNS_PLAYED, getPropertyValue(username, "*", CATEGORY_CONCRETE, OPER_COUNT, ""));
				
				// S2_MEMORY_LEVELS_WON
				data.put(N2_MEMORY_LEVELS_WON, getPropertyValue(username, "*", CATEGORY_MEMORY, OPER_COUNT, RESULT_LEVEL_WON));
				data.put(N2_MEMORY_MAX_LEVEL, getPropertyValue(username, "level", CATEGORY_MEMORY, OPER_MAX, RESULT_LEVEL_WON));
				data.put(N2_MEMORY_MEAN_TIME_PLAYED, getPropertyValue(username, "time", CATEGORY_MEMORY, OPER_AVG, ""));
				data.put(N2_MEMORY_LEVELS_LOST, getPropertyValue(username, "*", CATEGORY_MEMORY, OPER_COUNT, RESULT_LEVEL_LOST));
				data.put(N2_MEMORY_TIME_PLAYED, getPropertyValue(username, "time", CATEGORY_MEMORY, OPER_SUM, ""));
				data.put(N2_MEMORY_TURNS_PLAYED, getPropertyValue(username, "*", CATEGORY_MEMORY, OPER_COUNT, ""));
				
				// S2_PUZZLE_LEVELS_WON
				data.put(N2_PUZZLE_LEVELS_WON, getPropertyValue(username, "*", CATEGORY_PUZZLE, OPER_COUNT, RESULT_LEVEL_WON));
				data.put(N2_PUZZLE_MAX_LEVEL, getPropertyValue(username, "level", CATEGORY_PUZZLE, OPER_MAX, RESULT_LEVEL_WON));
				data.put(N2_PUZZLE_MOVS, getPropertyValue(username, "movs", CATEGORY_PUZZLE, OPER_SUM, ""));
//				data.put(N2_PUZZLE_HELP, getPropertyValue(username, "help", CATEGORY_PUZZLE, OPER_SUM, ""));
//				data.put(N2_PUZZLE_TIMEOUT, getPropertyValue(username, "timeout", CATEGORY_PUZZLE, OPER_SUM, ""));
				data.put(N2_PUZZLE_LEVELS_LOST, getPropertyValue(username, "*", CATEGORY_PUZZLE, OPER_COUNT, RESULT_LEVEL_LOST));
				
				result.add(data);
				logger.debug(data);
			}
		} catch (Exception e) {
			logger.error("Error al cargar los datos", e);
		} 
		
		return result;
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	private Float getPropertyValue(String username, String prop, String category, String oper, String won) throws Exception {
		Map<String, String> newParameters = new HashMap<String, String>();
		newParameters.put("username", username);
		newParameters.put("prop", prop);
		newParameters.put("category", category);
		newParameters.put("oper", oper);
		newParameters.put("won", won);
		
		return db.getPropertyValue(newParameters);
	}
	
}
