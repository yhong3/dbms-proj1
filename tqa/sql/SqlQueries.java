package sql;


public class SqlQueries {
    
    // The login user profiles for Professor 
    public static final String SQL_PROLOGIN = "SELECT P.USER_ID, P.PROFESSOR_NAME, U.ROLE FROM PROFESSOR P, USERID_PASSWORD U "
    		+ "WHERE P.USER_ID = ? AND U.ROLE = 1 AND U.USER_ID = P.USER_ID AND U.PASSWORD=?";
    
	// The login user profiles for TA
    public static final String SQL_TALOGIN = "SELECT S.USER_ID, S.STUDENT_NAME, U.ROLE FROM STUDENT S, USERID_PASSWORD U "
    		+ "WHERE S.USER_ID = ? AND U.ROLE = 2 AND U.USER_ID = S.USER_ID AND U.PASSWORD=? ";
    
    // The login user profiles for Student 
    public static final String SQL_STULOGIN =  "SELECT S.USER_ID, S.STUDENT_NAME, U.ROLE FROM STUDENT S, USERID_PASSWORD U "
    		+ "WHERE S.USER_ID = ? AND U.ROLE = 3 AND U.USER_ID = S.USER_ID AND U.PASSWORD=? ";
    
    // Check login role: 1-professor; 2-TA
    public static final String SQL_CHECKROLE = "SELECT U.ROLE FROM USERID_PASSWORD U WHERE U.USER_ID = ? ";
    
    // Check the personal info of Professor
    public static final String SQL_PROFPROFILE = "SELECT P.USER_ID, P.PROFESSOR_NAME FROM PROFESSOR P "
    		+ "WHERE P.USER_ID = ?";
    
    // Check the personal info of TA
    public static final String SQL_TAPROFILE = "SELECT S.USER_ID, S.STUDENT_NAME, S.YEAR_ENROLLED, S.TYPE, CS.COURSE_ID "
    		+ "FROM STUDENT S, COURSE_STAFF CS "
    		+ "WHERE CS.COURSE_ID = ? AND S.USER_ID = CS.USER_ID";
    
    // Check the personal info of Student
    public static final String SQL_STUDENTPROFILE = "SELECT S.USER_ID, S.STUDENT_NAME, S.YEAR_ENROLLED, S.TYPE "
    		+ "FROM STUDENT S "
    		+ "WHERE S.USER_ID = ? ";
    
    // Instructor View Course Info
    public static final String SQL_INSVIEWCOURSE = "SELECT C.COURSE_ID, C.COURSE_NAME, C.COURSE_START, C.COURSE_END "
    		+ "FROM COURSE_STAFF CS, COURSE C "
    		+ "WHERE CS.USER_ID = ? AND C.COURSE_ID = CS.COURSE_ID";
    
    // Studnet View a Certain Course Info
    public static final String SQL_STUVIEWCOURSE = "SELECT C.COURSE_ID, C.COURSE_NAME, C.COURSE_START, C.COURSE_END "
    		+ "FROM COURSE_STUDENT CS, COURSE C "
    		+ "WHERE CS.USER_ID = ? AND C.COURSE_ID = CS.COURSE_ID";
    
    // Student View All Course Info
    public static final String SQL_STUVIEWALLCOURSE = "SELECT CS.COURSE_ID FROM COURSE_STUDENT CS WHERE CS.USER_ID = ?";
    
    // Insert a Course
    public static final String SQL_INSERTCOURSE = "INSERT INTO COURSE(COURSE_ID, COURSE_NAME, COURSE_START, COURSE_END) "
    		+ "VALUES (?,?,?,?)";
    
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
    
    
    public static final String SQL_COUNTROWS = "SELECT COUNT (*) FROM ?";
    
    
    public static final String SQL_INSERTTOPIC = "INSERT INTO TOPIC(TOPIC_ID, TOPIC_NAME) VALUES (?,?)";
    
    
    public static final String SQL_INSERTQUESTION = "INSERT INTO QUESTION(QUESTION_ID, TOPIC_ID, QUESTION_TEXT, DIFFICULTY_LEVEL, HINT, DETAILED_EXPLANATION, TYPE)"
    		+ "VALUES (?,?,?,?,?,?,?)";
    
    
    public static final String SQL_INSERTAP = "INSERT INTO PARAMETER_ANSWER(QUESTION_ID, PARAMETER_ID, ANSWER_ID, PARAM_1, PARAM_2, PARAM_3, PARAM_4, PARAM_5, ANSWER_TEXT, SHORT_EXPLANATION, TYPE)"
    		+ "VALUES (?,?,?,?,?,?,?,?,?,?,?)";
    
    
    public static final String SQL_INSERTAC = "INSERT INTO CONCRETE_ANSWER(QUESTION_ID, ANSWER_ID, ANSWER_TEXT, SHORT_EXPLANATION, TYPE)"
    		+ "VALUES (?,?,?,?,?)";
}