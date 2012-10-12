package ar.edu.unicen.exa.games.writers;

import java.io.FileWriter;
import java.util.List;
import java.util.Map;

import ar.edu.unicen.exa.games.data.model.Data;
import ar.edu.unicen.exa.games.utils.UtilsConfig;
import ar.edu.unicen.exa.games.utils.UtilsFelderDimension;

/**
 * Escribe los registros pasados como parametro para el paper Lyx en formato CSV
 * 
 * @author Juan
 * 
 */
public class WriterCSV {
	private static final String CSV_SEPRATOR = ";";
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");	
	
	/**
	 * Escribe los datos
	 * 
	 * @param game
	 *            Nombre del juego
	 * @param datas
	 *            Datos del juego
	 * @throws Exception
	 */
	public final void writeRecords(String game, List<Data> datas) throws Exception {
		FileWriter fwGame = new FileWriter(UtilsConfig.get().getValue("arff.dir.path") + game + ".csv");
		
		fwGame.write(getHeader());
		fwGame.write(LINE_SEPARATOR);
		
		fwGame.write(getRecords(datas));
		fwGame.close();
	}

	/**
	 * Retorna el header del file
	 * 
	 * @return Header del file
	 */
	private String getHeader() {
		StringBuilder sb = new StringBuilder();

		sb.append("Student").append(CSV_SEPRATOR)
		.append("Name").append(CSV_SEPRATOR)
		.append("Perception ILS").append(CSV_SEPRATOR)
		.append("Result").append(CSV_SEPRATOR)
		.append("Total").append(CSV_SEPRATOR)
		.append("Time").append(CSV_SEPRATOR)
		.append("Perception detected");

		return sb.toString();
	}

	/**
	 * Retorna los registros pasados como parametro en formato CSV
	 * 
	 * @param datas
	 *            Datos de los registros
	 * @return Registros pasados como parametro en formato CSV
	 */
	private String getRecords(List<Data> datas) {
		StringBuilder sb = new StringBuilder();
		Map<String, String> mappings = UtilsFelderDimension.getMappingDiscretizationFelder();

		int index = 1;
		for (Data data : datas) {
			String clazz = data.getDisc(Data.CLASS);
			clazz = mappings.get(clazz);
			
			sb.append(index++).append(CSV_SEPRATOR)
			.append(data.getUsername()).append(CSV_SEPRATOR)
			.append(clazz).append(CSV_SEPRATOR)
			.append(data.getDisc(Data.RESULT)).append(CSV_SEPRATOR)
			.append(data.getDisc(Data.TIMES_PLAYED)).append(CSV_SEPRATOR)
			.append(data.getDisc(Data.TIME_PLAYED)).append(CSV_SEPRATOR)
			.append(LINE_SEPARATOR);
		}

		return sb.toString();
	}

}