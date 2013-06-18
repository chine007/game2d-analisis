package felder;

import java.util.List;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import features.persistence.model.User;
import felder.utils.UtilsHibernate;

/**
 * Esta clase testea que los valores de Felder en game2d.felder (Local) sean iguales
 * a los de game2d.user (Local)
 *  
 * @author Juan
 *
 */
@SuppressWarnings({"rawtypes"})
public class LocalGame2dFelderValues {
	private static Session sessionLocalGame2d;
	public static final int FELDER_PROCESSING 		= 1;
	public static final int FELDER_PERCEPTION 		= 2;
	public static final int FELDER_INPUT 			= 3;
	public static final int FELDER_UNDERSTANDING 	= 0;
	
	
	@Before
	public void setUp() {
		sessionLocalGame2d = UtilsHibernate.getCurrentSessionLocalGame2d();
		sessionLocalGame2d.beginTransaction();
	}
	
	@Test
	public void testCalculate() {
		List list = sessionLocalGame2d.createSQLQuery("select * from felder").list();
		
		for (Object quest : list) {
			Object[] responses = (Object[]) quest;
			String username = (String) responses[45]; 
			User user = (User) sessionLocalGame2d.createQuery("from User where username = :username")
					.setString("username", username)
					.uniqueResult();

			if (user != null && user.getPerception() != 0) {
				int[] felder = calculateFelder(responses);
				
				System.out.printf("Username: %s - tabla felder: %d,%d,%d,%d - tabla user: %d,%d,%d,%d %n", 
						username, 
						felder[FELDER_PROCESSING], felder[FELDER_PERCEPTION], felder[FELDER_INPUT], felder[FELDER_UNDERSTANDING],
						user.getProcessing(), user.getPerception(), user.getInput(), user.getUnderstanding());	
				
				Assert.assertEquals((Integer)felder[FELDER_PROCESSING], user.getProcessing());
				Assert.assertEquals((Integer)felder[FELDER_PERCEPTION], user.getPerception());
				Assert.assertEquals((Integer)felder[FELDER_INPUT], user.getInput());
				Assert.assertEquals((Integer)felder[FELDER_UNDERSTANDING], user.getUnderstanding());
			} else {
				System.err.println(username);
			}
		}
	}
	
	private int[] calculateFelder(Object[] responses) {
		int values[] = new int [4];
		int index = 1;
		
		// en 0 esta el id
		for (int i = 1; i < 45; i++) {
			int answer = (Integer) responses[i];
			if (answer == 1) {
				values[index]++;
			} else {
				values[index]--;
			}
			index = (index + 1) % 4;
		}
		
		return values;
	}

	@After
	public void tearDown() {
		sessionLocalGame2d.getTransaction().commit();
	}
	
}
