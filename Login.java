import utils.InterfaceInterface; 
import sql.ConnectionManager; 

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.Scanner;

//import com.sun.java_cup.internal.runtime.Scanner;

public class Login extends ConnectionManager{

	public static void main(String[] argv) throws ParseException {
		
		try {
			
			instructorLogin();

		} catch (SQLException e) {

			System.out.println(e.getMessage());

		}
	}
	
	private static void instructorLogin() throws SQLException, ParseException {
		
		ConnectionManager dbcon = new ConnectionManager();
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		
		Boolean loginIdx = true; 
		Scanner scanner = new Scanner(System.in);  
		
		while (loginIdx) {
		
			try {
	            Class.forName("oracle.jdbc.driver.OracleDriver");
	            // Get a connection from the first driver in the
	            // DriverManager list that recognizes the URL jdbcURL
	
	            dbConnection = dbcon.getDBConnection();
	
	        } catch (ClassNotFoundException e) {
	            e.printStackTrace();
	        }
		
			// Scanner scanner = new Scanner(System.in);  
			System.out.println(
					"************************* \n" +
					"Pleaes enter your userID: \n" +
					"************************* \n");
			String uid = scanner.next();
			System.out.println(
					"*************************** \n" +
					"Please enter your password: \n" +
					"*************************** \n");
			String password = scanner.next();
			
			String logInfo = InterfaceInterface.doLogin(dbConnection, uid, password);
			
			if (logInfo != null) {
				System.out.println("\n Successfully connected. \n");
			
			InterfaceInterface.welcomePage(dbConnection, uid);
			
		
			Statement statement = null;
			
			try {
					dbConnection = dbcon.getDBConnection();
					statement = dbConnection.createStatement();
			} catch(SQLException e) {
				
				System.out.println(e.getMessage());
				
			} finally {
				
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				
				if (dbConnection != null) {
					dbConnection.close();
				}}
			}
			else {System.out.println(
					"******************************************** \n" +
					"UserID/Passwork Incorect, failed to connect. \n" +
					"******************************************** \n");
			}
			
		}
	}

}