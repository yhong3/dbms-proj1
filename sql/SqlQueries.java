package sql;


public class SqlQueries {
    
	/// Profile Part
	// Check login role
	public static final String SQL_LOGINROLE = "SELECT U.ROLE FROM USERID_PASSWORD U WHERE U.USER_ID = ? AND U.PASSWORD= ? ";
    
    // Check the personal info of Professor
    public static final String SQL_PROFPROFILE = "SELECT P.USER_ID, P.PROFESSOR_NAME FROM PROFESSOR P "
    		+ "WHERE P.USER_ID = ?"; 
    
    // Instructor/TA View Related Course Info
    public static final String SQL_INSTAVIEWCOURSE = "SELECT C.COURSE_ID, C.COURSE_NAME, C.COURSE_START, C.COURSE_END "
    		+ "FROM COURSE_STAFF CS, COURSE C "
    		+ "WHERE CS.USER_ID = ? AND C.COURSE_ID = CS.COURSE_ID";
    
    // View a Certain Course Details
    public static final String SQL_VIEWCOURSEDETAIL = "SELECT C.COURSE_ID, C.COURSE_NAME, C.COURSE_START, C.COURSE_END, C.COURSE_LEVEL "
    		+ "FROM COURSE C WHERE C.COURSE_ID = ? ";
    
    // Student View Related Course Info
    public static final String SQL_STUVIEWALLCOURSE = "SELECT C.COURSE_ID, C.COURSE_NAME, C.COURSE_START, C.COURSE_END, C.COURSE_LEVEL "
    		+ "FROM STUDENT S, COURSE_STUDENT CS, COURSE C "
    		+ "WHERE S.USER_ID = ? AND S.USER_ID = CS.USER_ID AND CS.COURSE_ID = C.COURSE_ID ";
    
    // Check the personal info of TA
    public static final String SQL_TAPROFILE = "SELECT S.USER_ID, S.STUDENT_NAME, S.YEAR_ENROLLED, S.TYPE, CS.COURSE_ID "
    		+ "FROM STUDENT S, COURSE_STAFF CS "
    		+ "WHERE CS.COURSE_ID = ? AND S.USER_ID = CS.USER_ID";
    
    // Check the personal info of Student
    public static final String SQL_STUDENTPROFILE = "SELECT S.USER_ID, S.STUDENT_NAME, S.YEAR_ENROLLED, S.TYPE "
    		+ "FROM STUDENT S "
    		+ "WHERE S.USER_ID = ? ";
    
    // Insert a Course
    public static final String SQL_INSERTCOURSE = "INSERT INTO COURSE(COURSE_ID, COURSE_NAME, COURSE_START, COURSE_END, COURSE_LEVEL) "
    		+ "VALUES (?,?,?,?,?)";
    
    // Enroll a Student
    public static final String SQL_ENROLLSTUDENT = "INSERT INTO STUDENT(USER_ID, STUDENT_NAME, YEAR_ENROLLED, TYPE) "
    		+ "VALUES (?,?,?,?)";
    
    // Drop a Student
    public static final String SQL_DROPSTUDENT = "DELETE FROM STUDENT WHERE USER_ID = ?";
    
    // Enroll a Student to a course
    public static final String SQL_ENROLLSTUDENTCOURSE = "INSERT INTO COURSE_STUDENT(COURSE_ID, USER_ID) "
    		+ "VALUES (?,?)";
    
    // Drop a Student from a course 
    public static final String SQL_DROPSTUDENTCOURSE = "DELETE FROM COURSE_STUDENT WHERE COURSE_ID = ? AND USER_ID = ?";
    
    // Enroll a TA to a course
    public static final String SQL_ENROLLTACOURSE = "INSERT INTO COURSE_STAFF(COURSE_ID, USER_ID, ROLE) "
    		+ "VALUES (?,?,2)";
    
    // Check Student's level (Undergraduate/Graduate)
    public static final String SQL_CHECKLEVEL = "SELECT S.TYPE FROM STUDENT S WHERE S.USER_ID = ?";
    
    // Update Student's name
    public static final String SQL_UPDATESTUNAME = "UPDATE STUDENT SET STUDENT_NAME = ? WHERE USER_ID = ?";
    
    // Update Student's year enrolled 
    public static final String SQL_UPDATESTUENROLL = "UPDATE STUDENT SET YEAR_ENROLLED = ? WHERE USER_ID = ?";
    
    
    //// Report Part
    // Select all past exercise id for a user 
    public static final String SQL_ALLPASTEXEERCISE = "SELECT DISTINCT S.EXERCISE_ID FROM SUBMITS S WHERE S.USER_ID = ? AND S.COURSE_ID = ? ";
    
