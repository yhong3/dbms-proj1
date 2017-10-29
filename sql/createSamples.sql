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
FOREIGN KEY (scoring_policy_id) REFERENCES SCORING_POLICY
);

CREATE TABLE TOPIC(
  topic_id INT,
  topic_name VARCHAR(1000) NOT NULL,
  course_id VARCHAR(50), 
  FOREIGN KEY (course_id) REFERENCES COURSE,
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
  FOREIGN KEY (topic_id) REFERENCES TOPIC,
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
FOREIGN KEY (course_id) REFERENCES COURSE
);

CREATE TABLE COURSE_STUDENT(
course_id VARCHAR(50),
user_id VARCHAR(50) NOT NULL, 
FOREIGN KEY (course_id) REFERENCES COURSE,
FOREIGN KEY (user_id) REFERENCES STUDENT
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
FOREIGN KEY (exercise_id) REFERENCES EXERCISE,
FOREIGN KEY (question_id) REFERENCES QUESTION
); 

CREATE TABLE USERID_PASSWORD(
user_id VARCHAR(50),
role INT,
password VARCHAR(64)
);

Insert into COURSE (COURSE_ID,COURSE_NAME,COURSE_START,COURSE_END) values ('CSC440','Database Systems',to_date('27-AUG-17','DD-MON-RR'),to_date('12-DEC-17','DD-MON-RR'));
Insert into COURSE (COURSE_ID,COURSE_NAME,COURSE_START,COURSE_END) values ('CSC540','Database Systems',to_date('25-AUG-17','DD-MON-RR'),to_date('10-DEC-17','DD-MON-RR'));
Insert into COURSE (COURSE_ID,COURSE_NAME,COURSE_START,COURSE_END) values ('CSC541','Advanced Data Structures',to_date('25-AUG-17','DD-MON-RR'),to_date('06-DEC-17','DD-MON-RR'));

Insert into PROFESSOR (USER_ID,PROFESSOR_NAME) values ('kogan','Kemafor Ogan');
Insert into PROFESSOR (USER_ID,PROFESSOR_NAME) values ('rchirkova','Rada Chirkova');
Insert into PROFESSOR (USER_ID,PROFESSOR_NAME) values ('chealey','Christipher Healey');

Insert into STUDENT (USER_ID,STUDENT_NAME,YEAR_ENROLLED,TYPE) values ('tregan','Tom Regan',2016,'Undergraduate');
Insert into STUDENT (USER_ID,STUDENT_NAME,YEAR_ENROLLED,TYPE) values ('jmick','Jenelle Mick',2015,'Grad');
Insert into STUDENT (USER_ID,STUDENT_NAME,YEAR_ENROLLED,TYPE) values ('mfisher','Michal Fisher',2013,'Undergrad');
Insert into STUDENT (USER_ID,STUDENT_NAME,YEAR_ENROLLED,TYPE) values ('jander','Joseph Anderson',2015,'Undergrad');
Insert into STUDENT (USER_ID,STUDENT_NAME,YEAR_ENROLLED,TYPE) values ('jharla','Jitendra Harlalka',2015,'Grad');
Insert into STUDENT (USER_ID,STUDENT_NAME,YEAR_ENROLLED,TYPE) values ('aneela','Aishwarya Neelakantan',2013,'Grad');
Insert into STUDENT (USER_ID,STUDENT_NAME,YEAR_ENROLLED,TYPE) values ('mjones','Mary Jones',2015,'Grad');
Insert into STUDENT (USER_ID,STUDENT_NAME,YEAR_ENROLLED,TYPE) values ('jmoyer','James Moyer',2013,'Grad');

Insert into TOPIC (TOPIC_ID,TOPIC_NAME,COURSE_ID) values (1,'ER Model','CSC540');
Insert into TOPIC (TOPIC_ID,TOPIC_NAME,COURSE_ID) values (2,'SQL','CSC540');
Insert into TOPIC (TOPIC_ID,TOPIC_NAME,COURSE_ID) values (3,'Storing Data:Disks and Files','CSC540');
Insert into TOPIC (TOPIC_ID,TOPIC_NAME,COURSE_ID) values (4,'Primary File Organization','CSC540');
Insert into TOPIC (TOPIC_ID,TOPIC_NAME,COURSE_ID) values (5,'Hashing Techniques','CSC540');
Insert into TOPIC (TOPIC_ID,TOPIC_NAME,COURSE_ID) values (6,'Binary Tree Structures','CSC540');
Insert into TOPIC (TOPIC_ID,TOPIC_NAME,COURSE_ID) values (7,'AVL Trees','CSC540');
Insert into TOPIC (TOPIC_ID,TOPIC_NAME,COURSE_ID) values (8,'Sequential File Organization','CSC540');
Insert into TOPIC (TOPIC_ID,TOPIC_NAME,COURSE_ID) values (9,'BinarySearch','CSC540');
Insert into TOPIC (TOPIC_ID,TOPIC_NAME,COURSE_ID) values (10,'Interpolation Search','CSC540');

