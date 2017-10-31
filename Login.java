import utils.LoginInterface;
import utils.Menu;
import sql.ConnectionManager; 

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Scanner;

//import com.sun.java_cup.internal.runtime.Scanner;

public class Login extends ConnectionManager{
	
	public static void main(String[] argv) throws Throwable {
		
		String selection = "1";
		Scanner scanner = new Scanner(System.in);
		
		Boolean returnToRoot = true;
		
		while (selection != "2") {
			Menu.loginMenu();
		    selection = scanner.nextLine();
		     
	        switch (selection) {
	        // Login
            case "1": 
				try {
					returnToRoot = executeLogin();
					} catch (SQLException e) {
						System.out.println(e.getMessage());
					}
				break;
			// Exit
            case "2":
            	Menu.exitMessage();
            	break;
            	
            default:
            	Menu.warningMessage();
            	break;
            	
	        }//end switch
        }// end while
	}
	
	private static Boolean executeLogin() throws Throwable {
		
		ConnectionManager dbcon = new ConnectionManager();
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		
		Boolean returnToRoot = true;
		
		//Boolean loginIdx = true; 
		Scanner scanner = new Scanner(System.in);  
		
		//while (loginIdx) {
		
			try {
	            Class.forName("oracle.jdbc.driver.OracleDriver");
	            // Get a connection from the first driver in the
	            // DriverManager list that recognizes the URL jdbcURL
	
	            dbConnection = dbcon.getDBConnection();
	
	        } catch (ClassNotFoundException e) {
	            e.printStackTrace();
	        }
			
			Menu.enterUserIDMessage();
			String uid = scanner.next();
			
			Menu.enterPasswordMessage();
			String password = scanner.next();
			
			int logRole = LoginInterface.loginRole(dbConnection, uid, password);
			System.out.println(logRole);
			
			if (logRole != 0) {
				
				Menu.connSuccessMessage();
				returnToRoot = LoginInterface.welcomePage(dbConnection, uid, logRole);
				
				if (returnToRoot == false) { return returnToRoot; }
				
				try {
						dbConnection = dbcon.getDBConnection();
						
				} finally {
					
					if (preparedStatement != null) {
						preparedStatement.close();
					}
					
					if (dbConnection != null) {
						dbConnection.close();
					}}
			}
			else { Menu.connFailMessage(); }
			
			return returnToRoot; 
	}

}