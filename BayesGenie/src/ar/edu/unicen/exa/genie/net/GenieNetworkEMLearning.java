package ar.edu.unicen.exa.genie.net;

import smile.Network;
import smile.learning.DataMatch;
import smile.learning.DataSet;
import smile.learning.EM;


/**
 * Clase encargada de aprender los parametros de la red (CPT)
 * 
 * @author Juan
 * 
 */
public class GenieNetworkEMLearning {

	
	/**
	 * Aprende los parametros (CPT) de la red
	 * 
	 * @param networkFile Archivo de la red de Bayes
	 * @param dataSet Data set
	 * @return Red con los parametros aprendidos
	 */
	public Network learn(String networkFile, DataSet dataSet) {
		Network net = new Network();
		net.readFile(networkFile);
		return learn(net, dataSet);
	}

	/**
	 * Aprende los parametros (CPT) de la red
	 * 
	 * @param net Red de Bayes
	 * @param dataSet Data set
	 * @return Red con los parametros aprendidos
	 */
	public Network learn(Network net, DataSet dataSet) {
		EM em = new EM();
		em.setRandomizeParameters(true);
		em.setRelevance(false);
		
		DataMatch[] match = dataSet.matchNetwork(net);
		em.learn(dataSet, net, match);
		
		return net;
	}
	
}
