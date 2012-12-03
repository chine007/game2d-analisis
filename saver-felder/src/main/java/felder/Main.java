package felder;

import org.hibernate.Session;

import felder.utils.UtilsHibernate;

public class Main {
	
	public static void main(String[] args) {
		Session session = UtilsHibernate.getCurrentSession();
		session.beginTransaction();
		
		
		session.getTransaction().commit();
	}

}
