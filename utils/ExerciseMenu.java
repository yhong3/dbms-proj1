package utils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ExerciseMenu {
	private static PreparedStatement stmt;
	private static final int GOBACK = 0;
	
	public static void ViewExerciseMenu(Connection conn, String cid)
	{
		int choice=0;
		Scanner s = new Scanner(System.in);

		System.out.println("1. Display exercises for a course");
		System.out.println("2. Make a new Exercise");
		
		choice= s.nextInt();
		
		switch(choice)
		{
			case GOBACK:  return;
			case 1     :  ViewExercise(conn,cid);return;
			case 2 	   :  newExercise(conn,cid);return;
			default    :  System.out.println("wrong choice please try again!!"); return;
		}

	}
	public static void newExercise(Connection  conn,String cid)
	{
		
		Scanner s = new Scanner(System.in);
		ResultSet rs;
		int last_id=0;
		String find_last_id="Select max(exercise_id) as maxid from EXERCISE";
		int choice=0;
		
		System.out.println("Enter Exercise name:");
		String exename=s.nextLine();
		System.out.println("Enter Exercise start date as yyyy-mm-dd:");			// date is still stored as dd-MON-rr, this is just input
		String exe_startdate=s.nextLine();
		System.out.println("Enter Exercise end date as yyyy-mm-dd:");
		String exe_enddate=s.nextLine();
		System.out.println("Enter Exercise retries allowed");
		int exe_retry_count=s.nextInt();
		System.out.println("Enter no of questions in the exercise");
		int exe_quest_count=s.nextInt();
		System.out.println("Enter Exercise Scoring policy :\n 1: Latest attempt\n 2: Maximum Score 3: Average Score");
		int exe_scoring_policy=s.nextInt();
		System.out.println("Enter Exercise correct answer points:");
		int exe_correct_points=s.nextInt();
		System.out.println("Enter penalty for wrong answer");
		int exe_wrong_points=s.nextInt();
		System.out.println("Enter Exercise's minimum difficulty level(1-5) ");
		int exe_min_diff=s.nextInt();
		if(exe_min_diff < 1 || exe_min_diff >5)
		{
			System.out.println("out of range values..try again!! ");
			return ;
		}
		System.out.println("Enter Exercise's maximum difficulty level(1-5) ");
		int exe_max_diff=s.nextInt();
		if(exe_max_diff < 1 || exe_max_diff >5)
		{
			System.out.println("out of range values..try again!! ");
			return ;
		}
		if(exe_max_diff < exe_min_diff)
		{
			System.out.println("maximum difficulty cannot be less than minimum. Try again!!");
			return;
		}		
		System.out.println("Enter Exercise's mode 0:Standard mode 1:Adaptive mode");
		int exe_mode = s.nextInt();
		
		try
		{
			stmt = conn.prepareStatement(find_last_id);
			rs=stmt.executeQuery();
			if(rs.next())
			{
				last_id=rs.getInt("maxid");
			}
			else
			{
				last_id=0;
			}
			last_id=last_id+1;

			String make_exercise="Insert into EXERCISE values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
			stmt = conn.prepareStatement(make_exercise);
			stmt.setInt(1, last_id);
			stmt.setString(2, cid);
			stmt.setString(3, exename);
			stmt.setDate(4, java.sql.Date.valueOf(exe_startdate));
			stmt.setDate(5, java.sql.Date.valueOf(exe_enddate));
			stmt.setInt(6, exe_retry_count);
			stmt.setInt(7, exe_quest_count);
			stmt.setInt(8, exe_scoring_policy);
			stmt.setInt(9, exe_correct_points);
			stmt.setInt(10, exe_wrong_points);
			stmt.setInt(11, exe_min_diff);
			stmt.setInt(12, exe_max_diff);
			stmt.setInt(13, exe_mode);
			stmt.executeQuery();
			System.out.println("Succesfully entered an exercise. Go to set questions for the exercise id="+last_id);
			return;
		}
		catch (Exception e) {
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
		
		
	}
	public static void ViewExercise(Connection conn,String cid) {
		int exerciseId = 0;
		int choice = 0;
		int lastQuerySuccess = 0;
		ResultSet rs;
		ResultSet qs;
		Scanner s = new Scanner(System.in);

				System.out.print("Please Enter Exercise ID: ");

				exerciseId = s.nextInt();
				lastQuerySuccess = queryExercise(conn, exerciseId);
				int quest_limit=0;
				if (lastQuerySuccess == 1) { // set by latest query
					System.out.println("0: Go back to previous menu. ");
					System.out.println("1. Add  Questions to Exercise");
					System.out.println("2 :Remove Questions from Exercise");
				} else { // TODO handle SQL exception
					System.out.println("Exercise ID not found, please re-enter: ");
				}

				choice = s.nextInt();

				// handle user input
				if (choice == GOBACK) {
					return;
				} else {
					if(choice==1)
						{ 	
							String query_mode="Select exercise_mode FROM EXERCISE where EXERCISE_ID=?";
							String limit_query="Select NUM_OF_QUESTIONS FROM EXERCISE where EXERCISE_ID=?";
							String quest_count="select count(QUESTION_ID) AS C FROM EXERCISE_QUESTION where EXERCISE_ID=?";
							try {
								stmt=conn.prepareStatement(query_mode);
								stmt.setInt(1, exerciseId);
								rs=stmt.executeQuery();
								if(rs.next())
								{
									int exe_mode=rs.getInt("exercise_mode");
									if(exe_mode==0)
									{
										stmt=conn.prepareStatement(limit_query);
										stmt.setInt(1, exerciseId);
										rs=stmt.executeQuery();
										if(rs.next())
										{
											int max_no=rs.getInt("NUM_OF_QUESTIONS");
											stmt=conn.prepareStatement(quest_count);
											stmt.setInt(1, exerciseId);
											qs=stmt.executeQuery();
											if(qs.next())
											{
												int current_no=qs.getInt(1);
												if(current_no<max_no)
												{
													standard_exercise_add_question(conn,exerciseId,cid);
												}
												else
												{
													System.out.println("Max count reached first remove question ");
													return;
												}
											}	
										}
									}
									else
									{
										adaptive_exercise_add_question(conn, exerciseId, cid);
									}
								}	
								} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					else
					{
						if(choice==2)
						{
							exercise_remove_question(conn,exerciseId);
						}
					}
				}
	}// end displayMenu()
	public static void exercise_remove_question(Connection conn, int exerciseId)
	{
		ResultSet rs;
		Scanner sc= new Scanner(System.in);
		String see_quest="SELECT question.question_id,question.question_text FROM exercise,question,exercise_question WHERE exercise.exercise_id=? and exercise.exercise_id=exercise_question.exercise_id and question.question_id=exercise_question.question_id";	
		try {
		stmt = conn.prepareStatement(see_quest);
		stmt.setInt(1, exerciseId);
		rs=stmt.executeQuery();
		System.out.println("enter the question id that you want to delete..questions that don't exists will be ignored....press 0 when Done");
		if (!rs.isBeforeFirst() ) {    
		    System.out.println("questions don't exist"); 
		    return;
		} 
		while(rs.next())
			{
				int quest_id=rs.getInt("question_id");
				String text=rs.getString("question_text");
				System.out.println(quest_id + "  : "+text );	
			}
		
		int choice=sc.nextInt();
		while(choice!=0)
			{
				String delete_query="Delete from Exercise_question where question_id=?";
				stmt = conn.prepareStatement(delete_query);
				stmt.setInt(1, choice);
				stmt.executeQuery();
				choice=sc.nextInt();
			}
		System.out.println("Required Questions have been deleted!! Going back to main menu");
		return;
		}catch (Exception e) {
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
		System.out.println();	
	}
	public static void standard_exercise_add_question(Connection conn, int exerciseId, String cid)
	{
		int choice=0;
		Scanner sc=new Scanner(System.in);
		String find_topic="select TOPIC_ID,TOPIC_NAME from TOPIC,COURSE where TOPIC.COURSE_ID=COURSE.COURSE_ID and COURSE.COURSE_ID=?";		
		try {
			
			System.out.println("select a topicid \n ");
		ResultSet rs;
		ResultSet qs;
		stmt = conn.prepareStatement(find_topic);
		stmt.setString(1, cid);
		rs=stmt.executeQuery();
		ResultSet check_question;
		
		while(rs.next())
		{
			int topic_ids=rs.getInt("TOPIC_ID");
			String topic_names=rs.getString("TOPIC_NAME");
			System.out.println(topic_ids+" : "+topic_names);
		}
		String select_topic=sc.nextLine();
		
		String find_quest="SELECT question_id,question_text from question where topic_id=?";
		stmt = conn.prepareStatement(find_quest);
		stmt.setString(1, select_topic);
		rs=stmt.executeQuery();
		
		if (!rs.isBeforeFirst() ) {    
		    System.out.println("topic doesn't exist"); 
		    return;
		} 
		while(rs.next())
		{
			int topic_ids=rs.getInt("question_id");
			String topic_names=rs.getString("question_text");
			System.out.println(topic_ids+" : "+topic_names);
		}
		System.out.println("select a questionids and press 0 to stop adding questions to add\n ");
		choice=sc.nextInt();
		while(choice!=0)
		{		
				String limit_query="Select NUM_OF_QUESTIONS FROM EXERCISE where EXERCISE_ID=?";
				String quest_count="select count(QUESTION_ID) AS C FROM EXERCISE_QUESTION where EXERCISE_ID=?";
				try {
				stmt=conn.prepareStatement(limit_query);
				stmt.setInt(1, exerciseId);
				rs=stmt.executeQuery();
				if(rs.next())
				{
					int max_no=rs.getInt("NUM_OF_QUESTIONS");
					stmt=conn.prepareStatement(quest_count);
					stmt.setInt(1, exerciseId);
					qs=stmt.executeQuery();
					if(qs.next())
					{
						int current_no=qs.getInt(1);
						if(current_no<max_no)
						{
							String quest_already="select * from exercise_question where question_id=?"; 		// check if question is already in the exercise
							String quest_exists="select * from question where question_id=?";					// check if question exists
							stmt = conn.prepareStatement(quest_exists);
							stmt.setInt(1, choice);	
							check_question=stmt.executeQuery();
							if(!check_question.isBeforeFirst()) {    
								System.out.println("question doesn't exist!! aborting..start over again"); 
								return;
							} 
							stmt = conn.prepareStatement(quest_already);
							stmt.setInt(1, choice);	
							check_question=stmt.executeQuery();
							if(check_question.next()) {
								System.out.println("question already exists in the exercise!! aborting..start over again");
								return;
							}
							String add_query="Insert into EXERCISE_QUESTION values(?,?)";
							stmt = conn.prepareStatement(add_query);
							stmt.setInt(1, exerciseId);
							stmt.setInt(2, choice);	
							stmt.executeQuery();
							choice=sc.nextInt();
						}
						else
						{
							System.out.println("Max count reached first remove question ");
							return;
						}
					}	
				}	
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}	

			System.out.println("questions recorded !! going back");
			return;

		}catch (Exception e) {
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
}
	public static void adaptive_exercise_add_question(Connection conn, int exerciseId, String cid)
	{
		int choice=0;
		ResultSet check_topic;
		Scanner sc=new Scanner(System.in);
		String find_topic="select TOPIC_ID,TOPIC_NAME from TOPIC,COURSE where TOPIC.COURSE_ID=COURSE.COURSE_ID and COURSE.COURSE_ID=?";		
		String add_topic="Insert into Exercise_topic values(?,?)";
		String topic_exists = "Select * from topic where topic_id=?";
		try {
			
				System.out.println("select a topicid \n ");
				ResultSet rs;
				ResultSet qs;
				stmt = conn.prepareStatement(find_topic);
				stmt.setString(1, cid);
				rs=stmt.executeQuery();
				while(rs.next())
				{
					int topic_ids=rs.getInt("TOPIC_ID");
					String topic_names=rs.getString("TOPIC_NAME");
					System.out.println(topic_ids+" : "+topic_names);
				}
				System.out.println("select a topicids and press 0 to stop adding topics\n ");
				choice=sc.nextInt();
				while(choice!=0)
				{		
					stmt = conn.prepareStatement(topic_exists);
					stmt.setInt(1, choice);	
					check_topic=stmt.executeQuery();
					if(!check_topic.isBeforeFirst()) {    
						System.out.println("topic doesn't exist!! aborting..start over again"); 
						return;
					} 
					stmt=conn.prepareStatement(add_topic);
					stmt.setInt(1, exerciseId);
					stmt.setInt(2, choice);
					stmt.executeQuery();
					choice=sc.nextInt();				
				}	
				System.out.println("topics recorded !! going back");
				return;
			}
		catch (Exception e) {
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
	}	
	private static int queryExercise(Connection conn,int exerciseId) {
		
		try {
			String query = "";
			ResultSet rs;
			
			// exercise parameters

					
			query = "SELECT *"
					+ "FROM EXERCISE "
					+ "WHERE exercise_id=?";
			
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, exerciseId);
			rs = stmt.executeQuery();
			
			if (rs.next()) { // result found
				
					int exercise_id = 0;
					String exercise_name = "";
					String exercise_start = "";
					String exercise_end = "";
					int retries_allowed = 0;
					int num_of_questions = 0;
					String scoring_policy = "";
					int correct_answer_points = 0;
					int incorrect_answer_penalty = 0;
					int difficulty_level_min = 0;
					int difficulty_level_max = 0;
					int exercise_mode=0;
					///// for showing questions
					String quest_text="";

					exercise_id = rs.getInt("exercise_id");
					exercise_name = rs.getString("exercise_name");
					exercise_start = rs.getString("exercise_start"); 
					exercise_end = rs.getString("exercise_end"); 
					retries_allowed = rs.getInt("retries_allowed");
					num_of_questions = rs.getInt("num_of_questions");
					scoring_policy = rs.getString("scoring_policy_id");
					correct_answer_points = rs.getInt("correct_answer_points");
					incorrect_answer_penalty = rs.getInt("incorrect_answer_penalty");
					difficulty_level_min = rs.getInt("difficulty_level_min");
					difficulty_level_max = rs.getInt("difficulty_level_max");
					exercise_mode=rs.getInt("exercise_mode");
					
					//System.out.println(user_id + fullName);
					
					System.out.println("Exercise ID : " + exercise_id);
					System.out.println("Exercise Name : " + exercise_name);
					System.out.println("Exercise Start Date : " + exercise_start);
					System.out.println("Exercise End Date: " + exercise_end);
					System.out.println("Retries Allowed : " + retries_allowed);
					System.out.println("Number of Questions: " + num_of_questions);
					System.out.println("Scoring Policy: " + scoring_policy);
					System.out.println("Points for each correct answer: " + correct_answer_points);
					System.out.println("Penalty points for each incorect answer: " + incorrect_answer_penalty);
					System.out.println("Difficulty Level Range (max 1-5): " + difficulty_level_min + "-" + difficulty_level_max );
					System.out.println("Exercise Mode:");
					if(exercise_mode==0) {
						System.out.print("Standard");
						}
					else {
						System.out.print("Adaptive");
					}
					
					//String quest_query="Select * from Question";
					String quest_query="SELECT question.question_id,question.question_text FROM exercise,question,exercise_question WHERE exercise.exercise_id=? and exercise.exercise_id=exercise_question.exercise_id and question.question_id=exercise_question.question_id";
					stmt = conn.prepareStatement(quest_query);
					stmt.setInt(1, exerciseId);
					rs = stmt.executeQuery();
					int quest_cnt=0;
					System.out.println();
					while(rs.next())
					{
						quest_cnt++;
						quest_text=rs.getString("question_text");
						System.out.println(quest_cnt+" : "+quest_text);
					}
					// print out result

				return 1;
			} else { // result set empty
				System.out.println("result not found");
				return 0;
			}
		} catch (Exception e) {
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
		return 0;
	} // end setProfile()
	
} // end class
