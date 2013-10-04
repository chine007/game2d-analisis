package features.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public class SessionManager {
	private static SessionFactory sf;

	private SessionManager() {
	}
	
	public static Session getSession() {
		if (sf == null) {
			Configuration configuration = new Configuration().configure();
			ServiceRegistry serviceRegistry = new ServiceRegistryBuilder()
					.applySettings(configuration.getProperties())
					.buildServiceRegistry();
			sf = configuration.buildSessionFactory(serviceRegistry);
		}
		return sf.getCurrentSession();
	}
	
	public static void beginTransaction() {
		getSession().beginTransaction();
	}
	
	public static void commitTransaction() {
		getSession().getTransaction().commit();
	}
	
}
