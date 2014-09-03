package ar.edu.unicen.exa.games;

import java.util.List;

import smile.Network;
import smile.learning.DataSet;
import ar.edu.unicen.exa.games.loader.DataLoader;
import ar.edu.unicen.exa.games.utils.UtilsArffWriter;
import ar.edu.unicen.exa.genie.GenieFacade;
import ar.edu.unicen.exa.genie.data.GenieData;
import ar.edu.unicen.exa.genie.utils.IGenieConstants;


/**
 * Clase Main
 * 
 * @author Juan
 *
 */
public class Game2DAnalisis {
	
	/**
	 * Metodo Main
	 * 
	 * Ejecutarlo usando como VM arguments: -Djava.library.path=..\BayesGenie\lib
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// Inicializacion
		GenieFacade genie = new GenieFacade(true);
		
		// Carga los datos
		List<GenieData> data = new DataLoader().loadData();
		
		// Reemplaza los missing values
		genie.replaceMissingValues(data);
		
		// Construye el data set
		DataSet dataSet = genie.buildDataSet(data);
		dataSet.writeFile(IGenieConstants.FILE_DATASET);
		
		// Discretiza los datos
		dataSet = genie.discretize(dataSet);
		dataSet.writeFile(IGenieConstants.FILE_DATASET_DISCRETIZED);
		
		// Construye la red 
		Network net = genie.builNetwork();
		net.writeFile(IGenieConstants.FILE_BAYES_NET);
		
		// Cross validation (la ejecucion es NO determinista ya que el aprendizaje de los parametros de la red agrega randomizacion)
		genie.crossValidate(IGenieConstants.FILE_BAYES_NET, dataSet);
		
		// Genera el ARFF
		new UtilsArffWriter().write(dataSet);
	}
	
}