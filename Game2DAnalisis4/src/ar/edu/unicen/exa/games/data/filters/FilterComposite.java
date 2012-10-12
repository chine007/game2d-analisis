package ar.edu.unicen.exa.games.data.filters;

import java.util.ArrayList;
import java.util.List;

import ar.edu.unicen.exa.games.data.model.Data;

/**
 * Filtro compuesto
 * 
 * @author Juan
 *
 */
public class FilterComposite implements IFilter {
	private List<IFilter> filters;
	
	public FilterComposite() {
		filters = new ArrayList<IFilter>();
	}

	public FilterComposite add(IFilter filter) {
		filters.add(filter);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Data> filter(List<Data> datas) {
		List<Data> result = new ArrayList<Data>(datas);
		
		for (IFilter filter : filters) {
			result = filter.filter(result);
		}
		
		return result;
	}

}
