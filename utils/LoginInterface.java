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

public class LoginInterface {
	private static PreparedStatement preparedStatement_PRO;
    private static PreparedStatement preparedStatement_TA;
    private static PreparedStatement preparedStatement_STU;

    public static String doLogin(Connection connection, String uid, String password) {
        try {
        	preparedStatement_PRO = connection.prepareStatement(SqlQueries.SQL_PROLOGIN);
        	preparedStatement_PRO.setString(1, uid);
        	preparedStatement_PRO.setString(2, password);
        	
        	preparedStatement_TA = connection.prepareStatement(SqlQueries.SQL_TALOGIN);
        	preparedStatement_TA.setString(1, uid);
        	preparedStatement_TA.setString(2, password);
        	
        	preparedStatement_STU = connection.prepareStatement(SqlQueries.SQL_STULOGIN);
        	preparedStatement_STU.setString(1, uid);
        	preparedStatement_STU.setString(2, password);
        	
        	ResultSet rs_pro = preparedStatement_PRO.executeQuery();
            if(rs_pro.next()) {
                return rs_pro.getString("ROLE");
            }
            
            ResultSet rs_ta = preparedStatement_TA.executeQuery();
            if(rs_ta.next()) {
                return rs_ta.getString("ROLE");
            }
            
            ResultSet rs_stu = preparedStatement_STU.executeQuery();
            if(rs_stu.next()) {
                return rs_stu.getString("ROLE");
            }
            
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	if (preparedStatement_PRO != null)
                try {
                	preparedStatement_PRO.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        	
        	else if (preparedStatement_TA != null)
                try {
                	preparedStatement_TA.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        	
        	else if (preparedStatement_STU != null)
                try {
                	preparedStatement_STU.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return null;
    }
    
    public static void welcomePage(Connection connection, String uid) throws SQLException, ParseException {
    	
    	PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.SQL_CHECKROLE);
    	preparedStatement.setString(1, uid);
    	ResultSet rs_profile = preparedStatement.executeQuery();
    	
    	String logRole = null; 
    	
    	if (rs_profile.next()) {
    		System.out.print(
	    		"***********************************\n" +
	    		"Welcome To the Course System!\n" +
	            "***********************************\n");
            System.out.println("Login as with the role of: " + rs_profile.getString("ROLE") +
            		"  (1: Professor; 2: TA; 3: Student.) ");
            logRole = rs_profile.getString("ROLE"); 
            if (logRole.equals("1")) { 
            	System.out.println("ROLE: Professor."); 
            	instructorInterface(connection, uid, logRole);
            	}
            else if (logRole.equals("2")) { 
            	System.out.println("ROLE: TA."); 
            	instructorInterface(connection, uid, logRole);
            	}
            else if (logRole.equals("3")) { 
            	System.out.println("ROLE: Student."); 
            	studentInterface(connection, uid, logRole);
            	}
    	}
    	
    }
    
    public static void exitInterface() {
    	System.out.println(
			"*********** \n" +
			"Exit System \n" +
			"*********** \n");
    	System.exit(0);
    }
    
    public static void defaultInterface() {
    	System.out.println("WARNING: Cannot Recognize Your Entered Option, Try Again. \n ");
    }
    
    public static void studentInterface(Connection connection, String uid, String logRole) throws SQLException, ParseException {
    	
    	int selection = 1;
    	int profile_selection = 1; 
    	
    	Scanner scanner = new Scanner(System.in);
    	
    	while (selection != 3) {
    		
	        System.out.print(
	    		"***********************************\n" +
	    		"Student Main Menu\n" +
	            "Please Make a Selection (1-3):\n" +
	            "***********************************\n" +
	            "0. Return to Previous Menu \n" +
	            "1. View/Edit Profile\n" +
	            "2. View Courses\n" +
	            "3. Logout\n" +
	    		"***********************************\n");
	        
	        selection = scanner.nextInt();
            
            switch (selection) {
            case 0:
            	return;
            case 1:
            	Boolean editReturn = true; 
            	while (profile_selection != 3 && editReturn) {
	            	
        			System.out.println(
        			"***********************************\n" +
        			"View/Edit Profile: \n" +
	            	"Please Make a Selection (1-3):\n" +
	                "***********************************\n" +
	                "0. Return to Previous Menu \n" +
	                "1. View Profile\n" +
	                "2. Edit Profile\n" +
	                "3. Logout\n" +
	        		"***********************************\n");
        			
        			profile_selection = scanner.nextInt();
    	            
    	            switch (profile_selection) {
    	            case 0: 
    	            	editReturn = false;
    	            	break;
    	        	
    	            case 1:
    	            	Student.viewStudent(connection, uid);
    	            	break;
    	            	
    	            case 2:
    	            	Student.editStudent(connection, uid);
    	            	break;
    	            	
    	            case 3:
    	            	exitInterface();
    	            }
        			
            	}
            	break;
            	
            case 2:
            	Course.studentViewCourse(connection, uid);
            	System.out.println(
	        			"************************************************************\n" +
	        			"Please Enter the Course_id to View the details of a Course: \n" +
	        			"************************************************************\n");
            	scanner.nextLine();
    	    	String cid = scanner.nextLine();
    	    	Course.studentViewDetailCourse(connection, uid, cid);
	            break;
        	
            case 3:
            	exitInterface();
            	
            default:
            	System.out.println("WARNING: Cannot Recognize Your Entered Option, Try Again. \n ");
            	break;

            }//end switch
    	}//end while 
    }
    
    public static void instructorInterface(Connection connection, String uid, String logRole) throws SQLException, ParseException {
    	
    	int selection = 1;
    	int course_selection = 1; // selection for View/Add Course
    	int student_selection = 1; // selection for Enroll/Drop a student
    	int question_selection = 1; // selection for Search/Add questions to Question Bank
    	
    	Scanner scanner = new Scanner(System.in);
    	
    	while (selection != 5) {
    		
	        System.out.print(
	    		"***********************************\n" +
	    		"Instructor Main Menu\n" +
	            "Please Make a Selection (1-5):\n" +
	            "***********************************\n" +
	            "0. Return to Previous Menu \n" +
	            "1. View Profile\n" +
	            "2. View/Add Course\n" +
	            "3. Enroll/Drop a student\n" +
	            "4. Search/Add questions to Question Bank\n" +
	            "5. Logout\n" +
	    		"***********************************\n");
	
	            selection = scanner.nextInt();
	            
	            switch (selection) {
	            case 0:
	            	return;
	            case 1:
	            	
	            	if (logRole.equals("1")) { Professor.viewProfessor(connection, uid); }
	            	else if (logRole.equals("2")) { Student.viewStudent(connection, uid); }
	                break;
	                
	            case 2:
	            	Boolean courseReturn = true; 
	            	
	            	while (course_selection != 3 && courseReturn) {
		            	System.out.println(
		        			"***********************************\n" +
		        			"View/Add Course: \n" +
		        			"Please Make a Selection (1-3):\n" +
			                "***********************************\n" +
			                "0. Return to Previous Menu \n" +
			                "1. View a Course\n" +
			                "2. Add a Course\n" +
			                "3. Logout\n" +
			        		"***********************************\n");
		            	
		            	course_selection = scanner.nextInt();
			            
			            switch (course_selection) {	  
			            case 0: 
			            	courseReturn = false;
		            		break;
		            	
			            case 1:
			            	System.out.println(
		            			"************************************************\n" +
		            			"Please Enter the Course_id: \n" +
		            			"************************************************\n");
			            	scanner.nextLine();
	            	    	String cid = scanner.nextLine();
			            	Course.instructorViewCourse(connection, uid, cid);
			            	break;
			            
			            case 2:
			            	Course.addCourse(connection, uid);
			            	break;
			            	
			            case 3:
	    	            	exitInterface();
			            }//end switch
	            	} //end while 
	            	break;
	            		
	            case 3:
	            	Boolean enrollReturn = true; 
	            	
	            	while (student_selection != 3 && enrollReturn) {
		            	System.out.println(
		        			"***********************************\n" +
		        			"Enroll/Drop a Student: \n" +
			            	"Please Make a Selection (1-3):\n" +
			                "***********************************\n" +
			                "0. Return to Previous Menu \n" +
			                "1. Enroll a Student\n" +
			                "2. Drop a Student\n" +
			                "3. Logout\n" +
			        		"***********************************\n");
		            	
		            	student_selection = scanner.nextInt();
		            	
		            	switch (student_selection) {
		            	case 0: 
		            		enrollReturn = false;
		            		break;
			            case 1:
			            	Student.enrollStudent(connection, uid);
			            	break;
			            	
			            case 2:
			            	Student.dropStudent(connection, uid);
			            	break;
			                
			            case 3:
	    	            	exitInterface();
			            }//end switch
	            	}//end while 
	            	break;
	                
	            case 4:
		                Boolean questionReturn = true; 
		            	
		            	while (question_selection != 3 && questionReturn) {
			            	System.out.println(
			        			"***********************************\n" +
			        			"Search/Add Questions to Question Bank\n" +
				            	"Please Make a Selection (1-3):\n" +
				                "***********************************\n" +
				                "0. Return to Previous Menu \n" +
				                "1. Search Questions from Question Bank\n" +
				                "2. Add Questions to Question Bank\n" +
				                "3. Logout\n" +
				        		"***********************************\n");
			            	
			            	question_selection = scanner.nextInt();
			            	
			            	switch (question_selection) {
			            	case 0:
			            		questionReturn = false;
			            		break;
			            	
				            case 1:
				            	
				            	break;
				            	
				            case 2:
				            	
				            	break;
				            	
				            case 3:
		    	            	exitInterface();
			            }//end switch
	            	}//end while 
		            break;
		            	
	            case 5:
	            	exitInterface();
	            	
	            default:
	            	defaultInterface();
	            	break;
	            }//end switch
    	}//end while
    }

}