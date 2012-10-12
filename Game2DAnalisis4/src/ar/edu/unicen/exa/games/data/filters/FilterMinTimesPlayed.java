package ar.edu.unicen.exa.games.data.filters;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import ar.edu.unicen.exa.games.data.model.Data;

/**
 * Filtra los datos de aquellos usuarios que hayan jugado una cantidad de
 * veces que este entre "from" y "to"
 * 
 * @author Juan
 * 
 */
public class FilterMinTimesPlayed implements IFilter {
	private static Logger logger = Logger.getLogger(FilterMinTimesPlayed.class);
	private Integer from;
	private Integer to;

	public FilterMinTimesPlayed(Integer from, Integer to) {
		this.from = from;
		this.to = to;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Data> filter(List<Data> datas) {
		List<Data> result = new ArrayList<Data>(datas);

		// Calcula la cantidad de veces jugadas por cada jugador 
		String username = datas.size() > 0 ? datas.get(0).getUsername() : "";
		Map<String, Integer> counters = new LinkedHashMap<String, Integer>();
		int counter = 0;
		for (Data data : datas) {
			if (data.getUsername().equals(username)) {
				counter = data.get(Data.TIMES_PLAYED).intValue();
			} else {
				counters.put(username, counter);
				counter = data.get(Data.TIMES_PLAYED).intValue();
				username = data.getUsername();
			}
		}
		counters.put(username, counter);

		// Filtra los jugadores
		for (Map.Entry<String, Integer> entry : counters.entrySet()) {
			for (Iterator<Data> it = result.iterator(); it.hasNext();) {
				Data dc = (Data)it.next();
				if (dc.getUsername().equals(entry.getKey()) &&
					entry.getValue() >= from &&
					entry.getValue() <= to) {
					logger.info("Eliminando registro:" + dc);
					it.remove();
				}
			}
		}
		return result;
	}

}
