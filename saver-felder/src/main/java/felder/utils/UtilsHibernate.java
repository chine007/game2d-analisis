package felder.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public abstract class UtilsHibernate {
	private static SessionFactory sfRemoteSaver;
	private static SessionFactory sfLocalSaver;
	private static SessionFactory sfLocalGame2d;

	static {
		// conexion a game2d
		Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
		ServiceRegistry serviceRegistry = new ServiceRegistryBuilder()
				.applySettings(configuration.getProperties())
				.buildServiceRegistry();
		sfLocalGame2d = configuration.buildSessionFactory(serviceRegistry);

		// conexion local a saver
		configuration = new Configuration().configure("hibernate-local-saver.cfg.xml");
		serviceRegistry = new ServiceRegistryBuilder()
				.applySettings(configuration.getProperties())
				.buildServiceRegistry();
		sfLocalSaver = configuration.buildSessionFactory(serviceRegistry);

		// conexion remota a saver
		configuration = new Configuration().configure("hibernate-remote-saver.cfg.xml");
		serviceRegistry = new ServiceRegistryBuilder()
				.applySettings(configuration.getProperties())
				.buildServiceRegistry();
		sfRemoteSaver = configuration.buildSessionFactory(serviceRegistry);
	}
	
	public static Session getCurrentSessionLocalGame2d() {
		return sfLocalGame2d.getCurrentSession();
	}
	
	public static Session getCurrentSessionLocalSaver() {
		return sfLocalSaver.getCurrentSession();
	}
	
	public static Session getCurrentSessionRemoteSaver() {
		return sfRemoteSaver.getCurrentSession();
	}
}
