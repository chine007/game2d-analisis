package felder;

import java.util.Arrays;
import java.util.List;

import org.hibernate.Session;

import felder.model.QuestionnaireILS;
import felder.utils.UtilsHibernate;

/**
 * Copia los datos de la tabla SAVER.cuestionario_ils del sistema Saver
 * a la tabla saver.cuestionario_ils local
 * 
 * @author Juan
 *
 */
public class Main_RemoteSaver_LocalSaver {
	
	public static void main(String[] args) {
		deleteFromLocal();
		copyFromSaverToLocal();
		printLocal();
	}

	/**
	 * Borra los datos de las encuestas de la BD local
	 */
	private static void deleteFromLocal() {
		Session session = UtilsHibernate.getCurrentSessionLocalSaver();
		session.beginTransaction();
		session.createQuery("delete from QuestionnaireILS").executeUpdate();
		session.getTransaction().commit();
	}
	
	/**
	 * Copia los datos de las encuestas desde la BD de saver a la BD local
	 */
	private static void copyFromSaverToLocal() {
		Session session = UtilsHibernate.getCurrentSessionLocalSaver();
		session.beginTransaction();
		
		int first = 0;
		int max = 100;
		boolean end = false;
		while(!end) {
			System.out.println(String.format("Copiando desde %d hasta %d", first, first + max));

			List<QuestionnaireILS> list = getSaverQuestionnaries(first, max);
			for (QuestionnaireILS quest : list) {
				session.save(quest);
			}
			first += max;
			session.flush();
			session.clear();
			
			end = list.size() < max;
		}
		
		session.getTransaction().commit();
	}

	/**
	 * Retorna los datos de las encuestas desde la BD de saver
	 * 
	 * @param first
	 * @param max
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static List<QuestionnaireILS> getSaverQuestionnaries(int first, int max) {
		Session session = UtilsHibernate.getCurrentSessionRemoteSaver();
		session.beginTransaction();
		
		List<QuestionnaireILS> list = session
				.createCriteria(QuestionnaireILS.class)
				.setFirstResult(first)
				.setMaxResults(max)
				.list();
		
		session.getTransaction().commit();
		return list;
	}

	/**
	 * Imprime los datos por pantalla
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static List<QuestionnaireILS> printLocal() {
		Session session = UtilsHibernate.getCurrentSessionRemoteSaver();
		session.beginTransaction();
		
		List<QuestionnaireILS> list = session
				.createCriteria(QuestionnaireILS.class)
				.list();
		
		for (QuestionnaireILS ils : list) {
			System.out.println(ils.getFirstName() + " - " +
			ils.getLastName() + " - " + 
			Arrays.toString(ils.getQuestionnaire()));
		}
		
		session.getTransaction().commit();
		return list;
	}
	
}