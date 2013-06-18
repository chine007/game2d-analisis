package felder.utils;

public class UtilsFelder {
	public static final int FELDER_PROCESSING 		= 1;
	public static final int FELDER_PERCEPTION 		= 2;
	public static final int FELDER_INPUT 			= 3;
	public static final int FELDER_UNDERSTANDING 	= 0;

	/**
	 * Calcula los valores de Felder
	 * 
	 * @param responses Arreglo de respuestas
	 * @param from Inicio del arreglo 
	 * @param end Final del arreglo
	 * @param optionA Valor de la opcion A en el cuestionario de Felder. 
	 * En la tabla game2d.felder este valor debe ser 1. 
	 * En la tabla saver.custionario_ils este valor debe ser 0.
	 * @return
	 */
	public static int[] calculateFelder(Object[] responses, int from, int end, int optionA) {
		int values[] = new int [4];
		int index = 1;
		
		// en 0 esta el id
		for (int i = from; i <= end; i++) {
			int answer = ((Number) responses[i]).intValue();
			if (answer == optionA) {
				values[index]++;
			} else {
				values[index]--;
			}
			index = (index + 1) % 4;
		}
		
		return values;
	}
	
}
