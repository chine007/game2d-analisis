package r.db;

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
import features.utils.SessionManager;

public class DbFacade {
	private static final int FELDER_INT_HI = -11;
	private static final int FELDER_INT_LO = -5;
	private static final int FELDER_SEN_LO = 5;
	private static final int FELDER_SEN_HI = 11;

//	private static final String usergroup = "progexpl2013";
	private static final String usergroup = "nothing";
	
	
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
	public Map<String,Integer> getGameFeatures(String gameId) {
		List<Feature> features = SessionManager.getSession().createCriteria(Feature.class).list();
		Game game = (Game) SessionManager.getSession().createCriteria(Game.class)
				.add(Restrictions.eq("id", gameId))
				.uniqueResult();
		
		Map<String,Integer> gameFeatures = new LinkedHashMap<String, Integer>(); 
		for (Feature feature : features) {
			gameFeatures.put(feature.getDescription(), 0);
			if (game.getFeatures().contains(feature)) {
				gameFeatures.put(feature.getDescription(), 1);
			}
		}
		
		return gameFeatures;
	}
	
	private Criterion felderRestriction() {
		return Restrictions.or(
				Restrictions.between("u.perception", FELDER_INT_HI, FELDER_INT_LO),
				Restrictions.between("u.perception", FELDER_SEN_LO, FELDER_SEN_HI)
		);
	}

	private Criterion usergroupRestriction() {
		return Restrictions.ne("u.userGroup", usergroup).ignoreCase();
	}

}
