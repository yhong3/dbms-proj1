package utils;

import sql.ConnectionManager; 

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CreateAllTables {
	
	private static void createTable() throws SQLException {

		ConnectionManager dbcon = new ConnectionManager();
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;

		List<String> createTableSQL = new ArrayList<String>();
		
		String createCourse = "CREATE TABLE COURSE(" + 
				"course_id VARCHAR(50)," + 
				"course_name VARCHAR(20) NOT NULL," + 
				"course_start DATE NOT NULL," + 
				"course_end DATE NOT NULL," + 
				"PRIMARY KEY (course_id) " + 
				")";
		String createProfessor = "CREATE TABLE PROFESSOR(" +
				"user_id VARCHAR(50)," + 
				"professor_name VARCHAR(20) NOT NULL," + 
				"PRIMARY KEY (user_id)" + 
				")";

		String createStudent = "CREATE TABLE STUDENT(" + 
				"user_id VARCHAR(50)," + 
				"student_name VARCHAR(50) NOT NULL," + 
				"year_enrolled INT NOT NULL," + 
				"type VARCHAR(20) NOT NULL," + 
				"PRIMARY KEY (user_id)" + 
				")";
		String createExercise = "CREATE TABLE EXERCISE(" + 
				"exercise_id INT," + 
				"exercise_name VARCHAR(100) NOT NULL," + 
				"exercise_start DATE NOT NULL," + 
				"exercise_end DATE NOT NULL," + 
				"retries_allowed INT NOT NULL," + 
				"num_of_questions INT NOT NULL," + 
				"scoring_policy VARCHAR(10) NOT NULL," + 
				"PRIMARY KEY (exercise_id)" + 
				")";
		String createTopic = "CREATE TABLE TOPIC(" + 
				"  topic_id integer," + 
				"  topic_name char NOT NULL," + 
				"  course_id VARCHAR(50), " + 
				"  FOREIGN KEY (course_id) REFERENCES COURSE," + 
				"  PRIMARY KEY (topic_id)" + 
				")";
		String createQuestion = "CREATE TABLE QUESTION(" + 
				"  question_id integer," + 
				"  topic_id integer," + 
				"  difficulty_level number(1) NOT NULL," + 
				"  hint char NOT NULL," + 
				"  explanation char NOT NULL," + 
				"  question_text char NOT NULL," + 
				"  questtion_variable char," + 
				"  FOREIGN KEY (topic_id) REFERENCES TOPIC," + 
				"  PRIMARY KEY (question_id)" + 
				")";
		String createAnswer = "CREATE TABLE ANSWER(" + 
				"  answer_id integer," + 
				"  question_id integer," + 
				"  type char NOT NULL," + 
				"  answer_text char NOT NULL," + 
				"  PRIMARY KEY (answer_id, question_id)" + 
				")";
		String createCourseStaff = "CREATE TABLE COURSE_STAFF(" + 
				"course_id VARCHAR(50)," + 
				"user_id VARCHAR(50)," + 
				"role INT" +
				")";
		String createCourseStudent = "CREATE TABLE COURSE_STUDENT(" + 
				"course_id VARCHAR(50)," + 
				"user_id VARCHAR(50)," + 
				"FOREIGN KEY (course_id) REFERENCES COURSE," + 
				"FOREIGN KEY (user_id) REFERENCES STUDENT" + 
				")";
		String createStaffExercise = "CREATE TABLE STAFF_EXERCISE(" + 
				"  exercise_id integer," + 
				"  user_id integer," + 
				"  exercise_mode char NOT NULL," + 
				"  PRIMARY KEY (exercise_id, user_id)" + 
				")";
		String createExerciseQuestion = "CREATE TABLE EXERCISE_QUESTION(" + 
				"exercise_id INT," + 
				"question_id INT, " + 
				"FOREIGN KEY (exercise_id) REFERENCES EXERCISE," + 
				"FOREIGN KEY (question_id) REFERENCES QUESTION" + 
				")";
		String createUseridPassword = "CREATE TABLE USERID_PASSWORD(" + 
				"user_id VARCHAR(50)," + 
				"role INT," + 
				"password VARCHAR(64)" + 
				")";
		
		createTableSQL.add(createCourse);
		createTableSQL.add(createProfessor);
		createTableSQL.add(createStudent);
		createTableSQL.add(createExercise);
		createTableSQL.add(createTopic);
		createTableSQL.add(createQuestion);
		createTableSQL.add(createAnswer);
		createTableSQL.add(createCourseStaff);
		createTableSQL.add(createCourseStudent);
		createTableSQL.add(createStaffExercise);
		createTableSQL.add(createExerciseQuestion);
		createTableSQL.add(createUseridPassword);
		
		Statement statement = null;
		
		try {
			
			dbConnection = dbcon.getDBConnection();
			statement = dbConnection.createStatement();
			
			for( String stmt : createTableSQL ) {
				statement.executeUpdate(stmt);
				System.out.println(stmt);
			}
			
		} catch (SQLException e) {

			System.out.println(e.getMessage());

		} finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}

		}

	}
	
}
