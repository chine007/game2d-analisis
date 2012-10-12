package ar.edu.unicen.exa.games;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import ar.edu.unicen.exa.games.data.model.Data;
import ar.edu.unicen.exa.games.db.Database;
import ar.edu.unicen.exa.games.gameschema.XmlGameProperties;
import ar.edu.unicen.exa.games.gameschema.XmlInterval;
import ar.edu.unicen.exa.games.utils.UtilsConfig;
import ar.edu.unicen.exa.games.utils.UtilsFelderDimension;
import ar.edu.unicen.exa.games.utils.UtilsXML;
import ar.edu.unicen.exa.games.utils.UtilsXmlGameProperties;


/**
 * Imprime la cantidad de jugadores que jugaron a cada juego agrupados por dimension de Felder
 * 
 * @author Juan
 *
 */
public class Game2DDataSet {
	private static final Logger logger = Logger.getLogger(Game2DDataSet.class);
	private List<FelderDimensionRange> ranges;
	
	public static void main(String[] args) throws Exception {
		Game2DDataSet general = new Game2DDataSet();
		general.printSummary(UtilsConfig.get().getValue("felder.dimension"));
	}

	/**
	 * Constructor
	 */
	private Game2DDataSet() {
		try {
			// Obtiene las propiedades 
			XmlGameProperties props = UtilsXML.parseXML(UtilsConfig.get().getValue("bayes.net.dir"), "equilibrium");

			// Obtiene el mapeo de la discretizacion a Felder
			Map<String, String> mappings = UtilsFelderDimension.getMappingDiscretizationFelder();

			// Crea los rangos de Felder
			ranges = new ArrayList<Game2DDataSet.FelderDimensionRange>();
			List<XmlInterval> intervals = UtilsXmlGameProperties.getXmlIntervals(props.getDiscretization(), Data.CLASS);
			for (XmlInterval xmlInterval : intervals) {
				String felderDimension = mappings.get(xmlInterval.getTag());
				ranges.add(new FelderDimensionRange(felderDimension, xmlInterval.getFrom().intValue(), xmlInterval.getTo().intValue()));
			}
		} catch (Exception e) {
			logger.error("Error al instanciar la clase", e);
		}
	}
	
	/**
	 * Imprime la cantidad de jugadores que jugaron a cada juego agrupados por cada uno de los rangos
	 * de la dimension de Felder 
	 *
	 * @param felderDimension Dimension de Felder
	 * @throws Exception
	 */
	private void printSummary(String felderDimension) throws Exception {
		Database db = new Database();
		
		List<String> games = db.getAllGames();
		for (String game : games) {
			System.out.println("==================================================");
			System.out.println(game);
			System.out.println("==================================================");
			int total = 0;
			for (FelderDimensionRange range : ranges) {
				int counter = db.getCountPlayers(game, felderDimension, range.from, range.to);
				System.out.printf("%s: %d%n", range.name, counter);
				total += counter;
			}
			System.out.printf("Total: %d%n", total);
		}	
	}

	/**
	 * Rango de la dimension
	 * 
	 * @author Juan
	 *
	 */
	static class FelderDimensionRange {
		private String name;
		private int from;
		private int to;
		
		FelderDimensionRange(String name, int from, int to) {
			this.name = name;
			this.from = from;
			this.to = to;
		}

		public boolean inside(Integer value) {
			return value >= from && value <= to;
		}
		
	}
	
}
