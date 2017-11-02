CREATE TABLE COURSE(
course_id VARCHAR(50),
course_name VARCHAR(50) NOT NULL,
course_start DATE NOT NULL,
course_end DATE NOT NULL,
course_level VARCHAR(50) NOT NULL,
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
exercise_id INT NOT NULL,
course_id varchar(50),
exercise_name VARCHAR(50) NOT NULL,
exercise_start DATE NOT NULL,
exercise_end DATE NOT NULL,
retries_allowed INT NOT NULL,
num_of_questions INT NOT NULL,
scoring_policy_id INT NOT NULL,
correct_answer_poINTs INT NOT NULL,
incorrect_answer_penalty INT NOT NULL,
difficulty_level_min INT NOT NULL,
difficulty_level_max INT NOT NULL,
PRIMARY KEY (exercise_id),
FOREIGN KEY (scoring_policy_id) REFERENCES SCORING_POLICY ON DELETE CASCADE,
FOREIGN KEY (course_id) REFERENCES COURSE ON DELETE CASCADE
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
question_text VARCHAR(150) NOT NULL,
type INT NOT NULL,
difficulty_level number(1) NOT NULL,
hINT VARCHAR(50) NOT NULL,
detailed_explanation VARCHAR(150) NOT NULL,
FOREIGN KEY (topic_id) REFERENCES TOPIC ON DELETE CASCADE,
PRIMARY KEY (question_id)
);

CREATE TABLE CONCRETE_ANSWER(
answer_id INT,
question_id INT,
type INT NOT NULL,
answer_text VARCHAR(50) NOT NULL,
short_explanation VARCHAR(50) NOT NULL,
FOREIGN KEY (question_id) REFERENCES QUESTION ON DELETE CASCADE,
PRIMARY KEY (answer_id));

