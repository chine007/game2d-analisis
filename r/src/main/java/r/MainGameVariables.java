package r;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.log4j.Logger;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import r.db.DbFacade;
import r.utils.WriterArff;
import features.model.ProfileGame;
import features.model.TestGameFeature;
import features.utils.SessionManager;


/**
 * Genera un ARFF con todas las variables de los juegos (timesPlayed, level, time, etc) y el valor
 * de preferencia seteado por los estudiantes en la encuesta.
 * Los registros estan agrupados por usuario y por juego.
 * 
 * @author Juan
 *
 */
@SuppressWarnings("unused")
public class MainGameVariables {
	private static Logger logger = Logger.getLogger(MainGameVariables.class);
	private DbFacade db = new DbFacade();
	
	
	public static void main(String[] args) {
		new MainGameVariables().generateArff("c:/Temp/r.arff");
	}
	
	public void generateArff(String arffPath) {
		logger.info("================== Starting Java code ==================");
		SessionManager.beginTransaction();

		List<Map<String, Object>> result = db.getVariablesPreference();
		
		SessionManager.commitTransaction();
		
		// generate arff
		new WriterArff().write(arffPath, result);
	}

}
