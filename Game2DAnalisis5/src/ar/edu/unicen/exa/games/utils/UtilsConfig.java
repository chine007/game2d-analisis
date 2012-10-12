package ar.edu.unicen.exa.games.utils;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Clase de lectura de parametros de configuracion 
 * 
 * @author Juan
 *
 */
public final class UtilsConfig {
	private static final UtilsConfig instance = new  UtilsConfig();
	private static final Logger logger = Logger.getLogger(UtilsConfig.class);
	private Properties properties = new Properties(); 
	
	private UtilsConfig() {
		try {
			properties.load(this.getClass().getResourceAsStream("/config.properties"));
		} catch (IOException e) {
			logger.error("Error al leer el file de propiedades config.properties", e);
		}
	}
	
	public static UtilsConfig get() {
		return instance;
	}
	
	public String getValue(String property) {
		return getValue(property, "");
	}

	public String getValue(String property, String def) {
		if (!properties.containsKey(property)) {
			logger.warn("La propiedad \"" + property + "\" no esta en el file config.properties");
		}
		return properties.getProperty(property, def);
	}
	
	public Integer getValueAsInteger(String property) {
		return Integer.valueOf(getValue(property, "0"));
	}
	
}