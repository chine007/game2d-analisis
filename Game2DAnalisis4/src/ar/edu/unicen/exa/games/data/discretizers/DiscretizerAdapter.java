package ar.edu.unicen.exa.games.data.discretizers;

import java.util.List;

import ar.edu.unicen.exa.games.data.model.Data;

/**
 * Aplica varios discretizadores a los datos 
 * 
 * @author Juan
 *
 */
public class DiscretizerAdapter {
	private IDiscretizer discretizator;

	public DiscretizerAdapter(IDiscretizer discretizator) {
		this.discretizator = discretizator;
	}

	public void applyAll(List<Data> datas) {
		for (Data data : datas) {
			discretizator.apply(data);
		}
	}
	
}
