package ar.edu.unicen.exa.genie;

import java.io.File;
import java.util.List;
import java.util.Map;

import smile.Network;
import smile.learning.DataSet;
import ar.edu.unicen.exa.genie.data.GenieData;
import ar.edu.unicen.exa.genie.data.GenieDataSetBuilder;
import ar.edu.unicen.exa.genie.data.GenieDataSetDiscretizer;
import ar.edu.unicen.exa.genie.net.GenieNetworkBuilder;
import ar.edu.unicen.exa.genie.net.GenieNetworkEMLearning;
import ar.edu.unicen.exa.genie.net.analize.GenieNetworkCrossValidation;
import ar.edu.unicen.exa.genie.net.analize.GenieNetworkInference;
import ar.edu.unicen.exa.genie.utils.AbstractGenieUtils;
import ar.edu.unicen.exa.genie.utils.GenieUtilsFile;
import ar.edu.unicen.exa.genie.utils.IGenieConstants;

/**
 * Genie Facade
 * 
 * @author Juan
 *
 */
public class GenieFacade {

	
	/**
	 * Constructor
	 * 
	 * @param init True si se desea inicializar el directorio de salida
	 */
	public GenieFacade(boolean init) {
		if (true) {
			GenieUtilsFile.deleteDir(new File(IGenieConstants.FILE_OUTPUT_DIR));
			GenieUtilsFile.createDir(IGenieConstants.FILE_OUTPUT_DIR);
		}
	}

	/**
	 * Construye el data set
	 * 
	 * @param data Datos del data set
	 * @return
	 */
	public DataSet buildDataSet(List<GenieData> data) {
		AbstractGenieUtils.setData(data);
		return new GenieDataSetBuilder().build(data);
	}
	
	/**
	 * Discretiza los valores pasados como parametro
	 * 
	 * @param dataSet Datos a discretizar
	 * @return
	 */
	public DataSet discretize(DataSet dataSet) {
		return new GenieDataSetDiscretizer().discretize(dataSet);
	}
	
	/**
	 * Construye la red
	 *  
	 * @return
	 */
	public Network builNetwork() {
		return new GenieNetworkBuilder().buildNetwork();
	}

	/**
	 * Realiza inferencia
	 * 
	 * @param net Red de Bayes
	 * @param evidences Evidencia
	 */
	public void inference(Network net, Map<String, String> evidences) {
		new GenieNetworkInference().inference(net, evidences);
	}
	
	/**
	 * Aprende los parametros (CPT) de la red
	 * 
	 * @param net Red de Bayes
	 * @param dataSet Data set
	 */
	public void learn(Network net, DataSet dataSet) {
		new GenieNetworkEMLearning().learn(net, dataSet);
	}

	/**
	 * Realiza el cross validation
	 * 
	 * @param networkFile Archivo de la red de Bayes
	 * @param dataSet Data set
	 */
	public void crossValidate(String networkFile, DataSet dataSet) {
		new GenieNetworkCrossValidation().crossValidate(networkFile, dataSet);
	}
}