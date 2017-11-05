package utils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Scanner;
import sql.*;

public class Topics {
	private static final int SEARCH = 1;
	private static final int ADDT = 2;
	private static final int PREVIOUS = 3;
	private static int topic_id = -1;
	private static String topic_name = "";

	public static void displayMenu(Connection conn) {
		int choice = 0;
		int t_id = -1; 
		String c_id = "";
		int check = 1; 
		Scanner s = new Scanner(System.in);

		while (check == 1) {
			System.out.println("View Topics. Please choose an option: ");
			System.out.println("1. Search by Topic ID");
			System.out.println("2. Add more Topic");
			System.out.println("3. Go back to previous menu");
			System.out.println("Enter your choice and press Enter to continue. ");

			choice = s.nextInt();
			s.nextLine();
			switch (choice) {
			case SEARCH:
				//TODO view the question
				System.out.println("What is the topic id of the topic that you are looking for?");
				t_id = s.nextInt();
				s.nextLine();
				check = queryTopic(conn, t_id);
				break;
			case ADDT:
				System.out.println("What is the course id of the topic that you are looking for?");
				c_id = s.nextLine();
				check = addTopic(conn, s, c_id);
				break;
			case PREVIOUS:
				check = 0;
				break;
			default:
				System.out.println("Invalid menu choice!");
				break;
			} // end switch
		} // end while
	} // end displayMenu()

	private static int queryTopic(Connection conn, int topicID) {
		//System.out.println("called");
		PreparedStatement  stmt = null;


		try {
			String query = "";
			ResultSet rs;

			query = "SELECT topic_id, topic_name "
					+ "FROM TOPIC "
					+ "WHERE TOPIC_ID =?";
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, topicID);


			rs = stmt.executeQuery();
			while (rs.next()) {
				//System.out.println("result found");
				topic_id = rs.getInt("topic_id");
				topic_name = rs.getString("topic_name");

				System.out.println("Topic ID : " + topic_id);
				System.out.println("Topic name : " + topic_name);	
			}
			return 1;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return 0; 
	
	} // end setProfile()

	public static int addTopic(Connection conn, Scanner s, String cid) {
		int check_add_topic = 0;
		topic_id = findmaxid(conn, "topic_id", "TOPIC") + 1;
		
		System.out.println("\nThe name of the topic is: ");
		topic_name = s.nextLine();
	
		System.out.println("\nCourse ID: " + cid);
		
		PreparedStatement preparedStatement;
		try {
			preparedStatement = conn.prepareStatement(SqlQueries.SQL_INSERTTOPIC);
			preparedStatement.setInt(1, topic_id);
			preparedStatement.setString(2, topic_name);
			if(!cid.equals("")) {
				preparedStatement.setString(3, cid);
			}
			else {
				preparedStatement.setNull(3, Types.VARCHAR);
			}
			preparedStatement.execute();
			check_add_topic = 1;
			System.out.println();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			check_add_topic = 0;
			e.printStackTrace();
		}
		return check_add_topic;
	}
	
	public static int findmaxid(Connection conn, String another_query, String tablename) {
		ResultSet rs;
		int count = 0;
		PreparedStatement ps;
		String query = SqlQueries.SQL_FINDID + tablename;
		query = query.replace("?1", another_query);
		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while(rs.next()){
				count = rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}
}
