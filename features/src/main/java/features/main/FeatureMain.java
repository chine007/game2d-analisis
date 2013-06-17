package features.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

import features.persistence.dao.Dao;
import features.persistence.model.Game;
import features.persistence.model.ProfileGame;
import features.persistence.model.User;
import features.utils.Data;

public class FeatureMain {
	private Dao dao;
	private List<Data> allDatas = new ArrayList<Data>();

	
	/**
	 * Main
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new FeatureMain().analize();
	}
	
	/**
	 * Analiza las preferencias de todos los jugadores en todos los juegos que tengan una
	 * feature determinada
	 */
	private void analize() {
		dao = new Dao();
		dao.beginTransaction();

		// Feature a analizar
		String featureCode = "PI";

		// Obtiene todos los juegos que tienen esa feature
		List<Game> games = dao.getGamesByFeature(featureCode);
		
		// Imprime las preferencias
		for (Game game : games) {
			System.out.println("====================================================================================");
			System.out.println("\tImprimiendo datos para " + game);
			System.out.println("====================================================================================");
			printPreference(featureCode, game);
		}
		
		// Agrupa los datos por usuario 
		Map<User, List<Data>> map = new HashMap<User, List<Data>>();
		for (Data data : allDatas) {
			List<Data> list = map.containsKey(data.getUser()) ? map.get(data.getUser()) : new ArrayList<Data>();
			list.add(data);
			map.put(data.getUser(), list);
		}
		
		// Calcula los promedios de cada usuario
		List<Data> finalData = new ArrayList<Data>();
		for (Entry<User,List<Data>> entry : map.entrySet()) {
			Double avg = 0d;
			for (Data data : entry.getValue()) {
				avg += data.getPreference();
			}
			avg /= entry.getValue().size();
			
			finalData.add(new Data(featureCode, null, avg, entry.getKey()));
		}
		Collections.sort(finalData);

		// Imprime los resultados finales
//		for (Data data : finalData) {
//			System.out.println(data.getUser() + "\t" + data.getPreferenceToString());
//		}
	
		dao.commitTransaction();
	}
	
	/**
	 * Imprime las preferencias de todos los jugadores en todos los juegos que tengan una
	 * feature determinada
	 * 
	 * @param featureCode Codigo del feature
	 * @param game Juego
	 */
	private void printPreference(String featureCode, Game game) {
		List<Data> datas = new ArrayList<Data>();
		
		// Obtiene los valores maximos
		Double maxTimesPlated = dao.getMax(game, "timesPlayed");
		Double maxLevel = dao.getMax(game, "level");
		
		// Obtiene los datos de los jugadores
		Long minTimesPlayed = 0L;
		String userGroup = null;
		boolean neutralFilter = true;
		List<ProfileGame> profiles = dao.getProfileGame(game, minTimesPlayed, userGroup, neutralFilter);
		
		// Genera los datos de los jugadores
		for (ProfileGame profileGame : profiles) {
			// Calcula la preferencia
			Double preference = 0.5 * profileGame.getTimesPlayed()/maxTimesPlated
			+ 0.5 * profileGame.getLevel()/maxLevel;		
			
			// Guarda los datos
			Data data = new Data(featureCode, profileGame.getGame(), preference, profileGame.getUser());
			datas.add(data);
		}
		
		Collections.sort(datas);

		// Imprime la correlacion
		printCorrelation(datas);
		
		// Imprime los valores
		for (Data data : datas) {
			System.out.println(data);
		}
		
		// Acumula resultados
		allDatas.addAll(datas);
	}

	/**
	 * Imprime la correlacion entre la preferencia y el valor de la percepcion
	 * 
	 * @param datas Datos del juego
	 */
	private void printCorrelation(List<Data> datas) {
		PearsonsCorrelation pc = new PearsonsCorrelation();
		
		double[] mat1 = new double[datas.size()];
		double[] mat2 = new double[datas.size()];
		int index = 0;
		for (Data data : datas) {
			mat1[index] = data.getPreference();
			mat2[index++] = data.getUser().getPerceptionByCategory();
		}
		
		System.out.println(pc.correlation(mat1, mat2));
	}
}
