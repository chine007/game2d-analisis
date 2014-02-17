package r.db;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import features.model.Game;
import features.model.ProfileGame;
import features.model.TestGameFeature;
import features.model.TestPreference;
import features.model.User;
import features.utils.SessionManager;

public class DbFacade {
	private static final int FELDER_INT_HI = -11;
	private static final int FELDER_INT_LO = -1;
	private static final int FELDER_SEN_LO = 1;
	private static final int FELDER_SEN_HI = 11;


	/***********************************************************
	 * MAX VALUES
	 ************************************************************/
	public int getMaxTimesPlayedByGame(String game) {
		return getMaxCriteria(game, "timesPlayed", Projections.projectionList()
				.add(Projections.rowCount(), "timesPlayed")
		).intValue();
	}
	
	public int getMaxTimeByGame(String game) {
		return getMaxCriteria(game, "time", Projections.projectionList()
				.add(Projections.max("time"), "time")
		).intValue();
	}
	
	public int getMaxLevelByGame(String game) {
		return getMaxCriteria(game, "level", Projections.projectionList()
				.add(Projections.max("level"), "level")
		).intValue();
	}
	
	public int getMaxCorrectAnswersByGame(String game) {
		return getMaxCriteria(game, "correctAnswers", Projections.projectionList()
				.add(Projections.sum("correctAnswers"), "correctAnswers")
		).intValue();
	}
	
	public int getMaxIncorrectAnswersByGame(String game) {
		return getMaxCriteria(game, "incorrectAnswers", Projections.projectionList()
				.add(Projections.sum("incorrectAnswers"), "incorrectAnswers")
		).intValue();
	}
	
	private Number getMaxCriteria(String game, String orderProperty, ProjectionList projList) {
		Criteria crit = SessionManager.getSession().createCriteria(ProfileGame.class)
		.createAlias("user", "u")
		.add(Restrictions.eq("game.id", game))
		.add(felderRestriction())
		.add(usergroupRestriction())
		.setProjection(projList
				.add(Projections.groupProperty("user.username"))
		)
		.setMaxResults(1)
		.addOrder(Order.desc(orderProperty));
		
		return (Number)((Object[])crit.uniqueResult())[0];
	}

