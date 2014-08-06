package ar.edu.unicen.exa.genie.net.analize;

import java.util.Arrays;

import org.apache.log4j.Logger;

import smile.Network;
import smile.learning.DataMatch;
import smile.learning.DataSet;
import smile.learning.EM;
import smile.learning.Validator;
import ar.edu.unicen.exa.genie.net.GenieNetworkEMLearning;
import ar.edu.unicen.exa.genie.utils.AbstractGenieUtils;
import ar.edu.unicen.exa.genie.utils.IGenieConstants;
import ar.edu.unicen.exa.genie.utils.GenieUtilsFile;

/**
 * Clase encargada de realizar el cross-validation
 * 
 * @author Juan
 * 
 */
public class GenieNetworkCrossValidation {
	private Logger logger = Logger.getLogger(GenieNetworkCrossValidation.class);

	
	/**
	 * Realiza el cross validation usando leave-one-out
	 * 
	 * @param networkFile Archivo de la red de Bayes
	 * @param dataSet
	 */
	public void crossValidateJuan(String networkFile, DataSet dataSet) {
		int counter = 0;
		int recordNumber = dataSet.getRecordCount();
		
		for (int i = 0; i < recordNumber; i++) {
			// Obtiene datos de usuario
			String username = AbstractGenieUtils.getData(i).getUsername();
			username = username.replace('ñ', 'n');
			
			GenieUtilsFile.createDir(IGenieConstants.FILE_OUTPUT_DIR + "/" + username);
			logger.info(String.format("Username: %s", username));
			
			// Obtiene datos de prueba
			DataSet test = AbstractGenieUtils.getTestSet(dataSet, i);
			test.writeFile(String.format(IGenieConstants.FILE_DATASET_MASK, username, "test", i, "dat"));

			// Obtiene datos de entrenamiento
			DataSet train = AbstractGenieUtils.getTrainingSet(dataSet, i);
			train.writeFile(String.format(IGenieConstants.FILE_DATASET_MASK, username, "train", i, "dat"));
			
			// Aplica EM Learning
			Network net = new GenieNetworkEMLearning().learn(networkFile, train);
			net.writeFile(String.format(IGenieConstants.FILE_DATASET_MASK, username, "net", i, "xdsl"));
			
			// Realiza inferencia
			if (new GenieNetworkInference().inference(net, test)) {
				counter++;
			}
		}
		
		logger.info(String.format("Precision: %d/%d = %2.2f", counter, recordNumber, counter/(float)recordNumber));
	}
	
	/**
	 * Realiza el cross validation usando leave-one-out
	 * 
	 * @param networkFile Archivo de la red de Bayes
	 * @param dataSet
	 */
	public void crossValidateGenie(String networkFile, DataSet dataSet) {
		// Lee la red
		Network net = new Network();
		net.readFile(networkFile);
//		net.setBayesianAlgorithm(Network.BayesianAlgorithmType.Lauritzen);
		
		// Obtiene el matching nroColumnaDataSet - nroNodeRedBayes
		DataMatch[] matching = dataSet.matchNetwork(net);
		
		// Crea el validator 
		Validator val = new Validator(net, dataSet, matching);
		val.addClassNode(IGenieConstants.N0_ROOT);

		// Crea el algoritmo de aprendizaje de parametros 
		EM em = new EM();
		em.setRandomizeParameters(true);
		em.setUniformizeParameters(false);
		
		// Setea la validacion
//		val.leaveOneOut(em);
		val.kFold(em, 10);
		
		// Graba el resultado
		val.getResultDataSet().writeFile(IGenieConstants.FILE_CROSS_VALIDATE);
		
		// Imprime la matriz de confusion
		int[][] cm = val.getConfusionMatrix(IGenieConstants.N0_ROOT);
		for (int[] is : cm) {
			System.out.println(Arrays.toString(is));
		}
		
		// Imprime la precision por tipo de perception
		for (String felderValue : IGenieConstants.FELDER_VALUES) {
			System.out.println("Precision " + felderValue + " : " + val.getAccuracy(IGenieConstants.N0_ROOT, felderValue));
		}
		
		// Imprime el accuracy
		int sum = 0;
		for (int i = 0; i < cm.length; i++) {
			sum += cm[i][i];
		}
		System.out.println("Accuracy " + sum/(float)dataSet.getRecordCount());
	}
	
}
