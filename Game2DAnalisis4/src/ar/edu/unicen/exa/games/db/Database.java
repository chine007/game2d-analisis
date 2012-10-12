package ar.edu.unicen.exa.games.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbcp.BasicDataSource;

import ar.edu.unicen.exa.games.data.model.Data;
import ar.edu.unicen.exa.games.utils.UtilsConfig;
import ar.edu.unicen.exa.games.utils.UtilsFile;

/**
 * Clase encargada del acceso a la base de datos
 * 
 * @author Juan
 * 
 */
public class Database {
	public static BasicDataSource ds;

	static {
		ds = new BasicDataSource();
		//ds.setDriverClassName("com.mysql.jdbc.Driver");
		ds.setUsername(UtilsConfig.get().getValue("database.username"));
		ds.setPassword(UtilsConfig.get().getValue("database.password"));
		ds.setUrl(UtilsConfig.get().getValue("database.url"));
	}

	/**
	 * Retorna los registros del juego pasado como parametro
	 * 
	 * @param game
	 *            Nombre del juego cuyos registros se desean buscar
	 * @return
	 * @throws Exception
	 */
	public List<Data> getData(String game) throws Exception {
		Connection conn = ds.getConnection();
		List<Data> result = new ArrayList<Data>();

		try {
			// Read query file for getting data
			String sqlFile = getFileName(UtilsConfig.get().getValue("sql.file.data"));
			String query = UtilsFile.readFile(sqlFile);

			// Replace parameters
			query = query
			.replaceAll(":felderDimension", UtilsConfig.get().getValue("felder.dimension"))
			.replaceAll(":game", game);

			// Prepare statement
			PreparedStatement ps = conn.prepareStatement(query);

			// Execute query
			ResultSet rs = ps.executeQuery();

			// Get data
			while (rs.next()) {
				Data data = new Data();

				ResultSetMetaData md = rs.getMetaData();
				for (int i = 1; i <= md.getColumnCount(); i++) {
					String columnName = md.getColumnLabel(i);
					if (columnName.equals("username")) {
						data.setUsername(rs.getString("username"));
					} else {
						data.put(columnName, rs.getFloat(columnName));
					}
				}

				result.add(data);
			}

			return result;
		} finally {
			conn.close();
		}
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
			String sqlFile = getFileName(UtilsConfig.get().getValue("sql.file.allGames"));
			String query = UtilsFile.readFile(sqlFile);

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
			String sqlFile = getFileName(UtilsConfig.get().getValue("sql.file.countPlayers"));
			String query = UtilsFile.readFile(sqlFile);

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
	 * Retorna el path completo del file
	 * 
	 * @param sqlFile
	 *            Nombre del file
	 * @return Path completo del file
	 */
	private String getFileName(String sqlFile) {
		return getClass().getResource(sqlFile).getFile();
	}
	
	/**
	 * Actualiza la columna timesPlayed 
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Connection conn = DriverManager.getConnection(UtilsConfig.get().getValue("database.url"), 
		UtilsConfig.get().getValue("database.username"), 
		UtilsConfig.get().getValue("database.password"));
		
		try {
			List<String> games = new Database().getAllGames();
			for (String game : games) {
				// Read query file for getting data
				String query = 	"SELECT username, id " +
				"FROM profilerecord " +
				"where won != 'NEW' and won != 'ABANDON' " +
				"and id_game = ? " +
				"order by username, trace, level";	

				// Prepare statement
				PreparedStatement profileRecords = conn.prepareStatement(query);
				profileRecords.setString(1, game);
				
				PreparedStatement update = conn.prepareStatement("update profilerecord set timesPlayed = ? where id = ?");
				
				// Execute query
				ResultSet rs = profileRecords.executeQuery();
				
				// Get data
				String username = "";
				int number = 1;
				while(rs.next()) {
					String newUsername = rs.getString("username");
					
					if (!username.equals(newUsername)) {
						number = 1;
						username = newUsername;
					}
					
					update.setInt(1, number++);
					update.setInt(2, rs.getInt("id"));
					update.execute();
				}
			}
		} finally {
			conn.close();
		}
	}

}