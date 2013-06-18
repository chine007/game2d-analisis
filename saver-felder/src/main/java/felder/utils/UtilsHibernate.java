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

	/**
	 * Conexion a game2d  
	 * 
	 * @return
	 */
	public static Session getCurrentSessionLocalGame2d() {
		if (sfLocalGame2d == null) {
			// el file lo toma del proyecto features
			Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
			ServiceRegistry serviceRegistry = new ServiceRegistryBuilder()
					.applySettings(configuration.getProperties())
					.buildServiceRegistry();
			sfLocalGame2d = configuration.buildSessionFactory(serviceRegistry);
		}
		return sfLocalGame2d.getCurrentSession();
	}
	
	/**
	 * Conexion local a saver
	 *  
	 * @return
	 */
	public static Session getCurrentSessionLocalSaver() {
		if (sfLocalSaver == null) {
			Configuration configuration = new Configuration().configure("hibernate-local-saver.cfg.xml");
			ServiceRegistry serviceRegistry = new ServiceRegistryBuilder()
					.applySettings(configuration.getProperties())
					.buildServiceRegistry();
			sfLocalSaver = configuration.buildSessionFactory(serviceRegistry);
		}
		return sfLocalSaver.getCurrentSession();
	}
	
	/**
	 * Conexion remota a saver
	 * 
	 * @return
	 */
	public static Session getCurrentSessionRemoteSaver() {
		if (sfRemoteSaver == null) {
			Configuration configuration = new Configuration().configure("hibernate-remote-saver.cfg.xml");
			ServiceRegistry serviceRegistry = new ServiceRegistryBuilder()
					.applySettings(configuration.getProperties())
					.buildServiceRegistry();
			sfRemoteSaver = configuration.buildSessionFactory(serviceRegistry);
		}
		return sfRemoteSaver.getCurrentSession();
	}
}
