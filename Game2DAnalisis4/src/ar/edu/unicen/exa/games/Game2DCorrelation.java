package ar.edu.unicen.exa.games;

import java.util.List;

import ar.edu.unicen.exa.games.data.analizers.apache.AnalizerApacheMathByGame;
import ar.edu.unicen.exa.games.db.Database;


/**
 * Imprime la correlacion de las propiedades con la clase
 * 
 * @author Juan
 *
 */
public class Game2DCorrelation {
	
	public static void main(String[] args) throws Exception {
		List<String> games = new Database().getAllGames();
		new AnalizerApacheMathByGame().analize(games.toArray(new String[0]));
	}
	
}
