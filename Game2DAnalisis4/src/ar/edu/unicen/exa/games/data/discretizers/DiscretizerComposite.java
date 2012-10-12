package ar.edu.unicen.exa.games.data.discretizers;

import java.util.ArrayList;
import java.util.List;

import ar.edu.unicen.exa.games.data.model.Data;

/**
 * Discretizador compuesto
 * 
 * @author Juan
 *
 */
public class DiscretizerComposite implements IDiscretizer {
	private List<IDiscretizer> discretizators = new ArrayList<IDiscretizer>();
	
	public DiscretizerComposite add(IDiscretizer disc) {
		discretizators.add(disc);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void apply(Data data) {
		for (IDiscretizer discretizator : discretizators) {
			discretizator.apply(data);
		}
	}

}
