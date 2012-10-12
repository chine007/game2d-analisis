package ar.edu.unicen.exa.games.data.filters;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ar.edu.unicen.exa.games.data.model.Data;

/**
 * Filtra los datos cuya dimension de felder este dentro de un rango
 * 
 * @author Juan
 *
 */
public class FilterFelderDimension implements IFilter {
	private Integer from;
	private Integer to;
	
	public FilterFelderDimension(Integer from, Integer to) {
		this.from = from;
		this.to = to;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Data> filter(List<Data> datas) {
		List<Data> result = new ArrayList<Data>(datas);
		
		for (Iterator<Data> it = result.iterator(); it.hasNext();) {
			Integer value = it.next().get(Data.CLASS).intValue();
			if (value >= from && value <= to) {
				it.remove();
			}
		}
		
		return result;
	}

}
