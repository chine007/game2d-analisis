package ar.edu.unicen.exa.games.utils;

import java.util.List;

import ar.edu.unicen.exa.games.gameschema.XmlDiscretization;
import ar.edu.unicen.exa.games.gameschema.XmlDiscretizer;
import ar.edu.unicen.exa.games.gameschema.XmlGameProperties;
import ar.edu.unicen.exa.games.gameschema.XmlInterval;
import ar.edu.unicen.exa.games.gameschema.XmlProperty;

public abstract class UtilsXmlGameProperties {

	/**
	 * Retorna los intervalos de discretizacion de la propiedad pasada como
	 * parametro
	 * 
	 * @param disc
	 *            Datos de discretizacion
	 * @param propertyName
	 *            Nombre de la propiedad
	 * @return Intervalos de discretizacion
	 */
	public static List<XmlInterval> getXmlIntervals(XmlDiscretization disc, String propertyName) {
		for (XmlProperty property : disc.getProperty()) {
			if (property.getName().equals(propertyName)) {
				return property.getInterval();
			}
		}

		throw new IllegalArgumentException("Error al obtener los intervalos de " + propertyName);
	}

	/**
	 * Retorna los tags de la propiedad pasada como parametro
	 * 
	 * @param props
	 *            Propiedades de los juegos
	 * @param propertyName
	 *            Nombre de la propiedad
	 * @return Tags de la propiedad pasada como parametro
	 */
	public static String getTags(XmlGameProperties props, String propertyName) {
		for (XmlDiscretizer xmlDiscretizer : props.getDiscretization().getDiscretizer()) {
			if (xmlDiscretizer.getName().equals(propertyName)) {
				return xmlDiscretizer.getTags();
			}
		}

		throw new IllegalArgumentException("Error al obtener los tags de la propiedad " + propertyName);
	}
	
	/**
	 * Retorna las propiedades a analizar
	 * 
	 * @param props
	 *            Propiedades de los juegos
	 * @return Propiedades a analizar
	 */
	public static String[] getXmlAnalizedProperties(XmlGameProperties props) {
		return props.getAnalizedProperties().split(UtilsConfig.get().getValue("data.separator"));
	}
	
}
