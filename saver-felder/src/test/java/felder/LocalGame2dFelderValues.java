package felder;

import static felder.utils.UtilsFelder.FELDER_INPUT;
import static felder.utils.UtilsFelder.FELDER_PERCEPTION;
import static felder.utils.UtilsFelder.FELDER_PROCESSING;
import static felder.utils.UtilsFelder.FELDER_UNDERSTANDING;
import static felder.utils.UtilsFelder.calculateFelder;

import java.util.List;

import org.hibernate.Session;
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
	
	
	@Before
	public void setUp() {
		sessionLocalGame2d = UtilsHibernate.getCurrentSessionLocalGame2d();
		sessionLocalGame2d.beginTransaction();
	}
	
	@Test
	public void testCalculate() {
		// obtiene todas las encuestas
		List list = sessionLocalGame2d.createSQLQuery("select * from felder").list();
		
		for (Object quest : list) {
			Object[] responses = (Object[]) quest;

			// obtiene el usuario que respondio la encuesta
			String username = (String) responses[45]; 
			User user = (User) sessionLocalGame2d.createQuery("from User where username = :username")
					.setString("username", username)
					.uniqueResult();

			// si el usuario existe y respondio la encuesta
			if (user != null && user.getPerception() != 0) {
				int[] felder = calculateFelder(responses, 1, 44, 1);
				
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
	
	public void tearDown() {
		sessionLocalGame2d.getTransaction().commit();
	}
	
}
