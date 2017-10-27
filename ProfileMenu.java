import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ProfileMenu {
	public static final int GOBACK = 0;
	public static String fullName = "";
	public static String user_id = "";
	
    private static PreparedStatement stmt;

    public static void displayMenu(Connection conn, User currentUser) {
        int choice = 0;
        Scanner s = new Scanner(System.in);

        while (true) {
        	
        	// set profile for current user
        	setProfile(conn, currentUser);
        	
        	// seperate first name and last name
        	String[] nameSplited = fullName.split("\\s+");
        	
        	// print out profile
            System.out.println("You are currently logged in as: " + currentUser.getUser_id());
            System.out.println("Enter 0 to Go back to previous menu. ");
            System.out.println("1. First Name: " + nameSplited[0]);
            System.out.println("2. Last Name: " + nameSplited[1]);
            System.out.println("3. Employee ID: " + user_id);

            choice = s.nextInt();

            // handle user input
            if (choice == GOBACK) {
            	return;
            } else {
            	System.out.println("Invalid choice. Please try again.");
            }
             
        } // end while
    } // end displayMenu()
    private static void setProfile(Connection conn, User currentUser) {
    	
    	try {
        	int role = currentUser.getRole();
        	String query = "";
        	ResultSet rs;
        	
        	switch (role) {
        	case RoleType.PROFESSOR:
                query = "SELECT user_id, professor_name "
        				+ "FROM PROFESSOR "
        				+ "WHERE user_id=?";
            	stmt = conn.prepareStatement(query);
    			stmt.setString(1, currentUser.getUser_id());
                
                rs = stmt.executeQuery();
                
                while (rs.next()) {
                	user_id = rs.getString("user_id");
                	fullName = rs.getString("professor_name");
                	//System.out.println(user_id + fullName);
                }
        		break;
        	case RoleType.TA:
        		query = "SELECT user_id, student_name "
        				+ "FROM STUDENT "
        				+ "WHERE user_id=?";
            	stmt = conn.prepareStatement(query);
    			stmt.setString(1, currentUser.getUser_id());
                
                rs = stmt.executeQuery();
                while (rs.next()) {
                	user_id = rs.getString("user_id");
                	fullName = rs.getString("student_name");
                }
        	case RoleType.STUDENT:
        		// TODO add student profile here 
        		break;
        	}
        	

        } catch (SQLException e) {
			System.out.println(e.getMessage());
        } finally {
            if (stmt != null) {
               try {
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            }
        }
    } // end Login()

}
