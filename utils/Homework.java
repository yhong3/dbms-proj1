package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sql.SqlQueries;

public class Homework {
	// exercise mode
	private static final int STANDARD = 0;
	private static final int ADAPTIVE = 1;
	// question type
	private static final int CONCRETE = 0;
	private static final int PARAMETERIZED = 1;
	// answer type
	private static final int CORRECT = 1;
	private static final int INCORRECT = 0;
	
	// difficulty level
	private static final int DIFFICULTY_MAX = 6;
	private static final int DIFFICULTY_MIN = 1;
	private static final int DIFFICULTY_START = 3;
	
	
	private static PreparedStatement preparedStatement;
	private static PreparedStatement preparedStatement2;


    static Boolean attemptHomework(Connection connection, int exid, String uid, String cid) throws SQLException {
    	//System.out.println("debug attemptHomework");
    	// get exercise details
    	int ex_mode = 0;
    	preparedStatement = connection.prepareStatement(SqlQueries.SQL_GETEXERCISEDETAILS);
    	preparedStatement.setInt(1, exid);
    	ResultSet rs_exercise = preparedStatement.executeQuery();
    	
    	if (rs_exercise.next()) {
    		ex_mode = rs_exercise.getInt("EXERCISE_MODE");
    	}
    	// TODO maybe print exercise details, reuse ashiwood's code
    	
    	switch (ex_mode) {
    		case STANDARD:
    			attemptStandardHomework(connection, exid, uid, cid);
    			Menu.attemptHWSuccessMessage();
    			break;
    		case ADAPTIVE:
    			attemptAdaptiveHomework(connection, exid, uid, cid);
    			Menu.attemptHWSuccessMessage();
    			break;
    		default:
    			break;
    	}
    	
    	return true;
    }
    
