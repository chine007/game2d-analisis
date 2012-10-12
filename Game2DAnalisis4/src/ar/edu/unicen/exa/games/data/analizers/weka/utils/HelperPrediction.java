package ar.edu.unicen.exa.games.data.analizers.weka.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import weka.core.Instance;

/**
 * Clase helper para obtener resumen de los resultados predichos para todos los
 * juegos. 
 * Resumen: 
 * 			juego1 	juego2 	juego3 
 * user1 	LOW 	NEUTRAL LOW 
 * user2 	HIGH 	LOW		LOW
 * 
 * @author Juan
 * 
 */
public class HelperPrediction {
	private List<HelperPredictionUser> summaries = new ArrayList<HelperPredictionUser>();
	
	
	/**
	 * Agrega una prediccion
	 * 
	 * @param username
	 *            Nombre de usuario
	 * @param game
	 *            Nombre del juego
	 * @param realClass
	 *            Clase real
	 * @param predictedClass
	 *            Clase predicha
	 * @param inst
	 *            Instancia
	 */
	public void add(String username, String game, String realClass,
			String predictedClass, Instance inst) {
		HelperPredictionUser auxUser = new HelperPredictionUser(username, game,
				realClass);
		auxUser.addPrediction(predictedClass, inst);

		for (HelperPredictionUser user : summaries) {
			if (user.equals(auxUser)) {
				user.addPrediction(predictedClass, inst);
				return;
			}
		}

		summaries.add(auxUser);
	}

	/**
	 * Retorna el nombre de todos los juegos analizados
	 * 
	 * @return Nombre de todos los juegos analizados
	 */
	public Set<String> getAllGames() {
		Set<String> games = new TreeSet<String>();
		for (HelperPredictionUser user : summaries) {
			games.add(user.getGame());
		}
		return games;
	}
	
	/**
	 * Retorna el nombre de todos los usuarios
	 * 
	 * @return Nombre de todos los usuarios
	 */
	public Set<String> getAllUsernames() {
		Set<String> users = new TreeSet<String>();
		for (HelperPredictionUser user : summaries) {
			users.add(user.getUsername());
		}
		return users;
	}

	/**
	 * Retorna la prediccion para el usuario y el juego pasado como parametro
	 * 
	 * @param username
	 *            Nombre de usuario
	 * @param game
	 *            Nombre del juego
	 * @return Prediccion para el usuario y el juego pasado como parametro
	 */
	public HelperPredictionUser getUserPrediction(String username, String game) {
		HelperPredictionUser auxUser = new HelperPredictionUser(username, game,
				"");
		for (HelperPredictionUser user : summaries) {
			if (user.equals(auxUser)) {
				return user;
			}
		}
		return new HelperPredictionUser("", "", "");
	}

	/**
	 * Retorna la clase real del usuario
	 * 
	 * @param username
	 *            Nombre de usuario
	 * @return Clase real del usuario
	 */
	public String getUserRealClass(String username) {
		for (HelperPredictionUser user : summaries) {
			if (user.getUsername().equals(username)) {
				return user.getRealClass();
			}
		}
		return "NO CLASS";
	}

}