    // Select all distinct attempts for a certain exercise
    public static final String SQL_ALLATTEMPTS = "SELECT DISTINCT S.ATTEMPT FROM SUBMITS S WHERE S.USER_ID = ? AND S.COURSE_ID = ? AND S.EXERCISE_ID = ? ";
    
    // Select question_ids for a certain attempt
    public static final String SQL_QUESTIONSOFATTEMPT = "SELECT S.QUESTION_ID, S.PARAMETER_ID, "
    		+ "S.CONCRETE_ANSWER_ID, S.PARAMETER_ANSWER_ID, S.SUBMIT_TIME, Q.TYPE "
    		+ "FROM SUBMITS S, QUESTION Q "
    		+ "WHERE S.USER_ID = ? AND S.COURSE_ID = ? AND S.EXERCISE_ID = ? AND S.ATTEMPT = ? AND S.QUESTION_ID = Q.QUESTION_ID ";
    
    // Select paraAnswerText from PARAMETER_ANSWER table
    public static final String SQL_PARAANSWERTEXT = "SELECT ANSWER_TEXT FROM PARAMETER_ANSWER "
    		+ "WHERE QUESTION_ID = ? AND PARAMETER_ID = ? AND PARAMETER_ANSWER_ID = ? ";
    
    // Select paraAnswerText from PARAMETER_ANSWER table
    public static final String SQL_PARAQUESTIONTEXT = "SELECT QUESTION_TEXT FROM QUESTION "
    		+ "WHERE QUESTION_ID = ? ";
    
	
    public static final String SQL_FINDID = "SELECT MAX(?1) FROM ";

	public static final String SQL_INSERTTOPIC = "INSERT INTO TOPIC(TOPIC_ID, TOPIC_NAME) VALUES (?,?)";


	public static final String SQL_INSERTQUESTION = "INSERT INTO QUESTION(QUESTION_ID, TOPIC_ID, QUESTION_TEXT, DIFFICULTY_LEVEL, HINT, DETAILED_EXPLANATION, TYPE)"
			+ "VALUES (?,?,?,?,?,?,?)";


	public static final String SQL_INSERTAP = "INSERT INTO PARAMETER_ANSWER(QUESTION_ID, PARAMETER_ID, PARAMETER_ANSWER_ID, PARAM_1, PARAM_2, PARAM_3, PARAM_4, PARAM_5, ANSWER_TEXT, SHORT_EXPLANATION, TYPE)"
			+ "VALUES (?,?,?,?,?,?,?,?,?,?,?)";


	public static final String SQL_INSERTAC = "INSERT INTO CONCRETE_ANSWER(QUESTION_ID, CONCRETE_ANSWER_ID, ANSWER_TEXT, SHORT_EXPLANATION, TYPE)"
			+ "VALUES (?,?,?,?,?)";
	
	public static final String SQL_GETEXERCISE = "SELECT EXERCISE_NAME, EXERCISE_START, EXERCISE_END, NUM_OF_QUESTIONS, EXERCISE_MODE, RETRIES_ALLOWED, INCORRECT_ANSWER_PENALTY, CORRECT_ANSWER_POINTS FROM EXERCISE WHERE (COURSE_ID=? AND EXERCISE_ID=?)";
	
	public static final String SQL_GETATTEMPTEXERCISE = "SELECT QUESTION_ID, PARAMETER_ID, PARAMETER_ANSWER_ID, CONCRETE_ANSWER_ID, ATTEMPT FROM SUBMITS WHERE (COURSE_ID=? AND EXERCISE_ID=? AND USER_ID=? AND ATTEMPT=?)";

	public static final String SQL_SPOLICY = "SELECT SCORING_POLICY_TYPE FROM SCORING_POLICY WHERE SCORING_POLICY_ID=?";
	
	public static final String SQL_NOOFATTEMPT = "SELECT MAX(ATTEMPT) FROM SUBMITS WHERE (COURSE_ID=? AND EXERCISE_ID=? AND USER_ID=?)";
	
	public static final String SQL_GETTOPICS = "SELECT TOPIC_ID, TOPIC_NAME FROM TOPIC";
	
	public static final String SQL_GETQUESTIONBYTOPIC = "SELECT QUESTION_ID, QUESTION_TEXT FROM QUESTION WHERE TOPIC_ID=?";
	
	public static final String SQL_GETPANSWERTYPE = "SELECT TYPE FROM PARAMETER_ANSWER WHERE (QUESTION_ID=? AND PARAMETER_ID=? AND PARAMETER_ANSWER_ID=?)";
	
	public static final String SQL_GETCANSWERTYPE = "SELECT TYPE FROM CONCRETE_ANSWER WHERE (QUESTION_ID=? AND CONCRETE_ANSWER_ID=?)";
	
}
