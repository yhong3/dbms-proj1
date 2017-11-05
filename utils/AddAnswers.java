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
			concrete_id  = findmaxid(conn, "concrete_answer_id", "CONCRETE_ANSWER");
		}
		else {
			answer_id = findmaxid(conn, "parameter_answer_id", "PARAMETER_ANSWER");
			parameter_id = countrow(conn, SqlQueries.SQL_GETPARAMETERID, "");
		}
		//System.out.println("Question ID: " + question_id);
		//System.out.println("Concrete ID: " + concrete_id);
		//System.out.println("Answer ID: " + answer_id);
		//System.out.println("Parameter ID: " + parameter_id);
	}

	public static int parameterizeQ(Connection conn, Scanner s, int n_o_p) {
		//TO DO: update double to String
		PreparedStatement preparedStatement;
		int check = -1;
		int stop = -1;
		while (stop != 0) {
			parameter_id += 1;
			String[] parameters = new String[n_o_p];
			boolean no_null_param = true; 
			for(int i = 0; i < n_o_p; i++) {
				System.out.printf("The value of parameter %d of the question is:", i);
				parameters[i] = s.nextLine();
				if(!notEmptyStr(parameters[i])) {
					no_null_param = false; 
				}
				System.out.println();
			}
			if(no_null_param) {
				int count_correct_answer = 0;
				int count_incorrect_answer = 0;
				while(true) {
					System.out.println("What is the answer text for this set of parameters: ");
					answer_text = s.nextLine();

					System.out.println("What is the short explanation of this answer text? ");
					short_explanation = s.nextLine();

					System.out.println("Is this answer incorrect or correct one? (0 for incorrect, 1 for correct)");
					type = s.nextInt();
					s.nextLine();
					System.out.println();
					
					if(notEmptyStr(answer_text) ||  notEmptyStr(short_explanation) || (type == 1 || type == 0)) {
						if(type == 1) {
							count_correct_answer += 1;
						} else {
							count_incorrect_answer += 1;
						}
						
						if(count_correct_answer > 1 || count_incorrect_answer > 3) {
							System.out.printf("\n We already have %d correct answer ", count_correct_answer);
							System.out.printf("\n We already have %d incorrect answer ", count_incorrect_answer);
							System.out.printf("\n We should have 1 correct answer and 3 incorrect answer \n\n");
							if(type == 1) {
								count_correct_answer -= 1;
							} else {
								count_incorrect_answer -= 1;
							}
							clearsetting(conn, 1);
						} else {
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
								clearsetting(conn, 1);
								check = 1;
								
								if(count_correct_answer == 1 && count_incorrect_answer == 3) {
									System.out.printf("We already have 1 correct answer and 3 incorrect answer enough for this set of parameter\n");
									break;
								} else {
									System.out.println("Please add more answer possibilities for this set of parameters!");
								}
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								clearsetting(conn, 1);
								parameter_id -= 1;
								check = 0;
								e.printStackTrace();
							}
						}
					}
					else{
						System.out.println("One of the parameter answer's variables is invalid. Please re-add this answer!");
						System.out.println();
					}	
				}
			} else {
				System.out.println("One of the parameter of this set is null. Please re-add this set of parameters!");
				System.out.println();
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
	
	public static int findmaxid(Connection conn, String another_query, String tablename) {
		ResultSet rs;
		int count = 0;
		PreparedStatement ps;
		String query = SqlQueries.SQL_FINDID + tablename;
		query = query.replace("?1", another_query);
		//System.out.println(query);
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
	
	public static int concreteQ(Connection conn, Scanner s) {
		PreparedStatement preparedStatement;
		int check = -1;
		int count_correct_answer = 0;
		int count_incorrect_answer = 0;
		while (true) {
			System.out.println("What is the possible answer of this concrete question: ");
			answer_text = s.nextLine();

			System.out.println("\n What is the short explanation for this concrete answer: ");
			short_explanation = s.nextLine();

			System.out.println("\n Is this answer incorrect or correct one? (0 for incorrect, 1 for correct)");
			type = s.nextInt();
			s.nextLine();
			
			if(notEmptyStr(answer_text) ||  notEmptyStr(short_explanation) || (type == 1 || type == 0)) {
				if(type == 1) {
					count_correct_answer += 1;
				} else {
					count_incorrect_answer += 1;
				}
				if(count_correct_answer > 1 || count_incorrect_answer > 3) {
					System.out.printf("\n We have %d correct answer ", count_correct_answer);
					System.out.printf("\n We have %d incorrect answer ", count_incorrect_answer);
					System.out.printf("\n We should have 1 correct answer and 3 incorrect answer \n\n");
					if(type == 1) {
						count_correct_answer -= 1;
					} else {
						count_incorrect_answer -= 1;
					}
				}
				else {
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
						if(count_correct_answer == 1 && count_incorrect_answer == 3) {
							System.out.printf("We already have 1 correct answer and 3 incorrect answer enough for this set of parameter\n");
							System.out.println();
							break;
						} else {
							System.out.println("Please add more answer possibilities for this set of parameters!");
							System.out.println();
						}
						clearsetting(conn, 0);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						clearsetting(conn, 0);
						check = 0;
						e.printStackTrace();
						continue;
					}
				}
			}
			else {
				System.out.println("One of the concrete answer's variables is invalid. Please re-add this answer!");
				System.out.println();
			}
			
		} //ends while 
		return check;
	}

	public static void clearsetting(Connection conn, int setting) {
		type = -1;
		answer_text = "";
		short_explanation = "";
		if(setting == 1) {
			answer_id = findmaxid(conn, "parameter_answer_id", "PARAMETER_ANSWER");
		} else {
			concrete_id = findmaxid(conn, "concrete_answer_id", "CONCRETE_ANSWER");
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
	
	public static boolean notEmptyStr(String str) {
		return str != null && !str.isEmpty();
	}
}
