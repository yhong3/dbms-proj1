package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import sql.SqlQueries;
import utils.Menu;
import utils.Report;

public class TA extends Professor{
	
	private static PreparedStatement preparedStatement;
	
	public static Boolean taInterface(Connection connection, String uid) throws Throwable {
    	
    	String selection = "1";
    	String student_selection = "1"; // selection for Enroll/Drop a student
    	
    	Boolean taReturn = true;
    	Boolean returnToRoot = true;
    	
    	Scanner scanner = new Scanner(System.in);
    	
    	while (selection != "5" && taReturn && returnToRoot) {
    		
    		Menu.taMainMenu();	
            selection = scanner.nextLine();
            
            switch (selection) { 
            case "0": 
            	taReturn = false;
            	break;
            	
            case "1":
				Student.viewStudent(connection, uid);
                break;
                
            case "2":
            	returnToRoot = taViewCourseMenu(connection, uid);
            	break;
            		
            case "3":
            	returnToRoot = instructorEnrollStudent(connection, uid);
            	break;
	            	
            case "4":
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
	
	
	// View Course:  TA choose to View the Course
    // All courses TA in charge of are listed
    // If a course in charge of instructor is chosen, display the menu with edit options
    // Else of a a course not in charge of instructor, show a warning
    static Boolean taViewCourseMenu(Connection connection, String uid) throws SQLException, Throwable {
    	
    	Scanner scanner = new Scanner(System.in);
    	
    	PreparedStatement preparedStatement_course;
    	Boolean returnToRoot = true;
    	
    	preparedStatement_course = connection.prepareStatement(SqlQueries.SQL_INSTAVIEWCOURSE);
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
    	
    	if (courseList.contains(detailcid)) {
    		returnToRoot = Course.taViewCourse(connection, uid, detailcid);
    	} 
    	else if (Course.checkCourseExist(connection, detailcid)) { Menu.viewCourseFailPermitMessage();  Menu.returnToMenuCommand();}
    	
    	else { Menu.viewCourseFailureMessage(); Menu.returnToMenuCommand();}
    	
    	return returnToRoot;
    }
    
    // Check if a student has been assigned as a TA for a certain course
    static boolean checkTACourse (Connection connection, String sid, String cid) throws Throwable {
    	
    	Boolean stcourse = false; 
    	PreparedStatement preparedStatement_stcourse = connection.prepareStatement(SqlQueries.SQL_CHECKTAENROLLCOURSE);
    	preparedStatement_stcourse.setString(1, cid);
    	preparedStatement_stcourse.setString(2, sid);
    	ResultSet rs_senroll = preparedStatement_stcourse.executeQuery();
    	if (rs_senroll.next()) {
    		stcourse = true;
    	}
    	
		return stcourse;
    }

}
