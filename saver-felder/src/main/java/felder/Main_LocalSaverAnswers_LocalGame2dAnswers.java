package felder;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import features.persistence.model.User;
import felder.model.QuestionnaireILS;
import felder.utils.UtilsHibernate;

/**
 * Copia las respuestas del cuestionario de los alumnos desde
 * la tabla saver.cuestionario_ils local a la tabla game2d.felder
 * 
 * @author Juan
 *
 */
public class Main_LocalSaverAnswers_LocalGame2dAnswers {
	
	public static void main(String[] args) {
		copy();
	}

	private static void copy() {
		Session sessionLocalGame2d = UtilsHibernate.getCurrentSessionLocalGame2d();
		sessionLocalGame2d.beginTransaction();

		Session sessionLocalSaver = UtilsHibernate.getCurrentSessionLocalSaver();
		sessionLocalSaver.beginTransaction();
		
		// busca todos los usuarios que no tengan los valores de las encuestas
		@SuppressWarnings("unchecked")
		List<User> users = sessionLocalGame2d.createSQLQuery("select {u.*} from User {u} where not exists" +
				"(select f.username from Felder f where {u}.username = f.username)")
				.addEntity("u", User.class)
				.list();
		
		// crea el query
		StringBuilder sb = new StringBuilder();
		
		sb.append("INSERT INTO felder (");
		for (int i = 1; i < 45; i++) {
			sb.append("q" + i + ", ");
		}
		sb.append("username, elapsedtime")
		.append(") VALUES (");
		
		for (int i = 1; i < 47; i++) {
			sb.append("?, ");
		}
		sb.delete(sb.length() - 2, sb.length())
		.append(")");
		
		// itera todos los usuarios que no tienen los valores de las encuestas
		for (User user : users) {
			@SuppressWarnings("unchecked")
			List<QuestionnaireILS> list = sessionLocalSaver.createQuery("from QuestionnaireILS q where q.firstName = :firstName and q.lastName = :lastName order by idreg")
					.setString("firstName", user.getFirstName())
					.setString("lastName", user.getLastName())
					.list();
			
			// setea paremtros
			if (list.size() > 0) {
				QuestionnaireILS ques = list.get(0);
				Query query = sessionLocalGame2d.createSQLQuery(sb.toString());
				for (int i = 0; i < 44; i++) {
					query.setParameter(i, ques.getQuestionnaire()[i] + 1);
				}
				query.setParameter(44, user.getUsername());
				query.setParameter(45, 0);
				
				// ejecuta el query
				query.executeUpdate();
			}
		}
		
		sessionLocalGame2d.getTransaction().commit();
	}
	
	
}