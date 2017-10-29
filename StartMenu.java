import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class StartMenu {
    private static final int LOGIN = 1;
    private static final int EXIT = 2;
    private static Connection conn;
    private static User currentUser = new User();
    private static PreparedStatement stmt;
    
    private static void showLoginMenu(Connection conn) {
        Scanner scanner = new Scanner(System.in);
        User loggedInUser = new User();
        while (true) {
            System.out.print(
                    "1. Login\n" +
                    "2. Exit\n" +
                    "Enter your choice and press Enter to continue: ");
            int choice = scanner.nextInt();
            switch (choice) {
                case LOGIN: // TODO for wrong id input
                	String entered_user_id = "";
                	
                	System.out.print("Enter your userID: ");
                	entered_user_id = scanner.next();

                	loggedInUser = Login(conn, entered_user_id);
            		currentUser.setUser_id(loggedInUser.getUser_id());
                	currentUser.setRole(loggedInUser.getRole());
                	
                	switch(currentUser.getRole()) {
                		case RoleType.PROFESSOR:
                			InstructorMenu.displayMenu(conn, currentUser);
                			break;
                		case RoleType.STUDENT:
                			//TODO
                			break;
                		case RoleType.TA:
                			//TODO
                			break;
                	}                    
                    break;
                case EXIT:
                    System.exit(0);
                    break;
                default:
                	System.out.print("Invalid option");
                	continue;
            }
        }
    }
    
    private static User Login(Connection conn, String entered_user_id) {
    	User currentUser = null;
        try {
           
            String query = "SELECT user_id, role "
    				+ "FROM USERID_PASSWORD "
    				+ "WHERE user_id=?";
        	stmt = conn.prepareStatement(query);
			stmt.setString(1, entered_user_id);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
            	String user_id = rs.getString("user_id");
            	int role = rs.getInt("role");
            	currentUser = new User();
            	currentUser.setUser_id(user_id);
            	currentUser.setRole(role);
            }
            return currentUser;
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
        return null;
    } // end Login()
    
    public static void main(String[] args) {
		ConnectionManager dbcon = new ConnectionManager();
        conn = dbcon.getDBConnection();
        if (conn != null) {
            showLoginMenu(conn);
        } else {
            System.out.println("Connection to DB Failed!");
        }
    }
}
