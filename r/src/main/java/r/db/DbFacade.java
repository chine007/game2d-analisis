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

import features.model.Feature;
import features.model.Game;
import features.model.ProfileGame;
import features.model.TestGameFeature;
import features.model.TestPreference;
import features.utils.SessionManager;

public class DbFacade {
	private static final int FELDER_INT_HI = -11;
	private static final int FELDER_INT_LO = -5;
	private static final int FELDER_SEN_LO = 5;
	private static final int FELDER_SEN_HI = 11;

	private static final String usergroup = "progexpl2013";
//	private static final String usergroup = "nothing";
	
	
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
				.add(Projections.groupProperty("u.perception"), "perception")
				.add(Projections.groupProperty("u.username"), "username")
				.add(Projections.groupProperty("g.id"), "game")
		)
		.addOrder(Order.asc("game"))
		.addOrder(Order.asc("perception"))
		.addOrder(Order.asc("username"));
		
		return crit.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP).list();
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
	@SuppressWarnings("unchecked")
	public Map<String,Double> getGameFeaturesByJuan(String gameId) {
		List<Feature> features = SessionManager.getSession().createCriteria(Feature.class).list();
		Game game = (Game) SessionManager.getSession().createCriteria(Game.class)
				.add(Restrictions.eq("id", gameId))
				.uniqueResult();
		
		Map<String,Double> gameFeatures = new LinkedHashMap<String, Double>(); 
		for (Feature feature : features) {
			gameFeatures.put(feature.getCode(), 0d);
			if (game.getFeatures().contains(feature)) {
				gameFeatures.put(feature.getCode(), 1d);
			}
		}
		
		return gameFeatures;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String,Double> getGameFeaturesByStudents(String gameId) {
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
	 * UTILS
	 ************************************************************/
	private Criterion felderRestriction() {
		return Restrictions.or(
				Restrictions.between("u.perception", FELDER_INT_HI, FELDER_INT_LO),
				Restrictions.between("u.perception", FELDER_SEN_LO, FELDER_SEN_HI)
		);
	}

	private Criterion usergroupRestriction() {
		return Restrictions.eq("u.userGroup", usergroup).ignoreCase();
	}

}
