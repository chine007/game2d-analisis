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

@SuppressWarnings("unused")
public class MainCorrelationR {
	private static Logger logger = Logger.getLogger(MainCorrelationR.class);
	private DbFacade db = new DbFacade();
	
	
	public static void main(String[] args) {
		new MainCorrelationR().generateArff("c:/Temp/r.arff");
	}
	
	@SuppressWarnings({ "unchecked" })
	public void generateArff(String arffPath) {
		logger.info("================== Starting Java code ==================");
		SessionManager.beginTransaction();

		List<Map<String, Object>> result = SessionManager.getSession().createSQLQuery(
					"SELECT u.username AS username, " +
					"p.id_game AS game, " +
                    "u.perception AS perception, " +
                    "CAST(pr.value AS UNSIGNED) AS preference, " +
                    "count(*) AS timesPlayed, " +
                    "max(p.level) AS level, " +
                    "max(p.`time`) AS time, " +
                    "sum(p.correctAnswers) AS correctAnswers, " +
                    "sum(p.incorrectAnswers) AS incorrectAnswers, " +
                    "sum(p.movs) AS movs, " +
                    "sum(p.movs_in) AS movs_in, " +
                    "sum(p.movs_out) AS movs_out, " +
                    "sum(p.help) AS help, " +
                    "sum(p.timeout) AS timeout " +
              "FROM profilegame p " +
              "INNER JOIN user u " +
              "ON u.username = p.username " +
              "INNER JOIN test_preference pr " +
              "ON u.username = pr.username AND p.id_game = pr.id_game " +
              "GROUP BY p.username, p.id_game " +
              "ORDER BY p.username, p.id_game")
              .setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)
              .list();
		
		SessionManager.commitTransaction();
		
		// generate arff
		new WriterArff().write(arffPath, result);
	}

}
