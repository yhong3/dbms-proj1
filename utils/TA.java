package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import sql.SqlQueries;

public class TA extends Student{
	
	private static PreparedStatement preparedStatement;
	
	static void viewta(Connection connection, String cid) throws SQLException {
		System.out.println(
			"***********************************\n" +
			"TA Infos: \n" +
			"***********************************\n");
		
	    	preparedStatement = connection.prepareStatement(SqlQueries.SQL_TAPROFILE);
	    	preparedStatement.setString(1, cid);
	    	
	    	ResultSet rs_profile = preparedStatement.executeQuery();
	    	//System.out.println("Successfully queried. \n");
	    	while (rs_profile.next()) {
	            System.out.println(
	            "USER_ID: " + rs_profile.getString("USER_ID") + "\n" +
	            "STUDENT_NAME: " + rs_profile.getString("STUDENT_NAME") + "\n" +
	    		"YEAR_ENROLLED: " + rs_profile.getString("YEAR_ENROLLED") + "\n" +
	    		"TYPE: " + rs_profile.getString("TYPE") + "\n" +
				"TA FOR COURSE_ID: " + rs_profile.getString("COURSE_ID") + "\n" + 
				"***********************************\n");
	        }
	}
	
	static void addta(Connection connection, String cid, String sid) throws SQLException {
		      
    	preparedStatement = connection.prepareStatement(SqlQueries.SQL_ENROLLTACOURSE);
    	preparedStatement.setString(1, cid);
    	preparedStatement.setString(2, sid);
    	preparedStatement.execute();
		
    	System.out.println(sid + " has been set as a TA for " + cid + ". \n");
		
	}

}
