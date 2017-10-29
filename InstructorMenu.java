import java.sql.Connection;
import java.util.Scanner;

public class InstructorMenu {
    private static final int VIEWPROFILE = 1;
    private static final int EDITCOURSE = 2;
    private static final int EDITSTUDENT = 3;
    private static final int EDITQUESTIONBANK = 4;
    private static final int LOGOUT = 5;
    
    public static void displayMenu(Connection conn, User currentUser) {
        int choice = 0;
        Scanner s = new Scanner(System.in);

        while (true) {
            System.out.println("Logged in as Instructor: " + currentUser.getUser_id());
            System.out.println("Please make a choice: ");
            System.out.println("1. View Profile");
            System.out.println("2. View/Add Courses");
            System.out.println("3. Enroll/Drop A Student");
            System.out.println("4. Search/Add questions to Question Bank");
            System.out.println("5. Logout");
            System.out.print("Enter your choice and press Enter to continue: ");

            choice = s.nextInt();

            switch (choice) {
                case VIEWPROFILE:
                	ProfileMenu.displayMenu(conn, currentUser);
                    continue;
                case EDITCOURSE:
                    break;
                case EDITSTUDENT:
                    break;
                case EDITQUESTIONBANK:
                	// TODO use this for exercise temporarily 
                	ExerciseMenu.displayViewMenu(conn, currentUser);
                	break;
                case LOGOUT:
                	//TODO bring back to login menu
                	break;
                default:
                	System.out.println("Invalid menu choice!");
                    break;
            } // end switch
        } // end while
    } // end displayMenu()
}