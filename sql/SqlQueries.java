package sql;


public class SqlQueries {
    
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
    
}