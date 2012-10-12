package ar.edu.unicen.exa.games;

import ar.edu.unicen.exa.games.data.analizers.weka.AnalizerWekaByGame;
import ar.edu.unicen.exa.games.data.evaluators.weka.EvaluatorWekaCV;
import ar.edu.unicen.exa.games.data.generators.ArffGeneratorByGame;
import ar.edu.unicen.exa.games.utils.UtilsConfig;
import ar.edu.unicen.exa.games.writers.WriterArff;

/**
 * Clase Main
 * 
 * @author Juan
 *
 */
public class Game2DAnalisis {
	
	public static void main(String[] args) throws Exception {
		boolean appendUserName = true;
		boolean leaveOneOut = true;

		// Writer Arff
		WriterArff writerArff = new WriterArff();
		writerArff.setAppendUserName(appendUserName);
		
		// Juegos a analizar
//		String[] games = new Database().getAllGames().toArray(new String[0]);
//		String[] games = new Database().getAllGames().subList(0, 6).toArray(new String[0]);
		String[] games = new String[] {"equilibrium"};

		// Genera files ARFF
		String bayesDir = UtilsConfig.get().getValue("bayes.net.dir");
		new ArffGeneratorByGame(writerArff).generate(bayesDir, games);

		// Analiza el file ARFF
//		String[] classifierOptions = new String[]{"-K", "2", "-M", "4"};
//		String[] classifierOptions = new String[]{"-K", "4", "-M", "13"};
		
//		String[] classifierOptions = new String[]{"-K", "4", "-M", "13"};
//		String[] classifierOptions = new String[]{"-K", "1", "-M", "12"};
		new AnalizerWekaByGame()
		.setAppendUserName(appendUserName)
//		.setClassifierOptions(classifierOptions)
		.setEvaluator(new EvaluatorWekaCV(leaveOneOut))
		.analize(games);
	}
	
}
