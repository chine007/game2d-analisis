package ar.edu.unicen.exa.games.writers;

import java.io.FileWriter;
import java.io.InputStream;
import java.util.LinkedHashSet;
import java.util.List;

import ar.edu.unicen.exa.games.data.model.Data;
import ar.edu.unicen.exa.games.gameschema.XmlGameProperties;
import ar.edu.unicen.exa.games.utils.UtilsConfig;
import ar.edu.unicen.exa.games.utils.UtilsFile;
import ar.edu.unicen.exa.games.utils.UtilsXmlGameProperties;

/**
 * Escribe los registros pasados como parametro en archivos ARFF
 * 
 * @author Juan
 * 
 */
public class WriterArff {
	private boolean appendUserName;

	
	/**
	 * True si se desea agregar el nombre del usuario al ARFF
	 * 
	 * @param appendUserName
	 *            True si se desea agregar el nombre del usuario al ARFF
	 */
	public void setAppendUserName(boolean appendUserName) {
		this.appendUserName = appendUserName;
	}

	/**
	 * Escribe los archivos ARFF
	 * 
	 * @param game
	 *            Nombre del juego
	 * @param datas
	 *            Datos del juego
	 * @param props
	 *            Propiedades del juego
	 * @throws Exception
	 */
	public final void writeRecords(String game, List<Data> datas,
			XmlGameProperties props) throws Exception {
		// Lee el template
		InputStream file = WriterArff.class.getResourceAsStream("template.arff");
		String template = UtilsFile.readFile(file);

		// Obtiene las propiedades a analizar (atributos del ARFF)
		String[] analizedProperties = UtilsXmlGameProperties
				.getXmlAnalizedProperties(props);

		// Obtiene el header (los atributos del ARFF)
		String header = getHeader(datas, analizedProperties, props);

		// Obtiene el cuerpo (los registros del ARFF)
		String records = getRecords(datas, analizedProperties);
		
		// Genera el file
		FileWriter fwGame = new FileWriter(UtilsConfig.get().getValue("arff.dir.path") + game
				+ ".arff");
		fwGame.write(template
				.replace("${attributes}", header)
				.replace("${data}", records));
		fwGame.close();
	}

	/**
	 * Retorna el header del ARFF
	 * 
	 * @param datas
	 *            Registros del ARFF
	 * @param analizedProperties
	 *            Propiedades del header
	 * @param props
	 *            Propiedades del Juego
	 * @return Header del ARFF
	 */
	private String getHeader(List<Data> datas, String[] analizedProperties, 
			XmlGameProperties props) {
		StringBuilder sb = new StringBuilder();

		// Escribe el atributo userName
		if (appendUserName) {
			LinkedHashSet<String> users = new LinkedHashSet<String>();
			for (Data data : datas) {
				users.add(data.getUsername());
			}
			sb.append(String.format("@ATTRIBUTE %s %s", "username", users
					.toString().replace("[", "{").replace("]", "}")));
			sb.append(System.getProperty("line.separator"));
		}

		// Escribe las propiedades a analizar
		for (String propertyName : analizedProperties) {
			String tags = UtilsXmlGameProperties.getTags(props, propertyName);
			sb.append(String.format("@ATTRIBUTE %s %s", propertyName, tags));
			sb.append(System.getProperty("line.separator"));
		}

		// Escribe la clase
		String tags = UtilsXmlGameProperties.getTags(props, Data.CLASS);
		sb.append(String.format("@ATTRIBUTE %s %s", Data.CLASS, tags));

		return sb.toString();
	}

	/**
	 * Retorna las propiedades de los registros pasados como parametro
	 * 
	 * @param datas
	 *            Datos de los registros
	 * @param analizedProperties
	 *            Propiedades de los registros
	 * @return Propiedades de los registros pasados como parametro
	 */
	private String getRecords(List<Data> datas, String[] analizedProperties) {
		StringBuilder sb = new StringBuilder();

		for (Data data : datas) {
			sb.append(getRecord(data, analizedProperties));
		}

		return sb.toString();
	}

	/**
	 * Retorna un registro
	 * 
	 * @param data
	 *            Datos del registro
	 * @param analizedProperties
	 *            Propiedades de los registros
	 * @return Registro
	 */
	private String getRecord(Data data, String[] analizedProperties) {
		StringBuilder sb = new StringBuilder();

		if (appendUserName) {
			sb.append(data.getUsername()).append(UtilsConfig.get().getValue("data.separator"));
		}
		sb.append(data.toStringAnalisis(analizedProperties));

		return sb.toString();
	}

}