CREATE TABLE PARAMETER_ANSWER(
parameter_id INT,
answer_id INT,
question_id INT,
param_1 VARCHAR(10),
param_2 VARCHAR(10),
param_3 VARCHAR(10),
param_4 VARCHAR(10),
param_5 VARCHAR(10),
answer_text VARCHAR(50) NOT NULL,
short_explanation VARCHAR(50) NOT NULL,
type INT NOT NULL,
FOREIGN KEY (question_id) REFERENCES QUESTION ON DELETE CASCADE,
PRIMARY KEY (parameter_id, answer_id)
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


----------------
-- pending, not tested yet
----------------

CREATE TABLE SUBMITS(
user_id VARCHAR(50) NOT NULL,
exercise_id INT NOT NULL,
question_id INT NOT NULL,
parameter_id INT,
answer_id INT,
attempt INT NOT NULL,
submits_start DATE NOT NULL,
submits_end DATE,
FOREIGN KEY (user_id) REFERENCES STUDENT ON DELETE CASCADE,
FOREIGN KEY (exercise_id) REFERENCES EXERCISE ON DELETE CASCADE,
FOREIGN KEY (question_id) REFERENCES QUESTION ON DELETE CASCADE,
FOREIGN KEY (parameter_id, answer_id) REFERENCES PARAMETER_ANSWER ON DELETE CASCADE
);

------------------
-- INSERT
--------------------
Insert into COURSE (COURSE_ID,COURSE_NAME,COURSE_START,COURSE_END,COURSE_LEVEL) values ('CSC440','Database Systems',to_date('27-AUG-17','DD-MON-RR'),to_date('12-DEC-17','DD-MON-RR'),'Undergrad');		
Insert into COURSE (COURSE_ID,COURSE_NAME,COURSE_START,COURSE_END,COURSE_LEVEL) values ('CSC540','Database Systems',to_date('25-AUG-17','DD-MON-RR'),to_date('10-DEC-17','DD-MON-RR'),'Grad');		
Insert into COURSE (COURSE_ID,COURSE_NAME,COURSE_START,COURSE_END,COURSE_LEVEL) values ('CSC541','Advanced Data Structures',to_date('25-AUG-17','DD-MON-RR'),to_date('06-DEC-17','DD-MON-RR'),'Grad');		
 
Insert into PROFESSOR (USER_ID,PROFESSOR_NAME) values ('kogan','Kemafor Ogan');		
Insert into PROFESSOR (USER_ID,PROFESSOR_NAME) values ('rchirkova','Rada Chirkova');		
Insert into PROFESSOR (USER_ID,PROFESSOR_NAME) values ('chealey','Christipher Healey');		
 		
Insert into STUDENT (USER_ID,STUDENT_NAME,YEAR_ENROLLED,TYPE) values ('tregan','Tom Regan',2016,'Undergrad');		
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
 		
Insert into EXERCISE (EXERCISE_ID,COURSE_ID,EXERCISE_NAME,EXERCISE_START,EXERCISE_END,RETRIES_ALLOWED,NUM_OF_QUESTIONS,SCORING_POLICY_ID,CORRECT_ANSWER_POINTS,INCORRECT_ANSWER_PENALTY,DIFFICULTY_LEVEL_MIN,DIFFICULTY_LEVEL_MAX) values (1,'CSC540','Homework 1 for CSC540',to_date('12-AUG-17','DD-MON-RR'),to_date('19-SEP-17','DD-MON-RR'),2,3,1,3,1,1,3);		
Insert into EXERCISE (EXERCISE_ID,COURSE_ID,EXERCISE_NAME,EXERCISE_START,EXERCISE_END,RETRIES_ALLOWED,NUM_OF_QUESTIONS,SCORING_POLICY_ID,CORRECT_ANSWER_POINTS,INCORRECT_ANSWER_PENALTY,DIFFICULTY_LEVEL_MIN,DIFFICULTY_LEVEL_MAX) values (2,'CSC540','Homework 2 for CSC540 Adaptive Homework',to_date('21-SEP-17','DD-MON-RR'),to_date('10-OCT-17','DD-MON-RR'),1,3,3,4,1,3,5);		
Insert into EXERCISE (EXERCISE_ID,COURSE_ID,EXERCISE_NAME,EXERCISE_START,EXERCISE_END,RETRIES_ALLOWED,NUM_OF_QUESTIONS,SCORING_POLICY_ID,CORRECT_ANSWER_POINTS,INCORRECT_ANSWER_PENALTY,DIFFICULTY_LEVEL_MIN,DIFFICULTY_LEVEL_MAX) values (3,'CSC540','Homework 3 for CSC540',to_date('12-OCT-17','DD-MON-RR'),to_date('30-OCT-17','DD-MON-RR'),-1,3,3,4,0,3,5);		
 		
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


Insert into QUESTION (QUESTION_ID,TOPIC_ID,QUESTION_TEXT,TYPE,DIFFICULTY_LEVEL,HINT,DETAILED_EXPLANATION) values (1,1,'Question 1?',0,2,'Hint text Q1','detailed Explanation Q1');
Insert into QUESTION (QUESTION_ID,TOPIC_ID,QUESTION_TEXT,TYPE,DIFFICULTY_LEVEL,HINT,DETAILED_EXPLANATION) values (2,3,'Question 2?',0,3,'Hint text Q2','detailed Explanation Q2');
Insert into QUESTION (QUESTION_ID,TOPIC_ID,QUESTION_TEXT,TYPE,DIFFICULTY_LEVEL,HINT,DETAILED_EXPLANATION) values (3,4,'Consider a disk with a <?>, <?>, <?>, <?>, <?>.',1,2,'Hint text Q3','detailed Explanation Q3');


Insert into EXERCISE_QUESTION (EXERCISE_ID,QUESTION_ID) values (1,1);
Insert into EXERCISE_QUESTION (EXERCISE_ID,QUESTION_ID) values (1,2);
Insert into EXERCISE_QUESTION (EXERCISE_ID,QUESTION_ID) values (1,3);
Insert into EXERCISE_QUESTION (EXERCISE_ID,QUESTION_ID) values (3,1);
Insert into EXERCISE_QUESTION (EXERCISE_ID,QUESTION_ID) values (3,2);
Insert into EXERCISE_QUESTION (EXERCISE_ID,QUESTION_ID) values (3,3);



Insert into CONCRETE_ANSWER (ANSWER_ID,QUESTION_ID,TYPE,ANSWER_TEXT,SHORT_EXPLANATION) values (1,1,1,'q1 correct ans 1','explanation for correct ans 1 q1');
Insert into CONCRETE_ANSWER (ANSWER_ID,QUESTION_ID,TYPE,ANSWER_TEXT,SHORT_EXPLANATION) values (2,1,1,'q1 correct ans 2','explanation for correct ans 2 q1');
Insert into CONCRETE_ANSWER (ANSWER_ID,QUESTION_ID,TYPE,ANSWER_TEXT,SHORT_EXPLANATION) values (3,1,0,'q1 correct ans 3','explanation for correct ans 3 q1');
Insert into CONCRETE_ANSWER (ANSWER_ID,QUESTION_ID,TYPE,ANSWER_TEXT,SHORT_EXPLANATION) values (4,1,0,'q1 incorrect ans 4','explanation for incorrect ans 4 q1');
Insert into CONCRETE_ANSWER (ANSWER_ID,QUESTION_ID,TYPE,ANSWER_TEXT,SHORT_EXPLANATION) values (5,1,0,'q1 incorrect ans 5','explanation for incorrect ans 5 q1');
Insert into CONCRETE_ANSWER (ANSWER_ID,QUESTION_ID,TYPE,ANSWER_TEXT,SHORT_EXPLANATION) values (6,1,0,'q1 incorrect ans 6','explanation for incorrect ans 6 q1');
Insert into CONCRETE_ANSWER (ANSWER_ID,QUESTION_ID,TYPE,ANSWER_TEXT,SHORT_EXPLANATION) values (7,2,1,'q2 correct ans 1','explanation for correct ans 1 q2');
Insert into CONCRETE_ANSWER (ANSWER_ID,QUESTION_ID,TYPE,ANSWER_TEXT,SHORT_EXPLANATION) values (8,2,1,'q2 correct ans 2','explanation for correct ans 2 q2');
Insert into CONCRETE_ANSWER (ANSWER_ID,QUESTION_ID,TYPE,ANSWER_TEXT,SHORT_EXPLANATION) values (9,2,1,'q2 correct ans 3','explanation for correct ans 3 q2');
Insert into CONCRETE_ANSWER (ANSWER_ID,QUESTION_ID,TYPE,ANSWER_TEXT,SHORT_EXPLANATION) values (10,2,0,'q2 incorrect ans 4','explanation for incorrect ans 4 q2');
Insert into CONCRETE_ANSWER (ANSWER_ID,QUESTION_ID,TYPE,ANSWER_TEXT,SHORT_EXPLANATION) values (11,2,0,'q2 incorrect ans 5','explanation for incorrect ans 5 q2');
Insert into CONCRETE_ANSWER (ANSWER_ID,QUESTION_ID,TYPE,ANSWER_TEXT,SHORT_EXPLANATION) values (12,2,0,'q2 incorrect ans 6','explanation for incorrect ans 6 q2');



Insert into PARAMETER_ANSWER (PARAMETER_ID,ANSWER_ID,QUESTION_ID,PARAM_1,PARAM_2,PARAM_3,PARAM_4,PARAM_5,ANSWER_TEXT,SHORT_EXPLANATION,TYPE) values (1,1,3,'512','2000','50','5','10','Correct ans 1v1,','""',1);
Insert into PARAMETER_ANSWER (PARAMETER_ID,ANSWER_ID,QUESTION_ID,PARAM_1,PARAM_2,PARAM_3,PARAM_4,PARAM_5,ANSWER_TEXT,SHORT_EXPLANATION,TYPE) values (1,2,3,'512','2000','50','5','10','Correct ans 2v1,','""',1);
Insert into PARAMETER_ANSWER (PARAMETER_ID,ANSWER_ID,QUESTION_ID,PARAM_1,PARAM_2,PARAM_3,PARAM_4,PARAM_5,ANSWER_TEXT,SHORT_EXPLANATION,TYPE) values (1,3,3,'512','2000','50','5','10','Correct ans 3v1,','""',1);
Insert into PARAMETER_ANSWER (PARAMETER_ID,ANSWER_ID,QUESTION_ID,PARAM_1,PARAM_2,PARAM_3,PARAM_4,PARAM_5,ANSWER_TEXT,SHORT_EXPLANATION,TYPE) values (1,4,3,'512','2000','50','5','10','Incorrect ans 4v1,','short explanation 4',0);
Insert into PARAMETER_ANSWER (PARAMETER_ID,ANSWER_ID,QUESTION_ID,PARAM_1,PARAM_2,PARAM_3,PARAM_4,PARAM_5,ANSWER_TEXT,SHORT_EXPLANATION,TYPE) values (1,5,3,'512','2000','50','5','10','Incorrect ans 5v1,','short explanation 5',0);
Insert into PARAMETER_ANSWER (PARAMETER_ID,ANSWER_ID,QUESTION_ID,PARAM_1,PARAM_2,PARAM_3,PARAM_4,PARAM_5,ANSWER_TEXT,SHORT_EXPLANATION,TYPE) values (1,6,3,'512','2000','50','5','10','Incorrect ans 6v1,','short explanation 6',0);
Insert into PARAMETER_ANSWER (PARAMETER_ID,ANSWER_ID,QUESTION_ID,PARAM_1,PARAM_2,PARAM_3,PARAM_4,PARAM_5,ANSWER_TEXT,SHORT_EXPLANATION,TYPE) values (1,7,3,'512','2000','50','5','10','Incorrect ans 7v1,','short explanation 7',0);
Insert into PARAMETER_ANSWER (PARAMETER_ID,ANSWER_ID,QUESTION_ID,PARAM_1,PARAM_2,PARAM_3,PARAM_4,PARAM_5,ANSWER_TEXT,SHORT_EXPLANATION,TYPE) values (1,8,3,'512','2000','50','5','10','Incorrect ans 8v1,','short explanation 8',0);
Insert into PARAMETER_ANSWER (PARAMETER_ID,ANSWER_ID,QUESTION_ID,PARAM_1,PARAM_2,PARAM_3,PARAM_4,PARAM_5,ANSWER_TEXT,SHORT_EXPLANATION,TYPE) values (2,9,3,'256','1000','100','10','20','Correct ans 1v2,','""',1);
Insert into PARAMETER_ANSWER (PARAMETER_ID,ANSWER_ID,QUESTION_ID,PARAM_1,PARAM_2,PARAM_3,PARAM_4,PARAM_5,ANSWER_TEXT,SHORT_EXPLANATION,TYPE) values (2,10,3,'256','1000','100','10','20','Correct ans 2v2,','""',1);
Insert into PARAMETER_ANSWER (PARAMETER_ID,ANSWER_ID,QUESTION_ID,PARAM_1,PARAM_2,PARAM_3,PARAM_4,PARAM_5,ANSWER_TEXT,SHORT_EXPLANATION,TYPE) values (2,11,3,'256','1000','100','10','20','Correct ans 3v2,','""',1);
Insert into PARAMETER_ANSWER (PARAMETER_ID,ANSWER_ID,QUESTION_ID,PARAM_1,PARAM_2,PARAM_3,PARAM_4,PARAM_5,ANSWER_TEXT,SHORT_EXPLANATION,TYPE) values (2,12,3,'256','1000','100','10','20','Incorrect ans 4v2,','short explanation 4v1',0);
Insert into PARAMETER_ANSWER (PARAMETER_ID,ANSWER_ID,QUESTION_ID,PARAM_1,PARAM_2,PARAM_3,PARAM_4,PARAM_5,ANSWER_TEXT,SHORT_EXPLANATION,TYPE) values (2,13,3,'256','1000','100','10','20','Incorrect ans 5v2,','short explanation 5v1',0);
Insert into PARAMETER_ANSWER (PARAMETER_ID,ANSWER_ID,QUESTION_ID,PARAM_1,PARAM_2,PARAM_3,PARAM_4,PARAM_5,ANSWER_TEXT,SHORT_EXPLANATION,TYPE) values (2,14,3,'256','1000','100','10','20','Incorrect ans 6v2,','short explanation 6v1',0);
Insert into PARAMETER_ANSWER (PARAMETER_ID,ANSWER_ID,QUESTION_ID,PARAM_1,PARAM_2,PARAM_3,PARAM_4,PARAM_5,ANSWER_TEXT,SHORT_EXPLANATION,TYPE) values (2,15,3,'256','1000','100','10','20','Incorrect ans 7v2,','short explanation 7v1',0);
Insert into PARAMETER_ANSWER (PARAMETER_ID,ANSWER_ID,QUESTION_ID,PARAM_1,PARAM_2,PARAM_3,PARAM_4,PARAM_5,ANSWER_TEXT,SHORT_EXPLANATION,TYPE) values (2,16,3,'256','1000','100','10','20','Incorrect ans 8v2,','short explanation 8v1',0);


