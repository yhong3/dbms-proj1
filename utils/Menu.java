package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Menu {
	
	public static void loginMenu() {
		System.out.print(
    		"***********************************\n" +
    		"Main Menu\n" +
            "Please Make a Selection (1-2):\n" +
            "***********************************\n" +
            "1. Login \n" +
            "2. Exit \n" +
    		"***********************************\n");
    }
	
	public static void welcomeMessage(String logRole) {
		System.out.print(
    		"***********************************\n" +
    		"Welcome To the Course System!\n\n" +  				
			"Login as with the role of: " + logRole + "\n" +
			"***********************************\n\n");
    }
	
	public static void taLoginMenu() {
		System.out.print(
			"***********************************\n" +
            "Please Choose a Menu for (1-2):\n" +
            "***********************************\n" +
            "0. Return to Previous Menu \n" +
            "1. Student\n" +
            "2. Teaching Assistant\n" +
            "3. Logout\n" +
    		"***********************************\n");
    }
	
	public static void instructorMainMenu() {
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
    }
	
	public static void taMainMenu() {
		System.out.print(
    		"***********************************\n" +
    		"TA Main Menu\n" +
            "Please Make a Selection (1-5):\n" +
            "***********************************\n" +
            "0. Return to Previous Menu \n" +
            "1. View Profile\n" +
            "2. View Course\n" +
            "3. Enroll/Drop a student\n" +
            "4. View Report\n" +
            "5. Logout\n" +
    		"***********************************\n");
	}
	
	public static void studentMainMenu() {
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
    }
	
	public static void instructorCourseMenu() {
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
    }
	
	public static void instructorQuestionMenu() {
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
	}
	
	public static void instructorViewCourseMenu(String cname, String cstart, String cend) {
		System.out.println(
    		"***********************************\n" + 
			"Course Basic Infos: \n" +
			"***********************************\n" +	
			"0. Return to Previous Menu \n" +
            "1. COURSE_NAME: " + cname + "\n" +
    		"2. COURSE_START: " + cstart + "\n" +
			"   COURSE_END: " + cend + "\n" +
			"***********************************\n" +		
			"Please Make a Selection (3-7):\n" +
            "***********************************\n" +
	        "3. View/Add Exercise\n" +
	        "4. View/Add TA\n" +
	        "5. Enroll/Drop a student\n" +
	        "6. View Report\n" +
	        "7. Logout\n" +
			"***********************************\n");
    }
	
	public static void instructorExciseMenu() {
		 System.out.println(
		 "***********************************\n" +		
		 "View/Add Exercise: \n" +
		 "Please Make a Selection (1-3):\n" +
		 "***********************************\n" +
		 "0. Return to Previous Menu \n" +
		 "1. View Exercise\n" +
		 "2. Add Exercise\n" +
		 "3. Logout\n" +
		 "***********************************\n");
	}
	
	public static void instructorTAMenu() {
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
	}
	
	public static void taViewCourseMenu(String cname, String cstart, String cend) {
		System.out.println(
    		"***********************************\n" + 	
			"Course Basic Infos: \n" +
			"***********************************\n" +	
			"0. Return to Previous Menu \n" +
            "1. COURSE_NAME: " + cname + "\n" +
    		"2. COURSE_START: " + cstart + "\n" +
			"   COURSE_END: " + cend + "\n" +
			"***********************************\n" +
			"Please Make a Selection (3-7):\n" +
            "***********************************\n" +
	        "3. View/Add Exercise\n" +
	        "4. View TA\n" +
	        "5. Enroll/Drop a student\n" +
	        "6. View Report\n" +
	        "7. Logout\n" +
			"***********************************\n");
    }
	
	public static void enrollStuMenu() {
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
	}
	
	public static void studentViewEditProfileMenu() {
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
    }
	
	public static void studentEditProfileMenu() {
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
    }
	
	public static void studentViewCourseMenu(String cname, String cstart, String cend) {
		System.out.println(
			"******************************************\n" +
			"Student View of Course: \n" +
			"Check 3 to see the HW details:\n" +
			"******************************************\n" +
			"0. Return to Previous Menu \n" +
	        "1. COURSE_NAME: " + cname + "\n" +
			"2. COURSE_START: " + cstart + "\n" +
			"   COURSE_END: " + cend + "\n" +
	        "3. Homeworks\n" +
	        "4. Logout\n" +
			"******************************************\n");
    }
	
	public static void studentViewHWMenu() {
		System.out.println(
			"******************************************\n" +
			"Student View of Course: \n" +
			"Please Make a Selection (1-3):\n" +
			"******************************************\n" +
			"0. Return to Previous Menu \n" +
	        "1. Current HWs\n" +
	        "2. Past HWs\n" +
	        "3. Logout\n" +
			"******************************************\n");
    }
	
	public static void studentViewPastHWTitleMenu() {
		System.out.println(
			"******************************************\n" +
			"Student View of Past HWs: \n" +
			"Please Make a Selection (9-10):\n" +
			"******************************************\n" +
			"0. Return to Previous Menu \n");
			
    }
	
	public static void studentViewReportDetailMessage() { 
		System.out.println( "\n**Pleaes enter 9 for seeing the detailed report for each attempt.** \n"); 
		}
	
	public static void studentViewReportDetailMemu() { 
		System.out.println( 
			"9. Detailed report for each attempt. \n" +
			"10. Logout\n" +
			"******************************************\n"); 
		}
	
	public static void studentShowPastExeID(List<String> exerciseList) { 
		System.out.println( 
			"******************************************\n" +
			"PAST HWs LIST (exercise_id you have done): \n" +
			"******************************************\n"); 
		
		System.out.println(exerciseList);
		}
	
	public static void studentSelectExeMessage() { System.out.println( "\n**Please select an exercise id from the PAST HWs LIST shown above.**"); }
	
	public static void studentShowAttempts(List<String> attemptList) { 
		System.out.println( 
			"******************************************\n" +
			"ATTEMPT LIST: \n" +
			"******************************************\n"); 
		
		System.out.println(attemptList);
		}
	
	public static void studentSelectAttemptMessage() { System.out.println( "\n**Pleaes select an attempt from the ATTEMPT LIST shown above to view the details.**"); }
	
	
	
	//
	public static void studentViewPastHWReportMenu() {
		System.out.println(
			"******************************************\n" +
			"Student View of Past HW Reports: \n" +
			"Please Make a Selection (1-3):\n" +
			"******************************************\n" +
			"0. Return to Previous Menu \n" +
			"0. Return to Previous Menu \n" +
			"0. Return to Previous Menu \n" +
			"0. Return to Previous Menu \n" +
			"0. Return to Previous Menu \n" +
			"0. Return to Previous Menu \n" +
			"** Past HW Infos:** \n");
			
    }
	
	public static void studentProfileMessage(String sid, String sname, int syear, String stype) {
		System.out.println(
			"*********Personal Profile********** \n" +
            "USER_ID: " + sid + "\n" +
            "STUDENT_NAME: " + sname + "\n" +
    		"YEAR_ENROLLED: " + Integer.toString(syear) + "\n" +
    		"TYPE: " + stype + "\n" +
			"***********************************\n");
    }

	public static void courseInfoMessage(String cid, String cname, String cstart, String cend) {
		System.out.println(
			"1. COURSE_ID: " + cid + "\n" +
            "2. COURSE_NAME: " + cname + "\n" +
    		"3. COURSE_START: " + cstart + "\n" +
			"   COURSE_END: " + cend + "\n" +
			"***********************************\n");
    }
	
	public static void courseListMessage(String cid, String cname) {
		System.out.println(
			"COURSE_ID: " + cid + "\n" + 
			"COURSE_NAME: " + cname + "\n" + 
			"***********************************\n");
    }
	
	public static void stdCourseDisplayMessage(String cid, String cname, String cstart, String cend) {
		System.out.println("\n**********Courses Enrolled:********"); 
		courseInfoMessage(cid, cname, cstart, cend); 
		}
	
	public static void staffCourseDisplayMessage(String cid, String cname, String cstart, String cend) { 
		System.out.println( "\n*********Courses Work With:********"); 
		courseInfoMessage(cid, cname, cstart, cend); 
		}
	
	public static void staffAlienCourseDisplayMessage(String cid, String cname, String cstart, String cend) { 
		System.out.println( "\n******Courses Not Work With:*******"); 
		courseInfoMessage(cid, cname, cstart, cend); 
		}
	
	public static void stdCourseListMessage(String cid, String cname) {
		System.out.println("\n**********Courses Enrolled:********"); 
		courseListMessage(cid, cname); 
		}
	
	public static void staffCourseListMessage(String cid, String cname) { 
		System.out.println( "\n*********Courses Work With:********"); 
		courseListMessage(cid, cname); 
		}
	
	public static void instructorProfileMessage(String uid, String uname) {
		System.out.println(
			"*********Personal Profile********** \n" +
            "USER_ID: " + uid + "\n" +
            "PROFESSOR_NAME: " + uname + "\n" +
			"***********************************\n");
    }
	
	public static void returnToMenuCommand() {
		Scanner scanner = new Scanner(System.in);
		
		Menu.returnToMenuMessage();
    	String returnMessage = scanner.nextLine();
    	
    	switch(returnMessage) {
    	case "0":{
    		return;
    	}
    	default:
    		Menu.warningMessage();
    	}
    }
	
	public static void enterUserIDMessage() { System.out.println( "\n**Pleaes enter your userID:** \n");  }
	
	public static void enterPasswordMessage() { System.out.println( "\n**Please enter your password:** \n");  }
	
	public static void returnLoginMessage() { System.out.println( "\n************* Logout **************\n"); }
	
	public static void exitMessage() {
    	System.out.println( "\n*********** Exit System ***********");
    	System.exit(0); 
    }
	
	public static void warningMessage() {		
    	System.out.println("\n**WARNING: Cannot Recognize Your Entered Option, Try Again.**");
    }
	
	public static void connFailMessage() {	
		System.out.println( "\n**WARNING: UserID/Passwork Incorect, failed to connect.**");
    }
	
	public static void connSuccessMessage() { System.out.println( "\n**Successfully connected.**"); }
	
	public static void returnToMenuMessage() { System.out.println( "\n**Press 0 Return to Previous Menu.**"); }	
	
	public static void enterCidMessage() { System.out.println( "\n**Please Enter the Course_id to View the details:**"); }
	
	public static void addCourseMessage() { System.out.println("\n**Add a New Course, Please Enter the Course Info:**"); }
	
	public static void addCourseFailureMessage() { System.out.println("\n**Course has already been exist in the System.**"); }
	
	public static void viewCourseFailureMessage() { System.out.println("\n**Entered course does not exist in the System.**"); }
	
	public static void viewCourseFailPermitMessage() { System.out.println("\n**No Permission to View this course.**"); }
	
	public static void addCourseSuccessMessage(String cid) {
		System.out.println("\n**The New Course: " + cid + "has been created.**"); 
	}
	
	public static void enrollTAMessage() { 
		System.out.println("\n**Enroll a TA to Course, Please Enter the TA Info:**"); 
	}
	
	public static void enrollTALevelFailureMessage() { 
		System.out.println("\n**Undergraduate Students CANNOT Work as TA.**"); 
	}
	
	public static void enrollTASuccessMessage(String sid, String cid) { 
		System.out.println("\n**" + sid + " has been set as a TA for " + cid + ".**");
	}
	
	public static void enrollStuEnrollFailureMessage() {
		System.out.println(
			"\n**Student has already been enrolled in this course.**");
    }
	
	public static void dropStuEnrollFailureMessage() {
		System.out.println(
			"\n**Student has not been enrolled in this course.**");
    }
	
	public static void enrollStuLevelFailureMessage() {
		System.out.println(
			"\n**Student level does not consistent with course requrement.**");
    }
	
	public static void enrollStuCourseFailMessage() {
		System.out.println(
			"\n**No permission to enroll students to this course.**");
    }
	
	public static void enrollNoneStudentFailMessage() {
		System.out.println(
			"\n**Entered student id does not exist.**");
    }
	
	public static void dropStuCourseFailMessage() {
		System.out.println(
			"\n**No permission to drop students from this course.**");
    }
	
	public static void enrollStuCourseMessage() { 
		System.out.println("\n**Enroll a student to course, please enter the student info:**");
    }
	
	public static void dropStuCourseMessage() {
		System.out.println(
			"\n**Drop a student from course, please enter the student info:**");
    }
	
	public static void enrollSidMessage(String sid) {
		System.out.println("** Student " + sid + " have been enrolled in the course.");
	}
	
	public static void dropSidMessage(String sid) {
		System.out.println("Student: " + sid + " have been dropped from the course.");
    }
	
	/*****************************
	 * Student HW related
	 *****************************/
	public static void studentCurrentHWHeader() { 
		System.out.println( "\n******Current HWs*******");
	}
	
	public static void printCurrentHW(int exid, String ename, int attempt_left) {
		// format 
		//1. exercise name (Attempt left: 2)
		if (attempt_left == -1) {
			System.out.println(exid + ". " + ename + " (Attempt left: Unlimited)");
		} else {
			System.out.println(exid + ". " + ename + " (Attempt left: " + attempt_left + ")");
		}
		
	}
	public static void studentCurrentHWFooter() { 
		System.out.println( "Please choose the homework id you want to attempt: ");
		
	}
	public static void printStudentNoHWMessage() { System.out.println( "\n**Hurray! No open homeworks now.**"); }
	
	public static void printQuestion(int num, String text, ArrayList<String> ans) {
		System.out.println( 
				"\n******Question " + num + " *******\n"
				+ text
				+ "\n********************************"
				+ "\nOptions (1-4): "
				+ "\n 1. " + ans.get(0)
				+ "\n 2. " + ans.get(1)
				+ "\n 3. " + ans.get(2)
				+ "\n 4. " + ans.get(3)
				+ "\n");
	}
	
	public static void askAnswerMessage() {
		System.out.println(
			"\nPlease enter your answer (1-4): ");
    }
	
	public static void invalidAnswerMessage() {
		System.out.println(
			"\nInvalid option, please enter your answer (1-4): ");
    }
	
	public static void completeHWMessage() {
		System.out.println(
			"\n*** You have complete the attempt of this homework***");
    }
	public static void submitAnswerSuccessMessage() { System.out.println( "**Answer Submitted**"); }
	public static void attemptHWSuccessMessage() { System.out.println( "**You have finished this attempt successfully.**"); }

	// TODO remove
	/*
    public static void main(String args[]) {
        String text = "<?>b<?>c<?>d<?>e<?>";
        String[] splittedText = text.split(Pattern.quote("<?>"));

        System.out.println(Arrays.toString(splittedText));
    }
    */
}