    private static void attemptStandardHomework(Connection connection, int exid, String uid, String cid) throws SQLException {
    	//System.out.println("debug attemptStandardHomework");

    	// get questions, save id
    	ArrayList<Integer> questionIds = new ArrayList<Integer>();
    	//System.out.println("debug SQL_GETEXERCISEQUESTIONS");

    	preparedStatement = connection.prepareStatement(SqlQueries.SQL_GETEXERCISEQUESTIONS);
    	preparedStatement.setInt(1, exid);
    	ResultSet rs_questionIds = preparedStatement.executeQuery();
    	
    	while (rs_questionIds.next()) {
    		questionIds.add(rs_questionIds.getInt("QUESTION_ID"));
    	}
    	
    	// randomize question order
    	Collections.shuffle(questionIds);
    	//System.out.println("debug questionIds = " + questionIds.toString());
    	// print each question and ask for answer
    	int currentQuestionNum = 1;
    	
		for (int i = 0; i < questionIds.size(); i++) {
			int curQId = questionIds.get(i);
			int curQTopicId = 0;
			String curQText = "";
			int curQType = 0;
			int curQDifficultyLv = 0;
			String curQHint = "";
			String curQExp = "";
			
			//System.out.println("debug SQL_GETQUESTIONDETAILS");
			preparedStatement = connection.prepareStatement(SqlQueries.SQL_GETQUESTIONDETAILS);
	    	preparedStatement.setInt(1, curQId);
	    	ResultSet rs_question = preparedStatement.executeQuery();
	    	
	    	if (rs_question.next()) {
	    		curQTopicId = rs_question.getInt("TOPIC_ID");
				curQText = rs_question.getString("QUESTION_TEXT");
				curQType = rs_question.getInt("TYPE");
	    		curQDifficultyLv = rs_question.getInt("DIFFICULTY_LEVEL");
				curQHint = rs_question.getString("HINT");
				curQExp = rs_question.getString("DETAILED_EXPLANATION");
	    		
    			//System.out.println("debug curQType = " + curQType);

	    		if (curQType == CONCRETE) {
	    			//System.out.println("debug call showConcreteQuestion");
	    			showConcreteQuestion(connection, uid, cid, exid, curQId, curQText, currentQuestionNum);
	    			// get correct answers, only choose 1
	    			// get all incorrect answers in random order, choose 3

	    		} else if (curQType == PARAMETERIZED) {
	    			//System.out.println("debug call showParamterizedQuestion");
	    			showParamterizedQuestion(connection, uid, exid, cid, curQId, curQText, currentQuestionNum);
	    		} // end show question
	    		currentQuestionNum++;
	    	} // end of current question
		} // end of each question

    }
    private static void attemptAdaptiveHomework(Connection connection, int exid, String uid, String cid) throws SQLException {
    	//System.out.println("debug attemptAdaptiveHomework");
    	// initialize variables
    	int num_of_questions = 0;
    	int diff_max = 0;
    	int diff_min = 0;
    	int current_diff = DIFFICULTY_START;
    	ArrayList<Integer> questionIds = new ArrayList<Integer>();
    	ArrayList<Integer> questionDiffs = new ArrayList<Integer>();

    	ArrayList<Integer> usedQuestions = new ArrayList<Integer>();
    	
    	//System.out.println("debug SQL_GETEXERCISEDETAILS");
    	// get exercise details: num of questions, difficulty range
    	preparedStatement = connection.prepareStatement(SqlQueries.SQL_GETEXERCISEDETAILS);
    	preparedStatement.setInt(1, exid);
    	ResultSet rs_exercise = preparedStatement.executeQuery();
    	if (rs_exercise.next()) {
        	num_of_questions = rs_exercise.getInt("NUM_OF_QUESTIONS");
        	diff_max = rs_exercise.getInt("DIFFICULTY_LEVEL_MAX");
        	diff_min = rs_exercise.getInt("DIFFICULTY_LEVEL_MIN");
    	}
    	
    	// get the first question in topic with diff = DIFFICULTY_START
    	// saves qid into used question
  
    	// ---
    	// get list of questions under desired exercise_topic and diff range in random order
    	//System.out.println("debug SQL_GETADAPTIVEQUESTIONS");

    	preparedStatement = connection.prepareStatement(SqlQueries.SQL_GETADAPTIVEQUESTIONS);
    	preparedStatement.setInt(1, exid);
    	ResultSet rs_questionIds = preparedStatement.executeQuery();
    	
    	while (rs_questionIds.next()) {
    		questionIds.add(rs_questionIds.getInt("QUESTION_ID"));
    		questionDiffs.add(rs_questionIds.getInt("DIFFICULTY_LEVEL"));
    	}
    	
    	//System.out.println("debug questionIds = " + questionIds.toString());
    	// print each question and ask for answer
    	
		for (int i = 0; i < num_of_questions; i++) {
			//System.out.println("debug current_diff " + i + " = " + current_diff);

			// find the question that difficulty = current_diff
			int correctness = INCORRECT;
			int index = questionDiffs.indexOf(current_diff); // TODO index will be -1 if there is no quesiton found
			int curQId = questionIds.get(index);
			int curQTopicId = 0;
			String curQText = "";
			int curQType = 0;
			int curQDifficultyLv = 0;
			String curQHint = "";
			String curQExp = "";
			
			//System.out.println("debug SQL_GETQUESTIONDETAILS");
			preparedStatement = connection.prepareStatement(SqlQueries.SQL_GETQUESTIONDETAILS);
	    	preparedStatement.setInt(1, curQId);
	    	ResultSet rs_question = preparedStatement.executeQuery();
	    	
	    	if (rs_question.next()) {
	    		curQTopicId = rs_question.getInt("TOPIC_ID");
				curQText = rs_question.getString("QUESTION_TEXT");
				curQType = rs_question.getInt("TYPE");
	    		curQDifficultyLv = rs_question.getInt("DIFFICULTY_LEVEL");
				curQHint = rs_question.getString("HINT");
				curQExp = rs_question.getString("DETAILED_EXPLANATION");
	    		    			
    			// After we are done, remove the question from the quesitonIds and questionDiffs
    			questionDiffs.remove(index);
    			questionIds.remove(index);
    			
    			// after submit, check if the result is CORRECT or INCORRECT    			
	    		if (curQType == CONCRETE) {
	    			//System.out.println("debug call showConcreteQuestion");
	    			correctness = showConcreteQuestion(connection, uid, cid, exid, curQId, curQText, i+1);
	    			// get correct answers, only choose 1
	    			// get all incorrect answers in random order, choose 3

	    		} else if (curQType == PARAMETERIZED) {
	    			//System.out.println("debug call showParamterizedQuestion");
	    			correctness = showParamterizedQuestion(connection, uid, exid, cid, curQId, curQText, i+1);
	    		} // end show question
	    		
	    		// update difficulty based on the result
    			//System.out.println("debug correctness "+ correctness + " qid " + curQId + " current_diff " + current_diff + " curQType" + curQType);
	    		if(correctness == CORRECT) {
	    			current_diff++;
	    			if (current_diff > diff_max) { current_diff = diff_max; }
	    		} else if (correctness == INCORRECT) {
	    			current_diff--;
	    			if (current_diff < diff_min) { current_diff = diff_min; }
	    		}
	    	} // end of current question
	    	
		} // end of each question
    }
    private static int showParamterizedQuestion(Connection connection, String uid, int exid, String cid, int qid, String qtext, int num) throws SQLException {
		int correctness = INCORRECT; // initialize correctness for later return
		
    	//System.out.println("debug showParamterizedQuestion");
		//System.out.println("debug qid" + qid);

    	// choose a parameter_id in this question, print text
    	
    	// initialize answer variables 
    	Scanner scanner = new Scanner(System.in);
    	int pid = 0;
    	int aid = 0;
    	int atype = 0;
    	String atext = "";
    	ArrayList<String> param_texts = new ArrayList<String>();
    	ArrayList<Integer> answer_ids = new ArrayList<Integer>();
    	ArrayList<String> answer_texts = new ArrayList<String>();
    
		
    	// get a random parameter set and the answers linked to that
		// get a random parameter id first
    	//System.out.println("debug SQL_GETRANDOMPARAMETER");
    	preparedStatement = connection.prepareStatement(SqlQueries.SQL_GETRANDOMPARAMETER);
    	preparedStatement.setInt(1, qid);
    	ResultSet rs_pid = preparedStatement.executeQuery();
    	if(rs_pid.next()) {
    		pid = rs_pid.getInt("PARAMETER_ID"); // should be the same all the time
    	}

    	//System.out.println("debug SQL_GETPARAMETERIZEDANSWER");
    	preparedStatement = connection.prepareStatement(SqlQueries.SQL_GETPARAMETERIZEDANSWER);
    	preparedStatement.setInt(1, qid);
    	preparedStatement.setInt(2, pid);
    	System.out.println(preparedStatement.toString());
    	ResultSet rs_answers = preparedStatement.executeQuery();
    	
    	// loop through the answers, should be 1 correct, 3 incorrect
    	while (rs_answers.next()) {
    		//System.out.println("debug ANSWERS");

    		aid = rs_answers.getInt("PARAMETER_ANSWER_ID");
    		atype = rs_answers.getInt("TYPE");
    		atext = rs_answers.getString("ANSWER_TEXT");
    		
    		// save into array lists
    		answer_ids.add(aid);
    		answer_texts.add(atext);
    	}
		//System.out.println("debug answer_ids" + answer_ids.toString());
		//System.out.println("debug answer_texts" + answer_texts.toString());

    	// get the parameters from pid and qid
		//System.out.println("debug SQL_GETPARAMSBYPIDQID pid = "+ pid);

    	preparedStatement = connection.prepareStatement(SqlQueries.SQL_GETPARAMSBYPIDQID);
    	preparedStatement.setInt(1, qid);
    	preparedStatement.setInt(2, pid);
    	ResultSet rs_params = preparedStatement.executeQuery();
		
    	if (rs_params.next()) {
    		//System.out.println("debug PARAMS");

    		param_texts.add(rs_params.getString("PARAM_1"));
    		param_texts.add(rs_params.getString("PARAM_2"));
    		param_texts.add(rs_params.getString("PARAM_3"));
    		param_texts.add(rs_params.getString("PARAM_4"));
    		param_texts.add(rs_params.getString("PARAM_5"));
    		
    	}
    	
    	// parse parameters from the qtext and set the parameters
		Pattern p = Pattern.compile("\\<\\?\\>");
		int pcount = 0;
		Matcher m = p.matcher(qtext);

		while (m.find()) {
        	qtext = m.replaceFirst(param_texts.get(pcount));
        	m = p.matcher(qtext);
			pcount++; // found parameter count
        }
    	
    	// print the questions and options, request for answers
    	Menu.printQuestion(num, qtext, answer_texts);
    	Menu.askAnswerMessage();
    	
    	// take input and submit
    	int choice = 1;
    	while (choice != 0) {
    		choice = scanner.nextInt();
    		switch (choice) {
    		case 1:
    		case 2:
    		case 3:
    		case 4:
    			// valid option, get the answer_id to submit
    			correctness = submitAnswer(connection, uid, cid, exid, qid, PARAMETERIZED, pid, answer_ids.get(choice-1));
    			choice = 0; // exit input prompt whileloop
    			break;
    		default: //invalid option, retry
    			Menu.invalidAnswerMessage();
    			continue;
    		} // end switch
    	} // end while
    	return correctness;
    }
    
