package felder.model;

import java.util.List;

import org.hibernate.Session;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import felder.utils.UtilsHibernate;

public class QuestionnaireILSTest {
	private static Session session;

	
	@Before
	public void setUp() {
		session = UtilsHibernate.getCurrentSession();
		session.beginTransaction();
	}
	
	@Test
	public void testCalculate() {
		@SuppressWarnings("unchecked")
		List<QuestionnaireILS> list = session
				.createCriteria(QuestionnaireILS.class)
				.setFirstResult(0)
				.setMaxResults(100)
				.list();
		
		for (QuestionnaireILS quest : list) {
			Assert.assertEquals("El calculo de las respuesta es invalido", quest.getFelder(), quest.calculate());
		}
	}
	
	@Test
	public void testAcentos() {
		QuestionnaireILS quest = (QuestionnaireILS) session.get(QuestionnaireILS.class, 169);
		Assert.assertTrue(quest.getFirstName().contains("á"));
	}
	
	@After
	public void tearDown() {
		session.getTransaction().commit();
	}
	
	@AfterClass
	public static void tearDownClass() {
		UtilsHibernate.getSessionFactory().close();
	}
	
}
