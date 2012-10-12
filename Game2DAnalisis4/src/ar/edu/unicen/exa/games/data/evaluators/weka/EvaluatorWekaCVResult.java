package ar.edu.unicen.exa.games.data.evaluators.weka;

import weka.classifiers.Evaluation;
import weka.core.Instances;

/**
 * Clase con los resultados de la evaluacion
 * 
 * @author Juan
 *
 */
public class EvaluatorWekaCVResult {
	private Evaluation evaluation;
	private Instances predictedInstances;
	
	public EvaluatorWekaCVResult(Evaluation evaluation, Instances predictedInstances) {
		this.evaluation = evaluation;
		this.predictedInstances = predictedInstances;
	}
	public Evaluation getEvaluation() {
		return evaluation;
	}
	public Instances getPredictedInstances() {
		return predictedInstances;
	}
}