    private static int showConcreteQuestion(Connection connection, String uid, String cid, int exid, int qid, String qtext, int num) throws SQLException {
		//System.out.println("debug showConcreteQuestion");

    	// initialize variables 
		int correctness = INCORRECT; // initialize correctness for later return

		Scanner scanner = new Scanner(System.in);
    	int aid = 0;
    	int atype = 0;
    	String atext = "";
    	ArrayList<Integer> answer_ids = new ArrayList<Integer>();
    	ArrayList<String> answer_texts = new ArrayList<String>();
    	int pid = -1; // pid is really since we do not have paramter
    	
    	// execute sql query
		//System.out.println("debug SQL_GETCONCRETEANSWER");

    	preparedStatement = connection.prepareStatement(SqlQueries.SQL_GETCONCRETEANSWER);
    	preparedStatement.setInt(1, qid);
    	ResultSet rs_answers = preparedStatement.executeQuery();
    	
    	// loop through the answers, should be 1 correct, 3 incorrect
    	while (rs_answers.next()) {
    		aid = rs_answers.getInt("CONCRETE_ANSWER_ID");
    		atype = rs_answers.getInt("TYPE");
    		atext = rs_answers.getString("ANSWER_TEXT");
    		
    		// save into arraylists
    		answer_ids.add(aid);
    		answer_texts.add(atext);
    	}
    	
    	// print the questions and options, request for answers
    	Menu.printQuestion(num, qtext, answer_texts);
    	Menu.askAnswerMessage();
    	
    	int choice = 1;
    	while (choice != 0) {
    		choice = scanner.nextInt();
    		switch (choice) {
    		case 1:
    		case 2:
    		case 3:
    		case 4:
    			// valid option, get the answer_id to submit
    			correctness = submitAnswer(connection, uid, cid, exid, qid, CONCRETE, pid, answer_ids.get(choice-1));
    			choice = 0; // leave the while loop
    			break;
    		default: //invalid option, retry
    			Menu.invalidAnswerMessage();
    			continue;
    		} // end switch
    	} // end while
    	return correctness;
    }

