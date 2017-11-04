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
exercise_mode INT NOT NULL,
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
concrete_answer_id INT,
question_id INT,
type INT NOT NULL,
answer_text VARCHAR(50) NOT NULL,
short_explanation VARCHAR(50) NOT NULL,
FOREIGN KEY (question_id) REFERENCES QUESTION ON DELETE CASCADE,
PRIMARY KEY (concrete_answer_id)
);

CREATE TABLE PARAMETER_ANSWER(
parameter_id INT,
parameter_answer_id INT,
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
PRIMARY KEY (parameter_id, parameter_answer_id)
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


CREATE TABLE SUBMITS(
  user_id VARCHAR(50) NOT NULL,
  course_id VARCHAR(50) NOT NULL,
  exercise_id INT NOT NULL,
  question_id INT NOT NULL,
  parameter_id INT,
  concrete_answer_id INT,
  parameter_answer_id INT,
  attempt INT NOT NULL,
  submit_time DATE NOT NULL,
  FOREIGN KEY (course_id) REFERENCES COURSE ON DELETE CASCADE,
  FOREIGN KEY (user_id) REFERENCES STUDENT ON DELETE CASCADE,
  FOREIGN KEY (exercise_id) REFERENCES EXERCISE ON DELETE CASCADE,
  FOREIGN KEY (question_id) REFERENCES QUESTION ON DELETE CASCADE,
  FOREIGN KEY (parameter_id, parameter_answer_id) REFERENCES PARAMETER_ANSWER ON DELETE CASCADE,
  FOREIGN KEY (concrete_answer_id) REFERENCES CONCRETE_ANSWER ON DELETE CASCADE
);

CREATE TABLE EXERCISE_TOPIC(
exercise_id INT NOT NULL,
topic_id INT NOT NULL,
FOREIGN KEY (exercise_id) REFERENCES EXERCISE ON DELETE CASCADE,
FOREIGN KEY (topic_id) REFERENCES TOPIC ON DELETE CASCADE
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
Insert into TOPIC (TOPIC_ID,TOPIC_NAME,COURSE_ID) values (11,'FSS','CSC540');

Insert into SCORING_POLICY (SCORING_POLICY_ID,SCORING_POLICY_TYPE) values (1,'Latest attempt');		
Insert into SCORING_POLICY (SCORING_POLICY_ID,SCORING_POLICY_TYPE) values (2,'Maximum score');		
Insert into SCORING_POLICY (SCORING_POLICY_ID,SCORING_POLICY_TYPE) values (3,'Average score');		
 		
Insert into EXERCISE (EXERCISE_ID,COURSE_ID,EXERCISE_NAME,EXERCISE_START,EXERCISE_END,RETRIES_ALLOWED,NUM_OF_QUESTIONS,SCORING_POLICY_ID,CORRECT_ANSWER_POINTS,INCORRECT_ANSWER_PENALTY,DIFFICULTY_LEVEL_MIN,DIFFICULTY_LEVEL_MAX,EXERCISE_MODE) values (1,'CSC540','Homework 1 for CSC540',to_date('12-AUG-17','DD-MON-RR'),to_date('19-SEP-17','DD-MON-RR'),2,3,1,3,1,1,3,0);		
Insert into EXERCISE (EXERCISE_ID,COURSE_ID,EXERCISE_NAME,EXERCISE_START,EXERCISE_END,RETRIES_ALLOWED,NUM_OF_QUESTIONS,SCORING_POLICY_ID,CORRECT_ANSWER_POINTS,INCORRECT_ANSWER_PENALTY,DIFFICULTY_LEVEL_MIN,DIFFICULTY_LEVEL_MAX,EXERCISE_MODE) values (2,'CSC540','Homework 2 for CSC540 Adaptive Homework',to_date('21-SEP-17','DD-MON-RR'),to_date('10-DEC-17','DD-MON-RR'),1,3,3,4,1,3,5,1);		
Insert into EXERCISE (EXERCISE_ID,COURSE_ID,EXERCISE_NAME,EXERCISE_START,EXERCISE_END,RETRIES_ALLOWED,NUM_OF_QUESTIONS,SCORING_POLICY_ID,CORRECT_ANSWER_POINTS,INCORRECT_ANSWER_PENALTY,DIFFICULTY_LEVEL_MIN,DIFFICULTY_LEVEL_MAX,EXERCISE_MODE) values (3,'CSC540','Homework 3 for CSC540',to_date('12-OCT-17','DD-MON-RR'),to_date('30-NOV-17','DD-MON-RR'),-1,3,3,4,0,3,5,0);		
 		
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
Insert into QUESTION (QUESTION_ID,TOPIC_ID,QUESTION_TEXT,TYPE,DIFFICULTY_LEVEL,HINT,DETAILED_EXPLANATION) values (4,4,'For <?>, <?>, <?>',1,4,'Hint text Q4','detailed Explanation Q4');
Insert into QUESTION (QUESTION_ID,TOPIC_ID,QUESTION_TEXT,TYPE,DIFFICULTY_LEVEL,HINT,DETAILED_EXPLANATION) values (5,1,'what do you think about <?> for <?>?',1,5,'Hint text Q5','detailed Explanation Q5');
Insert into QUESTION (QUESTION_ID,TOPIC_ID,QUESTION_TEXT,TYPE,DIFFICULTY_LEVEL,HINT,DETAILED_EXPLANATION) values (6,3,'Question 6?',0,3,'Hint text Q6','detailed Explanation Q6');
Insert into QUESTION (QUESTION_ID,TOPIC_ID,QUESTION_TEXT,TYPE,DIFFICULTY_LEVEL,HINT,DETAILED_EXPLANATION) values (7,1,'Question 7?',0,1,'Hint text Q7','detailed Explanation Q7');
Insert into QUESTION (QUESTION_ID,TOPIC_ID,QUESTION_TEXT,TYPE,DIFFICULTY_LEVEL,HINT,DETAILED_EXPLANATION) values (8,11,'Question 8?',0,3,'Hint text Q8','detailed Explanation Q8');
Insert into QUESTION (QUESTION_ID,TOPIC_ID,QUESTION_TEXT,TYPE,DIFFICULTY_LEVEL,HINT,DETAILED_EXPLANATION) values (9,11,'Question 9?',0,6,'Hint text Q9','detailed Explanation Q9');
Insert into QUESTION (QUESTION_ID,TOPIC_ID,QUESTION_TEXT,TYPE,DIFFICULTY_LEVEL,HINT,DETAILED_EXPLANATION) values (10,3,'Question 10?',0,1,'Hint text Q10','detailed Explanation Q10');


Insert into EXERCISE_QUESTION (EXERCISE_ID,QUESTION_ID) values (1,1);
Insert into EXERCISE_QUESTION (EXERCISE_ID,QUESTION_ID) values (1,2);
Insert into EXERCISE_QUESTION (EXERCISE_ID,QUESTION_ID) values (1,3);
Insert into EXERCISE_QUESTION (EXERCISE_ID,QUESTION_ID) values (3,1);
Insert into EXERCISE_QUESTION (EXERCISE_ID,QUESTION_ID) values (3,2);
Insert into EXERCISE_QUESTION (EXERCISE_ID,QUESTION_ID) values (3,3);

Insert into CONCRETE_ANSWER (CONCRETE_ANSWER_ID,QUESTION_ID,TYPE,ANSWER_TEXT,SHORT_EXPLANATION) values (1,1,1,'Correct ans 2,','""');
Insert into CONCRETE_ANSWER (CONCRETE_ANSWER_ID,QUESTION_ID,TYPE,ANSWER_TEXT,SHORT_EXPLANATION) values (2,1,0,'Incorrect ans, 3','short explanation 3');
Insert into CONCRETE_ANSWER (CONCRETE_ANSWER_ID,QUESTION_ID,TYPE,ANSWER_TEXT,SHORT_EXPLANATION) values (3,1,0,'Incorrect ans, 4','short explanation 4');
Insert into CONCRETE_ANSWER (CONCRETE_ANSWER_ID,QUESTION_ID,TYPE,ANSWER_TEXT,SHORT_EXPLANATION) values (4,1,0,'Incorrect ans, 5','short explanation 5');
Insert into CONCRETE_ANSWER (CONCRETE_ANSWER_ID,QUESTION_ID,TYPE,ANSWER_TEXT,SHORT_EXPLANATION) values (5,2,1,'Correct ans 1,','""');
Insert into CONCRETE_ANSWER (CONCRETE_ANSWER_ID,QUESTION_ID,TYPE,ANSWER_TEXT,SHORT_EXPLANATION) values (6,2,0,'Incorrect ans, 4','short explanation 4');
Insert into CONCRETE_ANSWER (CONCRETE_ANSWER_ID,QUESTION_ID,TYPE,ANSWER_TEXT,SHORT_EXPLANATION) values (7,2,0,'Incorrect ans, 5','short explanation 5');
Insert into CONCRETE_ANSWER (CONCRETE_ANSWER_ID,QUESTION_ID,TYPE,ANSWER_TEXT,SHORT_EXPLANATION) values (8,2,0,'Incorrect ans, 6','short explanation 6');
Insert into CONCRETE_ANSWER (CONCRETE_ANSWER_ID,QUESTION_ID,TYPE,ANSWER_TEXT,SHORT_EXPLANATION) values (9,6,1,'Correct ans 2,','""');
Insert into CONCRETE_ANSWER (CONCRETE_ANSWER_ID,QUESTION_ID,TYPE,ANSWER_TEXT,SHORT_EXPLANATION) values (10,6,0,'Incorrect ans, 3','short explanation 3');
Insert into CONCRETE_ANSWER (CONCRETE_ANSWER_ID,QUESTION_ID,TYPE,ANSWER_TEXT,SHORT_EXPLANATION) values (11,6,0,'Incorrect ans, 4','short explanation 4');
Insert into CONCRETE_ANSWER (CONCRETE_ANSWER_ID,QUESTION_ID,TYPE,ANSWER_TEXT,SHORT_EXPLANATION) values (12,6,0,'Incorrect ans, 5','short explanation 5');
Insert into CONCRETE_ANSWER (CONCRETE_ANSWER_ID,QUESTION_ID,TYPE,ANSWER_TEXT,SHORT_EXPLANATION) values (13,7,1,'Correct ans 1,','""');
Insert into CONCRETE_ANSWER (CONCRETE_ANSWER_ID,QUESTION_ID,TYPE,ANSWER_TEXT,SHORT_EXPLANATION) values (14,7,0,'Incorrect ans, 3','short explanation 3');
Insert into CONCRETE_ANSWER (CONCRETE_ANSWER_ID,QUESTION_ID,TYPE,ANSWER_TEXT,SHORT_EXPLANATION) values (15,7,0,'Incorrect ans, 4','short explanation 4');
Insert into CONCRETE_ANSWER (CONCRETE_ANSWER_ID,QUESTION_ID,TYPE,ANSWER_TEXT,SHORT_EXPLANATION) values (16,7,0,'Incorrect ans, 5','short explanation 5');
Insert into CONCRETE_ANSWER (CONCRETE_ANSWER_ID,QUESTION_ID,TYPE,ANSWER_TEXT,SHORT_EXPLANATION) values (17,8,1,'Correct ans 1,','""');
Insert into CONCRETE_ANSWER (CONCRETE_ANSWER_ID,QUESTION_ID,TYPE,ANSWER_TEXT,SHORT_EXPLANATION) values (18,8,0,'Incorrect ans, 3','short explanation 3');
Insert into CONCRETE_ANSWER (CONCRETE_ANSWER_ID,QUESTION_ID,TYPE,ANSWER_TEXT,SHORT_EXPLANATION) values (19,8,0,'Incorrect ans, 4','short explanation 4');
Insert into CONCRETE_ANSWER (CONCRETE_ANSWER_ID,QUESTION_ID,TYPE,ANSWER_TEXT,SHORT_EXPLANATION) values (20,8,0,'Incorrect ans, 5','short explanation 5');
Insert into CONCRETE_ANSWER (CONCRETE_ANSWER_ID,QUESTION_ID,TYPE,ANSWER_TEXT,SHORT_EXPLANATION) values (21,9,1,'Correct ans 2,','""');
Insert into CONCRETE_ANSWER (CONCRETE_ANSWER_ID,QUESTION_ID,TYPE,ANSWER_TEXT,SHORT_EXPLANATION) values (22,9,0,'Incorrect ans, 3','short explanation 3');
Insert into CONCRETE_ANSWER (CONCRETE_ANSWER_ID,QUESTION_ID,TYPE,ANSWER_TEXT,SHORT_EXPLANATION) values (23,9,0,'Incorrect ans, 4','short explanation 4');
Insert into CONCRETE_ANSWER (CONCRETE_ANSWER_ID,QUESTION_ID,TYPE,ANSWER_TEXT,SHORT_EXPLANATION) values (24,9,0,'Incorrect ans, 5','short explanation 5');
Insert into CONCRETE_ANSWER (CONCRETE_ANSWER_ID,QUESTION_ID,TYPE,ANSWER_TEXT,SHORT_EXPLANATION) values (25,10,1,'Correct ans 2,','""');
Insert into CONCRETE_ANSWER (CONCRETE_ANSWER_ID,QUESTION_ID,TYPE,ANSWER_TEXT,SHORT_EXPLANATION) values (26,10,0,'Incorrect ans, 3','short explanation 3');
Insert into CONCRETE_ANSWER (CONCRETE_ANSWER_ID,QUESTION_ID,TYPE,ANSWER_TEXT,SHORT_EXPLANATION) values (27,10,0,'Incorrect ans, 4','short explanation 4');
Insert into CONCRETE_ANSWER (CONCRETE_ANSWER_ID,QUESTION_ID,TYPE,ANSWER_TEXT,SHORT_EXPLANATION) values (28,10,0,'Incorrect ans, 5','short explanation 5');

Insert into PARAMETER_ANSWER (PARAMETER_ID,PARAMETER_ANSWER_ID,QUESTION_ID,PARAM_1,PARAM_2,PARAM_3,PARAM_4,PARAM_5,ANSWER_TEXT,SHORT_EXPLANATION,TYPE) values (1,1,3,'512','2000','50','5','10','Correct ans 3v1,','""',1);
Insert into PARAMETER_ANSWER (PARAMETER_ID,PARAMETER_ANSWER_ID,QUESTION_ID,PARAM_1,PARAM_2,PARAM_3,PARAM_4,PARAM_5,ANSWER_TEXT,SHORT_EXPLANATION,TYPE) values (1,2,3,'512','2000','50','5','10','Incorrect ans 4v1,','short explanation 4',0);
Insert into PARAMETER_ANSWER (PARAMETER_ID,PARAMETER_ANSWER_ID,QUESTION_ID,PARAM_1,PARAM_2,PARAM_3,PARAM_4,PARAM_5,ANSWER_TEXT,SHORT_EXPLANATION,TYPE) values (1,3,3,'512','2000','50','5','10','Incorrect ans 5v1,','short explanation 5',0);
Insert into PARAMETER_ANSWER (PARAMETER_ID,PARAMETER_ANSWER_ID,QUESTION_ID,PARAM_1,PARAM_2,PARAM_3,PARAM_4,PARAM_5,ANSWER_TEXT,SHORT_EXPLANATION,TYPE) values (1,4,3,'512','2000','50','5','10','Incorrect ans 6v1,','short explanation 6',0);
Insert into PARAMETER_ANSWER (PARAMETER_ID,PARAMETER_ANSWER_ID,QUESTION_ID,PARAM_1,PARAM_2,PARAM_3,PARAM_4,PARAM_5,ANSWER_TEXT,SHORT_EXPLANATION,TYPE) values (2,5,3,'256','1000','100','10','20','Correct ans 1v2,','""',1);
Insert into PARAMETER_ANSWER (PARAMETER_ID,PARAMETER_ANSWER_ID,QUESTION_ID,PARAM_1,PARAM_2,PARAM_3,PARAM_4,PARAM_5,ANSWER_TEXT,SHORT_EXPLANATION,TYPE) values (2,6,3,'256','1000','100','10','20','Incorrect ans 4v2,','short explanation 4v1',0);
Insert into PARAMETER_ANSWER (PARAMETER_ID,PARAMETER_ANSWER_ID,QUESTION_ID,PARAM_1,PARAM_2,PARAM_3,PARAM_4,PARAM_5,ANSWER_TEXT,SHORT_EXPLANATION,TYPE) values (2,7,3,'256','1000','100','10','20','Incorrect ans 5v2,','short explanation 5v1',0);
Insert into PARAMETER_ANSWER (PARAMETER_ID,PARAMETER_ANSWER_ID,QUESTION_ID,PARAM_1,PARAM_2,PARAM_3,PARAM_4,PARAM_5,ANSWER_TEXT,SHORT_EXPLANATION,TYPE) values (2,8,3,'256','1000','100','10','20','Incorrect ans 6v2,','short explanation 6v1',0);
Insert into PARAMETER_ANSWER (PARAMETER_ID,PARAMETER_ANSWER_ID,QUESTION_ID,PARAM_1,PARAM_2,PARAM_3,PARAM_4,PARAM_5,ANSWER_TEXT,SHORT_EXPLANATION,TYPE) values (1,9,4,'1','5','10',null,null,'Correct ans 1v1,','""',1);
Insert into PARAMETER_ANSWER (PARAMETER_ID,PARAMETER_ANSWER_ID,QUESTION_ID,PARAM_1,PARAM_2,PARAM_3,PARAM_4,PARAM_5,ANSWER_TEXT,SHORT_EXPLANATION,TYPE) values (1,10,4,'1','5','10',null,null,'Incorrect ans 3v1,','short explanation 3',0);
Insert into PARAMETER_ANSWER (PARAMETER_ID,PARAMETER_ANSWER_ID,QUESTION_ID,PARAM_1,PARAM_2,PARAM_3,PARAM_4,PARAM_5,ANSWER_TEXT,SHORT_EXPLANATION,TYPE) values (1,11,4,'1','5','10',null,null,'Incorrect ans 4v1,','short explanation 4',0);
Insert into PARAMETER_ANSWER (PARAMETER_ID,PARAMETER_ANSWER_ID,QUESTION_ID,PARAM_1,PARAM_2,PARAM_3,PARAM_4,PARAM_5,ANSWER_TEXT,SHORT_EXPLANATION,TYPE) values (1,12,4,'1','5','10',null,null,'Incorrect ans 5v1,','short explanation 5',0);
Insert into PARAMETER_ANSWER (PARAMETER_ID,PARAMETER_ANSWER_ID,QUESTION_ID,PARAM_1,PARAM_2,PARAM_3,PARAM_4,PARAM_5,ANSWER_TEXT,SHORT_EXPLANATION,TYPE) values (2,13,4,'0','10','1',null,null,'Correct ans 1v2,','""',1);
Insert into PARAMETER_ANSWER (PARAMETER_ID,PARAMETER_ANSWER_ID,QUESTION_ID,PARAM_1,PARAM_2,PARAM_3,PARAM_4,PARAM_5,ANSWER_TEXT,SHORT_EXPLANATION,TYPE) values (2,14,4,'0','10','1',null,null,'Incorrect ans 3v2,','short explanation 3v1',0);
Insert into PARAMETER_ANSWER (PARAMETER_ID,PARAMETER_ANSWER_ID,QUESTION_ID,PARAM_1,PARAM_2,PARAM_3,PARAM_4,PARAM_5,ANSWER_TEXT,SHORT_EXPLANATION,TYPE) values (2,15,4,'0','10','1',null,null,'Incorrect ans 4v2,','short explanation 4v1',0);
Insert into PARAMETER_ANSWER (PARAMETER_ID,PARAMETER_ANSWER_ID,QUESTION_ID,PARAM_1,PARAM_2,PARAM_3,PARAM_4,PARAM_5,ANSWER_TEXT,SHORT_EXPLANATION,TYPE) values (2,16,4,'0','10','1',null,null,'Incorrect ans 5v2,','short explanation 5v1',0);
Insert into PARAMETER_ANSWER (PARAMETER_ID,PARAMETER_ANSWER_ID,QUESTION_ID,PARAM_1,PARAM_2,PARAM_3,PARAM_4,PARAM_5,ANSWER_TEXT,SHORT_EXPLANATION,TYPE) values (1,17,4,'1','5','10',null,null,'Correct ans 1v1,','""',1);
Insert into PARAMETER_ANSWER (PARAMETER_ID,PARAMETER_ANSWER_ID,QUESTION_ID,PARAM_1,PARAM_2,PARAM_3,PARAM_4,PARAM_5,ANSWER_TEXT,SHORT_EXPLANATION,TYPE) values (1,18,4,'1','5','10',null,null,'Incorrect ans 3v1,','short explanation 3',0);
Insert into PARAMETER_ANSWER (PARAMETER_ID,PARAMETER_ANSWER_ID,QUESTION_ID,PARAM_1,PARAM_2,PARAM_3,PARAM_4,PARAM_5,ANSWER_TEXT,SHORT_EXPLANATION,TYPE) values (1,19,4,'1','5','10',null,null,'Incorrect ans 4v1,','short explanation 4',0);
Insert into PARAMETER_ANSWER (PARAMETER_ID,PARAMETER_ANSWER_ID,QUESTION_ID,PARAM_1,PARAM_2,PARAM_3,PARAM_4,PARAM_5,ANSWER_TEXT,SHORT_EXPLANATION,TYPE) values (1,20,4,'1','5','10',null,null,'Incorrect ans 5v1,','short explanation 5',0);
Insert into PARAMETER_ANSWER (PARAMETER_ID,PARAMETER_ANSWER_ID,QUESTION_ID,PARAM_1,PARAM_2,PARAM_3,PARAM_4,PARAM_5,ANSWER_TEXT,SHORT_EXPLANATION,TYPE) values (2,21,5,'1','1',null,null,null,'Correct ans 1v2,','""',1);
Insert into PARAMETER_ANSWER (PARAMETER_ID,PARAMETER_ANSWER_ID,QUESTION_ID,PARAM_1,PARAM_2,PARAM_3,PARAM_4,PARAM_5,ANSWER_TEXT,SHORT_EXPLANATION,TYPE) values (2,22,5,'1','1',null,null,null,'Incorrect ans 3v2,','short explanation 3v1',0);
Insert into PARAMETER_ANSWER (PARAMETER_ID,PARAMETER_ANSWER_ID,QUESTION_ID,PARAM_1,PARAM_2,PARAM_3,PARAM_4,PARAM_5,ANSWER_TEXT,SHORT_EXPLANATION,TYPE) values (2,23,5,'1','1',null,null,null,'Incorrect ans 4v2,','short explanation 4v1',0);
Insert into PARAMETER_ANSWER (PARAMETER_ID,PARAMETER_ANSWER_ID,QUESTION_ID,PARAM_1,PARAM_2,PARAM_3,PARAM_4,PARAM_5,ANSWER_TEXT,SHORT_EXPLANATION,TYPE) values (2,24,5,'1','1',null,null,null,'Incorrect ans 5v2,','short explanation 5v1',0);
Insert into PARAMETER_ANSWER (PARAMETER_ID,PARAMETER_ANSWER_ID,QUESTION_ID,PARAM_1,PARAM_2,PARAM_3,PARAM_4,PARAM_5,ANSWER_TEXT,SHORT_EXPLANATION,TYPE) values (1,25,5,'0','1',null,null,null,'Correct ans 1v1,','""',1);
Insert into PARAMETER_ANSWER (PARAMETER_ID,PARAMETER_ANSWER_ID,QUESTION_ID,PARAM_1,PARAM_2,PARAM_3,PARAM_4,PARAM_5,ANSWER_TEXT,SHORT_EXPLANATION,TYPE) values (1,26,5,'0','1',null,null,null,'Incorrect ans 3v1,','short explanation 3',0);
Insert into PARAMETER_ANSWER (PARAMETER_ID,PARAMETER_ANSWER_ID,QUESTION_ID,PARAM_1,PARAM_2,PARAM_3,PARAM_4,PARAM_5,ANSWER_TEXT,SHORT_EXPLANATION,TYPE) values (1,27,5,'0','1',null,null,null,'Incorrect ans 4v1,','short explanation 4',0);
Insert into PARAMETER_ANSWER (PARAMETER_ID,PARAMETER_ANSWER_ID,QUESTION_ID,PARAM_1,PARAM_2,PARAM_3,PARAM_4,PARAM_5,ANSWER_TEXT,SHORT_EXPLANATION,TYPE) values (1,28,5,'0','1',null,null,null,'Incorrect ans 5v1,','short explanation 5',0);

Insert into SUBMITS (USER_ID,COURSE_ID,EXERCISE_ID,QUESTION_ID,PARAMETER_ID,CONCRETE_ANSWER_ID,PARAMETER_ANSWER_ID,ATTEMPT,SUBMIT_TIME) values ('tregan','CSC540',1,1,null,4,null,1,to_date('27-AUG-17','DD-MON-RR'));
Insert into SUBMITS (USER_ID,COURSE_ID,EXERCISE_ID,QUESTION_ID,PARAMETER_ID,CONCRETE_ANSWER_ID,PARAMETER_ANSWER_ID,ATTEMPT,SUBMIT_TIME) values ('tregan','CSC540',1,2,null,2,null,1,to_date('27-AUG-17','DD-MON-RR'));
Insert into SUBMITS (USER_ID,COURSE_ID,EXERCISE_ID,QUESTION_ID,PARAMETER_ID,CONCRETE_ANSWER_ID,PARAMETER_ANSWER_ID,ATTEMPT,SUBMIT_TIME) values ('tregan','CSC540',1,3,1,null,2,1,to_date('27-AUG-17','DD-MON-RR'));
Insert into SUBMITS (USER_ID,COURSE_ID,EXERCISE_ID,QUESTION_ID,PARAMETER_ID,CONCRETE_ANSWER_ID,PARAMETER_ANSWER_ID,ATTEMPT,SUBMIT_TIME) values ('tregan','CSC540',1,1,null,7,null,2,to_date('29-AUG-17','DD-MON-RR'));
Insert into SUBMITS (USER_ID,COURSE_ID,EXERCISE_ID,QUESTION_ID,PARAMETER_ID,CONCRETE_ANSWER_ID,PARAMETER_ANSWER_ID,ATTEMPT,SUBMIT_TIME) values ('tregan','CSC540',1,2,null,9,null,2,to_date('29-AUG-17','DD-MON-RR'));
Insert into SUBMITS (USER_ID,COURSE_ID,EXERCISE_ID,QUESTION_ID,PARAMETER_ID,CONCRETE_ANSWER_ID,PARAMETER_ANSWER_ID,ATTEMPT,SUBMIT_TIME) values ('tregan','CSC540',1,3,2,null,6,2,to_date('29-AUG-17','DD-MON-RR'));
Insert into SUBMITS (USER_ID,COURSE_ID,EXERCISE_ID,QUESTION_ID,PARAMETER_ID,CONCRETE_ANSWER_ID,PARAMETER_ANSWER_ID,ATTEMPT,SUBMIT_TIME) values ('jmick','CSC540',3,1,null,2,null,1,to_date('30-AUG-17','DD-MON-RR'));
Insert into SUBMITS (USER_ID,COURSE_ID,EXERCISE_ID,QUESTION_ID,PARAMETER_ID,CONCRETE_ANSWER_ID,PARAMETER_ANSWER_ID,ATTEMPT,SUBMIT_TIME) values ('jmick','CSC540',3,2,null,6,null,1,to_date('30-AUG-17','DD-MON-RR'));
Insert into SUBMITS (USER_ID,COURSE_ID,EXERCISE_ID,QUESTION_ID,PARAMETER_ID,CONCRETE_ANSWER_ID,PARAMETER_ANSWER_ID,ATTEMPT,SUBMIT_TIME) values ('jmick','CSC540',3,3,2,null,6,1,to_date('30-AUG-17','DD-MON-RR'));
Insert into SUBMITS (USER_ID,COURSE_ID,EXERCISE_ID,QUESTION_ID,PARAMETER_ID,CONCRETE_ANSWER_ID,PARAMETER_ANSWER_ID,ATTEMPT,SUBMIT_TIME) values ('jmick','CSC540',3,1,null,3,null,2,to_date('31-AUG-17','DD-MON-RR'));
Insert into SUBMITS (USER_ID,COURSE_ID,EXERCISE_ID,QUESTION_ID,PARAMETER_ID,CONCRETE_ANSWER_ID,PARAMETER_ANSWER_ID,ATTEMPT,SUBMIT_TIME) values ('jmick','CSC540',3,2,null,8,null,2,to_date('31-AUG-17','DD-MON-RR'));
Insert into SUBMITS (USER_ID,COURSE_ID,EXERCISE_ID,QUESTION_ID,PARAMETER_ID,CONCRETE_ANSWER_ID,PARAMETER_ANSWER_ID,ATTEMPT,SUBMIT_TIME) values ('jmick','CSC540',3,3,1,null,3,2,to_date('31-AUG-17','DD-MON-RR'));

Insert into EXERCISE_TOPIC (EXERCISE_ID,TOPIC_ID) values (2,1);
Insert into EXERCISE_TOPIC (EXERCISE_ID,TOPIC_ID) values (2,2);
Insert into EXERCISE_TOPIC (EXERCISE_ID,TOPIC_ID) values (2,3);		
Insert into EXERCISE_TOPIC (EXERCISE_ID,TOPIC_ID) values (2,4);
Insert into EXERCISE_TOPIC (EXERCISE_ID,TOPIC_ID) values (2,5);
Insert into EXERCISE_TOPIC (EXERCISE_ID,TOPIC_ID) values (2,6);
Insert into EXERCISE_TOPIC (EXERCISE_ID,TOPIC_ID) values (2,7);		
Insert into EXERCISE_TOPIC (EXERCISE_ID,TOPIC_ID) values (2,8);
Insert into EXERCISE_TOPIC (EXERCISE_ID,TOPIC_ID) values (2,9);		
Insert into EXERCISE_TOPIC (EXERCISE_ID,TOPIC_ID) values (2,10);
Insert into EXERCISE_TOPIC (EXERCISE_ID,TOPIC_ID) values (2,11);
