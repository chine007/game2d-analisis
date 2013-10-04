package r.db;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import features.persistence.model.Feature;
import features.persistence.model.Game;
import features.persistence.model.ProfileGame;
import features.utils.SessionManager;

public class DbFacade {
	private static final int FELDER_INT_HI = -11;
	private static final int FELDER_INT_LO = -5;
	private static final int FELDER_SEN_LO = 5;
	private static final int FELDER_SEN_HI = 11;
		
	public int getMaxTimesPlayedByGame(String game) {
		return getMaxCriteria(game, "maxTimesPlayed", Projections.projectionList()
				.add(Projections.rowCount(), "maxTimesPlayed")
		).intValue();
	}
	
	public int getMaxTimeByGame(String game) {
		return getMaxCriteria(game, "time", Projections.projectionList()
				.add(Projections.avg("time"), "time")
		).intValue();
	}
	
	public int getMaxLevelByGame(String game) {
		return getMaxCriteria(game, "level", Projections.projectionList()
				.add(Projections.max("level"), "level")
		).intValue();
	}
	
	private Number getMaxCriteria(String game, String orderProperty, ProjectionList projList) {
		Criteria crit = SessionManager.getSession().createCriteria(ProfileGame.class)
		.createAlias("user", "u")
		.add(Restrictions.eq("game.id", game))
		.add(Restrictions
				.or(
					Restrictions.between("u.perception", FELDER_INT_HI, FELDER_INT_LO),
					Restrictions.between("u.perception", FELDER_SEN_LO, FELDER_SEN_HI)
				)
		)
		.setProjection(projList
				.add(Projections.groupProperty("user.username"))
		)
		.addOrder(Order.desc(orderProperty));
		
		@SuppressWarnings("unchecked")
		List<Object[]> res = crit.list();
		
		return (Number) res.get(0)[0];
	}

	@SuppressWarnings("unchecked")
	public List<Map<?,?>> getData() {
		Criteria crit = SessionManager.getSession().createCriteria(ProfileGame.class)
		.createAlias("user", "u")
		.createAlias("game", "g")
		.add(Restrictions
				.or(
					Restrictions.between("u.perception", FELDER_INT_HI, FELDER_INT_LO),
					Restrictions.between("u.perception", FELDER_SEN_LO, FELDER_SEN_HI)
				)
		)
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

	public Map<?,?> getProfile(String game, String username) {
		Criteria crit = SessionManager.getSession().createCriteria(ProfileGame.class)
		.createAlias("user", "u")
		.createAlias("game", "g")
		.add(Restrictions
				.and(
						Restrictions.eq("g.id", game),
						Restrictions.and(
									Restrictions.eq("u.username", username),
									Restrictions.or(
										Restrictions.between("u.perception", FELDER_INT_HI, FELDER_INT_LO),
										Restrictions.between("u.perception", FELDER_SEN_LO, FELDER_SEN_HI)
									)
						)
				)
		)
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

}
