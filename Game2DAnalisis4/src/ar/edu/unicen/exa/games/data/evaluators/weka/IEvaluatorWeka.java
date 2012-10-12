package ar.edu.unicen.exa.games.data.evaluators.weka;

import java.io.File;

import weka.classifiers.Classifier;

/**
 * Evaluador
 * 
 * @author Juan
 * 
 */
public interface IEvaluatorWeka {

	/**
	 * Evalua los datos del juego usando el clasificador pasado como parametro
	 * 
	 * @param arff
	 *            File ARFF con los datos a evaluar
	 * @param cls
	 *            Clasificador a utilizar
	 * @return Resultado
	 * @throws Exception
	 */
	EvaluatorWekaCVResult evaluate(File arff, Classifier cls) throws Exception;

}
