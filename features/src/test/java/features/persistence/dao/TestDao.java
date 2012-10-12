package features.persistence.dao;

import org.hibernate.classic.Session;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import features.persistence.model.Feature;
import features.persistence.model.Game;
import features.persistence.model.GameCategory;
import features.persistence.model.ProfileGame;

public class TestDao {
	private static Dao dao;
	
	@BeforeClass
	public static void before() {
		dao = new Dao();
		dao.beginTransaction();
	}
	
	@Test
	public void test() {
		Session session = dao.getSession();
		
		Game game = (Game) session.get(Game.class, "equilibrium");
		System.out.println(game);
		
		for (GameCategory gc : game.getCategories()) {
			System.out.println(gc);
			for (Feature gf : gc.getFeatures()) {
				System.out.println(gf);
			}
		}
	
		for (ProfileGame pg : game.getProfileGame()) {
			System.out.println(pg + " " + pg.getUser());
		}
	}

	@AfterClass
	public static void after() {
		dao.commitTransaction();
	}
	
	
}