	/***********************************************************
	 * DATA
	 ************************************************************/
	@SuppressWarnings("unchecked")
	public List<Map<?,?>> getData() {
		Criteria crit = SessionManager.getSession().createCriteria(ProfileGame.class)
		.createAlias("user", "u")
		.createAlias("game", "g")
		.add(felderRestriction())
		.add(usergroupRestriction())
		.setProjection(Projections.projectionList()
				.add(Projections.property("u.perception").as("perception"))
				.add(Projections.property("u.processing").as("processing"))
				.add(Projections.property("u.input").as("input"))
				.add(Projections.property("u.understanding").as("understanding"))
				.add(Projections.groupProperty("u.username"), "username")
				.add(Projections.groupProperty("g.id"), "game")
		)
		.addOrder(Order.asc("game"))
		.addOrder(Order.asc("perception"))
		.addOrder(Order.asc("username"));
		
		return crit.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP).list();
	}

	@SuppressWarnings("unchecked")
	public Map<String, Integer> getLS(String username) {
		Criteria crit = SessionManager.getSession().createCriteria(User.class)
				.add(Restrictions.eq("username", username))
				.setProjection(Projections.projectionList()
				.add(Projections.property("perception").as("perception"))
				.add(Projections.property("processing").as("processing"))
				.add(Projections.property("input").as("input"))
				.add(Projections.property("understanding").as("understanding"))
		);
		
		return (Map<String, Integer>) crit.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP).uniqueResult();
	}

	/***********************************************************
	 * PROFILE
	 ************************************************************/
	public Map<?,?> getProfile(String game, String username) {
		Criteria crit = SessionManager.getSession().createCriteria(ProfileGame.class)
		.createAlias("user", "u")
		.createAlias("game", "g")
		.add(Restrictions.eq("g.id", game))
		.add(Restrictions.eq("u.username", username))
		.add(felderRestriction())
		.add(usergroupRestriction())
		.setProjection(Projections.projectionList()
				.add(Projections.groupProperty("u.username"), "username")
				.add(Projections.groupProperty("u.perception"), "perception")
				.add(Projections.rowCount(), "timesPlayed")
				.add(Projections.max("level"), "level")
				.add(Projections.max("time"), "time")
				.add(Projections.sum("correctAnswers"), "correctAnswers")
				.add(Projections.sum("incorrectAnswers"), "incorrectAnswers")
		)
		.addOrder(Order.asc("username"));
		
		return (Map<?, ?>) crit.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP).uniqueResult();
	}

	/***********************************************************
	 * GAME FEATURES
	 ************************************************************/
	/**
	 * Retorna los valores de features de un juego.
	 * Los valores se calculan como el promedio de todos los valores 
	 * ingresados por los estudiantes (segun la encuesta) para una feature para un juego 
	 * 
	 * @param gameId Id del juego del cual se desean los valores de feature
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String,Double> getGameFeaturesByGame(String gameId) {
		Criteria crit = SessionManager.getSession().createCriteria(TestGameFeature.class)
		.createAlias("feature", "f")
		.add(Restrictions.eq("game.id", gameId))
		.setProjection(Projections.projectionList()
				.add(Projections.property("f.code").as("code"))
				.add(Projections.avg("value"), "value")
				.add(Projections.groupProperty("f.id"))
		)
		.addOrder(Order.asc("f.id"));
		
		List<HashMap> result = crit.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP).list();
		
		Map<String, Double> finalRes = new LinkedHashMap<String, Double>(); 
		for (HashMap map : result) {
			finalRes.put(map.get("code").toString(), Double.valueOf(map.get("value").toString()));
		}
		
		return finalRes;
	}
	
	/**
	 * Retorna los valores de feature ingresado por un estudiante para un juego dado (segun la encuesta) 
	 * 
	 * @param gameId Id del juego del cual se desean los valores de feature
	 * @param username Nombre del estudiante que respondio la encuesta de features
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String,Double> getGameFeaturesByGameAndStudent(String gameId, String username) {
		Criteria crit = SessionManager.getSession().createCriteria(TestGameFeature.class)
		.createAlias("feature", "f")
		.createAlias("user", "u")
		.add(Restrictions.eq("game.id", gameId))
		.add(Restrictions.eq("u.username", username))
		.setProjection(Projections.projectionList()
				.add(Projections.property("f.code").as("code"))
				.add(Projections.property("value"), "value")
		)
		.addOrder(Order.asc("f.id"));
		
		List<HashMap> result = crit.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP).list();
		
		Map<String, Double> finalRes = new LinkedHashMap<String, Double>(); 
		for (HashMap map : result) {
			finalRes.put(map.get("code").toString(), Double.valueOf(map.get("value").toString()));
		}
		
		return finalRes;
	}
	
	/***********************************************************
	 * PREFERENCE
	 ************************************************************/
	public Double getPreference(String username, String game) {
		Number val = (Number) SessionManager.getSession().createCriteria(TestPreference.class)
		.add(Restrictions.eq("user.username", username))
		.add(Restrictions.eq("game.id", game))
		.setProjection(Projections.property("value"))
		.uniqueResult();
		
		return val != null ? val.doubleValue() : null;
	}
	
	/***********************************************************
	 * VARIABLES AND PREFERENCE
	 ************************************************************/
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getVariablesPreference() {
		return SessionManager.getSession().createSQLQuery(
				"SELECT u.username AS username, " +
				"p.id_game AS game, " +
                "u.perception AS perception, " +
                "u.processing AS processing, " +
                "u.input AS input, " +
                "u.understanding AS understanding, " +
                "CAST(pr.value AS UNSIGNED) AS preference, " +
                "count(*) AS v_timesPlayed, " +
                "max(p.level) AS v_level, " +
                "max(p.`time`) AS v_time, " +
                "sum(p.correctAnswers) AS v_correctAnswers, " +
                "sum(p.incorrectAnswers) AS v_incorrectAnswers, " +
                "sum(p.movs) AS v_movs, " +
                "sum(p.movs_in) AS v_movs_in, " +
                "sum(p.movs_out) AS v_movs_out, " +
                "sum(p.help) AS v_help, " +
                "sum(p.timeout) AS v_timeout " +
          "FROM profilegame p " +
          "INNER JOIN user u " +
          "ON u.username = p.username " +
          "INNER JOIN test_preference_mod2 pr " +
          "ON u.username = pr.username AND p.id_game = pr.id_game " +
          "GROUP BY p.username, p.id_game " +
          "ORDER BY p.username, p.id_game")
          .setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)
          .list();
	}

	/***********************************************************
	 * UTILS
	 ************************************************************/
	@SuppressWarnings("unchecked")
	public List<Game> getGames() {
		return SessionManager.getSession().createCriteria(Game.class)
		.list();
	}

	private Criterion felderRestriction() {
		return Restrictions.or(
				Restrictions.between("u.perception", FELDER_INT_HI, FELDER_INT_LO),
				Restrictions.between("u.perception", FELDER_SEN_LO, FELDER_SEN_HI)
		);
	}

	private Criterion usergroupRestriction() {
//		return Restrictions.eq("u.userGroup", "progexpl2013").ignoreCase();
		return Restrictions.ne("u.userGroup", "nothing").ignoreCase();
	}

}
