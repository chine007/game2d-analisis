package ar.edu.unicen.exa.games.data.discretizers;

import ar.edu.unicen.exa.games.data.model.Data;
import ar.edu.unicen.exa.games.gameschema.XmlDiscretization;

/**
 * Discretizador que no discretiza 
 * 
 * @author Juan
 *
 */
public class DiscretizerNull extends AbstractDiscretizer {

	public DiscretizerNull(XmlDiscretization disc, String property) {
		super(disc, property);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void apply(Data data) {
	}
	
}