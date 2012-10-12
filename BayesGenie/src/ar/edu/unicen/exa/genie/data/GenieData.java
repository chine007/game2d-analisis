package ar.edu.unicen.exa.genie.data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Esta clase representa los datos de un usuario
 * 
 * @author Juan
 *
 */
public class GenieData {
	private String username;
	private Integer felder;
	private Map<String, Float> properties;
	

	/**
	 * Constructor
	 */
	public GenieData(String username, Integer felder) {
		setUsername(username);
		setFelder(felder);
		properties = new LinkedHashMap<String, Float>();
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

	public void setFelder(Integer felder) {
		this.felder = felder;
	}

	public Integer getFelder() {
		return felder;
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

	public Map<String, Float> getProperties() {
		return new LinkedHashMap<String, Float>(properties);
	}

	/****************************************************/
	/* OBJECT */
	/****************************************************/
	@Override
	public final String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getUsername()).append("\t")
		.append(getFelder()).append("\t")
		.append(properties.toString());
		return sb.toString();
	}

	@Override
	public final boolean equals(Object obj) {
		GenieData data = (GenieData) obj;
		return getUsername().equals(data.getUsername());
	}
}
