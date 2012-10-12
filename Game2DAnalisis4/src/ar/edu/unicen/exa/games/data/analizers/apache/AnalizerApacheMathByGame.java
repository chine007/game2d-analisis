package ar.edu.unicen.exa.games.data.analizers.apache;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.stat.correlation.PearsonsCorrelation;
import org.apache.log4j.Logger;

import ar.edu.unicen.exa.games.data.analizers.AnalizerAbstract;
import ar.edu.unicen.exa.games.data.analizers.weka.AnalizerWekaByGame;
import ar.edu.unicen.exa.games.data.model.Data;
import ar.edu.unicen.exa.games.db.Database;


/**
 * Calcula la correlacion entre las propiedades del juego y el valor de Felder
 * 
 * @author Juan
 *
 */
public class AnalizerApacheMathByGame extends AnalizerAbstract {
	private static Logger logger = Logger.getLogger(AnalizerWekaByGame.class);

	/**
	 * Imprime la correlacion de las propiedades del juego
	 * 
	 * @param game Juego a analizar
	 */
	@Override
	protected void pAnalizeData(String game) throws Exception {
		PearsonsCorrelation pc = new PearsonsCorrelation();
		List<Pair> result = new ArrayList<Pair>();
		
		// Obtiene los datos del juego
		List<Data> datas = new Database().getData(game);
		
		// Calcula la correlacion
		double[][] matrix = new double[datas.size()][2];
		int row = 0;
		for (String column : Data.getNumericProperties()) {
			row = 0;
			for (Data data : datas) {
				matrix[row][0] = data.get(Data.CLASS);
				matrix[row++][1] = data.get(column);
			}
			
			if (row > 1) {
				RealMatrix rm = pc.computeCorrelationMatrix(matrix);
				result.add(new Pair(column, rm.getColumn(0)[1]));
			}
		}
		
		// Ordena por valor de correlacion
		Collections.sort(result);
		
		// Imprime los resultados
		for (Pair pair : result) {
			logger.info(pair.column + ": " + DecimalFormat.getInstance().format(pair.value));
		}
	}
	
	private static class Pair implements Comparable<Pair> {
		String column;
		Double value;

		public Pair(String column, Double value) {
			this.column = column;
			this.value = value;
		}

		@Override
		public int compareTo(Pair o) {
			return value.compareTo(o.value);
		}
	}

}
