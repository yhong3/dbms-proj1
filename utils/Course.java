package utils;

import sql.SqlQueries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import utils.InterfaceInterface; 
import utils.Student;
import utils.TA;

public class Course {
	
	private static PreparedStatement preparedStatement;
    
    static void addCourse(Connection connection, String uid) throws SQLException, ParseException
    {   
    	Scanner scanner = new Scanner(System.in);
    	
    	System.out.println(
		"************************************************\n" +
		"Add a New Course, Please Enter the Course Info: \n" +
		"************************************************\n");
    	
    	//scanner.nextLine(); 
    	
    	System.out.println("Add a course_id: \n");
    	String cid = scanner.nextLine();
    	
    	System.out.println("Add a course_name: \n");
    	String cname = scanner.nextLine();
    	
    	System.out.println("Add a course_start: \n");
    	String cstart = scanner.nextLine();
    	
    	System.out.println("Add a course_end: \n");
    	String cend = scanner.nextLine();
    	
    	SimpleDateFormat format= new SimpleDateFormat("yyyy/mm/dd"); //  hh:mi:ss AM
        java.util.Date dstart= format.parse(cstart);
        java.util.Date dend= format.parse(cend);
    	
        // Converting java.util.Date value to java.sql.Date class obj
        long constart = dstart.getTime();
        java.sql.Date qstart= new java.sql.Date(constart);
        long conend = dend.getTime();
        java.sql.Date qend= new java.sql.Date(conend);
    	
    	preparedStatement = connection.prepareStatement(SqlQueries.SQL_INSERTCOURSE);
    	preparedStatement.setString(1, cid);
    	preparedStatement.setString(2, cname);
    	preparedStatement.setDate(3, qstart);
    	preparedStatement.setDate(4, qend);
    	preparedStatement.execute();
    }
    
    static void studentViewCourse(Connection connection, String uid) throws SQLException, ParseException
    {	
    	preparedStatement = connection.prepareStatement(SqlQueries.SQL_STUVIEWALLCOURSE);
    	preparedStatement.setString(1, uid);
    	ResultSet rs_profile = preparedStatement.executeQuery();
    	System.out.println("Successfully queried. \n");
		
    	System.out.println(
    		"***********************************\n" + 	
			"Your Enrolled Courses: \n" +
			"***********************************\n");
    	
    	while (rs_profile.next()) {
    		System.out.println("COURSE_ID: " + rs_profile.getString("COURSE_ID") + "\n");
    	}
    }
    
    static void studentViewDetailCourse(Connection connection, String uid, String cid) throws SQLException, ParseException
    {	
    	preparedStatement = connection.prepareStatement(SqlQueries.SQL_STUVIEWCOURSE);
    	preparedStatement.setString(1, uid);
    	ResultSet rs_profile = preparedStatement.executeQuery();
    	System.out.println("Successfully queried. \n");
		
    	if (rs_profile.next()) {
    		System.out.println(
    	    		"***********************************\n" + 	
    				"Course Basic Infos: \n" +
    				"***********************************\n" +	
    				"0. Return to Previous Menu \n" +
    	            "1. COURSE_NAME: " + rs_profile.getString("COURSE_NAME") + "\n" +
    	    		"2. COURSE_START: " + rs_profile.getString("COURSE_START") + "\n" +
    				"   COURSE_END: " + rs_profile.getString("COURSE_END") + "\n");
    	}
    }

