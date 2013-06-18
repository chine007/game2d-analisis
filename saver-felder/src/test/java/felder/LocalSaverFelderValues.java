package felder;

import java.util.List;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import features.persistence.model.User;
import felder.model.QuestionnaireILS;
import felder.utils.UtilsHibernate;

/**
 * Esta clase testea que los valores de Felder en saver.cuestionario_ils (Local) sean iguales
 * a los de game2d.user (Local)
 *  
 * @author Juan
 *
 */
@SuppressWarnings({"unchecked", "rawtypes"})
@Deprecated
public class LocalSaverFelderValues {
	private static Session sessionLocalGame2d;
	private static Session sessionLocalSaver;

	
	@Before
	public void setUp() {
		sessionLocalGame2d = UtilsHibernate.getCurrentSessionLocalGame2d();
		sessionLocalGame2d.beginTransaction();

		sessionLocalSaver = UtilsHibernate.getCurrentSessionLocalSaver();
		sessionLocalSaver.beginTransaction();
	}
	
	@Test
	public void testCalculate() {
		// Busca todos los usuarios locales
		List<User> users = sessionLocalGame2d.createQuery("from User").list();
		
		for (User user : users) {
//			// Verifica si el usuario completo la encuesta de Felder antes de jugar
//			List value = sessionLocalGame2d.createSQLQuery("select count(*) from felder f where f.username = :username")
//			.setString("username", user.getUsername()).list();
			
			// Si no completo la encuesta antes de jugar verifica sincronizacion Local - Saver
			if (/*((Number)value.get(0)).intValue() == 0 && */user.getPerception() != 0) {
				List saverUsers = sessionLocalSaver.createQuery("select q from QuestionnaireILS q where firstName = :firstName" +
						" and lastName = :lastName order by id")
						.setString("firstName", user.getFirstName())
						.setString("lastName", user.getLastName())
						.list();
				
				if (saverUsers.size() > 0) {
					String msg = String.format("El usuario %s no esta sincronizado", user.getUsername());
					QuestionnaireILS userSaver = (QuestionnaireILS) saverUsers.get(0);
					Assert.assertEquals(msg, user.getProcessing(), (Integer)userSaver.getFelder().getProcessing());
					Assert.assertEquals(msg, user.getPerception(), (Integer)userSaver.getFelder().getPerception());
					Assert.assertEquals(msg, user.getInput(), (Integer)userSaver.getFelder().getInput());
					Assert.assertEquals(msg, user.getUnderstanding(), (Integer)userSaver.getFelder().getUnderstanding());
				} else {
					System.out.printf("El usuario %s (%s - %s) no existe en Saver%n", user.getUsername(),
							user.getFirstName(), user.getLastName());
				}
			}
		}
	}
	
	@After
	public void tearDown() {
		sessionLocalGame2d.getTransaction().commit();
		sessionLocalSaver.getTransaction().commit();
	}
	
}
