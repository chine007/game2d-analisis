package ar.edu.unicen.exa.games.data.discretizers;

import ar.edu.unicen.exa.games.data.model.Data;

/**
 * Discretiza los datos
 * 
 * @author Juan
 * 
 */
public interface IDiscretizer {

	/**
	 * Discretiza los datos pasados como parametro
	 * 
	 * @param data
	 *            Datos a discretizar
	 */
	public void apply(Data data);

}