    static void instructorViewCourse(Connection connection, String uid, String cid) throws SQLException, ParseException
    {
    	
    	Scanner scanner = new Scanner(System.in);
    	
    	int selection = 1;
    	int exercise_selection = 1; // selection for View/Add Course
    	int ta_selection = 1; // selection for Enroll/Drop a student
    	int student_selection = 1; // selection for Search/Add questions to Question Bank
    	
    	preparedStatement = connection.prepareStatement(SqlQueries.SQL_INSVIEWCOURSE);
    	preparedStatement.setString(1, uid);
    	ResultSet rs_profile = preparedStatement.executeQuery();
    	//System.out.println("Successfully queried. \n");
    	
    	String rt_cname = null;
		String rt_cstart = null;
		String rt_cend = null;
		
    	if (rs_profile.next()) {
    		rt_cname = rs_profile.getString("COURSE_NAME"); 
    		rt_cstart = rs_profile.getString("COURSE_START");
    		rt_cend = rs_profile.getString("COURSE_END");
    	}
    	
    	while (selection != 7) {
	    		
            System.out.println(
    		"***********************************\n" +		
			"Course Basic Infos: \n" +
			"***********************************\n" +	
			"0. Return to Previous Menu \n" +
            "1. COURSE_NAME: " + rt_cname + "\n" +
    		"2. COURSE_START: " + rt_cstart + "\n" +
			"   COURSE_END: " + rt_cend + "\n" +
			"***********************************\n" +		
			"Please Make a Selection (3-7):\n" +
            "***********************************\n" +
	        "3. View/Add Exercise\n" +
	        "4. View/Add TA\n" +
	        "5. Enroll/Drop a student\n" +
	        "6. View Report\n" +
	        "7. Logout\n" +
			"***********************************\n");
	    	
	    	selection = scanner.nextInt();
	    	
	    	switch (selection) {
	    	case 0: 
        		return;
	    	
	    	// 3. View/Add Exercise
	        case 3:
	        	
	        	break;
	        	
	        // 4. View/Add TA
	        case 4:
	        	Boolean studentReturn = true; 
	        	
	        	while (ta_selection != 3 && studentReturn) {
	            	
	        			System.out.println(
	        			"***********************************\n" +
	        			"View/Add TA: \n" +
		            	"Please Make a Selection (1-3):\n" +
		                "***********************************\n" +
		                "0. Return to Previous Menu \n" +
		                "1. View TA\n" +
		                "2. Add TA\n" +
		                "3. Logout\n" +
		        		"***********************************\n");
		            	
		            	ta_selection = scanner.nextInt();
		            	
		            	switch (ta_selection) {
		            	case 0: 
		            		studentReturn = false;
		            		break;
		            		
			            case 1:
			            	TA.viewta(connection, cid);
			            	break;
			            	
			            case 2:
			            	Scanner scan = new Scanner(System.in);
			            	
			            	System.out.println(
			        		"***********************************************************\n" +
			        		"Enroll a TA to Course, Please Enter the TA Info: \n" +
			        		"***********************************************************\n");
			            	
			            	//scanner.nextLine(); 
			            	
			            	System.out.println("Please Enter the TA's user_id: \n");
			            	String sid = scan.nextLine();
			            	
			            	String slevel = Student.checkStudentLevel(connection, sid);
			            	boolean tapermit = slevel.contains("Undergrad"); 
			            	
			            	if (tapermit) { System.out.println("Undergraduate Students CANNOT Work as TA. \n"); }
			            	else { TA.addta(connection, cid, sid); }
			            	break;
			                
			            case 3:
			            	InterfaceInterface.exitInterface();
			            	
			            default:
			            	InterfaceInterface.defaultInterface();
			            	break;
			            
			            }//end switch
            	}//end while
	        	break;
	        
        	// 5. Enroll/Drop a student
	        case 5:
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
		            	Student.enrollStudentCourse(connection, cid, uid);
		            	break;
		            	
		            case 2:
		            	Student.dropStudentCourse(connection, cid, uid);
		            	break;
		                
		            case 3:
		            	InterfaceInterface.exitInterface();
		            	
		            default:
		            	InterfaceInterface.defaultInterface();
		            	break;
		            
		            }//end switch
            	}//end while
	        	break;
	        // 6. View Report
	        case 6:
	        	
	        	break;
	        	
	        case 7:
            	InterfaceInterface.exitInterface();

            default:
            	InterfaceInterface.defaultInterface();
            	break;
            
	    	}//end switch
    	}//end while
    }

}
