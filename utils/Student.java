package utils;

import sql.SqlQueries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import utils.InterfaceInterface; 

public class Student {
	
	private static PreparedStatement preparedStatement;

    static void dropStudentCourse(Connection connection, String cid, String uid) throws SQLException
    {
    	Scanner scanner = new Scanner(System.in);
    	
    	System.out.println(
		"***********************************************************\n" +
		"Drop a student from course, please enter the student info: \n" +
		"***********************************************************\n");
    	
    	//scanner.nextLine(); 
    	
    	System.out.println("Please enter the student's user_id: \n");
    	String sid = scanner.nextLine();
    	
    	preparedStatement = connection.prepareStatement(SqlQueries.SQL_DROPSTUDENTCOURSE);
    	preparedStatement.setString(1, cid);
    	preparedStatement.setString(2, sid);
    	preparedStatement.execute();
    	System.out.println("Student: " + sid + " have been dropped from the course.\n");
    	//scanner.close();
    }
    
    
    static void enrollStudentCourse(Connection connection, String cid, String uid) throws SQLException
    {
    	
    	Scanner scanner = new Scanner(System.in);
    	
    	System.out.println(
		"***********************************************************\n" +
		"Enroll a student to course, please enter the student info: \n" +
		"***********************************************************\n");
    	
    	//scanner.nextLine(); 
    	
    	System.out.println("Please enter the student's user_id: \n");
    	String sid = scanner.nextLine();
        
    	preparedStatement = connection.prepareStatement(SqlQueries.SQL_ENROLLSTUDENTCOURSE);
    	preparedStatement.setString(1, cid);
    	preparedStatement.setString(2, sid);
    	preparedStatement.execute();
    	System.out.println("** Student " + sid + " have been enrolled in the course.\n");
    	// scanner.close();
    }
    
    
    static void dropStudent(Connection connection, String uid) throws SQLException
    {
    	Scanner scanner = new Scanner(System.in);
    	
    	System.out.println(
		"***********************************************\n" +
		"Drop a student, please enter the student info: \n" +
		"***********************************************\n");
    	
    	//scanner.nextLine(); 
    	
    	System.out.println("Please enter the student's user_id: \n");
    	String sid = scanner.nextLine();
    	
    	preparedStatement = connection.prepareStatement(SqlQueries.SQL_DROPSTUDENT);
    	preparedStatement.setString(1, sid);
    	preparedStatement.execute();
    	System.out.println("\n** Student " + sid + " have been Dropped.**\n");
    	//scanner.close();
    }
    
    
    static void enrollStudent(Connection connection, String uid) throws SQLException
    {
    	
    	Scanner scanner = new Scanner(System.in);
    	
    	System.out.println(
		"*****************************************************\n" +
		"Enroll a new student, please enter the student info: \n" +
		"*****************************************************\n");
    	
    	//scanner.nextLine(); 
    	
    	System.out.println("Please Enter the Student's user_id: \n");
    	String sid = scanner.nextLine();
    	
    	System.out.println("Please Enter the Student's name: \n");
    	String cname = scanner.nextLine();
    	
    	System.out.println("Please Enter the year Student being Enrolled: \n");
    	int year = scanner.nextInt();
    	
    	scanner.nextLine(); 
    	
    	System.out.println("Please Enter the Student's type(Undergrad/Grad): \n");
    	String type = scanner.nextLine();
        
    	preparedStatement = connection.prepareStatement(SqlQueries.SQL_ENROLLSTUDENT);
    	preparedStatement.setString(1, sid);
    	preparedStatement.setString(2, cname);
    	preparedStatement.setInt(3, year);
    	preparedStatement.setString(4, type);
    	preparedStatement.execute();
    	
    	System.out.println("\n** Student " + sid + " have been Enrolled.**\n");
    	//scanner.close();
    }
    
