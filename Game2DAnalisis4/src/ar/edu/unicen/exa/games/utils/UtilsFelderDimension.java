package ar.edu.unicen.exa.games.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Clase de utilidades para el mapeo de la discretizacion (High,Neutral,Low) a las 
 * dimensiones de Felder (Sen,Neu,Int - Act,Neu,Ref - etc)
 * 
 * @author Juan
 *
 */
public abstract class UtilsFelderDimension {
	
	/**
	 * Retorna una hash cuyas claves son los valores de discretizacion y los valores
	 * las dimensiones de Felder
	 * 
	 * @return
	 */
	public static Map<String, String> getMappingDiscretizationFelder() {
		String[] mappings = UtilsConfig.get().getFelderMapping().split(",");
		
		Map<String, String> result = new HashMap<String, String>();
		for (String mapping : mappings) {
			String[] map = mapping.split("-");
			result.put(map[0], map[1]);
		}
		
		return result;
	}

}
