package ar.edu.unicen.exa.games.data.analizers.weka.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import weka.core.Instance;
import ar.edu.unicen.exa.games.data.model.Data;

/**
 * Clase helper para almacenar las predicciones de un usuario en un juego 
 * 
 * @author Juan
 *
 */
public class HelperPredictionUser implements Comparable<HelperPredictionUser> {
	/** Nombre de usuario */ 
	private String username;
	/** Nombre del juego */ 
	private String game;
	/** Clase real del usuario (high, neutral, low) */
	private String realClass;
	/** Asocia una clase (high, neutral, low) con la cantidad de predicciones para esa clase */
	private Map<String, Integer> classifications = new TreeMap<String, Integer>();
	/** Listas de instancias predichas */
	private List<Instance> instances = new ArrayList<Instance>();

	
	/**
	 * Constructor
	 * 
	 * @param username
	 *            Nombre de usuario
	 * @param game
	 *            Nombre del juego
	 * @param realClass
	 *            Clase real del usuario
	 */
	public HelperPredictionUser(String username, String game, String realClass) {
		this.username = username;
		this.game = game;
		this.realClass = realClass;
	}

	/**
	 * Agrega una prediccion
	 * 
	 * @param predictedClass
	 *            Clase predicha
	 * @param inst
	 *            Instancia
	 */
	public void addPrediction(String predictedClass, Instance inst) {
		Integer counter = classifications.get(predictedClass);
		if (counter == null) {
			counter = 0;
		}
		counter++;

		classifications.put(predictedClass, counter);
		instances.add(inst);
	}

	/**
	 * Retorna las clases predichas
	 * 
	 * @return Clases predichas
	 */
	public Set<String> getPredictedClasses() {
		return classifications.keySet();
	}

	/**
	 * Retorna la cantidad de veces que se predijo la clase pasada como parametro
	 * 
	 * @param predictedClass
	 *            Clase predicha
	 * @return Cantidad de veces que se predijo la clase pasada como parametro
	 */
	public Integer getValue(String predictedClass) {
		if (classifications.containsKey(predictedClass)) {
			return classifications.get(predictedClass);
		}
		return 0;
	}

	/**
	 * Retorna la clasificacion final del usuario
	 * 
	 * @return Clasificacion final del usuario
	 */
	public String getFinalClassification() {
		int maxValue = Integer.MIN_VALUE;
		String finalClass = "";

		for (String predictedClass : getPredictedClasses()) {
			Integer value = getValue(predictedClass);

			if (maxValue < value) {
				maxValue = value;
				finalClass = predictedClass;
			} else if (maxValue == value) {
				finalClass = "EQUALS";
			}
		}

		return finalClass;
	}

	/**
	 * Retorna el nombre de usuario
	 * 
	 * @return Nombre de usuario
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Retorna el nombre del juego
	 * 
	 * @return Nombre del juego
	 */
	public String getGame() {
		return game;
	}
	
	/**
	 * Retorna la clase real del usuario
	 * 
	 * @return Clase real del usuario
	 */
	public String getRealClass() {
		return realClass;
	}

	/**
	 * Retorna la cantidad de registros analizados
	 * 
	 * @return Cantidad de registros analizados
	 */
	public int getRecordsQuantity() {
		int counter = 0;
		for (int count : classifications.values()) {
			counter += count;
		}
		return counter;
	}

	/**
	 * Retorna las instancias del usuario ordenadas por el atributo {@code Data.TIMES_PLAYED}
	 * 
	 * @return
	 */
	public List<Instance> getInstances() throws Exception {
		if (instances.size() == 0) {
			return instances;
		}
		
		// Obtiene el indice del atributo timesPlayed
		int index = 0;
		Instance inst = instances.get(0);
		for (int i = 0; i < inst.numAttributes(); i++) {
			if (inst.attribute(i).name().equals(Data.TIMES_PLAYED)) {
				index = i;
				break;
			}
		}
		final int timesPlayedIndex = index;
		
		Collections.sort(instances, new Comparator<Instance>() {
			@Override
			public int compare(Instance o1, Instance o2) {
				return (int)(o1.value(timesPlayedIndex) - o2.value(timesPlayedIndex));
			}
		});
		
		return instances;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((game == null) ? 0 : game.hashCode());
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HelperPredictionUser other = (HelperPredictionUser) obj;
		if (game == null) {
			if (other.game != null)
				return false;
		} else if (!game.equals(other.game))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public int compareTo(HelperPredictionUser user) {
		return getUsername().compareTo(user.getUsername());
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getUsername()).append(" ")
		.append(getGame()).append(" (")
		.append(getFinalClassification()).append(") ")
		.append(classifications.toString()).append(" ");
		return sb.toString();
	}

}
