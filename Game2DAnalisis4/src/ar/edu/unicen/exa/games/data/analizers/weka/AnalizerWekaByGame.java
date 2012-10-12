package ar.edu.unicen.exa.games.data.analizers.weka;

import java.io.File;
import java.util.Arrays;

import org.apache.log4j.Logger;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.unsupervised.attribute.Remove;
import ar.edu.unicen.exa.games.data.analizers.AnalizerAbstract;
import ar.edu.unicen.exa.games.data.analizers.weka.utils.HelperPrediction;
import ar.edu.unicen.exa.games.data.evaluators.weka.EvaluatorWekaCV;
import ar.edu.unicen.exa.games.data.evaluators.weka.EvaluatorWekaCVResult;
import ar.edu.unicen.exa.games.data.evaluators.weka.IEvaluatorWeka;
import ar.edu.unicen.exa.games.utils.UtilsConfig;
import ar.edu.unicen.exa.games.utils.VelocityPrinter;

/**
 * Analiza los datos usando un clasificador
 * 
 * @author Juan
 * 
 */
public class AnalizerWekaByGame extends AnalizerAbstract {
	private static final Logger logger = Logger.getLogger(AnalizerWekaByGame.class);
	private String[] classifierOptions;

	private boolean appendUserName;
	private HelperPrediction summary = new HelperPrediction();
	private IEvaluatorWeka evaluator = new EvaluatorWekaCV();

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void pAnalizeData(String game) throws Exception {
		// Crea el clasificador a usar
//		Classifier cls = new NaiveBayes();
		Classifier cls = new BayesNet();
//		Classifier cls = new J48();
//		Classifier cls = new RandomTree();
		if (classifierOptions != null) {
			cls.setOptions(classifierOptions.clone());
		}
		
		// Crea un clasificador que filtra el atributo username (para poder
		// hacer un seguimiento de las instancias clasificadas)
		// VER http://weka.wikispaces.com/Instance+ID
		FilteredClassifier fcls = new FilteredClassifier();
		fcls.setClassifier(cls);

		// Crea el filtro que remueve el atributo username (para que el
		// clasificador no lo tenga en cuenta)
		if (appendUserName) {
			Remove filter = new Remove();
			filter.setAttributeIndices("first");
			fcls.setFilter(filter);
		}

		// Obtiene el file ARFF a analizar
		String directory = UtilsConfig.get().getValue("arff.dir.path");
		File file = new File(directory + game + ".arff");
		if (!file.exists()) {
			logger.warn("El file " + file + " NO EXISTE");
			return;
		}
		
		// Evalua la clasificacion
		EvaluatorWekaCVResult result = evaluator.evaluate(file, fcls);
		
		// Recolecta las predicciones
		if (result != null) {
			generateSummary(game, result.getPredictedInstances());
		}
	}

	/**
	 * Genera el resumen de las predicciones
	 * 
	 * @param game
	 *            Nombre del juego al cual pertenecen las instancias predichas
	 * @param predictedInstances
	 *            Instancias predichas
	 */
	private void generateSummary(String game,
			Instances predictedInstances) {
		// ordena los datos por nombre de usuario
		predictedInstances.sort(0);

		// imprime las predicciones
		for (int i = 0; i < predictedInstances.numInstances(); i++) {
			Instance inst = predictedInstances.instance(i);

			String username = inst.stringValue(0);
			String realClass = inst.stringValue(inst.classIndex());
			String predictedClass = inst.stringValue(inst.classIndex() + 1);

			summary.add(username, game, realClass, predictedClass, inst);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void afterAnalize() {
		new VelocityPrinter().print(summary, Arrays.toString(classifierOptions));
	}

	/*==============================================================*/
	/*					SETTERS										*/
	/*==============================================================*/
	public AnalizerWekaByGame setSummary(HelperPrediction summary) {
		this.summary = summary;
		return this;
	}

	public AnalizerWekaByGame setEvaluator(IEvaluatorWeka evaluator) {
		this.evaluator = evaluator;
		return this;
	}
	
	public AnalizerWekaByGame setClassifierOptions(String[] classifierOptions) {
		this.classifierOptions = classifierOptions;
		return this;
	}

	public AnalizerWekaByGame setAppendUserName(boolean appendUserName) {
		this.appendUserName = appendUserName;
		return this;
	}
	
}