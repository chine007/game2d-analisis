package felder;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import felder.model.QuestionnaireILS;
import felder.utils.UtilsHibernate;

public class Main {
	private static SessionFactory sf;
	
	static {
		Configuration configuration = new Configuration().configure("hibernate-game.cfg.xml");
		ServiceRegistry serviceRegistry = new ServiceRegistryBuilder()
				.applySettings(configuration.getProperties())
				.buildServiceRegistry();
		sf = configuration.buildSessionFactory(serviceRegistry);
	}
	
	
	public static void main(String[] args) {
		deleteFromGame();
		copyFromSaverToGame();
	}

	/**
	 * Borra los datos de las encuestas de la BD local
	 */
	private static void deleteFromGame() {
		Session session = sf.getCurrentSession();
		session.beginTransaction();
		session.createQuery("delete from QuestionnaireILS").executeUpdate();
		session.getTransaction().commit();
	}
	
	/**
	 * Copia los datos de las encuestas desde la BD de saver a la BD local
	 */
	private static void copyFromSaverToGame() {
		Session session = sf.getCurrentSession();
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
		Session session = UtilsHibernate.getCurrentSession();
		session.beginTransaction();
		
		List<QuestionnaireILS> list = session
				.createCriteria(QuestionnaireILS.class)
				.setFirstResult(first)
				.setMaxResults(max)
				.list();
		
		session.getTransaction().commit();
		return list;
	}
	
}