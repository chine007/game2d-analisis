package ar.edu.unicen.exa.games.data.evaluators.weka;

import java.io.File;
import java.util.Arrays;

import org.apache.log4j.Logger;

import ar.edu.unicen.exa.games.utils.UtilsConfig;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Debug.Random;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AddClassification;

/**
 * Evaluador CrossValidation
 * 
 * Aplica CrossValidation al file ARFF del juego pasado como parametro
 * 
 * @author Juan
 *
 */
public class EvaluatorWekaCV implements IEvaluatorWeka {
	private static final Logger logger = Logger.getLogger(EvaluatorWekaCV.class);
	private int folds = 10;
	private boolean leaveOneOut;

	/**
	 * Constructor
	 */
	public EvaluatorWekaCV() {
		this(false);
	}

	/**
	 * Constructor
	 * 
	 * @param leaveOneOut True si se desea usar Leave One Out
	 */
	public EvaluatorWekaCV(boolean leaveOneOut) {
		this.leaveOneOut = leaveOneOut;
	}

	/**
	 * {@inheritDoc}
	 */
	public EvaluatorWekaCVResult evaluate(File file,
			Classifier cls) throws Exception {
		// loads data and set class index
		DataSource dataSource = new DataSource(file.getPath());
		Instances instances = dataSource.getDataSet();
		instances.setClassIndex(instances.numAttributes() - 1);

		// other options
		int seed = 1;
		folds = Math.min(folds, instances.numInstances());
		if (leaveOneOut) {
			folds = instances.numInstances();
		}
		if (folds <= 1) {
			logger.warn("No hay datos para " + file.getName());
			return null;
		}

		// randomize data
		Random rand = new Random(seed);
		Instances randData = new Instances(instances);
		randData.randomize(rand);
		if (randData.classAttribute().isNominal()) {
			randData.stratify(folds);
		}

		// perform cross-validation and add predictions
		Instances predictedData = null;
		Evaluation eval = new Evaluation(randData);
		for (int n = 0; n < folds; n++) {
//			Instances train = randData.trainCV(folds, n);
			Instances test = randData.testCV(folds, n);
			// the above code is used by the StratifiedRemoveFolds filter, the
			// code below by the Explorer/Experimenter:
			Instances train = randData.trainCV(folds, n, rand);

			// build and evaluate classifier
			Classifier clsCopy = Classifier.makeCopy(cls);
			clsCopy.buildClassifier(train);
			eval.evaluateModel(clsCopy, test);

			// add predictions
			AddClassification filter = new AddClassification();
			filter.setClassifier(cls);
			filter.setOutputClassification(true);
			filter.setOutputDistribution(true);
			filter.setOutputErrorFlag(true);
			filter.setInputFormat(train);
			Filter.useFilter(train, filter); // trains the classifier
			Instances pred = Filter.useFilter(test, filter); // perform predictions on test set
			
			// Copia las predicciones
			if (predictedData == null) {
				predictedData = new Instances(pred, 0);
			}
			for (int j = 0; j < pred.numInstances(); j++) {
				predictedData.add(pred.instance(j));
			}
		}

		// output evaluation
		logger.info(Arrays.toString(cls.getOptions()));
		logger.info(eval.toSummaryString("=== " + folds
				+ "-fold Cross-validation ===", false));
		logger.info(UtilsConfig.get().getFelderMapping());
		logger.info(eval.toMatrixString());

		// Retorna las instancias predichas
		return new EvaluatorWekaCVResult(eval, predictedData);
	}

	/**
	 * Setea la cantidad de folds
	 * 
	 * @param folds Cantidad de folds 
	 * @return
	 */
	public EvaluatorWekaCV setFolds(int folds) {
		this.folds  = folds;
		return this;
	}

}
