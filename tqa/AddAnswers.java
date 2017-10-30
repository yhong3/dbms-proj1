import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;
import sql.*;

public class AddAnswers {
	private static int n_o_p = -1;
	private static int question_id = -1;
	private static int answer_id = -1;
	private static int parameter_id = -1; 
	private static int concrete_id = -1; 
	private static String answer_text = "";
	private static String short_explanation = "";
	private static int type = -1;
	
	AddAnswers(Connection conn, int number_of_p){
		n_o_p = number_of_p;
		PreparedStatement preparedStatement;
		try {
			preparedStatement = conn.prepareStatement(SqlQueries.SQL_COUNTROWS);
			PreparedStatement qps = preparedStatement;
			PreparedStatement apps = preparedStatement;
			qps.setString(1, "QUESTION");
			question_id  = qps.executeQuery().getInt(1);
			if(n_o_p == -1) {
				apps.setString(1, "CONCRETE_ANSWER");
				concrete_id = apps.executeQuery().getInt(1);
			}
			else {
				PreparedStatement pps = conn.prepareStatement("SELECT PARAMETER_ID" +
															"FROM PARAMETER_ANSWER" +
															"WHERE (QUESTION _ID = ? AND ANSWER_ID = ?)");
				apps.setString(1, "PARAMETER_ANSWER");
				answer_id = apps.executeQuery().getInt(1);
				pps.setInt(1, question_id);
				pps.setInt(2, answer_id);
				parameter_id = pps.executeQuery().getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static int parameterizeQ(Connection conn, Scanner s, int n_o_p) {
		PreparedStatement preparedStatement;
		int check = -1;
		int stop = -1;
    	while (stop != 0) {
    		parameter_id += 1;
            double[] parameters = new double[n_o_p];
            for(int i = 0; i < n_o_p; i++) {
            	System.out.printf("The value of parameter %i of the question is:", i);
                parameters[i] = s.nextDouble();
            }
            
            while(true) {
            	System.out.println("What is the answer text for this set of parameters: ");
                answer_text = s.nextLine();
                
                System.out.println("What is the short explanation of this answer text? ");
                short_explanation = s.nextLine();
                
                System.out.println("Is this answer incorrect or correct one? (0 for incorrect, 1 for correct)");
                type = s.nextInt();
                
                try {
    				answer_id += 1;
    				preparedStatement = conn.prepareStatement(SqlQueries.SQL_INSERTAP);
    				preparedStatement.setInt(1, question_id);
    	        	preparedStatement.setInt(2, parameter_id);
    	        	for(int i = 0; i < n_o_p; i++) {
    	        		preparedStatement.setDouble(i+2, parameters[i]);
    	        	}
    	        	preparedStatement.setString(9, answer_text);
    	        	preparedStatement.setString(10, short_explanation);
    	        	preparedStatement.setInt(11, type);
    	        	preparedStatement.execute();
    	        	System.out.println("Do you want to add more answer text for this set of parameters?");
                	String at_decision = s.nextLine();
                	if(at_decision.toLowerCase().charAt(0) == 'y') {
                		check = 1;
                	} else {
                		break;
                	}
    			} catch (SQLException e) {
    				// TODO Auto-generated catch block
    				clearsetting(1);
    				parameter_id -= 1;
        			check = 0;
    				e.printStackTrace();
    			}
            }
            
            System.out.println("Do you want to add another set of parameters and answer texts with it?");
        	String sp_decision = s.nextLine();
        	if(sp_decision.toLowerCase().charAt(0) == 'y') {
        		stop = 1;
        	} else {
        		stop = 0;
        	}
        } // end while
    	return check;
    }
    
    public static int concreteQ(Connection conn, Scanner s) {
    	PreparedStatement preparedStatement;
    	int check = -1; 
    	while (true) {
    		System.out.println("What is the possible answer of this concrete question: ");
    		answer_text = s.nextLine();
            
            System.out.println("What is the short explanation for this concrete answer: ");
            short_explanation = s.nextLine();
            
            System.out.println("Is this answer incorrect or correct one? (0 for incorrect, 1 for correct)");
            type = s.nextInt();
            
            try {
    			preparedStatement = conn.prepareStatement(SqlQueries.SQL_INSERTAC);
    			concrete_id += 1;
    			preparedStatement.setInt(1, question_id);
            	preparedStatement.setInt(2, concrete_id);
            	preparedStatement.setString(3, answer_text);
            	preparedStatement.setString(4, short_explanation);
            	preparedStatement.setInt(5, type);
            	preparedStatement.execute();
            	System.out.println("Do you want to add more answers for this concrete question?");
            	String decision = s.nextLine();
            	if(decision.toLowerCase().charAt(0) == 'y') {
            		check = 1;
            		continue;
            	} else {
            		check = 0;
            		break;
            	}
    		} catch (SQLException e) {
    			// TODO Auto-generated catch block
    			clearsetting(0);
    			check = 0;
    			e.printStackTrace();
    			continue;
    		}
    	} //ends while 
    	return check;
    }
    
    public static void clearsetting(int setting) {
    	type = -1;
    	answer_text = "";
		short_explanation = "";
		if(setting == 1) {
			answer_id -= 1;
		} else {
			concrete_id -= 1;
		}
    }
}
