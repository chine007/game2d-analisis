package felder;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import features.model.User;
import felder.model.QuestionnaireILS;
import felder.utils.UtilsHibernate;

/**
 * Copia las respuestas del cuestionario desde
 * la tabla saver.cuestionario_ils local a la tabla game2d.felder
 * 
 * @author Juan
 *
 */
public class Main_LocalSaverAnswers_LocalGame2dAnswers {
	
	public static void main(String[] args) {
		copy();
	}

	@SuppressWarnings("unchecked")
	private static void copy() {
		Session sessionLocalGame2d = UtilsHibernate.getCurrentSessionLocalGame2d();
		sessionLocalGame2d.beginTransaction();

		Session sessionLocalSaver = UtilsHibernate.getCurrentSessionLocalSaver();
		sessionLocalSaver.beginTransaction();
		
		// busca todos los usuarios que no hayan contestado la encuesta de Felder publicada en Game2d
		List<User> users = sessionLocalGame2d.createSQLQuery(
				"select {u.*} from User {u} " +
				"left join Felder f " +
				"on u.username = f.username " +
				"where f.elapsedtime = 0")
				.addEntity("u", User.class)
				.list();
		
		// crea el query de insercion
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
		
		// itera todos los usuarios que no hayan contestado la encuesta de Felder publicada en Game2d
		for (User user : users) {
			List<QuestionnaireILS> list = sessionLocalSaver.createQuery("from QuestionnaireILS q where q.firstName = :firstName and q.lastName = :lastName order by idreg")
					.setString("firstName", user.getFirstName())
					.setString("lastName", user.getLastName())
					.list();
			
			// el usuario contesto al menos una vez la encuesta de saver
			if (list.size() > 0) {
				// borra las respuestas de la BD local
				sessionLocalGame2d.createSQLQuery("delete from Felder where username = :username")
				.setString("username", user.getUsername())
				.executeUpdate();
				
				// setea parametros
				QuestionnaireILS ques = list.get(0); // se queda con la primer encuesta contestada
				Query query = sessionLocalGame2d.createSQLQuery(sb.toString());
				for (int i = 0; i < 44; i++) {
					query.setParameter(i, ques.getQuestionnaire()[i] + 1); //saver usa 0 y 1 - game2d.felder usa 1 y 2
				}
				query.setParameter(44, user.getUsername());
				query.setParameter(45, 0);
				
				// ejecuta el query
				query.executeUpdate();
				
				// actualiza valores en la tabla user
//				int[] felder = UtilsFelder.calculateFelder(ques.getQuestionnaire(), 0, 43, 0);
				user.setProcessing(ques.getFelder().getProcessing());
				user.setPerception(ques.getFelder().getPerception());
				user.setInput(ques.getFelder().getInput());
				user.setUnderstanding(ques.getFelder().getUnderstanding());
			}
		}
		
		sessionLocalGame2d.getTransaction().commit();
		sessionLocalSaver.getTransaction().commit();
	}
	
	
}