package ar.edu.unicen.exa.genie.utils;

public interface IGenieConstants {
	/** Nombre de los nodos de la red */
	String N0_ROOT 										= "perception";
	
	String N1_INNOVATION 								= "innovation";
	String N1_COMPLEXITY 								= "complexity";
	String N1_MEMORIZATION 								= "memorization";
	String N1_DETAIL 									= "detail";
	String N1_REPETITION 								= "repetition";
	String N1_PRACTICE 									= "practice";
	String N1_STANDARD_METHODS 							= "standard_methods";
	String N1_THEORY 									= "theory";
	String N1_NEW_CONCEPTS 								= "new_concepts";
	String N1_SYMBOLS 									= "symbols";
	String N1_VELOCITY 									= "velocity";

	String N2_PUZZLE_HELP 								= "puzzle_help";
	String N2_PUZZLE_LEVELS_WON 						= "puzzle_levels_won";
	String N2_PUZZLE_LEVELS_LOST 						= "puzzle_levels_lost";
	String N2_PUZZLE_MAX_LEVEL 							= "puzzle_max_level";
	String N2_PUZZLE_MEAN_TIME_PLAYED 					= "puzzle_mean_time_played";
	String N2_PUZZLE_MOVS 								= "puzzle_movs";
	String N2_PUZZLE_TIMEOUT 							= "puzzle_timeout";
	String N2_PUZZLE_TIME_PLAYED 						= "puzzle_time_played";
	String N2_PUZZLE_TURNS_PLAYED 						= "puzzle_turns_played";

	String N2_MEMORY_HELP 								= "memory_help";
	String N2_MEMORY_LEVELS_WON 						= "memory_levels_won";
	String N2_MEMORY_LEVELS_LOST 						= "memory_levels_lost";
	String N2_MEMORY_MAX_LEVEL 							= "memory_max_level";
	String N2_MEMORY_MEAN_TIME_PLAYED 					= "memory_mean_time_played";
	String N2_MEMORY_MOVS 								= "memory_movs";
	String N2_MEMORY_TIMEOUT 							= "memory_timeout";
	String N2_MEMORY_TIME_PLAYED 						= "memory_time_played";
	String N2_MEMORY_TURNS_PLAYED 						= "memory_turns_played";

	String N2_CONCRETE_HELP 							= "concrete_help";
	String N2_CONCRETE_LEVELS_WON 						= "concrete_levels_won";
	String N2_CONCRETE_LEVELS_LOST 						= "concrete_levels_lost";
	String N2_CONCRETE_MAX_LEVEL 						= "concrete_max_level";
	String N2_CONCRETE_MEAN_TIME_PLAYED 				= "concrete_mean_time_played";
	String N2_CONCRETE_MOVS 							= "concrete_movs";
	String N2_CONCRETE_TIMEOUT 							= "concrete_timeout";
	String N2_CONCRETE_TIME_PLAYED 						= "concrete_time_played";
	String N2_CONCRETE_TURNS_PLAYED 					= "concrete_turns_played";

	String N2_ABSTRACT_HELP 							= "abstract_help";
	String N2_ABSTRACT_LEVELS_WON 						= "abstract_levels_won";
	String N2_ABSTRACT_LEVELS_LOST 						= "abstract_levels_lost";
	String N2_ABSTRACT_MAX_LEVEL 						= "abstract_max_level";
	String N2_ABSTRACT_MEAN_TIME_PLAYED 				= "abstract_mean_time_played";
	String N2_ABSTRACT_MOVS 							= "abstract_movs";
	String N2_ABSTRACT_TIMEOUT 							= "abstract_timeout";
	String N2_ABSTRACT_TIME_PLAYED 						= "abstract_time_played";
	String N2_ABSTRACT_TURNS_PLAYED 					= "abstract_turns_played";

	/** Valores de los nodos de la red */
	String[] LEVEL2_NODES_VALUES						= {"l2_low", "l2_medium", "l2_high"};

	/** Valores de los nodos de la red */
	String[] LEVEL1_NODES_VALUES						= {"l1_low", "l1_medium", "l1_high"};
	
	/** Valores del nodo raiz */
	String[] FELDER_VALUES 								= {"intuitive", "neutral", "sensitive"};
	int[] FELDER_INTERVALS 								= {-5, 3, 11};

	/** Nombre de los files a grabar */
	String FILE_OUTPUT_DIR								= "output";
	String FILE_DATASET_MASK							= FILE_OUTPUT_DIR + "/%s/%s-%02d.%s";
	String FILE_DATASET 								= FILE_OUTPUT_DIR + "/data.dat";
	String FILE_DATASET_DISCRETIZED 					= FILE_OUTPUT_DIR + "/dataDis.dat";
	String FILE_BAYES_NET 								= FILE_OUTPUT_DIR + "/net.xdsl";
	String FILE_CROSS_VALIDATE 							= FILE_OUTPUT_DIR + "/crossValidate.txt";

	/** Missing value */
	Integer DATASET_MISSING_VALUE 						= Integer.MAX_VALUE;
}
