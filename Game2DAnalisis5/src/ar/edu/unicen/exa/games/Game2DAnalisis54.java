package ar.edu.unicen.exa.games;

import static ar.edu.unicen.exa.genie.utils.IGenieConstants.FELDER_VALUES;
import static ar.edu.unicen.exa.genie.utils.IGenieConstants.LEVEL2_NODES_VALUES;
import static ar.edu.unicen.exa.genie.utils.IGenieConstants.N0_ROOT;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import smile.Network;
import smile.learning.DataSet;
import ar.edu.unicen.exa.genie.GenieFacade;
import ar.edu.unicen.exa.genie.data.GenieData;
import ar.edu.unicen.exa.genie.utils.IGenieConstants;

/**
 * Clase Main
 * 
 * @author Juan
 * 
 */
public class Game2DAnalisis54 {

	/**
	 * Metodo Main
	 */
	public static void main(String[] args) throws Exception {
		// Inicializacion
		GenieFacade genie = new GenieFacade(true);

		// Carga los datos
		List<GenieData> data = loadData();
		
		// Construye el data set
		DataSet dataSet = genie.buildDataSet(data);
		dataSet.writeFile(IGenieConstants.FILE_DATASET);
		
		// Discretiza los datos
		dataSet = genie.discretize(dataSet);
		dataSet.writeFile(IGenieConstants.FILE_DATASET_DISCRETIZED);
		
		// Construye la red 
		new File("output54").mkdir();
		Network net = buildNetwork();
		net.writeFile("output54/tlt.xdsl");
		
		// Cross validation
		genie.crossValidate(net, dataSet);
	}

	private static List<GenieData> loadData() throws Exception {
		List<GenieData> result = new ArrayList<GenieData>();
		
//		List<Data> datas = new Database4().getData("equilibrium");
//		for (Data data : datas) {
//			GenieData gd = new GenieData(data.getUsername(), data.get(Data.CLASS));
//			gd.put(Data.RESULT, data.get(Data.RESULT));
//			gd.put(Data.TIMES_PLAYED, data.get(Data.TIMES_PLAYED));
//			gd.put(Data.TIME_PLAYED, data.get(Data.TIME_PLAYED));
//			gd.put(Data.LEVEL, data.get(Data.LEVEL));
//			result.add(gd);
//		}

		Scanner scanner = new Scanner(new FileInputStream("input54/dat.txt"));

		try {
			String[] header = scanner.nextLine().split(","); 
			
			while (scanner.hasNextLine()) {
				String[] data = scanner.nextLine().split(",");
				GenieData gd = new GenieData(data[0], Float.valueOf(data[data.length - 1]).intValue());
			
				for (int i = 1; i < data.length - 1; i++) {
					gd.put(header[i], Float.valueOf(data[i]));
				}
				
				result.add(gd);
			}
		} finally {
			scanner.close();
		}

		return result;
	}

	public static Network buildNetwork() throws Exception {
		Network net = new Network();

		// LEVEL 0
		addNode(net, null, N0_ROOT, FELDER_VALUES);

		// LEVEL 1
		addNode(net, N0_ROOT, "result", LEVEL2_NODES_VALUES);
		addNode(net, N0_ROOT, "timesPlayed", LEVEL2_NODES_VALUES);
		addNode(net, N0_ROOT, "timePlayed", LEVEL2_NODES_VALUES);
		addNode(net, N0_ROOT, "level", LEVEL2_NODES_VALUES);
//		addNode(net, N0_ROOT, "movs", LEVEL2_NODES_VALUES);
//		addNode(net, N0_ROOT, "movsin", LEVEL2_NODES_VALUES);
//		addNode(net, N0_ROOT, "movsout", LEVEL2_NODES_VALUES);
//		addNode(net, N0_ROOT, "timeout", LEVEL2_NODES_VALUES);

		return net;
	}

	/**
	 * Agregar un nodo a la red
	 * 
	 * @param net
	 *            Red
	 * @param parent
	 *            Nodo padre
	 * @param child
	 *            Nodo hijo
	 */
	private static void addNode(Network net, String parent, String child,
			String[] childStates) {
		net.addNode(Network.NodeType.Cpt, child);

		net.setOutcomeId(child, 0, childStates[0]);
		net.setOutcomeId(child, 1, childStates[1]);
		for (int i = 2; i < childStates.length; i++) {
			net.addOutcome(child, childStates[i]);
		}

		if (parent != null) {
			net.addArc(parent, child);
		}
	}
}
