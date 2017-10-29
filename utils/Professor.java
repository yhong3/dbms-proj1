package utils;

import sql.SqlQueries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Professor {
	
	private static PreparedStatement preparedStatement;    

    static void viewProfessor(Connection connection, String uid) throws SQLException
    {
    	System.out.println(
		"***********************************\n" +
		"Personal profile of Professor: \n" +
		"***********************************\n");
    	
    	preparedStatement = connection.prepareStatement(SqlQueries.SQL_PROFPROFILE);
    	preparedStatement.setString(1, uid);
    	
    	ResultSet rs_profile = preparedStatement.executeQuery();
    	//System.out.println("Successfully queried. \n");
    	
    	while (rs_profile.next()) {
            System.out.println(
            "USER_ID: " + rs_profile.getString("USER_ID") + "\n" +
            "PROFESSOR_NAME: " + rs_profile.getString("PROFESSOR_NAME") + "\n" +
			"***********************************\n");
        }
    }

}
