package ar.edu.unicen.exa.games.data.filters;

import java.util.List;

import ar.edu.unicen.exa.games.data.model.Data;

public interface IFilter {

	/**
	 * Filtra los datos recibidos como parametro
	 * 
	 * @param data
	 *            Datos a filtrar
	 * @return Datos filtrados
	 */
	List<Data> filter(List<Data> data);

}
