package ar.edu.unicen.exa.games.data.model;

import java.util.HashMap;
import java.util.Map;

import ar.edu.unicen.exa.games.utils.UtilsConfig;

/**
 * Esta clase representa un registro Arff
 * 
 * @author Juan
 *
 */
public class Data {
	public static final String TIMES_PLAYED = "timesPlayed";
	public static final String LEVEL = "level";
	public static final String CORRECT_ANSWERS = "correctAnswers";
	public static final String INCORRECT_ANSWERS = "incorrectAnswers";
	public static final String TIME_PLAYED = "timePlayed";
	public static final String RESULT = "result";
	public static final String RESULT_WON = "resultWon";
	public static final String RESULT_LOST = "resultLost";
	private static final String DISC_SUFIX = "_DISC";
	public static final String CLASS = "class";

	private String username;
	private Map<String, Float> properties;
	private Map<String, String> propertiesDiscretized;
	

	/**
	 * Constructor
	 */
	public Data() {
		properties = new HashMap<String, Float>();
		propertiesDiscretized = new HashMap<String, String>();
	}

	/****************************************************/
	/* METODOS GENERALES */
	/****************************************************/
	public final void setUsername(String username) {
		this.username = username;
	}

	public final String getUsername() {
		return username;
	}

	public static String[] getNumericProperties() {
		return new String[] { Data.TIMES_PLAYED, Data.TIME_PLAYED, Data.LEVEL,
				Data.CORRECT_ANSWERS, Data.INCORRECT_ANSWERS, Data.RESULT_WON,
				Data.RESULT_LOST, Data.RESULT };
	}
	
	/****************************************************/
	/* PROPIEDADES SIN DISCRETIZAR */
	/****************************************************/
	public final void put(String name, Float value) {
		properties.put(name, value);
	}

	public final Float get(String name) {
		return properties.get(name);
	}

	/****************************************************/
	/* PROPIEDADES DISCRETIZADAS */
	/****************************************************/
	public void putDisc(String name, String value) {
		propertiesDiscretized.put(name + DISC_SUFIX, value);
	}

	public String getDisc(String name) {
		return propertiesDiscretized.get(name + DISC_SUFIX);
	}

	public String toStringAnalisis(String[] props) {
		StringBuilder sb = new StringBuilder();
		
		for (String prop : props) {
			String value = getDisc(prop);
			if (value == null) {
				value = get(prop).toString();
			}
			sb.append(value).append(UtilsConfig.get().getValue("data.separator"));
		}
		
		String abs = getDisc(CLASS);
		if (abs == null) {
			abs = get(CLASS).toString();
		}
		sb.append(abs).append(System.getProperty("line.separator"));
		
		return sb.toString();
	}

	/****************************************************/
	/* OBJECT */
	/****************************************************/
	@Override
	public final String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getUsername()).append(UtilsConfig.get().getValue("data.separator"))
		.append(properties.toString());
		return sb.toString();
	}

	@Override
	public final boolean equals(Object obj) {
		Data data = (Data) obj;
		return getUsername().equals(data.getUsername());
	}
}
