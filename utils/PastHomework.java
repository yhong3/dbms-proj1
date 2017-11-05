package utils;
import java.awt.List;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import java.sql.Types;
import sql.*;

public class PastHomework {
	private static int homework = -1;
	private static int mode = -1;
	private static String scoring_policy = "";
	private static int topic = -1;
	private static int student_score = -1;
	private static int retries_allowed = -1;
	private static int incorrect_answer_penalty = -1;
	private static int total_pts_hw = -1;
	private static int correct_answer_points = -1;
	private static int num_of_student_attempts = -1;
	private static int num_of_questions = -1; 
	
	
	public static void pastHW(Connection conn, String uid, String cid, String eid) {
		PreparedStatement stmt;
		try {
			stmt = conn.prepareStatement(SqlQueries.SQL_GETEXERCISE);
			stmt.setString(1, cid);
			stmt.setString(2, eid);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				String exercise_name = rs.getString("exercise_name");
				String exercise_start = rs.getString("exercise_start"); 
				String exercise_end = rs.getString("exercise_end"); 
				retries_allowed = rs.getInt("retries_allowed");
				num_of_questions = rs.getInt("num_of_questions");
				String sp_tempt = rs.getString("scoring_policy_id");
				correct_answer_points = rs.getInt("correct_answer_points");
				incorrect_answer_penalty = rs.getInt("incorrect_answer_penalty");
				mode  = rs.getInt("exercise_mode");
				stmt = conn.prepareStatement(SqlQueries.SQL_SPOLICY);
				stmt.setString(1, sp_tempt);
				ResultSet rs_sp = stmt.executeQuery();
				while(rs_sp.next()){
					scoring_policy = rs_sp.getString(1);
				}
				total_pts_hw = correct_answer_points * num_of_questions;
				student_score = calculate_score(conn, cid, uid, eid);
				
				System.out.println("1. Exercise Start: " + exercise_start);
				System.out.println("2. Exercise End: " + exercise_end);
				String e_mode = "";
				if(mode == 0) {
					e_mode = "Standard";
				}
				else {
					e_mode = "Adaptive";
				}
				System.out.println("3. Type of this HW: " + e_mode);
				System.out.println("4. Total Points of the HW: " + total_pts_hw);
				System.out.println("5. Scoring Policy: " + scoring_policy);
				System.out.println("6. Student's score according to the method: " + student_score);
				System.out.println("7. Number of Available Attempts : " + retries_allowed);
				System.out.println("8. Number of Student's Attempts : " + num_of_student_attempts);
				clearSetting();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			clearSetting();
			e.printStackTrace();
		}
	}	
	
	private static int[] HW_submission(Connection conn, String cid, String uid, String eid) {
		PreparedStatement stmt;
		num_of_student_attempts = NoOfAttempt(conn, cid, uid, eid);
		//System.out.println("Number of attempts: " + num_of_student_attempts);
		int[] hw_score_attempts = new int[num_of_student_attempts];
		if(num_of_student_attempts > 0) {
			for(int i  = 0; i < num_of_student_attempts; i++) {
				int correct_q = 0;
				int hw_attempt_score = 0;
				try {
					stmt = conn.prepareStatement(SqlQueries.SQL_GETATTEMPTEXERCISE);
					stmt.setString(1, cid);
					stmt.setString(2, eid);
					stmt.setString(3, uid);
					stmt.setInt(4, (i+1));
					ResultSet rs = stmt.executeQuery();
					while(rs.next()) {
						int question_id = rs.getInt("question_id");
						int concrete_answer_id = rs.getInt("concrete_answer_id");
						int parameter_id = -1;
						int parameter_answer_id = -1;
						if (rs.wasNull()) {
							//System.out.println("Parameter Question!");
							concrete_answer_id = -1; 
							parameter_id = rs.getInt("parameter_id");
							parameter_answer_id = rs.getInt("parameter_answer_id");
						}
						correct_q += score_per_question(conn, question_id, parameter_id, parameter_answer_id, concrete_answer_id);
					}
					//System.out.println("Incorrect penalty: " + incorrect_answer_penalty);
					//System.out.println("Num of questions: " + num_of_questions);
					//System.out.println("Correct answers: " + correct_q);
					hw_attempt_score = correct_q * correct_answer_points - incorrect_answer_penalty*(num_of_questions - correct_q);
					//System.out.println("Score per attempt: " + hw_attempt_score);
					//System.out.println();
					hw_score_attempts[i] = hw_attempt_score;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		/**
		else {
			System.out.println("The student has made no attempts of this HW!");
		}*/
		return hw_score_attempts;
	}
	
	private static int NoOfAttempt(Connection conn, String cid, String uid, String eid) {
		PreparedStatement stmt;
		int attempts = 0;
		try {
			stmt = conn.prepareStatement(SqlQueries.SQL_NOOFATTEMPT);
			stmt.setString(1, cid);
			stmt.setString(2, eid);
			stmt.setString(3, uid);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				attempts = rs.getInt("N_ATTEMPT");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return attempts;
	}
	
	
	private static int calculate_score(Connection conn, String cid, String uid, String eid) {
		char sp = scoring_policy.toLowerCase().charAt(0);
		int score = -1;
		int[] student_score_attempts = HW_submission(conn, cid, uid, eid);
		if (student_score_attempts != null && student_score_attempts.length > 0) {
			switch(sp) {
			case 'l':
				score = student_score_attempts[student_score_attempts.length-1];
				break;
			case 'm':
				score = student_score_attempts[0];
				for (int i = 1; i < student_score_attempts.length; i++) {
				    if (student_score_attempts[i] > score) {
				    	score = student_score_attempts[i];
				    }
				}
				break;
			case 'a':
				score = 0;
				for (int i = 0; i < student_score_attempts.length; i++) {
				    if (student_score_attempts[i] > score) {
				    	score += student_score_attempts[i];
				    }
				}
				//System.out.println("Score: " + score);
				score = score / student_score_attempts.length;
				break;
			default:
				score = 0;
				break;
			}
		}
		else {
			score = 0;
		}
		return score;
	}
	
	public static int score_per_question(Connection conn, int q_id, int p_id, int pa_id, int ca_id) {
		PreparedStatement stmt;
		int a_type = -1;
		int score = 0;
		try {
			if(ca_id == -1) {
				//System.out.println("Parameter answer from question_id: " + q_id);
				stmt = conn.prepareStatement(SqlQueries.SQL_GETPANSWERTYPE);
				stmt.setInt(1, q_id);
				stmt.setInt(2, p_id);
				stmt.setInt(3, pa_id);
			}
			else {
				//System.out.println("Concrete answer from question_id: " + q_id);
				stmt = conn.prepareStatement(SqlQueries.SQL_GETCANSWERTYPE);
				stmt.setInt(1, q_id);
				stmt.setInt(2, ca_id);
			}
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				//System.out.println("I am before getting the type of the answer!");
				a_type = rs.getInt("type");
				//System.out.println("Type of answer: " + a_type);
				if(a_type == 1) {
					//System.out.println("The answer is correct!");
					score = 1;
				}
				else {
					score = 0;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return score;
	}
	
	private static void clearSetting() {
		 homework = -1;
		 mode = -1;
		 scoring_policy = "";
		 topic = -1;
		 student_score = -1;
		 retries_allowed = -1;
		 incorrect_answer_penalty = -1;
		 total_pts_hw = -1;
		 correct_answer_points = -1;
		 num_of_student_attempts = -1;
		 num_of_questions = -1; 
	}
	
}
