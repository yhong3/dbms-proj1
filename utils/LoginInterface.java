package utils;

import sql.SqlQueries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Scanner;

import utils.Course; 
import utils.Student;
import utils.Menu;

public class LoginInterface {
	
	private static PreparedStatement preparedStatement;
	
    public static int loginRole(Connection connection, String uid, String password) {
    try {
        	preparedStatement = connection.prepareStatement(SqlQueries.SQL_LOGINROLE);
        	preparedStatement.setString(1, uid);
        	preparedStatement.setString(2, password);
    		
        	ResultSet rs_role = preparedStatement.executeQuery();
        	if(rs_role.next()) { return rs_role.getInt("ROLE"); }
            
        	return 0;
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	        	if (preparedStatement != null)
	                try {
	                	preparedStatement.close();
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
        }
        return 0;
    }
    
    public static Boolean welcomePage(Connection connection, String uid, int logRole) throws Throwable {
    	
    	Scanner scanner = new Scanner(System.in);
    	Boolean returnToRoot = true;
    	
    	while(returnToRoot) {
    	
	    	switch(logRole) {
	    	case 1:
	    		Menu.welcomeMessage("Professor");
	    		returnToRoot = Professor.instructorInterface(connection, uid);
	    		return returnToRoot;
	        
	    	case 2:
	    		String role_selection = "1";
	        	Boolean roleReturn = true; 
	        	
	        	while(role_selection != "3" && roleReturn) {
	        		Menu.taLoginMenu();
	        		role_selection = scanner.nextLine();
	            	// Choose interface for TA to login as Student or TA
	            	switch(role_selection) {
	    	        case "1":
	    	        	Menu.welcomeMessage("Student");
	    	        	returnToRoot = Student.studentInterface(connection, uid);
	            		return returnToRoot;
	            	
	    	        case "2":
	    	        	Menu.welcomeMessage("TA");
	    	        	returnToRoot = TA.taInterface(connection, uid);
	            		return returnToRoot;
	            		
	    	        case "3":
	    	        	roleReturn = false;
	    	        	Menu.returnLoginMessage();
	    	        	returnToRoot = false; 
	    	        	return returnToRoot;
	    	        	
	    	        default:
	    	        	Menu.warningMessage();
	    	        	break;
	            	}//end switch
	        	}//end while 
	        	break;
	        	
	    	case 3: 
	    		Menu.welcomeMessage("Student");
	    		returnToRoot = Student.studentInterface(connection, uid);
	        	break;
	    	}//end while
    	}
    	
    	return returnToRoot;
    }
}