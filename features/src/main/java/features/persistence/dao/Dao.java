package features.persistence.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import features.persistence.model.Game;
import features.persistence.model.ProfileGame;
import features.utils.SessionManager;

public class Dao {
	
	/**
	 * Retorna el maximo valor de una propiedad observada de un juego
	 *  
	 * @param game Juego
	 * @param property Propiedad observada del juego
	 * @return
	 */
	public Double getMax(Game game, String property) {
		return ((Number) SessionManager.getSession().createCriteria(Game.class)
		.createAlias("profileGame", "pf")
		.add(Restrictions.eq("id", game.getId()))
		.setProjection(Projections.max("pf." + property))
		.uniqueResult()).doubleValue();
	}

	/**
	 * Retorna los juegos que tienen el feature pasado como parametro
	 * 
	 * @param featureCode Codigo del feature
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Game> getGamesByFeature(String featureCode) {
		return SessionManager.getSession().createCriteria(Game.class)
		.createAlias("categories", "c")
		.createAlias("c.features", "f")
		.add(Restrictions.eq("f.code", featureCode))
		.list();
	}
	
	/**
	 * Retorna los registros del perfil
	 * 
	 * @param game Juego
	 * @param minTimesPlayed Cantidad minima de veces jugadas
	 * @param userGroup Grupo de usuarios a filtrar
	 * @param neutralFilter True si se desea filtar a los estudiantes neutros
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ProfileGame> getProfileGame(Game game, Long minTimesPlayed, String userGroup, boolean neutralFilter) {
		Criteria crit = SessionManager.getSession().createCriteria(ProfileGame.class)
		.add(Restrictions.eq("game.id", game.getId()));
		
		// Filtra por grupo de usuario
		if (userGroup != null) {
			crit.createAlias("user", "u")
			.add(Restrictions.eq("u.userGroup", "ProgExpl2012"));
		}

		// Filtra por valor de percepcion
		if (neutralFilter) {
			crit.createAlias("user", "u")
			.add(Restrictions.not(Restrictions.between("u.perception", -3, 3)));
		}
		
		return crit.add(Restrictions.gt("timesPlayed", minTimesPlayed))
		.setProjection(Projections
			.projectionList()
			.add(Projections.property("user").as("user"))
			.add(Projections.property("game").as("game"))
			.add(Projections.max("timesPlayed").as("timesPlayed"))
			.add(Projections.max("level").as("level"))
			.add(Projections.sum("correctAnswers").as("correctAnswers"))
			.add(Projections.sum("incorrectAnswers").as("incorrectAnswers"))
			.add(Projections.sum("movs").as("movs"))
			.add(Projections.sum("movsIn").as("movsIn"))
			.add(Projections.sum("movsOut").as("movsOut"))
			.add(Projections.sum("timeout").as("timeout"))
			.add(Projections.sum("time").as("time"))
			.add(Projections.groupProperty("user.username"))
		)
		.setResultTransformer(Transformers.aliasToBean(ProfileGame.class))
		.list();
	}
	
}
