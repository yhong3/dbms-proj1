import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ExerciseMenu {
	private static PreparedStatement stmt;
	private static final int GOBACK = 0;
	
	public static void displayViewMenu(Connection conn, User currentUser) {
		int exerciseId = 0;
		int choice = 0;
		int lastQuerySuccess = 0;
		Scanner s = new Scanner(System.in);

		while (true) {

			// set profile for current user
			switch (currentUser.getRole()) {
			case RoleType.PROFESSOR:
			case RoleType.TA:

				// print out menu
				System.out.println("You are currently logged in as: " + currentUser.getUser_id());
				System.out.println("Display all exercises for a course");
				System.out.print("Please Enter Exercise ID: ");

				exerciseId = s.nextInt();
				lastQuerySuccess = queryExercise(conn, exerciseId);
				
				if (lastQuerySuccess == 1) { // set by latest query
					System.out.println("0: Go back to previous menu. ");
					System.out.println("1. Add to/ Remove Questions from Exercise");
				} else { // TODO handle SQL exception
					System.out.println("Exercise ID not found, please re-enter: ");
					continue;
				}

				choice = s.nextInt();

				// handle user input
				if (choice == GOBACK) {
					return;
				} else {
					System.out.println("Invalid choice. Please try again.");
				}
				break;
			case RoleType.STUDENT:
				// student menu
				break;

			}             
		} // end while
	} // end displayMenu()
	private static int queryExercise(Connection conn, int exerciseId) {
		System.out.println("called");
		
		try {
			String query = "";
			ResultSet rs;
			
			// exercise parameters

					
			query = "SELECT exercise_id, exercise_name, exercise_start, exercise_end, retries_allowed, num_of_questions, scoring_policy, correct_answer_points, incorrect_answer_penalty, difficulty_level_min, difficulty_level_max "
					+ "FROM EXERCISE "
					+ "WHERE exercise_id=?";
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, exerciseId);
			
			// loop through result
			rs = stmt.executeQuery();
			
			if (rs.next()) { // result found
				System.out.println("rsfound");

				while (rs.next()) {
					int exercise_id = 0;
					String exercise_name = "";
					String exercise_start = "";
					String exercise_end = "";
					int retries_allowed = 0;
					int num_of_questions = 0;
					String scoring_policy = "";
					int correct_answer_points = 0;
					int incorrect_answer_penalty = 0;
					int difficulty_level_min = 0;
					int difficulty_level_max = 0;
					System.out.println("result found");

					exercise_id = rs.getInt("exercise_id");
					exercise_name = rs.getString("exercise_name");
					exercise_start = rs.getString("exercise_start"); 
					exercise_end = rs.getString("exercise_end"); 
					retries_allowed = rs.getInt("retires_allowed");
					num_of_questions = rs.getInt("num_of_questions");
					scoring_policy = rs.getString("scoring_policy");
					correct_answer_points = rs.getInt("correct_answer_points");
					incorrect_answer_penalty = rs.getInt("incorrect_answer_penalty");
					difficulty_level_min = rs.getInt("difficulty_level_min");
					difficulty_level_max = rs.getInt("difficulty_level_max");
					//System.out.println(user_id + fullName);
					
					System.out.println("Exercise ID : " + exercise_id);
					System.out.println("Exercise Name : " + exercise_name);
					System.out.println("Exercise Start Date : " + exercise_start);
					System.out.println("Exercise End Date: " + exercise_end);
					System.out.println("Retries Allowed : " + retries_allowed);
					System.out.println("Number of Questions: " + num_of_questions);
					System.out.println("Scoring Policy: " + scoring_policy);
					System.out.println("Points for each correct answer: " + correct_answer_points);
					System.out.println("Penalty points for each inccorect answer: " + incorrect_answer_penalty);
					System.out.println("Difficulty Level Range (max 1-5): " + difficulty_level_min + "-" + difficulty_level_max );
			
				}
				// print out result

				return 1;
			} else { // result set empty
				System.out.println("result not found");
				return 0;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return 0;
	} // end setProfile()
	
} // end class
