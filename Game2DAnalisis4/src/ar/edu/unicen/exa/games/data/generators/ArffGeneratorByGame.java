package ar.edu.unicen.exa.games.data.generators;

import java.io.File;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import ar.edu.unicen.exa.games.data.discretizers.DiscretizerAdapter;
import ar.edu.unicen.exa.games.data.filters.IFilter;
import ar.edu.unicen.exa.games.data.model.Data;
import ar.edu.unicen.exa.games.db.Database;
import ar.edu.unicen.exa.games.gameschema.XmlGameProperties;
import ar.edu.unicen.exa.games.utils.UtilsConfig;
import ar.edu.unicen.exa.games.utils.UtilsReflection;
import ar.edu.unicen.exa.games.utils.UtilsXML;
import ar.edu.unicen.exa.games.writers.WriterArff;

/**
 * Genera los archivos ARFF por juego
 * 
 * @author Juan
 *
 */
public class ArffGeneratorByGame {
	private static final Logger logger = Logger.getLogger(ArffGeneratorByGame.class);
	private WriterArff writerArff;
	
	/**
	 * Constructor
	 * 
	 * @param writerArff Writer de archivos ARFF
	 * 
	 */
	public ArffGeneratorByGame(WriterArff writerArff) {
		this.writerArff = writerArff;
	}

	/**
	 * Genera los archivos Arff para todos los juegos
	 * 
	 * @param bayesDir Nombre del directorio de bayes a usar 
	 * @param games Juegos a analizar 
	 * @throws Exception
	 */
	public final void generate(String bayesDir, String[] games) throws Exception {
		// Crea el directorio de salida si no existe
		File output = new File(UtilsConfig.get().getValue("arff.dir.path"));
		if (!output.exists()) {
			output.mkdirs();
		}
		
		// Elimina files viejos
		for (File file : output.listFiles()) {
			file.delete();
		}

		// Genera files nuevos
		for (String game : games) {
			generate(bayesDir, game);
		}
	}

	/**
	 * Genera el archivo Arff para el juego pasado como parametro
	 * 
	 * @param bayesDir Nombre del directorio de bayes a usar 
	 * @param game Nombre del juego
	 * @throws Exception
	 */
	public final void generate(String bayesDir, String game) throws Exception {
		logger.info("Generando datos para el juego " + game + "...");

		// Obtiene las propiedades del juego
		XmlGameProperties props = UtilsXML.parseXML(bayesDir, game);  
		
		// Obtiene los registros del juego
		List<Data> datas = new Database().getData(game);
		
		// Calcula las propiedades que se acumulan
//		new AddAttribute().execute(datas, Data.RESULT);
//		new AddAttribute().execute(datas, Data.TIME_PLAYED);
		
		// Filtra los registros
		IFilter filter = UtilsReflection.createFilters(props);
		datas = filter.filter(Collections.unmodifiableList(datas));

		// Discretiza los registros
		DiscretizerAdapter da = new DiscretizerAdapter(UtilsReflection.createDiscretizers(props));
		da.applyAll(datas);
		
		// Genera los arff files
		writerArff.writeRecords(game, datas, props);
	}

}
