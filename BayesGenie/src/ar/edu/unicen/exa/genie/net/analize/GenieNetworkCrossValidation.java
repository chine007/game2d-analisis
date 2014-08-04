package ar.edu.unicen.exa.genie.net.analize;

import org.apache.log4j.Logger;

import smile.Network;
import smile.learning.DataSet;
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
	public void crossValidate(String networkFile, DataSet dataSet) {
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
}
