package ar.edu.unicen.exa.genie.net;

import static ar.edu.unicen.exa.genie.utils.IGenieConstants.*;
import smile.Network;
import smile.SMILEException;

/**
 * Clase encargada de construir la red
 * 
 * @author Juan
 *
 */
public class GenieNetworkBuilder {

	/**
	 * Crea la red
	 * 
	 * @return
	 */
	public Network buildNetwork() {
		Network net = new Network();
		
		// LEVEL 0
		addNode(net, null, N0_ROOT, FELDER_VALUES);

		// LEVEL 1
		addNode(net, N0_ROOT, N1_INNOVATION, LEVEL1_NODES_VALUES);
		addNode(net, N0_ROOT, N1_COMPLEXITY, LEVEL1_NODES_VALUES);
		addNode(net, N0_ROOT, N1_MEMORIZATION, LEVEL1_NODES_VALUES);
		addNode(net, N0_ROOT, N1_DETAIL, LEVEL1_NODES_VALUES);
		addNode(net, N0_ROOT, N1_REPETITION, LEVEL1_NODES_VALUES);
		addNode(net, N0_ROOT, N1_PRACTICE, LEVEL1_NODES_VALUES);
		addNode(net, N0_ROOT, N1_STANDARD_METHODS, LEVEL1_NODES_VALUES);
		addNode(net, N0_ROOT, N1_THEORY, LEVEL1_NODES_VALUES);
		addNode(net, N0_ROOT, N1_NEW_CONCEPTS, LEVEL1_NODES_VALUES);
		addNode(net, N0_ROOT, N1_SYMBOLS, LEVEL1_NODES_VALUES);
		addNode(net, N0_ROOT, N1_VELOCITY, LEVEL1_NODES_VALUES);

		// LEVEL 2
		// innovation
		addNode(net, N1_INNOVATION, N2_PUZZLE_LEVELS_WON, LEVEL2_NODES_VALUES);
		addNode(net, N1_INNOVATION, N2_PUZZLE_MAX_LEVEL, LEVEL2_NODES_VALUES);
		addNode(net, N1_INNOVATION, N2_PUZZLE_MOVS, LEVEL2_NODES_VALUES);
		
		// complexity
		addNode(net, N1_COMPLEXITY, N2_PUZZLE_HELP, LEVEL2_NODES_VALUES);
		addNode(net, N1_COMPLEXITY, N2_PUZZLE_TIMEOUT, LEVEL2_NODES_VALUES);
		addNode(net, N1_COMPLEXITY, N2_PUZZLE_LEVELS_LOST, LEVEL2_NODES_VALUES);

		// memorization
		addNode(net, N1_MEMORIZATION, N2_MEMORY_LEVELS_WON, LEVEL2_NODES_VALUES);
		addNode(net, N1_MEMORIZATION, N2_MEMORY_MAX_LEVEL, LEVEL2_NODES_VALUES);

		// detail
		addNode(net, N1_DETAIL, N2_MEMORY_MEAN_TIME_PLAYED, LEVEL2_NODES_VALUES);
		addNode(net, N1_DETAIL, N2_MEMORY_LEVELS_LOST, LEVEL2_NODES_VALUES);

		// repetition
		addNode(net, N1_REPETITION, N2_MEMORY_TIME_PLAYED, LEVEL2_NODES_VALUES);
		addNode(net, N1_REPETITION, N2_MEMORY_TURNS_PLAYED, LEVEL2_NODES_VALUES);
		addNode(net, N1_REPETITION, N2_CONCRETE_TURNS_PLAYED, LEVEL2_NODES_VALUES);
		addNode(net, N1_REPETITION, N2_CONCRETE_TIME_PLAYED, LEVEL2_NODES_VALUES);

		// practice
		addNode(net, N1_PRACTICE, N2_CONCRETE_TURNS_PLAYED, LEVEL2_NODES_VALUES);

		// standard methods
		addNode(net, N1_STANDARD_METHODS, N2_CONCRETE_LEVELS_WON, LEVEL2_NODES_VALUES);

		// theory
		addNode(net, N1_THEORY, N2_ABSTRACT_TURNS_PLAYED, LEVEL2_NODES_VALUES);

		// new concepts
		addNode(net, N1_NEW_CONCEPTS, N2_ABSTRACT_MEAN_TIME_PLAYED, LEVEL2_NODES_VALUES);

		// symbols
		addNode(net, N1_SYMBOLS, N2_ABSTRACT_MAX_LEVEL, LEVEL2_NODES_VALUES);
		addNode(net, N1_SYMBOLS, N2_ABSTRACT_LEVELS_WON, LEVEL2_NODES_VALUES);
		
		// velocity
		addNode(net, N1_VELOCITY, N2_PUZZLE_MEAN_TIME_PLAYED, LEVEL2_NODES_VALUES);
		addNode(net, N1_VELOCITY, N2_MEMORY_MEAN_TIME_PLAYED, LEVEL2_NODES_VALUES);
		addNode(net, N1_VELOCITY, N2_CONCRETE_MEAN_TIME_PLAYED, LEVEL2_NODES_VALUES);
		addNode(net, N1_VELOCITY, N2_ABSTRACT_MEAN_TIME_PLAYED, LEVEL2_NODES_VALUES);
		addNode(net, N1_VELOCITY, N2_PUZZLE_TIMEOUT, LEVEL2_NODES_VALUES);
		addNode(net, N1_VELOCITY, N2_MEMORY_TIMEOUT, LEVEL2_NODES_VALUES);
		addNode(net, N1_VELOCITY, N2_CONCRETE_TIMEOUT, LEVEL2_NODES_VALUES);
		addNode(net, N1_VELOCITY, N2_ABSTRACT_TIMEOUT, LEVEL2_NODES_VALUES);
		
		return net;
	}
	
	/**
	 * Agregar un nodo a la red
	 * 
	 * @param net Red
	 * @param parent Nodo padre
	 * @param child Nodo hijo
	 */
	private void addNode(Network net, String parent, String child, String[] childStates) {
		if (!exist(net, child)) {
			net.addNode(Network.NodeType.Cpt, child);
			
			net.setOutcomeId(child, 0, childStates[0]);
			net.setOutcomeId(child, 1, childStates[1]);
			for (int i = 2; i < childStates.length; i++) {
				net.addOutcome(child, childStates[i]);
			}
		}
		
		if (parent != null) {
			net.addArc(parent, child);
		}
	}
	
	private boolean exist(Network net, String nodeId) {
		try {
			net.getNode(nodeId);
			return true;
		} catch (SMILEException e) {
			return false;
		}
	}
	
}
