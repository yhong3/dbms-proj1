CREATE TABLE COURSE(
course_id VARCHAR(50),
course_name VARCHAR(50) NOT NULL,
course_start DATE NOT NULL,
course_end DATE NOT NULL,
PRIMARY KEY (course_id) 
);


CREATE TABLE PROFESSOR(
user_id VARCHAR(50) NOT NULL,
professor_name VARCHAR(50) NOT NULL,
PRIMARY KEY (user_id)
);

CREATE TABLE STUDENT(
user_id VARCHAR(50) NOT NULL,
student_name VARCHAR(50) NOT NULL,
year_enrolled INT NOT NULL,
type VARCHAR(20) NOT NULL,
PRIMARY KEY (user_id)
);

CREATE TABLE SCORING_POLICY(
scoring_policy_id INT NOT NULL,
scoring_policy_type VARCHAR(50) NOT NULL,
PRIMARY KEY (scoring_policy_id) 
);

CREATE TABLE EXERCISE(
exercise_id INT,
exercise_name VARCHAR(50) NOT NULL,
exercise_start DATE NOT NULL,
exercise_end DATE NOT NULL,
retries_allowed INT NOT NULL,
num_of_questions INT NOT NULL,
scoring_policy_id INT NOT NULL,
correct_answer_points INT NOT NULL,
incorrect_answer_penalty INT NOT NULL,
difficulty_level_min INT NOT NULL,
difficulty_level_max INT NOT NULL,
PRIMARY KEY (exercise_id),
FOREIGN KEY (scoring_policy_id) REFERENCES SCORING_POLICY ON DELETE CASCADE
);

CREATE TABLE TOPIC(
  topic_id INT,
  topic_name VARCHAR(1000) NOT NULL,
  course_id VARCHAR(50), 
  FOREIGN KEY (course_id) REFERENCES COURSE ON DELETE CASCADE,
  PRIMARY KEY (topic_id)
);

CREATE TABLE QUESTION(
  question_id INT,
  topic_id INT,
  difficulty_level number(1) NOT NULL,
  hint VARCHAR(1000) NOT NULL,
  explanation VARCHAR(1000) NOT NULL,
  question_text VARCHAR(1000) NOT NULL,
  questtion_variable char,
  FOREIGN KEY (topic_id) REFERENCES TOPIC ON DELETE CASCADE,
  PRIMARY KEY (question_id)
);
CREATE TABLE ANSWER(
  answer_id INT,
  question_id INT,
  type VARCHAR(50) NOT NULL,
  answer_text VARCHAR(1000) NOT NULL,
  short_explanation VARCHAR(1000) NOT NULL,
  PRIMARY KEY (answer_id, question_id)
);

CREATE TABLE COURSE_STAFF(
course_id VARCHAR(50),
user_id VARCHAR(50) NOT NULL, 
role INT,
FOREIGN KEY (course_id) REFERENCES COURSE ON DELETE CASCADE
);

CREATE TABLE COURSE_STUDENT(
course_id VARCHAR(50),
user_id VARCHAR(50) NOT NULL, 
FOREIGN KEY (course_id) REFERENCES COURSE ON DELETE CASCADE,
FOREIGN KEY (user_id) REFERENCES STUDENT ON DELETE CASCADE
);

CREATE TABLE STAFF_EXERCISE(
  exercise_id INT,
  staff_id INT,
  exercise_mode VARCHAR(50) NOT NULL,
  PRIMARY KEY (exercise_id, staff_id)
);

CREATE TABLE EXERCISE_QUESTION(
exercise_id INT,
question_id INT, 
FOREIGN KEY (exercise_id) REFERENCES EXERCISE ON DELETE CASCADE,
FOREIGN KEY (question_id) REFERENCES QUESTION ON DELETE CASCADE
); 

CREATE TABLE USERID_PASSWORD(
user_id VARCHAR(50),
role INT,
password VARCHAR(64)
);

