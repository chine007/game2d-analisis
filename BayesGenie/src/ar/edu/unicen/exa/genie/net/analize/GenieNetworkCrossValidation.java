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
			
			// Crea directorio para usuario
			GenieUtilsFile.createDir(IGenieConstants.FILE_OUTPUT_DIR + "/" + username);
			logger.info(String.format("Username: %s", username));
			
			// Obtiene datos de prueba
			DataSet test = AbstractGenieUtils.getTestSet(dataSet, i);
			test.writeFile(String.format(IGenieConstants.FILE_DAT_MASK, username, "test", i, "dat"));

			// Obtiene datos de entrenamiento
			DataSet train = AbstractGenieUtils.getTrainingSet(dataSet, i);
			train.writeFile(String.format(IGenieConstants.FILE_DAT_MASK, username, "train", i, "dat"));
			
			// Aplica EM Learning
			Network net = new GenieNetworkEMLearning().learn(networkFile, train);
			net.writeFile(String.format(IGenieConstants.FILE_DAT_MASK, username, "net", i, "xdsl"));
			
			// Realiza inferencia
			if (new GenieNetworkInference().inference(net, test)) {
				counter++;
			}
		}
		
		logger.info(String.format("Accuracy: %d/%d = %2.2f", counter, recordNumber, counter/(float)recordNumber));
	}
	
	/**
	 * Realiza el cross validation
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
		Validator validator = new Validator(net, dataSet, matching);
		validator.addClassNode(IGenieConstants.N0_ROOT);

		// Crea el algoritmo de aprendizaje de parametros 
		EM em = new EM();
		em.setRandomizeParameters(true);
		em.setUniformizeParameters(false);
		em.setRelevance(true);
		
		// Setea la validacion
//		validator.leaveOneOut(em);
		validator.kFold(em, 10);
		
		// Graba el resultado
		validator.getResultDataSet().writeFile(IGenieConstants.FILE_CROSS_VALIDATE);
		
		// Imprime los resultados para cada usuario
		DataSet result = validator.getResultDataSet();		
		for (int record = 0; record < result.getRecordCount(); record++) {
			String username = AbstractGenieUtils.getData(record).getUsername();
			logger.info("================================================");
			logger.info(String.format("Username: %s", username));
			
			// Obtiene la clase del usuario
			int realValue = result.getInt(0, record);
			
			// Obtiene la clase predicha del usuario
			int predictedValue = result.getInt(result.getVariableCount() - 1, record);
			
			// Imprime el resultado
			logger.info(String.format("Felder real: %s - Felder clasificado: %s", 
			IGenieConstants.FELDER_VALUES[realValue], IGenieConstants.FELDER_VALUES[predictedValue]));  

			// Obtiene los valores de probabilidad para cada posible valor de la clase
			int states = IGenieConstants.FELDER_VALUES.length;
			float[] prob = new float[states];
			for (int i = 0; i < states; i++) {
				prob[i] = result.getFloat(result.getVariableCount() - 1 - states + i, record);
			}
			logger.info("(-11) " + Arrays.toString(prob) + " (+11)"); 
		}
		
		// Imprime la matriz de confusion
		int[][] cm = validator.getConfusionMatrix(IGenieConstants.N0_ROOT);
		for (int[] is : cm) {
			logger.info(Arrays.toString(is));
		}
		
		// Imprime la precision por tipo de perception
		for (String felderValue : IGenieConstants.FELDER_VALUES) {
			logger.info("Precision " + felderValue + ": " + validator.getAccuracy(IGenieConstants.N0_ROOT, felderValue));
		}
		
		// Imprime el accuracy
		int sum = 0;
		for (int i = 0; i < cm.length; i++) {
			sum += cm[i][i];
		}
		logger.info("Accuracy " + sum/(float)dataSet.getRecordCount());
	}
	
}

