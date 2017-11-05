package utils;

import sql.SqlQueries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import utils.LoginInterface; 

public class Student {
	
	private static PreparedStatement preparedStatement;
	
	public static Boolean studentInterface(Connection connection, String uid) throws Throwable {
    	
    	String selection = "1";
    	Scanner scanner = new Scanner(System.in);
    	
    	Boolean studentReturn = true;
    	Boolean returnToRoot = true;
    	
    	while (selection != "3" && studentReturn && returnToRoot) {
    		
    		Menu.studentMainMenu();
	        selection = scanner.nextLine();
            
            switch (selection) {
            
            case "0": 
            	studentReturn = false;
            	break;
            	
            // View/Edit Profile
            case "1":
            	returnToRoot = studentViewEditProfile(connection, uid);
            	break;
            
        	// View Courses
            case "2":
            	returnToRoot = studentViewCourse(connection, uid); 	
	            break;

            case "3":
            	Menu.returnLoginMessage();
            	returnToRoot = false;
            	return returnToRoot;
            	
            default:
            	Menu.warningMessage();
            	break;

            }//end switch
    	}//end while
    	return returnToRoot;
    }
	
	
	// Main Menu --> View/Edit Profile
	static Boolean studentViewEditProfile(Connection connection, String uid) throws SQLException{
		
		Scanner scanner = new Scanner(System.in);
		String profile_selection = "1"; 
		
		Boolean editReturn = true; 
		Boolean returnToRoot = true;
		
    	while (profile_selection != "3" && editReturn && returnToRoot) {
        	
    		Menu.studentViewEditProfileMenu();
			profile_selection = scanner.nextLine();
            
            switch (profile_selection) {
            case "0": 
            	editReturn = false;
            	break;
        	
            case "1":
            	viewStudent(connection, uid);
            	break;
            	
            case "2":
            	editStudent(connection, uid);
            	break;
            	
            case "3":
            	Menu.returnLoginMessage();
            	returnToRoot = false;
            	return returnToRoot;
         
        	default:
            	Menu.warningMessage();
            	break;
            }// end switch
    	}// end while 
    	return returnToRoot;
    }
    
	// Main Menu --> View Course
	static Boolean studentViewCourse(Connection connection, String uid) throws ParseException, Throwable{
		
		Scanner scanner = new Scanner(System.in);
    	
    	PreparedStatement preparedStatement_course;
    	Boolean returnToRoot = true;
    	
    	preparedStatement_course = connection.prepareStatement(SqlQueries.SQL_STUVIEWALLCOURSE);
    	preparedStatement_course.setString(1, uid);
    	ResultSet rs_course = preparedStatement_course.executeQuery();

    	List<String> courseList = new ArrayList<String>();
    	
    	while (rs_course.next()) {
    		String cid = rs_course.getString("COURSE_ID");
    		String cname = rs_course.getString("COURSE_NAME");
    		Menu.staffCourseListMessage(cid, cname);
    		courseList.add(cid);
        }
    	
    	Menu.enterCidMessage();
    	String detailcid = scanner.nextLine();
    	
    	// Submenu --> studentViewCourse
    	if (courseList.contains(detailcid)) {  returnToRoot = Course.studentViewCourse(connection, uid, detailcid); } 
    	
    	else if (Course.checkCourseExist(connection, detailcid)) { Menu.viewCourseFailPermitMessage();  Menu.returnToMenuCommand(); }
    	
    	else { Menu.viewCourseFailureMessage();  Menu.returnToMenuCommand(); }
    	
    	return returnToRoot;		
	}	
	
	
    // Check Student's type as a graduate/undergraduate
    static String checkStudentType(Connection connection, String sid) throws SQLException
    {
    	
    	preparedStatement = connection.prepareStatement(SqlQueries.SQL_CHECKLEVEL);
    	preparedStatement.setString(1, sid);
    	
    	ResultSet rs_profile = preparedStatement.executeQuery();
    	
    	if (rs_profile.next()) {
            System.out.println("The Student has the Level of: " + rs_profile.getString("TYPE")); 
        }
    	String slevel = rs_profile.getString("TYPE");
    	return slevel;
    }

