package ar.edu.unicen.exa.games.data.discretizers;

import ar.edu.unicen.exa.games.gameschema.XmlDiscretization;

/**
 * Discretizador abstracto 
 * 
 * @author Juan
 *
 */
public abstract class AbstractDiscretizer implements IDiscretizer {
	protected XmlDiscretization disc;
	protected String property;

	public AbstractDiscretizer(XmlDiscretization disc, String property) {
		this.disc = disc;
		this.property = property;
	}
	
}