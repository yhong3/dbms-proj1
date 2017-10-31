import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import sql.*;

public class CreateTQA extends ConnectionManager{

	private static final String DB_DRIVER = "oracle.jdbc.driver.OracleDriver";
	private static final String DB_CONNECTION = "jdbc:oracle:thin:@localhost:1521:orcl";
	private static final String DB_USER = "admin";
	private static final String DB_PASSWORD = "password";

	public static void main(String[] argv) {
		try {
			createTable();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	private static void createTable() throws SQLException {
		ConnectionManager dbcon = new ConnectionManager();
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;

		List<String> createTableSQL = new ArrayList<String>();
		
		String createTopic = "CREATE TABLE TOPIC(" + 
				"  topic_id integer," + 
				"  topic_name VARCHAR(50) NOT NULL," + 
				"  PRIMARY KEY (topic_id)" + 
				")";
		String createQuestion = "CREATE TABLE QUESTION(" + 
				"  question_id integer," + 
				"  topic_id integer," + 
				"  question_text VARCHAR(150) NOT NULL," + 
				"  type int NOT NULL," + 
				"  difficulty_level number(1) NOT NULL," + 
				"  hint VARCHAR(50) NOT NULL," + 
				"  detailed_explanation VARCHAR(150) NOT NULL," +
				"  FOREIGN KEY (topic_id) REFERENCES TOPIC," + 
				"  PRIMARY KEY (question_id)" + 
				")";
		String createConcrete = "CREATE TABLE CONCRETE_ANSWER(" + 
				"  answer_id integer," + 
				"  question_id integer," + 
				"  type int NOT NULL," + 
				"  answer_text VARCHAR(50) NOT NULL," +
				"  short_explanation VARCHAR(50) NOT NULL," +
				"  FOREIGN KEY (question_id) REFERENCES QUESTION," + 
				"  PRIMARY KEY (answer_id)" + 
				")";
		String createParameters = "CREATE TABLE PARAMETER_ANSWER(" + 
				"  parameter_id integer," +
				"  answer_id integer," +
				"  question_id integer," + 
				"  param_1 DOUBLE PRECISION," +
				"  param_2 DOUBLE PRECISION," +
				"  param_3 DOUBLE PRECISION," +
				"  param_4 DOUBLE PRECISION," +
				"  param_5 DOUBLE PRECISION," + 
				"  answer_text VARCHAR(50) NOT NULL," +
				"  short_explanation VARCHAR(50) NOT NULL," +
				"  type int NOT NULL," + 
				"  FOREIGN KEY (question_id) REFERENCES QUESTION," + 
				"  PRIMARY KEY (parameter_id, answer_id)" + 
				")";
		
		createTableSQL.add(createTopic);
		createTableSQL.add(createQuestion);
		createTableSQL.add(createConcrete);
		createTableSQL.add(createParameters);
		
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