    private static int submitAnswer(Connection connection, String uid, String cid, int exid, int qid, int qtype, int pid, int aid) throws SQLException {
		int correctness = INCORRECT; // initialize correctness for later return
    	//System.out.println("debug submitAnswer");
    	// for each submission, get the latest attempt of the student of this exercise
    	int stuAttemptCount = 0; // attempt rows, be 0 if no attempt
    	int stuMaxAttempt = 0; // max attempt submitted
    	
		//System.out.println("debug SQL_STUDENTMAXATTEMPTQUESTION");

		preparedStatement = connection.prepareStatement(SqlQueries.SQL_STUDENTMAXATTEMPTQUESTION);
    	preparedStatement.setInt(1, exid);
    	preparedStatement.setInt(2, qid);
    	
    	ResultSet rs_studentmaxattempt = preparedStatement.executeQuery();
    	if (rs_studentmaxattempt.next()) {
    		stuAttemptCount = rs_studentmaxattempt.getInt("ROWCNT");
    		//System.out.println("debug SQL_INSERTSUBMITS");

    		// create new attempt, set basic values
    		preparedStatement = connection.prepareStatement(SqlQueries.SQL_INSERTSUBMITS);

			preparedStatement.setString(1, uid); 
			preparedStatement.setString(2, cid); 
			preparedStatement.setInt(3, exid);
			preparedStatement.setInt(4, qid); 
			
			// 5: parameter_id, 6 : concrete_anser_id, 7: parameterized_answer_id
			// set parameter_id 
			if (qtype == CONCRETE) {
				// throw away pid, parameter_id = NULL
	    		//System.out.println("debug CONCRETEANS");

    			preparedStatement.setNull(5, java.sql.Types.INTEGER);
    			preparedStatement.setInt(6, aid);
    			preparedStatement.setNull(7, java.sql.Types.INTEGER);
    			
    			//check correctness
    			preparedStatement2 = connection.prepareStatement(SqlQueries.SQL_CHECKCONCRETEANSWER);
    	    	preparedStatement2.setInt(1, qid);
    	    	preparedStatement2.setInt(2, aid);
	    		//System.out.println("debug qid" + qid + "aid" + aid);
	    		//System.out.println("debug aid" + aid);

    	    	ResultSet rs_correctness = preparedStatement2.executeQuery();
    	    	if(rs_correctness.next()) {
    	    		correctness = rs_correctness.getInt("TYPE");
    	    	}
    			
			} else if (qtype == PARAMETERIZED) {
				// take pid
	    		//System.out.println("debug PARAMANS");

    			preparedStatement.setInt(5, pid);
    			preparedStatement.setNull(6, java.sql.Types.INTEGER);
    			preparedStatement.setInt(7, aid);
    			
    			//check correctness
    			preparedStatement2 = connection.prepareStatement(SqlQueries.SQL_CHECKPARAMETERIZEDANSWER);
    	    	preparedStatement2.setInt(1, qid);
    	    	preparedStatement2.setInt(2, pid);
    	    	preparedStatement2.setInt(3, aid);
    	    	//System.out.println("debug qid" + qid);
    	    	//System.out.println("debug pid" + pid);
	    		//System.out.println("debug aid" + aid);
    	    	ResultSet rs_correctness = preparedStatement2.executeQuery();
    	    	if(rs_correctness.next()) {
    	    		correctness = rs_correctness.getInt("TYPE");
    	    	}
			}
			
			// set attempt
    		if (stuAttemptCount == 0 ) { // student have never attempt the exercise
    	    	// 1. create a new attempt if no row, attempt = 1
    			preparedStatement.setInt(8, 1); // first attempt = 1
    		} else { // student has attempted the exercise
    			// check the max attempt number that is completed
    	    	// 2. create a new attempt of last attempt + 1
    			stuMaxAttempt = rs_studentmaxattempt.getInt("MAXATTEMPT");
    			preparedStatement.setInt(8, stuMaxAttempt+1);
    		}
    		// execute insert
        	preparedStatement.executeQuery();
        	Menu.submitAnswerSuccessMessage();
        	        	
    	}
    	return correctness;
    }
    
    public static void main(String args[]) {
    	

    }
}
