package ar.edu.unicen.exa.games.db;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;

import ar.edu.unicen.exa.games.utils.UtilsConfig;
import ar.edu.unicen.exa.genie.utils.IGenieConstants;
import ar.edu.unicen.exa.genie.utils.GenieUtilsFile;

/**
 * Clase encargada del acceso a la base de datos
 * 
 * @author Juan
 * 
 */
public class Database {
	private static final Logger logger = Logger.getLogger(Database.class);
	public static BasicDataSource ds;

	
	static {
		ds = new BasicDataSource();
		//ds.setDriverClassName("com.mysql.jdbc.Driver");
		ds.setUsername(UtilsConfig.get().getValue("database.username"));
		ds.setPassword(UtilsConfig.get().getValue("database.password"));
		ds.setUrl(UtilsConfig.get().getValue("database.url"));
	}

	/**
	 * Retorna los nombres de todos los juegos
	 * 
	 * @return Nombre de los juegos
	 * @throws Exception
	 */
	public List<String> getAllGames() throws Exception {
		Connection conn = ds.getConnection();

		try {
			// Read query file
			InputStream sqlFile = getFileName("allGames.sql");
			String query = GenieUtilsFile.readFile(sqlFile);

			// Create statement
			Statement st = conn.createStatement();

			// Execute query
			ResultSet rs = st.executeQuery(query);

			// Get data
			List<String> games = new ArrayList<String>();
			while (rs.next()) {
				games.add(rs.getString("game"));
			}

			return games;
		} finally {
			conn.close();
		}
	}
	
	/**
	 * Retorna los nombres de todos los usuarios que jugaron
	 * 
	 * @param felderDimension Dimension de Felder 
	 * @return Nombres de todos los usuarios que jugaron
	 * @throws Exception
	 */
	public List<String> getAllUsersPlayed(String felderDimension) throws Exception {
		Connection conn = ds.getConnection();

		try {
			// Read query file
			InputStream sqlFile = getFileName("allUsersPlayed.sql");
			String query = GenieUtilsFile.readFile(sqlFile);
					
			// Replace parameters
			query = query.replaceAll(":felderDimension", felderDimension);

			// Create statement
			Statement st = conn.createStatement();

			// Execute query
			ResultSet rs = st.executeQuery(query);

			// Get data
			List<String> games = new ArrayList<String>();
			while (rs.next()) {
				games.add(rs.getString("username").replaceAll("ñ", "n"));
			}

			return games;
		} finally {
			conn.close();
		}
	}

	/**
	 * Retorna el valor de Felder para la dimension y usuario pasado como parametro
	 * 
	 * @param username Nombre de usuario
	 * @param felderDimension Dimension de Felder 
	 * @return
	 * @throws Exception
	 */
	public Integer getFelderDimensionValue(String username, String felderDimension) throws Exception {
		Connection conn = ds.getConnection();

		try {
			// Read query file
			InputStream sqlFile = getFileName("felderDimensionValue.sql");
			String query = GenieUtilsFile.readFile(sqlFile);

			// Replace parameters
			query = query.replaceAll(":username", username)
			.replaceAll(":felderDimension", felderDimension);
					
			// Create statement
			Statement st = conn.createStatement();

			// Execute query
			ResultSet rs = st.executeQuery(query);

			// Get data
			rs.next();
			return rs.getInt("felderDimension");
		} finally {
			conn.close();
		}
	}

	/**
	 * Retorna la cantidad de jugadores que jugaron el juego pasado como parametro y cuyo 
	 * valor de dimension de Felder este entre from y to 
	 * 
	 * @param game Juego
	 * @param felderDimension Dimension de Felder
	 * @param from Valor inferior
	 * @param to Valor superior
	 * @return
	 * @throws Exception
	 */
	public int getCountPlayers(String game, String felderDimension, Integer from, Integer to) throws Exception {
		Connection conn = ds.getConnection();

		try {
			// Read query file for getting data
			InputStream sqlFile = getFileName("countPlayers.sql");
			String query = GenieUtilsFile.readFile(sqlFile);

			// Replace parameters
			query = query.replaceAll(":game", game)
			.replaceAll(":felderDimension", felderDimension)
			.replaceAll(":from", from.toString())
			.replaceAll(":to", to.toString());
			
			// Create statement
			Statement st = conn.createStatement();

			// Execute query
			ResultSet rs = st.executeQuery(query);

			// Get data
			rs.next();
			return rs.getInt("counter");
		} finally {
			conn.close();
		}
	}
	
	/**
	 * Retorna el valor de una propiedad
	 * 
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Float getPropertyValue(Map<String, String> parameters) throws Exception {
		Connection conn = ds.getConnection();

		try {
			// Read query file for getting data
			InputStream sqlFile = getFileName("propValue.sql");
			String query = GenieUtilsFile.readFile(sqlFile);

			// Replace parameters
			for (Entry<String, String> param : parameters.entrySet()) {
				String paramName = param.getKey();
				String paramValue = param.getValue();
				query = query.replaceAll(":" + paramName, paramValue);
			}
			logger.debug(query);
			
			// Create statement
			Statement st = conn.createStatement();

			// Execute query
			ResultSet rs = st.executeQuery(query);

			// Get data
			rs.next();
			Float value = rs.getFloat("value");
			return value == null || value == 0 ? IGenieConstants.DATASET_MISSING_VALUE : value;   
		} finally {
			conn.close();
		}
	}

	/**
	 * Retorna el path completo del file
	 * 
	 * @param sqlFile
	 *            Nombre del file
	 * @return Path completo del file
	 */
	private InputStream getFileName(String sqlFile) {
		return getClass().getResourceAsStream(sqlFile);
	}

}