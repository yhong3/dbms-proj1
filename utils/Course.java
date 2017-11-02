package utils;

import sql.SqlQueries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import utils.Professor;
import utils.Student;
import utils.TA;

public class Course {
	
	private static PreparedStatement preparedStatement;

    static Boolean instructorViewCourse(Connection connection, String uid, String cid) throws Throwable {
    	
    	Scanner scanner = new Scanner(System.in);
    	String selection = "1";
    	Boolean courseView = true; 
    	Boolean returnToRoot = true;
    	
    	preparedStatement = connection.prepareStatement(SqlQueries.SQL_INSTAVIEWCOURSE);
    	preparedStatement.setString(1, uid);
    	ResultSet rs_profile = preparedStatement.executeQuery();
    	
    	String rt_cname = null;
		String rt_cstart = null;
		String rt_cend = null;
		
    	if (rs_profile.next()) {
    		rt_cname = rs_profile.getString("COURSE_NAME"); 
    		rt_cstart = rs_profile.getString("COURSE_START");
    		rt_cend = rs_profile.getString("COURSE_END");
    	}
    	
    	while (selection != "7" && courseView && returnToRoot) {
	    	
    		Menu.instructorViewCourseMenu(rt_cname, rt_cstart, rt_cend);
	    	selection = scanner.nextLine();
	    	
	    	switch (selection) {
	    	case "0": 
	    		courseView = false;
        		break;
	    	
	    	// TODO: 3. View/Add Exercise
	        case "3": 
			ExerciseMenu.ViewExerciseMenu(connection,cid);
	        	break;
	        	
	        // 4. View/Add TA
	        case "4":
	        	returnToRoot = instructorViewAddTA(connection, uid, cid);
	        	break;
	        
        	// 5. Enroll/Drop a student
	        case "5":
	        	returnToRoot = instructorEnrollStudent(connection, cid);
	        	break;
	        
	        // 6. TODO: View Report
	        case "6":
	        	// TODO 
	        	break;
	        	
	        case "7":
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
    
    // TODO: View/Add Exercise:  Instructors view exercises
    static void instructorViewAddExecise(Connection connection, String uid) throws Throwable {
    	
    	String exercise_selection = "1"; // selection for View/Add Exercise
    	Scanner scanner = new Scanner(System.in);
    	
    	
    }
    
    // View/Add TA:  Instructors view/add TAs for the course
    static Boolean instructorViewAddTA(Connection connection, String uid, String cid) throws Throwable {
    	
    	String ta_selection = "1"; // selection for View/Add TA
    	Scanner scanner = new Scanner(System.in);
    	
    	Boolean studentReturn = true; 
    	Boolean returnToRoot = true;
    	
    	while (ta_selection != "3" && studentReturn && returnToRoot) {
        	
    		Menu.instructorTAMenu();
    		ta_selection = scanner.nextLine();
        	
        	switch (ta_selection) {
        	case "0": 
        		studentReturn = false;
        		break;
        	// View/Add TA --> View TA
            case "1":
            	viewTA(connection, cid);
            	break;
        	// View/Add TA --> Add TA
            case "2":
            	instructorEnrollTA(connection, cid);
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
    
    // Enroll/Drop a student:  Instructor enroll/drop a student from STUDENT table
    static Boolean instructorEnrollStudent(Connection connection, String cid) throws Throwable {
 	   
 	   Scanner scanner = new Scanner(System.in);
 	   String student_selection = "1"; // selection for Enroll/Drop a student
 	   
 	   Boolean enrollReturn = true;
 	   Boolean returnToRoot = true;
    	
 	   while (student_selection != "3" && enrollReturn && returnToRoot) {
 			
 		   Menu.enrollStuMenu();         	
 		   student_selection = scanner.nextLine();
 		   	
 		   switch (student_selection) {
 		   case "0": 
 			   enrollReturn = false;
 			   break;
		   // Enroll/Drop a student --> Enroll student
 		   case "1":
 			  Professor.enrollStudentCourse(connection, cid);
 			   break;
		   // Enroll/Drop a student --> Drop student
 		   case "2":
 			  Professor.dropStudentCourse(connection, cid);
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
    
 // View/Add TA --> Add TA:  Instructor enroll a student as TA
    static void instructorEnrollTA(Connection connection, String cid) throws Throwable {
    	
    	Scanner scan = new Scanner(System.in);
    	
    	Menu.enrollTAMessage();
    	System.out.println("Please Enter the TA's user_id: \n");
    	String sid = scan.nextLine();
    	
    	boolean tapermit = false; 
    	
    	// Student enrolled in the system, not enrolled in the course, and has the type of "Graduate"
    	if (Student.checkStudentSysEnrollment(connection, sid) && Course.checkCourseRequirement(connection, sid, cid) && !tapermit)
    	{ addTA(connection, cid, sid); }
    	
    	// Student enrolled in the system, not enrolled in the course, and has the type of "Undergraduate"
    	else if (Student.checkStudentSysEnrollment(connection, sid) && Course.checkCourseRequirement(connection, sid, cid) && tapermit)
    	{ Menu.enrollTALevelFailureMessage(); }
    	
    	// Student enrolled in the system, have already been enrolled in the course
    	else if (Student.checkStudentSysEnrollment(connection, sid) && !Course.checkCourseRequirement(connection, sid, cid))
    	{ Menu.enrollStuEnrollFailureMessage();}
    	
    	// Student not enrolled in the system
    	else { Menu.enrollNoneStudentFailMessage(); }
    	
    	Menu.returnToMenuCommand();
    }
    
    // Check if a course exist in system
    static boolean checkCourseExist(Connection connection, String cid) throws Throwable {
    	
    	Boolean cexist = false; 
    	PreparedStatement preparedStatement_course = connection.prepareStatement(SqlQueries.SQL_VIEWCOURSEDETAIL);
    	preparedStatement_course.setString(1, cid);
    	ResultSet rs_allcourse = preparedStatement_course.executeQuery();

    	while( rs_allcourse.next() ) {
    		cexist = true;
    	}
    	return cexist;
    }
    
    
    // Check if student has the same level (Undergraduate/Graduate with the course requirement)
    static boolean checkCourseRequirement(Connection connection, String sid, String cid) throws Throwable {
    	
    	String clevel = null;
    	String slevel = null;
    	
    	PreparedStatement preparedStatement_clevel = connection.prepareStatement(SqlQueries.SQL_VIEWCOURSEDETAIL);
    	preparedStatement_clevel.setString(1, cid);
    	ResultSet rs_clevel = preparedStatement_clevel.executeQuery();
    	if (rs_clevel.next()) {
    		clevel = rs_clevel.getString("COURSE_LEVEL");
    	}
    	
    	PreparedStatement preparedStatement_slevel = connection.prepareStatement(SqlQueries.SQL_STUDENTPROFILE);
    	preparedStatement_slevel.setString(1, sid);
    	ResultSet rs_slevel = preparedStatement_slevel.executeQuery();
    	if (rs_slevel.next()) {
    		slevel = rs_slevel.getString("TYPE");
    	}

		if (clevel != null && slevel != null && (clevel.contains(slevel)) || slevel.contains(clevel)) { return true; }
    	else { return false; }
    	
    }
    
    // Check if student has been already enrolled in the course
    static boolean checkStudentEnrollment(Connection connection, String sid, String cid) throws Throwable {
    	
    	boolean cenrollIdx = false;
    	
    	PreparedStatement preparedStatement_cenroll = connection.prepareStatement(SqlQueries.SQL_STUVIEWALLCOURSE);
    	preparedStatement_cenroll.setString(1, sid);
    	ResultSet rs_cenroll = preparedStatement_cenroll.executeQuery();
    	
    	while (rs_cenroll.next()) {
    		cenrollIdx = true;
    	}

		return cenrollIdx; 
    }
    
    // Instructors view details infos of a course
    static ResultSet instructorViewCourseDetail(Connection connection, String cid) throws SQLException, ParseException {
    	
    	Scanner scanner = new Scanner(System.in);
    	
    	preparedStatement = connection.prepareStatement(SqlQueries.SQL_VIEWCOURSEDETAIL);
    	preparedStatement.setString(1, cid);
    	ResultSet rs_profile = preparedStatement.executeQuery();
    	
    	return rs_profile;
    }
    
    // Students view details infos of a course
    static Boolean studentViewCourse(Connection connection, String uid, String cid) throws SQLException, ParseException
    {	
    	
    	Scanner scanner = new Scanner(System.in);
    	String course_selection = "1";
    	Boolean courseReturn = true; 
    	Boolean returnToRoot = true;
    	
    	preparedStatement = connection.prepareStatement(SqlQueries.SQL_STUVIEWALLCOURSE);
    	preparedStatement.setString(1, uid);
    	ResultSet rs_profile = preparedStatement.executeQuery();
    	
    	String rt_cname = null;
		String rt_cstart = null;
		String rt_cend = null;
		
    	if (rs_profile.next()) {
    		rt_cname = rs_profile.getString("COURSE_NAME"); 
    		rt_cstart = rs_profile.getString("COURSE_START");
    		rt_cend = rs_profile.getString("COURSE_END");
    	}
 	   	
 	   while (course_selection != "4" && courseReturn && returnToRoot) {
 		   Menu.studentViewCourseMenu(rt_cname, rt_cstart, rt_cend);
 		   course_selection = scanner.nextLine();
	    	
 		   switch (course_selection) {
 		   case "0": 
 			  courseReturn = false;
 			   break;
	    	
	    	// TODO: 4. View Current/Past HWs
	        case "3":
	        	// TODO 
	        	returnToRoot = studentViewCourseHM(connection, cid);
	        	
	        	break;
	        	
	        // Return to login page
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
    
    // TODO: Students view details a course, including:
    // current HWs, past HWs
    static Boolean studentViewCourseHM(Connection connection, String cid) throws SQLException, ParseException
    {	
    	Boolean returnToRoot = true;
    	//preparedStatement = connection.prepareStatement(SqlQueries.SQL_STUVIEWALLCOURSE);
    	//preparedStatement.setString(1, cid);
    	//ResultSet rs_course = preparedStatement.executeQuery();
    	
    	Menu.studentViewHWMenu();
    	Menu.returnToMenuCommand();
    	return returnToRoot;
	}
    
    static Boolean taViewCourse(Connection connection, String uid, String cid) throws Throwable {
    	
    	Scanner scanner = new Scanner(System.in);
    	String selection = "1";
    	Boolean courseView = true; 
    	Boolean returnToRoot = true;
    	
    	preparedStatement = connection.prepareStatement(SqlQueries.SQL_INSTAVIEWCOURSE);
    	preparedStatement.setString(1, uid);
    	ResultSet rs_profile = preparedStatement.executeQuery();
    	
    	String rt_cname = null;
		String rt_cstart = null;
		String rt_cend = null;
		
    	if (rs_profile.next()) {
    		rt_cname = rs_profile.getString("COURSE_NAME"); 
    		rt_cstart = rs_profile.getString("COURSE_START");
    		rt_cend = rs_profile.getString("COURSE_END");
    	}
    	
    	while (selection != "7" && courseView && returnToRoot) {
	    	
    		Menu.taViewCourseMenu(rt_cname, rt_cstart, rt_cend);
	    	selection = scanner.nextLine();
	    	
	    	switch (selection) {
	    	case "0": 
	    		courseView = false;
        		break;
	    	
	    	// TODO: 3. View/Add Exercise
	        case "3":
	        	// TODO 
	        	break;
	        	
	        // 4. View TA
	        case "4":
	        	viewTA(connection, cid);
	        	break;
	        
        	// 5. Enroll/Drop a student
	        case "5":
	        	returnToRoot = instructorEnrollStudent(connection, cid);
	        	break;
	        
	        // 6. TODO: View Report
	        case "6":
	        	// TODO 
	        	break;
	        	
	        case "7":
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
    
    static void viewTA(Connection connection, String cid) throws SQLException 
	{
    	preparedStatement = connection.prepareStatement(SqlQueries.SQL_TAPROFILE);
    	preparedStatement.setString(1, cid);
    	
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
	
	static void addTA(Connection connection, String cid, String sid) throws SQLException {
		      
    	preparedStatement = connection.prepareStatement(SqlQueries.SQL_ENROLLTACOURSE);
    	preparedStatement.setString(1, cid);
    	preparedStatement.setString(2, sid);
    	preparedStatement.execute();
		
    	Menu.enrollTASuccessMessage(sid, cid);
	}

}
