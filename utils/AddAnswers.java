package utils;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.Types;
import sql.*;

public class AddAnswers {
	private static int n_o_p = -1;
	private static int question_id = -1;
	private static int answer_id = -1;
	private static int parameter_id = -1; 
	private static int concrete_id = -1; 
	private static String answer_text = "";
	private static String short_explanation = "";
	private static int type = -1;

	AddAnswers(Connection conn, int number_of_p){
		n_o_p = number_of_p;
		question_id  = countrow(conn, "question_id", "QUESTION");
		if(n_o_p == -1) {
			concrete_id  = countrow(conn, "answer_id", "CONCRETE_ANSWER");
		}
		else {
			String query = "SELECT PARAMETER_ID FROM PARAMETER_ANSWER " +
					"WHERE (QUESTION_ID=1? AND PARAMETER_ANSWER_ID=2?)";
			answer_id = countrow(conn, "answer_id", "PARAMETER_ANSWER");
			parameter_id = countrow(conn, query, "");
		}
		System.out.println("Question ID: " + question_id);
		System.out.println("Concrete ID: " + concrete_id);
		System.out.println("Answer ID: " + answer_id);
		System.out.println("Parameter ID: " + parameter_id);
	}

	public static int parameterizeQ(Connection conn, Scanner s, int n_o_p) {
		//TO DO: update double to String
		PreparedStatement preparedStatement;
		int check = -1;
		int stop = -1;
		while (stop != 0) {
			parameter_id += 1;
			String[] parameters = new String[n_o_p];
			for(int i = 0; i < n_o_p; i++) {
				System.out.printf("The value of parameter %d of the question is:", i);
				parameters[i] = s.nextLine();
				System.out.println();
			}

			while(true) {
				System.out.println("What is the answer text for this set of parameters: ");
				answer_text = s.nextLine();

				System.out.println("What is the short explanation of this answer text? ");
				short_explanation = s.nextLine();

				System.out.println("Is this answer incorrect or correct one? (0 for incorrect, 1 for correct)");
				type = s.nextInt();
				s.nextLine();

				try {
					answer_id += 1;
					preparedStatement = conn.prepareStatement(SqlQueries.SQL_INSERTAP);
					preparedStatement.setInt(1, question_id);
					preparedStatement.setInt(2, parameter_id);
					preparedStatement.setInt(3, answer_id);
					for(int i = 0; i < n_o_p; i++) {
						preparedStatement.setString(i+4, parameters[i]);
					}
					for(int i = 0; i < 5 - n_o_p; i++) {
						preparedStatement.setNull(8-i, Types.VARCHAR);
					}
					preparedStatement.setString(9, answer_text);
					preparedStatement.setString(10, short_explanation);
					preparedStatement.setInt(11, type);
					preparedStatement.execute();
					check = 1;
					System.out.println("Do you want to add more answer text for this set of parameters?");
					String at_decision = s.nextLine();
					if(at_decision.toLowerCase().charAt(0) == 'n') {
						break;
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					clearsetting(1);
					parameter_id -= 1;
					check = 0;
					e.printStackTrace();
				}
			}

			System.out.println("Do you want to add another set of parameters and answer texts with it?");
			String sp_decision = s.nextLine();
			if(sp_decision.toLowerCase().charAt(0) == 'y') {
				stop = 1;
			} else {
				stop = 0;
			}
		} // end while
		return check;
	}

	public static int concreteQ(Connection conn, Scanner s) {
		PreparedStatement preparedStatement;
		int check = -1; 
		while (true) {
			System.out.println("What is the possible answer of this concrete question: ");
			answer_text = s.nextLine();

			System.out.println("What is the short explanation for this concrete answer: ");
			short_explanation = s.nextLine();

			System.out.println("Is this answer incorrect or correct one? (0 for incorrect, 1 for correct)");
			type = s.nextInt();
			s.nextLine();

			try {
				preparedStatement = conn.prepareStatement(SqlQueries.SQL_INSERTAC);
				concrete_id += 1;
				preparedStatement.setInt(1, question_id);
				preparedStatement.setInt(2, concrete_id);
				preparedStatement.setString(3, answer_text);
				preparedStatement.setString(4, short_explanation);
				preparedStatement.setInt(5, type);
				preparedStatement.execute();
				check = 1;
				System.out.println("Do you want to add more answers for this concrete question?");
				String decision = s.nextLine();
				if(decision.toLowerCase().charAt(0) == 'n') {
					break;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				clearsetting(0);
				check = 0;
				e.printStackTrace();
				continue;
			}
		} //ends while 
		return check;
	}

	public static void clearsetting(int setting) {
		type = -1;
		answer_text = "";
		short_explanation = "";
		if(setting == 1) {
			answer_id -= 1;
		} else {
			concrete_id -= 1;
		}
	}

	public static int countrow(Connection conn, String another_query, String tablename) {
		ResultSet rs;
		int count = 0;
		PreparedStatement ps;
		String query = "";
		try {

			if(tablename != "") {
				query = SqlQueries.SQL_FINDID + tablename;
				query = query.replace("?1", another_query);
			}
			else {
				another_query = another_query.replace("1?", Integer.toString(question_id));
				another_query = another_query.replace("2?", Integer.toString(answer_id));
				query = another_query;
			}
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while(rs.next()){
				count = rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}
}
