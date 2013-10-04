package features.model;

import org.hibernate.classic.Session;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import features.model.Feature;
import features.model.Game;
import features.model.ProfileGame;
import features.utils.SessionManager;

public class TestModel {
	
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