    // View/Edit Profile --> View Profile
    static void viewStudent(Connection connection, String uid) throws SQLException
    {
    	preparedStatement = connection.prepareStatement(SqlQueries.SQL_STUDENTPROFILE);
    	preparedStatement.setString(1, uid);
    	
    	ResultSet rs_profile = preparedStatement.executeQuery();
    	
    	while (rs_profile.next()) {
    		
    		String sid = rs_profile.getString("USER_ID");
    		String sname = rs_profile.getString("STUDENT_NAME");
    		int syear = rs_profile.getInt("YEAR_ENROLLED");
    		String stype = rs_profile.getString("TYPE");
    		
    		Menu.studentProfileMessage(sid, sname, syear, stype);
        }
    	
    	Menu.returnToMenuCommand();
    }
    
 // View/Edit Profile --> Edit Profile --> Edit Name 
    static void editStudentName(Connection connection, String uid) throws SQLException 
    {
    	Scanner scan = new Scanner(System.in);
    	
    	System.out.println("Please Enter the New Name: \n");
    	String uname = scan.nextLine();
  		
    	preparedStatement = connection.prepareStatement(SqlQueries.SQL_UPDATESTUNAME);
    	preparedStatement.setString(1, uname);
    	preparedStatement.setString(2, uid);
    	preparedStatement.executeUpdate();
    	
    	System.out.println("The Student's name has been Updated as: " + uname); 
    	
    }
    
    // View/Edit Profile --> Edit Profile --> Edit Enrolled Year
    static void editStudentYear(Connection connection, String uid) throws SQLException
    {
    	Scanner scan = new Scanner(System.in);
    	
    	System.out.println("Please Enter the New Enrolled Year: \n");
    	int uyear = scan.nextInt();
  		
    	preparedStatement = connection.prepareStatement(SqlQueries.SQL_UPDATESTUENROLL);
    	preparedStatement.setInt(1, uyear);
    	preparedStatement.setString(2, uid);
    	preparedStatement.executeUpdate();
    	
    	System.out.println("The Student's Enrolled Year has been Updated as: " + uyear); 
    	
    }
    
    // View/Edit Profile --> Edit Profile:  Edit student's profile
    static Boolean editStudent(Connection connection, String uid) throws SQLException
    {
    	String edit_selection = "1"; 
    	Scanner scanner = new Scanner(System.in);
    	
    	Boolean editReturn = true;
    	Boolean returnToRoot = true;
    	
    	
    	while (edit_selection != "3" && editReturn && returnToRoot) {
    		
    		Menu.studentEditProfileMenu();    	
    		edit_selection = scanner.nextLine();
    	
    		switch (edit_selection) {
        	case "0": 
        		editReturn = false;
        		break;
        		
            case "1": //Edit the Student's Name
            	editStudentName(connection, uid);
            	break;
            
            case "2": //Edit the Student's Year_enrolled
            	editStudentYear(connection, uid);
            	break;
            
            case "3":
            	Menu.returnLoginMessage();
            	returnToRoot = false;
            	return returnToRoot;
            	
            default:
            	Menu.warningMessage();
            	break;
            
            }//end switch
    	}//end while
    	return returnToRoot;
    }
    
    // Check student's enrollment in the system
    static boolean checkStudentSysEnrollment(Connection connection, String sid) throws SQLException
    {
    	Boolean senroll = false; 
    	PreparedStatement preparedStatement_senroll = connection.prepareStatement(SqlQueries.SQL_STUDENTPROFILE);
    	preparedStatement_senroll.setString(1, sid);
    	ResultSet rs_senroll = preparedStatement_senroll.executeQuery();
    	if (rs_senroll.next()) {
    		senroll = true;
    	}
    	
		return senroll;
    }
    
    // Check student's enrollment of a course
    static boolean checkStudentEnrollCourse(Connection connection, String sid) throws SQLException
    {
    	Boolean senroll = false; 
    	PreparedStatement preparedStatement_senroll = connection.prepareStatement(SqlQueries.SQL_CHECKSTUENROLLCOURSE);
    	preparedStatement_senroll.setString(1, sid);
    	ResultSet rs_senroll = preparedStatement_senroll.executeQuery();
    	if (rs_senroll.next()) {
    		senroll = true;
    	}
    	
		return senroll;
    }
    
}
