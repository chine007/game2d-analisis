package ar.edu.unicen.exa.games.data.newattribute;

import java.util.List;

import ar.edu.unicen.exa.games.data.model.Data;

/**
 * Calcula el valor de un atributo como la suma de los valores obtenidos hasta ese momento.
 * Este atributo se calcula de la siguiente manera: para cada jugador se van acumulando los valores del atributo
 * obtenido hasta ese momento.
 * 
 * Ejemplo:
 * 		<username>	<resultOriginal>	<resultAcumulado> 	
 * 		user1		-1					-1		
 * 		user1		-1					-2
 * 		user1		1					-1
 * 		user1		1 					0
 *
 * 
 * NOTA: recordar que el valor RESULT devuelto por la clase {@code Database} es la suma de los atributos RESULTWON y
 * RESULTLOST
 * 
 * @author Juan
 *
 */
public class AddAttribute {

	public void execute(List<Data> datas, String attributeName) {
		if (datas.size() > 0) {
			String username = datas.get(0).getUsername();
			int counter = datas.get(0).get(attributeName).intValue();
					
			for (int i = 1; i < datas.size(); i++) {
				String usernameAux = datas.get(i).getUsername();
				if (!username.equals(usernameAux)) {
					counter = 0;
					username = usernameAux;
				}
				counter += datas.get(i).get(attributeName);
				datas.get(i).put(attributeName, (float)counter);
			}
		}
	}

}