    static String checkStudentLevel(Connection connection, String sid) throws SQLException
    {
    	
    	preparedStatement = connection.prepareStatement(SqlQueries.SQL_CHECKLEVEL);
    	preparedStatement.setString(1, sid);
    	
    	ResultSet rs_profile = preparedStatement.executeQuery();
    	//System.out.println("Successfully queried. \n");
    	
    	if (rs_profile.next()) {
            System.out.println("The Student has the Level of: " + rs_profile.getString("TYPE")); 
        }
    	String slevel = rs_profile.getString("TYPE");
    	return slevel;
    }

    static void viewStudent(Connection connection, String uid) throws SQLException
    {
    	System.out.println(
		"***********************************\n" +
		"Personal Profile: \n" +
		"***********************************\n");
    	
    	preparedStatement = connection.prepareStatement(SqlQueries.SQL_STUDENTPROFILE);
    	preparedStatement.setString(1, uid);
    	
    	ResultSet rs_profile = preparedStatement.executeQuery();
    	//System.out.println("Successfully queried. \n");
    	
    	while (rs_profile.next()) {
            System.out.println(
            "USER_ID: " + rs_profile.getString("USER_ID") + "\n" +
            "STUDENT_NAME: " + rs_profile.getString("STUDENT_NAME") + "\n" +
    		"YEAR_ENROLLED: " + rs_profile.getString("YEAR_ENROLLED") + "\n" +
    		"TYPE: " + rs_profile.getString("TYPE") + "\n" +
			"***********************************\n");
        }
    }
    
    static void editStudentName(Connection connection, String uid) throws SQLException 
    {
    	Scanner scan = new Scanner(System.in);
    	
    	System.out.println("Please Enter the New Name: \n");
    	//scanner.nextLine(); 
    	String uname = scan.nextLine();
  		
    	preparedStatement = connection.prepareStatement(SqlQueries.SQL_UPDATESTUNAME);
    	preparedStatement.setString(1, uname);
    	preparedStatement.setString(2, uid);
    	preparedStatement.executeUpdate();
    	
    	//System.out.println("Successfully queried. \n");           	
    	System.out.println("The Student's name has been Updated as: " + uname); 
    	
    	//scan.close();
    }
    
    static void editStudentYear(Connection connection, String uid) throws SQLException
    {
    	Scanner scan = new Scanner(System.in);
    	
    	System.out.println("Please Enter the New Enrolled Year: \n");
    	//scanner.nextLine(); 
    	int uyear = scan.nextInt();
  		
    	preparedStatement = connection.prepareStatement(SqlQueries.SQL_UPDATESTUENROLL);
    	preparedStatement.setInt(1, uyear);
    	preparedStatement.setString(2, uid);
    	preparedStatement.executeUpdate();
    	
    	//System.out.println("Successfully queried. \n");           	
    	System.out.println("The Student's Enrolled Year has been Updated as: " + uyear); 
    	
    	//scan.close();
    }
    
    static void editStudent(Connection connection, String uid) throws SQLException
    {
    	int edit_selection = 1; 
    	Scanner scanner = new Scanner(System.in);
    	
    	while (edit_selection != 3) {
    		
	    	System.out.println(
				"******************************************\n" +
				"Enter the Profile You Would Like to Edit: \n" +
				"Please Make a Selection (1-3):\n" +
				"******************************************\n" +
				"0. Return to Previous Menu \n" +
		        "1. Student Name\n" +
		        "2. Student Year_enrolled\n" +
		        "3. Logout\n" +
				"******************************************\n");
	    	
    		edit_selection = scanner.nextInt();
    	
    		switch (edit_selection) {
        	case 0: 
        		return;
        		
            case 1: //Edit the Student's Name
            	editStudentName(connection, uid);
            	break;
            
            case 2: //Edit the Student's Year_enrolled
            	
            	editStudentYear(connection, uid);
            	break;
            
            case 3:
            	InterfaceInterface.exitInterface();
            	
            default:
            	InterfaceInterface.defaultInterface();
            	break;
            
            }//end switch
    	}//end while
    	//scanner.close();
    }
}
