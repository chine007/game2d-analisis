package features.utils;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;

public class SessionManager {
	private static SessionFactory sf;

	private SessionManager() {
	}
	
	public static Session getSession() {
		if (sf == null) {
			sf = new Configuration().configure().buildSessionFactory();
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
