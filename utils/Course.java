package utils;

import sql.SqlQueries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	    	
	    	// 3. View/Add Exercise
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
	        
	        // 6. View Report
	        case "6":
	        	Report.viewreport(connection, cid);
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
				instructorEnrollTA(connection, uid, cid);
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
    static void instructorEnrollTA(Connection connection, String uid, String cid) throws Throwable {
    	
    	Scanner scan = new Scanner(System.in);
    	
    	Menu.enrollTAMessage();
    	System.out.println("Please Enter the TA's user_id: \n");
    	String sid = scan.nextLine();
    	
    	// Student enrolled in the system, not enrolled in the course, has the type of "Graduate", and not already been set as the TA for this course    	
    	if (Student.checkStudentSysEnrollment(connection, sid) && !checkStudentEnrollment(connection, sid, cid) 
    			&& checkTARequirement(connection, sid) && !TA.checkTACourse (connection, sid, cid))
    	{	addTA(connection, uid, cid, sid); }
    	
    	// Student not enrolled in the system
    	else if (!Student.checkStudentSysEnrollment(connection, sid))
    	{ Menu.enrollNoneStudentFailMessage(); } 
    	
    	// Student has already been enrolled in the course
    	else if (checkStudentEnrollment(connection, sid, cid))
    	{ Menu.enrollStuEnrollFailureMessage(); }
    	
    	// Student has the type of "Undergraduate"
    	else if (!checkTARequirement(connection, sid))
    	{ Menu.enrollTALevelFailureMessage(); }
    	
    	// Student has already been set as the TA for this course
    	else if ( TA.checkTACourse (connection, sid, cid))
    	{ Menu.enrollStuRedoFailureMessage(); }	
    	
    	// Else
    	else { System.out.println("\n**Cannot recognize your input.**"); }
    	
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
	
// Check if a student meet the requirement of TA
    static boolean checkTARequirement(Connection connection, String sid) throws Throwable {

    	String slevel = null;
    	
    	PreparedStatement preparedStatement_slevel = connection.prepareStatement(SqlQueries.SQL_STUDENTPROFILE);
    	preparedStatement_slevel.setString(1, sid);
    	ResultSet rs_slevel = preparedStatement_slevel.executeQuery();
    	if (rs_slevel.next()) {
    		slevel = rs_slevel.getString("TYPE");
    	}
    	
    	String tlevel = "Grad";
		if (tlevel.contains(slevel) || slevel.contains(tlevel)) { return true; }
    	else { return false; }
    	
    }
    
    // Check if student has been already enrolled in the course
    static boolean checkStudentEnrollment(Connection connection, String sid, String cid) throws Throwable {
    	
    	boolean cenrollIdx = false;
    	
    	PreparedStatement preparedStatement_cenroll = connection.prepareStatement(SqlQueries.SQL_CHECKSTUCOURSEENROLL);
    	preparedStatement_cenroll.setString(1, sid);
    	preparedStatement_cenroll.setString(2, cid);
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
    	
    	// UPDATED HERE to VIEW INSTRUCTOR AND TA
    	List<String> instaList = new ArrayList<String>(); 
    	List<String> roleList = new ArrayList<String>(); 
    	
    	PreparedStatement preparedStatement_insta = connection.prepareStatement(SqlQueries.SQL_STUVIEINSTA);
    	preparedStatement_insta.setString(1, cid);
    	ResultSet rs_instaprofile = preparedStatement_insta.executeQuery();
    	
    	while (rs_instaprofile.next()) {
    		String temp_iaID = rs_instaprofile.getString("USER_ID");
    		instaList.add(temp_iaID);
    		String temp_iaRole = rs_instaprofile.getString("ROLE");
    		roleList.add(temp_iaRole);
    	}
    	
    	// UPDATE HERE to VIEW INSTRUCTOR AND TA
		   for (int i = 0; i < instaList.size(); i++) {
			  String temp_id = instaList.get(i);
			  String temp_role = roleList.get(i);
			  
			  if (temp_role.contains("1")) {System.out.println("Instructor: " + temp_id);}
			  else {System.out.println("Teaching Assistant: " + temp_id);}
		}
    	
    	
 	   	while (course_selection != "4" && courseReturn && returnToRoot) {
 		   Menu.studentViewCourseMenu(rt_cname, rt_cstart, rt_cend);
 		   
 		   course_selection = scanner.nextLine();
	    	
 		   switch (course_selection) {
 		   case "0": 
 			  courseReturn = false;
 			   break;
	    	
	    	// 3. View Current/Past HWs
	        case "3":
	        	returnToRoot = studentViewHM(connection, uid, cid);
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
    
    static Boolean studentViewHM(Connection connection, String uid, String cid) throws SQLException, ParseException
    {	    	
    	Scanner scanner = new Scanner(System.in);
    	String selection = "1";
    	Boolean homeworkView = true;
    	Boolean returnToRoot = true;
    	
    	while (selection != "7" && homeworkView && returnToRoot) {
    		
    		Menu.studentViewHWMenu();
    		selection = scanner.nextLine();
    		
    		switch (selection) {
    		case "0": 
    			homeworkView = false;
   			   break;
   			   
    		case "1":
	    		returnToRoot = studentViewCurrentHW(connection, cid, uid);
    			homeworkView = false;
	    		break;
    			
    		case "2":
    			ArrayList<String> past_exes = pastSubmissions(connection, uid, cid);
				if(past_exes.isEmpty()) {
					System.out.println("There is no past homework to show yet!");
				}
				else {
					String eid = studentChoosePastHM(connection, uid, cid, past_exes);
					if (eid != null) {
						returnToRoot = studentViewPastHM(connection, uid, cid, eid);
					}
					else {
						Menu.returnToMenuMessage();
					}
				}
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
    
    static String studentChoosePastHM(Connection connection, String uid, String cid, ArrayList <String> exerciseList) throws SQLException, ParseException
    {
    	Scanner scanner = new Scanner(System.in);
    	Menu.studentShowPastExeID(exerciseList);
    	Menu.studentSelectExeMessage();
    	
    	// Let user enter an exercise id to view the details
    	String input_eid = scanner.nextLine();
    	
    	if (exerciseList.contains(input_eid)) {
    		return input_eid;
    	} 
    	else { 
    		Menu.warningMessage();
    		return null;
    	}
    }
    	
    
    static Boolean studentViewPastHM(Connection connection, String uid, String cid, String eid) throws SQLException, ParseException
    {	    	
    	Scanner scanner = new Scanner(System.in);
    	String selection = "1";
    	Boolean pastCourseView = true;
    	Boolean returnToRoot = true;
    	
    	
    	while (selection != "10" && pastCourseView && returnToRoot) {
    		
    		Menu.studentViewPastHWTitleMenu();
    		
    		//Student View Report i --> viii (1-8)
    		
    		//Insert Huy's function HERE
    		PastHomework ph = new PastHomework();
    		ph.pastHW(connection, uid, cid, eid);
    		//Student View Report ix (9)
    		Menu.studentViewReportDetailMemu();
    		
    		selection = scanner.nextLine();
    		
    		switch (selection) {
    		case "0": 
    			pastCourseView = false;
        		break;
    		
    		case "9":
    			// Show all the attempt student has for that exercise
    			String atid = studentChooseAttempt(connection, uid, cid, eid); // attempt
    			if (atid.contains("tEmpty")) { break; }
    			else {
	    			studentViewDetailAttempt(connection, uid, cid, eid, atid);
	    			break;
    			}
    			
    		case "10":
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

    static Boolean studentViewCurrentHW(Connection connection, String cid, String uid) throws SQLException, ParseException
    {	
    	Boolean returnToRoot = true;
    	Scanner scanner = new Scanner(System.in);
    	int exid = 0;
    	String ename = "";
    	int retries_allowed = 0;
    	int excount = 0;
    	int stuAttemptCount = 0;
    	int stuMaxAttempt = 0; 
        ArrayList<Integer> HWOptions= new ArrayList<Integer>();

    	
    	// query for available hw for this course
        int hwcount = 0;
        //System.out.println("debug Run SQL_CURRENTOPENHW");
        //System.out.println("debug cid" + cid);

    	preparedStatement = connection.prepareStatement(SqlQueries.SQL_CURRENTOPENHW);
    	preparedStatement.setString(1, cid);
    	ResultSet rs_openexercise = preparedStatement.executeQuery();
    	// print menu header
    	Menu.studentCurrentHWHeader();
    	
    	// open exercise > 1
    	while (rs_openexercise.next()) {
    		excount++;
        		exid = rs_openexercise.getInt("EXERCISE_ID");
        		ename = rs_openexercise.getString("EXERCISE_NAME");
        		retries_allowed = rs_openexercise.getInt("RETRIES_ALLOWED");
        		
        		if (retries_allowed == -1) { // retries unlimited, just print
	    			Menu.printCurrentHW(exid, ename, retries_allowed);
	    			HWOptions.add(exid);
        		} else {
        			// check the current student max attempt     		
        	        //System.out.println("debug Run SQL_STUDENTMAXATTEMPT");

        			preparedStatement = connection.prepareStatement(SqlQueries.SQL_STUDENTMAXATTEMPTEXERCISE);
        	    	preparedStatement.setInt(1, exid);
        	    	ResultSet rs_studentmaxattempt = preparedStatement.executeQuery();
        	    	if (rs_studentmaxattempt.next()) {
        	    		stuAttemptCount = rs_studentmaxattempt.getInt("ROWCNT");
        	    		if (stuAttemptCount == 0 ) { // student have never attempt the exercise
        	        		// if no attempt, just print the exercise with retries_allowed
        	    			Menu.printCurrentHW(exid, ename, retries_allowed);
        	    			HWOptions.add(exid);
        	    		} else { // student has attempted the exercise
        	    			// check the max attempt number that is completed
        	    			stuMaxAttempt = rs_studentmaxattempt.getInt("MAXATTEMPT");
        	    			if (stuMaxAttempt < retries_allowed) { // student still have attempt remaining
            	    			Menu.printCurrentHW(exid, ename, retries_allowed - stuMaxAttempt);
            	    			HWOptions.add(exid);
        	    			}
        	    		}
        	    	}
        		}
    	} // end while open hws
    	Menu.returnToMenuMessage();
    	
    	if (excount == 0) { // no open homeworks
			Menu.printStudentNoHWMessage();    		
    	} else {
    	// print menu footer
    	Menu.studentCurrentHWFooter();
    	}
    	
    	System.out.println(Arrays.toString(HWOptions.toArray()));     	// TODO debug...

    	int selection = 1;
    	while (selection != 0) {
	    	selection = scanner.nextInt();
	    	if (HWOptions.contains(selection)) {
	    		// selection is exid
	    		Homework.attemptHomework(connection, selection, uid, cid);
	    		selection = 0; // exit after attempt successfully
	    	} else if (selection == 0) {
	    		return returnToRoot;
	    	} else {
            	Menu.warningMessage();
	    	}
    	}
    	return returnToRoot;
    }
    static String studentChooseAttempt(Connection connection, String uid, String cid, String eid) throws SQLException, ParseException
    {	
    	Scanner scanner = new Scanner(System.in);
    	
    	// Display all available attempts for that user
    	preparedStatement = connection.prepareStatement(SqlQueries.SQL_ALLATTEMPTS);
    	preparedStatement.setString(1, uid);
    	preparedStatement.setString(2, cid);
    	preparedStatement.setString(3, eid);
    	ResultSet rs_exercise = preparedStatement.executeQuery();
    	
    	List<String> attemptList = new ArrayList<String>();
    	
    	while (rs_exercise.next()) {
    		String temp_aid = rs_exercise.getString("ATTEMPT");
    		attemptList.add(temp_aid);
        }
	    
	if (attemptList.isEmpty()) { 
		System.out.println("\n**No attempt has been submitted.**");
		String tsignal = "tEmpty";
		return tsignal;
    	}
    	
    	Menu.studentShowAttempts(attemptList);
    	Menu.studentSelectAttemptMessage();
    	
    	// Let user enter an exercise id to view the details
    	String input_aid = scanner.nextLine();
    	
    	if (attemptList.contains(input_aid)) {
    		return input_aid;
    	} 
    	else { 
    		Menu.warningMessage();
    		Menu.returnToMenuMessage();
    		return null;
    	}
    }
    
    // View Courses --> Past HWs --> Detailed report for each attempt
    static void studentViewDetailAttempt(Connection connection, String uid, String cid, String eid, String atid) throws SQLException, ParseException {
    	
    	// Select all question_ids from the SUBMITS table
    	preparedStatement = connection.prepareStatement(SqlQueries.SQL_QUESTIONSOFATTEMPT);
    	preparedStatement.setString(1, uid);
    	preparedStatement.setString(2, cid);
    	preparedStatement.setString(3, eid);
    	preparedStatement.setString(4, atid);
    	
    	ResultSet rs_attempt = preparedStatement.executeQuery();
    	
    	List<String> questionList = new ArrayList<String>();
    	List<String> paramterList = new ArrayList<String>();
    	List<String> conAnswerList = new ArrayList<String>();
    	List<String> paraAnswerList = new ArrayList<String>();
    	List<Date> dateList = new ArrayList<Date>();
    	List<String> qTypeList = new ArrayList<String>();
    	
    	String temp_qid = null;
    	String temp_paraId = null;
    	String temp_conAnsId = null;
    	String temp_paraAnsId = null;
    	Date temp_date = null;
    	String temp_qType = null;
    	
    	while (rs_attempt.next()) {
    		
    		temp_qid = rs_attempt.getString("QUESTION_ID");
    		temp_paraId = rs_attempt.getString("PARAMETER_ID");
    		temp_conAnsId = rs_attempt.getString("CONCRETE_ANSWER_ID");
    		temp_paraAnsId = rs_attempt.getString("PARAMETER_ANSWER_ID");
    		temp_date = rs_attempt.getDate("SUBMIT_TIME");
    		temp_qType = rs_attempt.getString("TYPE");
    		
    		questionList.add(temp_qid);
    		paramterList.add(temp_paraId);
    		conAnswerList.add(temp_conAnsId);
    		paraAnswerList.add(temp_paraAnsId);
    		dateList.add(temp_date);
    		qTypeList.add(temp_qType);
    		
        }
    	
    	int count_corrans = 0; // Record the number of correct answers
		int count_incorrans = 0; // Record the number of incorrect answers
		Boolean anscorrIdx = true;
		
    	for (int i = 0; i < qTypeList.size(); i++) {
    		temp_qType = qTypeList.get(i);
    		
    		// If question type is 0 (concrete)
    		if(temp_qType.equals("0")) {
    			System.out.println("\n***********************************");
    			System.out.println("The answer of Quesiton " + questionList.get(i) + ": ");
    			System.out.println("***********************************");
    			anscorrIdx = ConcreteQuestionAnswer(connection, uid, cid, eid, questionList.get(i), atid, dateList.get(i));
    		}
    		// If question type is 1 (parameter)
    		else {
			System.out.println("\n***********************************");
			System.out.println("The answer of Quesiton " + questionList.get(i) + ": ");
			System.out.println("***********************************");

			// Generate the text for question
			preparedStatement = connection.prepareStatement(SqlQueries.SQL_PARAQUESTIONTEXT);
			preparedStatement.setString(1, questionList.get(i));
			ResultSet rs_paraQuesText = preparedStatement.executeQuery();

			String temp_paraText = null; 
			while (rs_paraQuesText.next()) {
				temp_paraText = rs_paraQuesText.getString("QUESTION_TEXT");
			}
			anscorrIdx = parameterQuestionAnswer(connection, uid, cid, eid, temp_paraText, questionList.get(i), atid, dateList.get(i)); 
		}//end parameter answers
    		
    		if (anscorrIdx == true) { count_corrans += 1; }
    		else { count_incorrans += 1; }
		}//end answers for each attempt
    	
    	// Total points for that attempt
    	// 6. Total points for that attempt    	
    	// Check the CORRECT_ANSWER_POINTS and INCORRECT_ANSWER_PENALTY given eid
    	
    	PreparedStatement preparedStatement_apoint = connection.prepareStatement(SqlQueries.SQL_CHECKPOINTS);
    	preparedStatement_apoint.setString(1, eid);
    	
    	ResultSet rs_apoint= preparedStatement_apoint.executeQuery();
    	
    	int temp_coPoint = 0; 
    	int temp_incoPoint = 0; 
    	
    	while (rs_apoint.next()) {
    		temp_coPoint = rs_apoint.getInt("CORRECT_ANSWER_POINTS");
    		temp_incoPoint = rs_apoint.getInt("INCORRECT_ANSWER_PENALTY");
    	}
    	
	System.out.println();
	int total_score = temp_coPoint*count_corrans + ((-1)*temp_incoPoint)*count_incorrans;
	// If the total score > 0, display the total score
	if (total_score > 0) {
		System.out.println();
		System.out.println("\n***********************************");
		System.out.println("**6. Total points for that attempt**");
		System.out.println("Points scored for this question is: " + Integer.toString(total_score));
		System.out.println("***********************************");
	}
	// Else if the total score <= 0, display the total score as 0 
	else {
		System.out.println();
		System.out.println("\n***********************************");
		System.out.println("**6. Total points for that attempt**");
		System.out.println("Points scored for this question is: 0");
		System.out.println("***********************************");
	}

	Menu.returnToMenuCommand();
    }
    

    // Count the number of parameters in a question
    static int countSString(String str) {
	Pattern p = Pattern.compile("<\\s*\\?\\s*>");
	Matcher matcher = p.matcher(str);
    	int count = 0;
    	int pos = 0;
    	while (matcher.find(pos))
	{
		count++;
		pos = matcher.start() + 1;
	}
	return count;
    }
    
	static ArrayList<String> pastSubmissions(Connection connection, String uid, String cid) throws SQLException, ParseException
	{
		ArrayList<String> pastSubmission = new ArrayList<String>();
		//while(true) {
		try {
			preparedStatement = connection.prepareStatement(SqlQueries.SQL_GETEXERCISETIME);
			preparedStatement.setString(1, cid);
			ResultSet rs = preparedStatement.executeQuery();
			if (!rs.isBeforeFirst() ) {    
				System.out.println("No exercise exist for this course yet"); 
				return null;
			} 
			while(rs.next()) {
				String e_id = rs.getString("exercise_id");
				java.util.Date e_date = rs.getDate("exercise_end");
				ZoneId z = ZoneId.of( "America/New_York" ); // use the new_york time zone to determine current date
				LocalDate currentZoneDate = LocalDate.now(z);
				java.util.Date currentDate = java.sql.Date.valueOf(currentZoneDate);

				// If current data after the exercise due, show the explanation

				preparedStatement = connection.prepareStatement(SqlQueries.SQL_GETSTUDENTAPERCOURSE);
				preparedStatement.setString(1, cid);
				preparedStatement.setString(2, e_id);
				preparedStatement.setString(3, uid);
				ResultSet rs_exercise = preparedStatement.executeQuery();

				while (rs_exercise.next()) {
					int ex_in_submit = rs_exercise.getInt(1);
					if(ex_in_submit > 0 || currentDate.compareTo(e_date) > 0) {
						pastSubmission.add(e_id); 
					}
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//}
		return pastSubmission;
	}	
	
    // Detailed report for each attempt display
    static boolean DetailedReportDisplay(String answer_type, String short_explanation, String answer_hint, Date exercise_due,
    		String question_type, String corr_points, String incorr_points) {

    	Boolean corransIdx = true;
    	
	// 3. Solution for each question
	// Display the hints/solutions for each question
	ZoneId z = ZoneId.of( "America/New_York" ); // use the new_york time zone to determine current date
	LocalDate currentZoneDate = LocalDate.now(z);
	java.util.Date currentDate = java.sql.Date.valueOf(currentZoneDate);

	// If current data after the exercise due, show the explanation
	if (currentDate.compareTo(exercise_due) > 0) {
		System.out.println();
		System.out.println("**3. Solution for this question**");
		System.out.println("Short Explanation: " + short_explanation);
	}
	// Else of current data before the exercise due, show the hint
	else {
		System.out.println();
		System.out.println("**3. Solution for this question**");
		System.out.println("Question Hint: " + answer_hint);
	}

	// 4. Whether the selected answer was correct or not (type_0: Incorrect; type_1: Correct)
	// 5. Points scored for each question

	if (answer_type.equals("0")) { 
		System.out.println();
		System.out.println("**4. Whether the selected answer was correct or not**");
		System.out.println("Answer for this question is: " + "Incorrect");
		System.out.println();
		System.out.println("**5. Points scored for this question**");
		System.out.println("Points scored for this question is: " + incorr_points);
		corransIdx = false;
	}

	else { 
		System.out.println();
		System.out.println("**4. Whether the selected answer was correct or not**");
		System.out.println("Answer for this question is: " + "Correct");
		System.out.println();
		System.out.println("**5. Points scored for this question**");
		System.out.println("Points scored for this question is: " + corr_points);
	}
	return corransIdx;
    }
    
   // Display of concrete questions & answers
 	static boolean ConcreteQuestionAnswer(Connection connection, String uid, String cid, String eid, String qid, String atid, Date submit_time) throws ParseException, SQLException {
 		
 		boolean anscorrIdx = true;
 		
 		String query = "SELECT CA.CONCRETE_ANSWER_ID, CA.ANSWER_TEXT, CA.SHORT_EXPLANATION, CA.TYPE, Q.QUESTION_TEXT, Q.HINT, " +
 				"E.EXERCISE_END, E.CORRECT_ANSWER_POINTS, E.INCORRECT_ANSWER_PENALTY " + 
 				"FROM CONCRETE_ANSWER CA, QUESTION Q, SUBMITS S, EXERCISE E " + 
 				"WHERE S.USER_ID = ? AND S.COURSE_ID = ? AND E.EXERCISE_ID = ? AND "
 				+ "S.QUESTION_ID = ? AND S.ATTEMPT = ? AND "
 				+ "CA.QUESTION_ID = Q.QUESTION_ID " +
 				"AND CA.CONCRETE_ANSWER_ID = S.CONCRETE_ANSWER_ID AND S.EXERCISE_ID = E.EXERCISE_ID ";	
 		
		PreparedStatement stmt = connection.prepareStatement(query);
		ResultSet rs;
		stmt.setString(1, uid);
		stmt.setString(2, cid);
		stmt.setString(3, eid);
		stmt.setString(4, qid);
		stmt.setString(5, atid);
		
		
		rs = stmt.executeQuery();
		int count_rs = 0;
		
		while (rs.next()) {
			count_rs += 1;
			String answer_type = rs.getString("type");
			String question_text = rs.getString("question_text");
			String answer_text = rs.getString("answer_text");
			String short_explanation = rs.getString("short_explanation");
			String answer_hint = rs.getString("hint");
			Date exercise_due = rs.getDate("exercise_end");
			String corr_points = rs.getString("correct_answer_points");
			String incorr_points = rs.getString("incorrect_answer_penalty");
			
			// 1. All the questions in that attempt
			
			System.out.println("**1. Question in this attempt**");
			System.out.println("Concrete Answers from Question ID: " + qid);
			System.out.println("Question text : " + question_text);

			// 2. Answers selected by the student for each attempt
			System.out.println();
			System.out.println("**2. Answers selected by the student for this Question**");
			System.out.println("Answer text : " + answer_text);

			// 3-5
			String question_type = "concrete";
			anscorrIdx = DetailedReportDisplay( answer_type, short_explanation, answer_hint, exercise_due, question_type, corr_points, incorr_points);
		}
		
 		return anscorrIdx;
 	}
    
	
 	// Display of parameter questions & answers
	static boolean parameterQuestionAnswer(Connection connection, String uid, String cid, String eid, String paraQuesText, 
			String qid, String atid, Date submit_time) {
		Pattern p = Pattern.compile("<\\s*\\?\\s*>");
		
		int para_num = countSString(paraQuesText);
		String[] parameters = new String[para_num];
		String query = "";
		boolean anscorrIdx = true;
		
		
		for(int i = 0; i < para_num; i++) {
			if(query == "") {
				query = "SELECT PARAM_" + Integer.toString(i+1);
			}
			else {
				query += ", PARAM_" + Integer.toString(i+1);
			}
		}
		query += ", PA.PARAMETER_ID, PA.PARAMETER_ANSWER_ID, PA.ANSWER_TEXT, PA.SHORT_EXPLANATION, PA.TYPE, Q.QUESTION_TEXT, Q.HINT, "
				+ "E.EXERCISE_END, E.CORRECT_ANSWER_POINTS, E.INCORRECT_ANSWER_PENALTY " + 
				"FROM PARAMETER_ANSWER PA, QUESTION Q, SUBMITS S, EXERCISE E " +  
				"WHERE S.USER_ID = ? AND S.COURSE_ID = ? AND "
				+ "S.EXERCISE_ID=? AND S.QUESTION_ID = ? AND S.ATTEMPT = ? AND "
				+ "PA.QUESTION_ID = Q.QUESTION_ID " +
				"AND PA.PARAMETER_ANSWER_ID = S.PARAMETER_ANSWER_ID AND S.EXERCISE_ID = E.EXERCISE_ID ";
		
		try {
			PreparedStatement stmt = connection.prepareStatement(query);
			ResultSet rs;
			stmt.setString(1, uid);
			stmt.setString(2, cid);
			stmt.setString(3, eid);
			stmt.setString(4, qid);
			stmt.setString(5, atid);
			
			rs = stmt.executeQuery();
			String s = "";
			int count_rs = 0;
 			
			while (rs.next()) {
				Matcher m = p.matcher(paraQuesText);
				count_rs += 1;
				int count = 0;
				
				String answer_type = rs.getString("type");
				String question_text = rs.getString("question_text");
				String answer_text = rs.getString("answer_text");
				String short_explanation = rs.getString("short_explanation");
				String answer_hint = rs.getString("hint");
 				Date exercise_due = rs.getDate("exercise_end");
 				String corr_points = rs.getString("correct_answer_points");
 				String incorr_points = rs.getString("incorrect_answer_penalty");
 				
 				// 1. All the questions in that attempt
 				for(int i = 0; i < para_num; i++) {
 					parameters[i] = rs.getString("PARAM_" + Integer.toString(i+1));
 				}
 				
 				while (m.find()) {
					s = m.replaceFirst(parameters[count]);
					m = p.matcher(s);
					count += 1;
				}
 				
 				System.out.println("**1. Question in this attempt**");
 				System.out.println("Parameter Answers from Question ID: " + qid);
 				System.out.println("Question Root text: " + s);
 				System.out.print("The set of parameters include: "); 
 				System.out.println();
 				for(int j = 0; j < para_num; j++) {
 					System.out.print(parameters[j] + "  ");
 				}
 				
				// 2. Answers selected by the student for each attempt
				System.out.println();
				System.out.println("**2. Answers selected by the student for this Question**");
				System.out.println("Answer text : " + answer_text); 				

				// 3-5
				String question_type = "parameter";
				anscorrIdx = DetailedReportDisplay( answer_type, short_explanation, answer_hint, exercise_due, question_type, corr_points, incorr_points);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return anscorrIdx;
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
	    	
	    	// 3. View/Add Exercise
	        case "3":
	        	ExerciseMenu.ViewExerciseMenu(connection,cid); 
	        	break;
	        	
	        // 4. View TA
	        case "4":
	        	viewTA(connection, cid);
	        	break;
	        
        	// 5. Enroll/Drop a student
	        case "5":
	        	returnToRoot = instructorEnrollStudent(connection, cid);
	        	break;
	        
	        // 6. View Report
	        case "6":
	        	Report.viewreport(connection, cid);
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
    
    static void viewTA(Connection connection, String cid) throws SQLException {
	    
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
	
	static void addTA(Connection connection, String uid, String cid, String sid) throws SQLException {
		
		preparedStatement = connection.prepareStatement(SqlQueries.SQL_ENROLLTACOURSE);
		preparedStatement.setString(1, cid);
		preparedStatement.setString(2, sid);
		preparedStatement.execute();
    	
		// Revise the role of student in userid_password table as 2
		PreparedStatement preparedStatement_role = connection.prepareStatement(SqlQueries.SQL_REVISETAROLE);
		preparedStatement_role.setString(1, sid);
		preparedStatement_role.execute();

		Menu.enrollTASuccessMessage(sid, cid);
	}

	static ArrayList<String> pastExes(Connection conn, String cid, String sid){
		ArrayList<String> past_exes = new ArrayList<String>();
		try {
			preparedStatement = conn.prepareStatement(SqlQueries.SQL_GETEXERCISETIME);
			preparedStatement.setString(1, cid);
	    	ResultSet rs = preparedStatement.executeQuery();
	    	while(rs.next()) {
	    		String e_id = rs.getString("exercise_id");
	    		java.util.Date e_date = rs.getDate("exercise_end");
	    		ZoneId z = ZoneId.of( "America/New_York" ); // use the new_york time zone to determine current date
	    		LocalDate currentZoneDate = LocalDate.now(z);
	    		java.util.Date currentDate = java.sql.Date.valueOf(currentZoneDate);
	    		
	    		// If current data after the exercise due, show the explanation
	    		if (currentDate.compareTo(e_date) > 0) {
	    			past_exes.add(e_id);
	    		}
	    	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return past_exes;
	}
}
