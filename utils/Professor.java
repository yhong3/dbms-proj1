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

import utils.Course;
import utils.Topics;

public class Professor {
    
    public static Boolean instructorInterface(Connection connection, String uid) throws Throwable {
    	
    	String selection = "1";    	
    	Scanner scanner = new Scanner(System.in);
    	
    	Boolean instructorReturn = true;
    	Boolean returnToRoot = true;
    	
    	while (selection != "5" && instructorReturn && returnToRoot) {
		
		Menu.instructorMainMenu();
		selection = scanner.nextLine();
		
		switch (selection) {  
		    case "0": 
			instructorReturn = false;
			break;

		    // View Profile
		    case "1":
			instructorProfile(connection, uid);
			break;
		    // View/Add Course 
		    case "2":
			returnToRoot = instructorViewCourseProfile(connection, uid);
			break;
		    // Enroll/Drop a student	
		    case "3":
			returnToRoot = instructorEnrollStudent(connection, uid);
			break;
		    // Search/Add questions to Question Bank 
		    case "4":
			returnToRoot = instructorSearchAddQuestion(connection, uid);
			    break;
			// Logout     	
		    case "5":
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
    
   // View/Add Course:  Instructor view/add a course in the COURSE table
   static Boolean instructorViewCourseProfile(Connection connection, String uid) throws Throwable {
	   
	   Scanner scanner = new Scanner(System.in);
	   String courseSelection = "1"; // selection for View/Add Course
	   
	   Boolean courseReturn = true; 
	   Boolean returnToRoot = true;

	   while (courseSelection != "3" && courseReturn && returnToRoot) {
		
		   Menu.instructorCourseMenu();
		   courseSelection = scanner.nextLine();
		   
		   switch (courseSelection) {	  
		   case "0": 
			   courseReturn = false;
			   break;
			
		   case "1":
			   returnToRoot = instructorViewCourseMenu(connection, uid);
			   break;
			   
		   case "2":
			   instructorAddCourse(connection, uid);
			   break;
			   	
		   case "3":
			   Menu.returnLoginMessage();
			   returnToRoot = false;
			   return returnToRoot;
			   
		   default:
			   Menu.warningMessage();
			   break;
		   }//end switch
	   } //end while  
	   return returnToRoot;
   }
    
   // Enroll/Drop a student:  Instructor enroll/drop a student from STUDENT table
   static Boolean instructorEnrollStudent(Connection connection, String uid) throws Throwable {
	   
	   Scanner scanner = new Scanner(System.in);
	   String student_selection = "1"; // selection for Enroll/Drop a student
	   
	   Boolean returnToRoot = true;
	   Boolean enrollReturn = true; 
   	
	   while (student_selection != "3" && enrollReturn && returnToRoot) {
			
		   Menu.enrollStuMenu();         	
		   student_selection = scanner.nextLine();
		   	
		   switch (student_selection) {
		   case "0": 
			   enrollReturn = false;
			   break;
			   
		   case "1":
			   enrollStudent(connection, uid);
			   break;
			   	
		   case "2":
			   dropStudent(connection, uid);
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
   
   // Search/Add questions to Question Bank:  Instructor search/add questions to question bank
   static Boolean instructorSearchAddQuestion(Connection connection, String uid) throws Throwable {
	   	Scanner scanner = new Scanner(System.in);
		String question_selection = "1"; // selection for Search/Add questions to Question Bank
		QuestionMenu qm = new QuestionMenu();
		Boolean questionReturn = true; 
		Boolean returnToRoot = true;

		while (question_selection != "3" && questionReturn && returnToRoot) {
			Menu.instructorQuestionMenu();
			question_selection = scanner.nextLine();
			switch (question_selection) {
			case "0":
				questionReturn = false;
				break;

			case "1":
				//TODO view the question
				System.out.println("View Question Bank. Please choose an option: ");
				System.out.println("1. Search by Question ID");
				System.out.println("2. Search by Topic");
				System.out.print("Enter your choice and press Enter to continue. ");
				int search_choice = scanner.nextInt();
				scanner.nextLine();
				qm.searchQ(connection, scanner, search_choice);
				qm.clearsetting();
				break;

			case "2":
				int check_add = qm.addQuestion(connection, scanner);
				if (check_add == 0) {
					questionReturn = false;
				}
				qm.clearsetting();
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
   
    // View Profile:  Instructor View the profile, including: 
    // 1) Basic profiles: PROFESSOR_ID and PROFESSOR_NAME
    // 2) Courses instructor in charge of: COURSE_ID, COURSE_NAME, COURSE_START, COURSE_END
    private static void instructorProfile(Connection connection, String uid) throws SQLException {
    	
    	PreparedStatement preparedStatement_pro;
    	PreparedStatement preparedStatement_course;
    	
    	preparedStatement_pro = connection.prepareStatement(SqlQueries.SQL_PROFPROFILE);
    	preparedStatement_pro.setString(1, uid);
    	
    	preparedStatement_course = connection.prepareStatement(SqlQueries.SQL_INSTAVIEWCOURSE);
    	preparedStatement_course.setString(1, uid);
    	
    	ResultSet rs_profile = preparedStatement_pro.executeQuery();
    	ResultSet rs_course = preparedStatement_course.executeQuery();
    	
    	if (rs_profile.next()) {
    		String uname = rs_profile.getString("PROFESSOR_NAME");
    		Menu.instructorProfileMessage(uid, uname);
        }
    	
    	while (rs_course.next()) {
    		String cid = rs_course.getString("COURSE_ID");
    		String cname = rs_course.getString("COURSE_NAME");
    		String cstart = rs_course.getString("COURSE_START");
    		String cend = rs_course.getString("COURSE_END");
    		
    		Menu.staffCourseDisplayMessage(cid, cname, cstart, cend);
        }
    	
    	Menu.returnToMenuCommand();
	}
    
    // View/Add Course:  Instructor choose View the Course
    // All courses instructor in charge of are listed
    // If a course in charge of instructor is chosen, display the menu with edit options
    // Else of a a course not in charge of instructor, only display the basic Infos of the course
    private static Boolean instructorViewCourseMenu(Connection connection, String uid) throws SQLException, Throwable {
    	
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
    		returnToRoot = Course.instructorViewCourse(connection, uid, detailcid);
    	} 
    	else {
    		// Check if the course has already exist
        	if (Course.checkCourseExist(connection, detailcid)) {
	    		ResultSet rs_cdetail = Course.instructorViewCourseDetail(connection, detailcid);
	    		if (rs_cdetail.next()) {
	    			Menu.staffAlienCourseDisplayMessage(rs_cdetail.getString("COURSE_ID"), rs_cdetail.getString("COURSE_NAME"),
		    				rs_cdetail.getString("COURSE_START"), rs_cdetail.getString("COURSE_END"));
	    		}
	    		Menu.returnToMenuCommand();
        	}
        	else { Menu.viewCourseFailureMessage();  Menu.returnToMenuCommand();}
    	}
    	
    	return returnToRoot;
    }
    
    // View/Add Course:  Instructor add a new course to COURSE table
    static boolean instructorAddCourse(Connection connection, String uid) throws Throwable
    {   
    	Scanner scanner = new Scanner(System.in);
    	Menu.addCourseMessage(); 
    	Boolean returnToRoot = true;
    	
    	System.out.println("Add a course_id: \n");
    	String cid = scanner.nextLine();
    	
    	String addCourse_selection = "1"; // selection for Add Course
		Boolean addCourseReturn = true;

		while (addCourse_selection != "4" && addCourseReturn && returnToRoot) {
			   Menu.addCourseMenu();
			   addCourse_selection = scanner.nextLine();

			   switch (addCourse_selection) {
			   case "0":
				   addCourseReturn = false;
				   break;
			   // Add course basic infos (Insert into the table of COURSE)
			   case "1":
				   // Check if the course has already exist
				if (Course.checkCourseExist(connection, cid)) { Menu.addCourseFailureMessage(); }

				else {

					System.out.println("Enter the Basic Infos of this New Course: \n");
					addCourseBasicInfo(connection, cid);
					Menu.addCourseSuccessMessage(cid);
				}
				   break;

			   // Add course instructor and TAs (Insert into the table of COURSE_STAFF)
			   case "2":
				   System.out.println("Add an instructor or TA for this New Course: \n");
				   returnToRoot = addStaffForCourse(connection, cid);
				   break;

			   // Add course topics (Insert into the table of TOPIC)
			   case "3":
				   Topics.addTopic(connection, scanner, cid);
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
	
    static void addCourseBasicInfo(Connection connection, String cid) throws Throwable {
    	
    	Scanner scanner = new Scanner(System.in);
    	
    	if (Course.checkCourseExist(connection, cid)) { Menu.addCourseFailureMessage(); }
    	
    	else {
    		
    		// Add course basic infos (Insert into the table of COURSE)
			System.out.println("Add a course_name: \n");
	    	String cname = scanner.nextLine();
	    	
	    	System.out.println("Add a course_start: \n");
	    	String cstart = scanner.nextLine();
	    	
	    	System.out.println("Add a course_end: \n");
	    	String cend = scanner.nextLine();
	    	
	    	System.out.println("Add a course_level: \n");
	    	String clevel = scanner.nextLine();
	    	
	    	SimpleDateFormat format= new SimpleDateFormat("yyyy/mm/dd"); //  hh:mi:ss AM
	        java.util.Date dstart= format.parse(cstart);
	        java.util.Date dend= format.parse(cend);
	    	
	        // Converting java.util.Date value to java.sql.Date class obj
	        long constart = dstart.getTime();
	        java.sql.Date qstart= new java.sql.Date(constart);
	        long conend = dend.getTime();
	        java.sql.Date qend= new java.sql.Date(conend);
    	
	    	PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.SQL_INSERTCOURSE);
	    	preparedStatement.setString(1, cid);
	    	preparedStatement.setString(2, cname);
	    	preparedStatement.setDate(3, qstart);
	    	preparedStatement.setDate(4, qend);
	    	preparedStatement.setString(5, clevel);
	    	preparedStatement.execute();
    	}
    }

	// Add a staff to the new added course 
	static boolean addStaffForCourse(Connection connection, String cid) throws Throwable {
    	
    	Scanner scanner = new Scanner(System.in);    	
    	String selection = "1";
    	Boolean addStaffView = true;
    	Boolean returnToRoot = true;
    	
    	while (selection != "7" && addStaffView && returnToRoot) {
    		Menu.addInstructorTAMenu();
    		
    		selection = scanner.nextLine();
    		
    		switch (selection) {
    		case "0": 
    			addStaffView = false;
            	break;
    		
    		case "1":
    			System.out.println("\n**Please enter the user_id of instructor.**");
    			// The id of instructor being assigned
    			String iid = scanner.nextLine();
    			
    			// check if there has been instructor assigned to the course
    			// If already assigned
    			if (!checkInstructorAssigned(connection, cid) && checkInstructorExist(connection, iid)) {
    				// execute the query to insert instructor into COURSE_STAFF table
    				PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.SQL_ASSIGNINSTRUCTOR);
				preparedStatement.setString(1, cid);
				preparedStatement.setString(2, iid);
				preparedStatement.execute();

				System.out.println("\n**Instructor " + iid + " has been assigned for the course " + cid + ".**");
    			}
    			else if (checkInstructorAssigned(connection, cid) && checkInstructorExist(connection, iid))
    			{
    				System.out.println("\n**This course has already been assigned to another instructor.**");
    			}
    			else {
    				System.out.println("\n**The assigned instructor does not exist in the system.**");
    			}
    			break;
    			
    		case "2":
    			Course.instructorEnrollTA(connection, cid);
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
	
	// Check if a course has been assigned an instructor
    static boolean checkInstructorAssigned(Connection connection, String cid) throws Throwable
    {
    	Boolean inassign = false; 
    	PreparedStatement preparedStatement_inassign = connection.prepareStatement(SqlQueries.SQL_CHECKCOURSEINS);
    	preparedStatement_inassign.setString(1, cid);
    	ResultSet rs_inassign = preparedStatement_inassign.executeQuery();
    	if (rs_inassign.next()) {
    		inassign = true;
    	}
		return inassign;
    }

	// Check if instructor exist in the system
    static boolean checkInstructorExist (Connection connection, String uid) throws SQLException
    {
    	Boolean iexist = false; 
    	PreparedStatement preparedStatement_senroll = connection.prepareStatement(SqlQueries.SQL_PROFPROFILE);
    	preparedStatement_senroll.setString(1, uid);
    	ResultSet rs_senroll = preparedStatement_senroll.executeQuery();
    	if (rs_senroll.next()) {
    		iexist = true;
    	}
    	
		return iexist;
    }
	
	
	
    // Instructor Main Menu --> Enroll/Drop a student:  Enroll student to course
    static void enrollStudent(Connection connection, String uid) throws Throwable
    {
    	PreparedStatement preparedStatement_course = connection.prepareStatement(SqlQueries.SQL_INSTAVIEWCOURSE);
    	preparedStatement_course.setString(1, uid);
    	ResultSet rs_course = preparedStatement_course.executeQuery();

    	List<String> courseList = new ArrayList<String>();
    	
    	while (rs_course.next()) {
    		String cid = rs_course.getString("COURSE_ID");
    		String cname = rs_course.getString("COURSE_NAME");
    		Menu.staffCourseListMessage(cid, cname);
    		courseList.add(cid);
        }
    	
    	Scanner scanner = new Scanner(System.in);
    	System.out.println("Please enter the course_id: \n");
    	String selectcid = scanner.nextLine();
    	
    	// Enroll student to the course
    	if (Course.checkCourseExist(connection, selectcid) && courseList.contains(selectcid)) {
    		enrollStudentCourse(connection, selectcid);
    	} 
    	
    	// If the course is not in charge of instructor
    	else if (Course.checkCourseExist(connection, selectcid) && !courseList.contains(selectcid)) {
    		Menu.enrollStuCourseFailMessage();
    		Menu.returnToMenuCommand();
    	}
    	
    	// If course does not exist in system
    	else if (!Course.checkCourseExist(connection, selectcid)) {
    		Menu.viewCourseFailureMessage();
    		Menu.returnToMenuCommand();
    	}
    }
    
    // View/Add Course -- > Enroll/Drop a student:  Enroll student to course
    static void enrollStudentCourse(Connection connection, String cid) throws Throwable
    {
    	
    	Scanner scanner = new Scanner(System.in);
    	Menu.enrollStuCourseMessage();
    	
    	System.out.println("Please enter the student's user_id: \n");
    	String sid = scanner.nextLine();
    	
    	// If student has been enrolled in the system && has not been enrolled in the course && has the level required by course 
    	if (Student.checkStudentSysEnrollment(connection, sid) && Course.checkCourseRequirement(connection, sid, cid)) {
	    	PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.SQL_ENROLLSTUDENTCOURSE);
	    	preparedStatement.setString(1, cid);
	    	preparedStatement.setString(2, sid);
	    	preparedStatement.execute();
	    	Menu.enrollSidMessage(sid);
    	}
    	
    	// If student has been enrolled in the system, has not been enrolled in the course, but has the different level with requirement
    	else if (Student.checkStudentSysEnrollment(connection, sid) && !Course.checkCourseRequirement(connection, sid, cid)) { 
    		Menu.enrollStuLevelFailureMessage();
    		Menu.enrollStuCourseFailMessage(); } 
    	// If student has not been enrolled in the system
    	else { 
    		Menu.enrollNoneStudentFailMessage(); 
    		Menu.returnToMenuCommand();
    		}
    }
    
    // Instructor Main Menu --> Enroll/Drop a student:  Drop student from course
    static void dropStudent(Connection connection, String uid) throws Throwable
    {	
    	PreparedStatement preparedStatement_course = connection.prepareStatement(SqlQueries.SQL_INSTAVIEWCOURSE);
    	preparedStatement_course.setString(1, uid);
    	ResultSet rs_course = preparedStatement_course.executeQuery();

    	List<String> courseList = new ArrayList<String>();
    	
    	while (rs_course.next()) {
    		String cid = rs_course.getString("COURSE_ID");
    		String cname = rs_course.getString("COURSE_NAME");
    		Menu.staffCourseListMessage(cid, cname);
    		courseList.add(cid);
        }
    	
    	Scanner scanner = new Scanner(System.in);
    	System.out.println("Please enter the course_id: \n");
    	String selectcid = scanner.nextLine();
    	
    	if (courseList.contains(selectcid)) {
    		dropStudentCourse(connection, selectcid); 
    	} 
    	else {
    		Menu.dropStuCourseFailMessage();
    		Menu.returnToMenuCommand();
    	}
    }
    
	// View/Add Course -- > Enroll/Drop a student:  Drop student from a course
    static void dropStudentCourse(Connection connection, String cid) throws Throwable
    {
    	Scanner scanner = new Scanner(System.in);
    	Menu.dropStuCourseMessage();
    	
    	System.out.println("Please enter the student's user_id: \n");
    	String sid = scanner.nextLine();
    	
    	// Check if student enrolled in this course
    	if (Course.checkStudentEnrollment(connection, sid, cid)) {
    		PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.SQL_DROPSTUDENTCOURSE);
        	preparedStatement.setString(1, cid);
        	preparedStatement.setString(2, sid);
        	preparedStatement.execute();
        	Menu.dropSidMessage(sid);
    	}
    	else { 
    		Menu.dropStuEnrollFailureMessage();  
    		Menu.returnToMenuCommand();}
    }
}
