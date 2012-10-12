package ar.edu.unicen.exa.genie.net.analize;

import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import smile.Network;
import smile.learning.DataMatch;
import smile.learning.DataSet;
import ar.edu.unicen.exa.genie.utils.IGenieConstants;

/**
 * Clase encargada de realizar inferencias
 * 
 * @author Juan
 * 
 */
public class GenieNetworkInference {
	private Logger logger = Logger.getLogger(GenieNetworkInference.class);

	
	/**
	 * Realiza inferencia
	 * 
	 * @param net Red de Bayes
	 * @param evidences Evidencias
	 */
	public boolean inference(Network net, DataSet evidences) {
		// Obtiene el valor real
		Integer realValue = evidences.getInt(0, 0);

		// Obtiene el matching nroColumnaDataSet - nroNodeRedBayes
		DataMatch[] match = evidences.matchNetwork(net);
		
		// Introduce la evidencia
		for (int i = 1; i < evidences.getVariableCount(); i++) {
			int nodeHandle = match[i].node;
			int outcomeIndex = evidences.getInt(i, 0);
			
			if (!IGenieConstants.DATASET_MISSING_VALUE.equals(outcomeIndex)) {
				net.setEvidence(nodeHandle, outcomeIndex);
			}
		}
		
		// Realiza la inferencia
		return doInference(net, realValue);
	}

	/**
	 * Realiza inferencia
	 * 
	 * @param net Red de Bayes
	 * @param evidences Evidencias - La clave es el nombre de la variable y el valor 
	 * es el valor observado (ej: {@code evidences.put(S2_PUZZLE_LEVELS_WON, DISCRETE_LOW)}
	 * @return 
	 */
	public boolean inference(Network net, Map<String, String> evidences) {
		// Obtiene el valor real
		String value = evidences.remove(IGenieConstants.N0_ROOT);
		int realValue = Arrays.asList(net.getOutcomeIds(IGenieConstants.N0_ROOT)).indexOf(value);

		// Introduce la evidencia
		for (Entry<String, String> evidence : evidences.entrySet()) {
			String nodeId = evidence.getKey();
			String nodeValue = evidence.getValue();
			
			if (!IGenieConstants.DATASET_MISSING_VALUE.toString().equals(nodeValue)) {
				net.setEvidence(nodeId, nodeValue);
			}
		}
		
		// Realiza la inferencia
		return doInference(net, realValue);
	}
	
	/**
	 * Realiza la inferencia
	 * 
	 * @param net
	 * @param realValue
	 * @return
	 */
	private boolean doInference(Network net, int realValue) {
		// Updating the network:
		net.updateBeliefs();

		// Getting the handle of the node "perception":
		int handle = net.getNode(IGenieConstants.N0_ROOT);

		// Getting the value of the probability:
		double[] values = net.getNodeValue(handle);
		int maxIdx = 0;
		for (int i = 1; i < values.length; i++) {
			if (values[i] > values[maxIdx]) {
				maxIdx = i;
			}
		}

		boolean res = realValue == maxIdx;
		logger.info(String.format("Felder clasificado: %s - Felder real: %s - Resultado: %b", 
		IGenieConstants.FELDER_VALUES[maxIdx], IGenieConstants.FELDER_VALUES[realValue], res));
		logger.info(Arrays.toString(values));
		
		// Clear evidence
		net.clearAllEvidence();
		
		return res;
	}
	
}
