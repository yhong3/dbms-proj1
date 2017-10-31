import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import sql.*;

public class QuestionMenu {
	private static final int SEARCH = 1;
	private static final int ADDQ = 2;
	private static final int PREVIOUS = 3;

	private static int question_id = -1;
	private static int type = -1;
	private static String question_text = "";
	private static int topic = -1;
	private static String hint = "";
	private static int difficulty_level = -1;
	private static String detailed_explanation = "";


	public static void displayMenu(Connection conn) {
		int choice = 0;
		int q_id = -1; 
		int check = 1; 
		Scanner s = new Scanner(System.in);

		while (check == 1) {
			System.out.println("View Question Bank. Please choose an option: ");
			System.out.println("1. Search by Question ID");
			System.out.println("2. Add Question to the bank");
			System.out.println("3. Go back to previous menu");
			System.out.print("Enter your choice and press Enter to continue. ");

			choice = s.nextInt();

			switch (choice) {
			case SEARCH:
				//TODO view the question
				System.out.println("What is the question id of the question that you are looking for?");
				q_id = s.nextInt();
				check = queryQuestion(conn, q_id);

			case ADDQ:
				check = addQuestion(conn, s);
				break;
			case PREVIOUS:
				check = 0;
				break;
			default:
				System.out.println("Invalid menu choice!");
				break;
			} // end switch
		} // end while
	} // end displayMenu()

	private static int queryQuestion(Connection conn, int questionId) {
		System.out.println("called");
		PreparedStatement  stmt = null;
		try {
			String query = "";
			ResultSet rs;

			query = "SELECT question_id, topic_id, type, question_text, hint, difficulty_level, detailed_explanation"
					+ "FROM QUESTION "
					+ "WHERE question_id=?";
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, questionId);

			// loop through result
			rs = stmt.executeQuery();
			System.out.println("rsfound");
			while (rs.next()) {
				System.out.println("result found");

				question_id = rs.getInt("question_id");
				type = rs.getInt("type");
				question_text = rs.getString("question_text");
				topic = rs.getInt("topic_id");
				hint = rs.getString("hint");
				difficulty_level = rs.getInt("difficulty_level");
				detailed_explanation = rs.getString("detailed_explanation");

				System.out.println("Question ID : " + question_id);
				System.out.println("Question text : " + question_text);
				System.out.println("Question type : " + type);
				System.out.println("Detailed Explanation: " + detailed_explanation);
				System.out.println("Topic : " + topic);
				System.out.println("Hint : " + hint);
				System.out.println("Difficulty Level: " + difficulty_level);
				System.out.println();
			}
			return 1;
		} 
		catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return 0;
	} // end setProfile()

	private static int addQuestion(Connection conn, Scanner s) {
		int check_add_question = 0;
		question_id = findmaxid(conn, "question_id", "QUESTION") + 1;
		System.out.print("The topic id of the question is: ");
		topic = s.nextInt();
		s.nextLine();
		System.out.print("The text of the question is: \n");
		question_text = s.nextLine();
		System.out.println("The hint of the question is: ");
		hint = s.nextLine();
		System.out.println("The detailed explanation of the question is: ");
		detailed_explanation = s.nextLine();
		System.out.println("The difficulty level of the question is: ");
		difficulty_level = s.nextInt();
		s.nextLine();

		PreparedStatement preparedStatement;
		int check_question_info = 0;
		try {
			preparedStatement = conn.prepareStatement(SqlQueries.SQL_INSERTQUESTION);
			preparedStatement.setInt(1, question_id);
			preparedStatement.setInt(2, topic);
			preparedStatement.setString(3, question_text);
			preparedStatement.setInt(4, difficulty_level);
			preparedStatement.setString(5, hint);
			preparedStatement.setString(6, detailed_explanation);
			preparedStatement.setInt(7, type);
			preparedStatement.execute();
			while(check_question_info == 0) {
				check_question_info = questioninfo(conn);
				if(check_question_info == 0) {
					System.out.println("Please add at least one answer for the added question!");
				}
			}
			check_add_question = 1;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			check_add_question = 0;
			e.printStackTrace();
		}
		return check_add_question;
	} 

	public static int questioninfo(Connection conn) {
		int choice = 0;
		int n_o_p = -1; 
		Scanner s = new Scanner(System.in);
		int check = -1;
		int loopc = 1;


		while (loopc == 1) {
			System.out.println("What type of question do you want to add?");
			System.out.println("1. Parameterized");
			System.out.println("2. Concrete");
			choice = s.nextInt();
			s.nextLine();
			switch (choice) {
			case 1:
				//TODO view the question
				type = 0;
				System.out.println("How many parameters are you looking to have in your questions?");
				System.out.println("(The maximum number is 5)");
				n_o_p = s.nextInt();
				s.nextLine();
				if(n_o_p <= 5 & n_o_p > 0) {
					AddAnswers aa = new AddAnswers(conn, n_o_p);
					check = aa.parameterizeQ(conn, s, n_o_p);
					loopc = questionflow(check, loopc, s);
				}
				break;
			case 2:
				type = 1;
				AddAnswers aa = new AddAnswers(conn, n_o_p);
				check = aa.concreteQ(conn, s);
				loopc = questionflow(check, loopc, s);
				break;
			default:
				System.out.println("Invalid menu choice!");
				s.close();
				break;
			} // end switch
		} // end while
		if(check == 1) {
			return 1;
		}
		else {
			return 0;
		}
	}

	public static void clearsetting() {
		type = -1;
		question_id -= 1;
		question_text = "";
		topic = -1;
		hint = "";
		difficulty_level = -1;
		detailed_explanation = "";
	}

	public static int questionflow(int check_insert, int loop_check, Scanner s) {
		String sp_decision; 
		if(check_insert == 0) {
			System.out.println("The question was unsucessfully added, would you want to readd or add another answer?");
			sp_decision = s.nextLine();
		}
		else {
			System.out.println("The question was sucessfully added, would you want to readd or add another answer?");
			sp_decision = s.nextLine();
		}

		if(sp_decision.toLowerCase().charAt(0) == 'y') {
			loop_check = 1;
		} else {
			loop_check = 0;
		}
		return loop_check;
	}
	
	public static int findmaxid(Connection conn, String another_query, String tablename) {
		ResultSet rs;
		int count = 0;
		PreparedStatement ps;
		String query = SqlQueries.SQL_FINDID + tablename;
		query = query.replace("?1", another_query);
		System.out.println(query);
		try {
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