package felder.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public abstract class UtilsHibernate {
	private static SessionFactory sfSaver;
	private static SessionFactory sfLocal;

	static {
		Configuration configuration = new Configuration().configure("hibernate-local.cfg.xml");
		ServiceRegistry serviceRegistry = new ServiceRegistryBuilder()
				.applySettings(configuration.getProperties())
				.buildServiceRegistry();
		sfLocal = configuration.buildSessionFactory(serviceRegistry);

		configuration = new Configuration().configure("hibernate-saver.cfg.xml");
		serviceRegistry = new ServiceRegistryBuilder()
				.applySettings(configuration.getProperties())
				.buildServiceRegistry();
		sfSaver = configuration.buildSessionFactory(serviceRegistry);
	}
	
	public static Session getCurrentSessionLocal() {
		return sfLocal.getCurrentSession();
	}
	
	public static Session getCurrentSessionSaver() {
		return sfSaver.getCurrentSession();
	}
}
