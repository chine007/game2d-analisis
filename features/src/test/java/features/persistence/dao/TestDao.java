package features.persistence.dao;

import org.hibernate.classic.Session;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import features.persistence.model.Feature;
import features.persistence.model.Game;
import features.persistence.model.ProfileGame;
import features.utils.SessionManager;

public class TestDao {
	
	@BeforeClass
	public static void before() {
		SessionManager.beginTransaction();
	}
	
	@Test
	public void test() {
		Session session = SessionManager.getSession();
		
		Game game = (Game) session.get(Game.class, "equilibrium");
		System.out.println(game);
		
		for (Feature gf : game.getFeatures()) {
			System.out.println(gf);
		}
	
		for (ProfileGame pg : game.getProfileGame()) {
			System.out.println(pg + " " + pg.getUser());
		}
	}

	@AfterClass
	public static void after() {
		SessionManager.commitTransaction();
	}
	
	
}
