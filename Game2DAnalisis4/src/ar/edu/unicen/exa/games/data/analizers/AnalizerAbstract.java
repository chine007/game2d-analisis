package ar.edu.unicen.exa.games.data.analizers;

import org.apache.log4j.Logger;

public abstract class AnalizerAbstract {
	private static final Logger logger = Logger.getLogger(AnalizerAbstract.class);

	public final void analize(String[] games) throws Exception {
		// Analiza los juegos
		for (String game : games) {
			analizeData(game);
		}
		
		afterAnalize();
	}
	
	public final void analizeData(String data) throws Exception {
		logger.info("=============================================================================");
		logger.info("Analizando los datos para " + data + " (" + this.getClass().getSimpleName() + ")");
		logger.info("=============================================================================");
		pAnalizeData(data);
	}

	/**
	 * Analiza el dato (juego o jugador)
	 * 
	 * @param data
	 *            Dato a analizar (juego o jugador)
	 * @throws Exception
	 */
	protected abstract void pAnalizeData(String data) throws Exception;

	/**
	 * Metodo invocado luego de analizar los datos
	 */
	protected void afterAnalize() {
	}

}