Insert into SCORING_POLICY (SCORING_POLICY_ID,SCORING_POLICY_TYPE) values (1,'Latest attempt');
Insert into SCORING_POLICY (SCORING_POLICY_ID,SCORING_POLICY_TYPE) values (2,'Maximum score');
Insert into SCORING_POLICY (SCORING_POLICY_ID,SCORING_POLICY_TYPE) values (3,'Average score');

Insert into EXERCISE (EXERCISE_ID,EXERCISE_NAME,EXERCISE_START,EXERCISE_END,RETRIES_ALLOWED,NUM_OF_QUESTIONS,SCORING_POLICY_ID,CORRECT_ANSWER_POINTS,INCORRECT_ANSWER_PENALTY,DIFFICULTY_LEVEL_MIN,DIFFICULTY_LEVEL_MAX) values (1,'Homework 1 for CSC540',to_date('12-AUG-17','DD-MON-RR'),to_date('19-SEP-17','DD-MON-RR'),2,3,1,3,1,1,3);
Insert into EXERCISE (EXERCISE_ID,EXERCISE_NAME,EXERCISE_START,EXERCISE_END,RETRIES_ALLOWED,NUM_OF_QUESTIONS,SCORING_POLICY_ID,CORRECT_ANSWER_POINTS,INCORRECT_ANSWER_PENALTY,DIFFICULTY_LEVEL_MIN,DIFFICULTY_LEVEL_MAX) values (2,'Homework 2 for CSC540 Adaptive Homework',to_date('21-SEP-17','DD-MON-RR'),to_date('10-OCT-17','DD-MON-RR'),1,3,3,4,1,3,5);
Insert into EXERCISE (EXERCISE_ID,EXERCISE_NAME,EXERCISE_START,EXERCISE_END,RETRIES_ALLOWED,NUM_OF_QUESTIONS,SCORING_POLICY_ID,CORRECT_ANSWER_POINTS,INCORRECT_ANSWER_PENALTY,DIFFICULTY_LEVEL_MIN,DIFFICULTY_LEVEL_MAX) values (3,'Homework 3 for CSC540',to_date('12-OCT-17','DD-MON-RR'),to_date('30-OCT-17','DD-MON-RR'),-1,3,3,4,0,3,5);


Insert into COURSE_STAFF (COURSE_ID,USER_ID,ROLE) values ('CSC540','aneela',2);
Insert into COURSE_STAFF (COURSE_ID,USER_ID,ROLE) values ('CSC541','mjones',2);
Insert into COURSE_STAFF (COURSE_ID,USER_ID,ROLE) values ('CSC440','rchirkova',1);
Insert into COURSE_STAFF (COURSE_ID,USER_ID,ROLE) values ('CSC540','kogan',1);
Insert into COURSE_STAFF (COURSE_ID,USER_ID,ROLE) values ('CSC541','chealey',1);

Insert into COURSE_STUDENT (COURSE_ID,USER_ID) values ('CSC440','tregan');
Insert into COURSE_STUDENT (COURSE_ID,USER_ID) values ('CSC540','jmick');
Insert into COURSE_STUDENT (COURSE_ID,USER_ID) values ('CSC440','mfisher');
Insert into COURSE_STUDENT (COURSE_ID,USER_ID) values ('CSC440','jander');
Insert into COURSE_STUDENT (COURSE_ID,USER_ID) values ('CSC541','jharla');
Insert into COURSE_STUDENT (COURSE_ID,USER_ID) values ('CSC540','jmoyer');

Insert into USERID_PASSWORD (USER_ID,ROLE,PASSWORD) values ('tregan',3,'tregan');
Insert into USERID_PASSWORD (USER_ID,ROLE,PASSWORD) values ('jmick',3,'jmick');
Insert into USERID_PASSWORD (USER_ID,ROLE,PASSWORD) values ('mfisher',3,'mfisher');
Insert into USERID_PASSWORD (USER_ID,ROLE,PASSWORD) values ('jander',3,'jander');
Insert into USERID_PASSWORD (USER_ID,ROLE,PASSWORD) values ('jharla',3,'jharla');
Insert into USERID_PASSWORD (USER_ID,ROLE,PASSWORD) values ('aneela',2,'aneela');
Insert into USERID_PASSWORD (USER_ID,ROLE,PASSWORD) values ('mjones',2,'mjones');
Insert into USERID_PASSWORD (USER_ID,ROLE,PASSWORD) values ('jmoyer',3,'jmoyer');
Insert into USERID_PASSWORD (USER_ID,ROLE,PASSWORD) values ('kogan',1,'kogan');
Insert into USERID_PASSWORD (USER_ID,ROLE,PASSWORD) values ('rchirkova',1,'rchirkova');
Insert into USERID_PASSWORD (USER_ID,ROLE,PASSWORD) values ('chealey',1,'chealey');

