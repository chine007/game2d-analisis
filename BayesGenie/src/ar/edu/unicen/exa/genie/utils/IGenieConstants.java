package ar.edu.unicen.exa.genie.utils;

public interface IGenieConstants {
	/** Nombre de los nodos de la red */
	String N0_ROOT 										= "n0_perception";
	
	String N1_INNOVATION 								= "n1_innovation";
	String N1_COMPLEXITY 								= "n1_complexity";
	String N1_MEMORIZATION 								= "n1_memorization";
	String N1_DETAIL 									= "n1_detail";
	String N1_REPETITION 								= "n1_repetition";
	String N1_PRACTICE 									= "n1_practice";
	String N1_STANDARD_METHODS 							= "n1_standard_methods";
	String N1_THEORY 									= "n1_theory";
	String N1_NEW_CONCEPTS 								= "n1_new_concepts";
	String N1_SYMBOLS 									= "n1_symbols";
	String N1_VELOCITY 									= "n1_velocity";

	String N2_PUZZLE_HELP 								= "n2_puzzle_help";
	String N2_PUZZLE_LEVELS_WON 						= "n2_puzzle_levels_won";
	String N2_PUZZLE_LEVELS_LOST 						= "n2_puzzle_levels_lost";
	String N2_PUZZLE_MAX_LEVEL 							= "n2_puzzle_max_level";
	String N2_PUZZLE_MEAN_TIME_PLAYED 					= "n2_puzzle_mean_time_played";
	String N2_PUZZLE_MOVS 								= "n2_puzzle_movs";
	String N2_PUZZLE_TIMEOUT 							= "n2_puzzle_timeout";
	String N2_PUZZLE_TIME_PLAYED 						= "n2_puzzle_time_played";
	String N2_PUZZLE_TURNS_PLAYED 						= "n2_puzzle_turns_played";

	String N2_MEMORY_HELP 								= "n2_memory_help";
	String N2_MEMORY_LEVELS_WON 						= "n2_memory_levels_won";
	String N2_MEMORY_LEVELS_LOST 						= "n2_memory_levels_lost";
	String N2_MEMORY_MAX_LEVEL 							= "n2_memory_max_level";
	String N2_MEMORY_MEAN_TIME_PLAYED 					= "n2_memory_mean_time_played";
	String N2_MEMORY_MOVS 								= "n2_memory_movs";
	String N2_MEMORY_TIMEOUT 							= "n2_memory_timeout";
	String N2_MEMORY_TIME_PLAYED 						= "n2_memory_time_played";
	String N2_MEMORY_TURNS_PLAYED 						= "n2_memory_turns_played";

	String N2_CONCRETE_HELP 							= "n2_concrete_help";
	String N2_CONCRETE_LEVELS_WON 						= "n2_concrete_levels_won";
	String N2_CONCRETE_LEVELS_LOST 						= "n2_concrete_levels_lost";
	String N2_CONCRETE_MAX_LEVEL 						= "n2_concrete_max_level";
	String N2_CONCRETE_MEAN_TIME_PLAYED 				= "n2_concrete_mean_time_played";
	String N2_CONCRETE_MOVS 							= "n2_concrete_movs";
	String N2_CONCRETE_TIMEOUT 							= "n2_concrete_timeout";
	String N2_CONCRETE_TIME_PLAYED 						= "n2_concrete_time_played";
	String N2_CONCRETE_TURNS_PLAYED 					= "n2_concrete_turns_played";

	String N2_ABSTRACT_HELP 							= "n2_abstract_help";
	String N2_ABSTRACT_LEVELS_WON 						= "n2_abstract_levels_won";
	String N2_ABSTRACT_LEVELS_LOST 						= "n2_abstract_levels_lost";
	String N2_ABSTRACT_MAX_LEVEL 						= "n2_abstract_max_level";
	String N2_ABSTRACT_MEAN_TIME_PLAYED 				= "n2_abstract_mean_time_played";
	String N2_ABSTRACT_MOVS 							= "n2_abstract_movs";
	String N2_ABSTRACT_TIMEOUT 							= "n2_abstract_timeout";
	String N2_ABSTRACT_TIME_PLAYED 						= "n2_abstract_time_played";
	String N2_ABSTRACT_TURNS_PLAYED 					= "n2_abstract_turns_played";

	/** Valores del nodo raiz */
	String[] FELDER_VALUES 								= {"intuitive", "neutral", "sensitive"};
	int[] FELDER_INTERVALS 								= {-11, -3, 5, 12}; // cerrado a izquierda y abierto a derecha. Ejemplo para neutro: -3 <= x < 5 

	/** Valores de los nodos de la red */
	String[] LEVEL1_NODES_VALUES						= {"l1_low", "l1_medium", "l1_high"};
	
	/** Valores de los nodos de la red */
	String[] LEVEL2_NODES_VALUES						= {"l2_low", "l2_medium", "l2_high"};

	/** Nombre de los files a grabar */
	String FILE_OUTPUT_DIR								= "output";
	
	String FILE_BAYES_NET 								= FILE_OUTPUT_DIR + "/net.xdsl";
	String FILE_CROSS_VALIDATE 							= FILE_OUTPUT_DIR + "/crossValidate.txt";
	
	String FILE_DAT_MASK								= FILE_OUTPUT_DIR + "/%s/%s-%02d.%s";
	String FILE_DAT_REPLACED 							= FILE_OUTPUT_DIR + "/datReplaced.dat";
	String FILE_DAT_REPLACED_DISCRETIZED 				= FILE_OUTPUT_DIR + "/datReplacedDis.dat";
	
	String FILE_ARFF 									= FILE_OUTPUT_DIR + "/weka.arff";
	
	String FILE_TXT_ORIG 								= FILE_OUTPUT_DIR + "/txtDataOrig.txt";
	String FILE_TXT_REPLACED							= FILE_OUTPUT_DIR + "/txtDataReplaced.txt";

	/** Missing value */
	Integer DATASET_MISSING_VALUE 						= Integer.MAX_VALUE;
}
