package ar.edu.unicen.exa.games.data.discretizers;

import java.util.List;

import org.apache.log4j.Logger;

import ar.edu.unicen.exa.games.data.model.Data;
import ar.edu.unicen.exa.games.gameschema.XmlDiscretization;
import ar.edu.unicen.exa.games.gameschema.XmlInterval;
import ar.edu.unicen.exa.games.utils.UtilsXmlGameProperties;

/**
 * Aplica un discretizador de acuerdo a los intervalos definidos en discretize/<file.xml> 
 * 
 * @author Juan
 *
 */
public class DiscretizerGeneral extends AbstractDiscretizer {
	private static Logger logger = Logger.getLogger(DiscretizerGeneral.class);
	
	public DiscretizerGeneral(XmlDiscretization disc, String property) {
		super(disc, property);
	}
	
	public Float getValue(Data data) {
		return data.get(property);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void apply(Data data) {
		Float value = getValue(data);
		
		if (value == null) {
			logger.warn("La propiedad " + property + " es null");
			return;
		}
		
		List<XmlInterval> intervals = UtilsXmlGameProperties.getXmlIntervals(disc, property);
		for (XmlInterval inter : intervals) {
			Float from = (inter.getFrom() == null) ? Integer.MIN_VALUE : inter.getFrom();
			Float to = (inter.getTo() == null) ? Integer.MAX_VALUE : inter.getTo();

			if (value >= from && value <= to) {
				((Data)data).putDisc(property, inter.getTag());
				return;
			}
		}

		throw new IllegalArgumentException("Error al discretizar la propiedad: " + property + " - valor: " + value);
	}
	
}