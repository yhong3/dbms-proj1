----------------------------
-- CREATE TABLES
----------------------------
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
correct_answer_points INT NOT NULL,
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
hint VARCHAR(50) NOT NULL,
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
  answer_score INT NOT NULL,
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


----------------------------
-- CREATE TRIGGERS
----------------------------

-- Check the submits exists in the exercise_question before inserting (standard mode)

CREATE OR REPLACE TRIGGER check_standard_submits_in_exercise_questoin
BEFORE INSERT
ON SUBMITS
FOR EACH ROW
declare
  v_cnt number(10);
  exmode number(1);
BEGIN
    SELECT EX.exercise_mode
    into exmode 
    FROM EXERCISE EX
    WHERE EX.exercise_id = :new.exercise_id;
    
    if exmode = 0 then
      SELECT count(*)
      into v_cnt
      FROM EXERCISE_QUESTION EQ
      where EQ.exercise_id = :new.exercise_id
      and EQ.question_id = :new.question_id
      ;

      if v_cnt= 0 then
        raise_application_error(-20101, 'ERROR, (exercise_id, question_id) does not exist in EXERCISE_QUESITON');
      end if;
    end if;
    

END;
/
------------------------
-- check submits, that attempt cannot bigger than exercise.retries_allowed
------------------------
CREATE OR REPLACE TRIGGER submit_attempt_exceed
   BEFORE INSERT OR UPDATE
   ON SUBMITS
   FOR EACH ROW
DECLARE
   attempt_exceeded   EXCEPTION;
   PRAGMA EXCEPTION_INIT (attempt_exceeded, -20001);
   n_count      NUMBER (1);
BEGIN
   SELECT max(EX.retries_allowed)
     INTO n_count
     FROM EXERCISE EX
    WHERE EX.exercise_id = :NEW.exercise_id;

   IF n_count != -1 AND :new.attempt > n_count
   THEN
      RAISE attempt_exceeded;
   END IF;
EXCEPTION
   WHEN attempt_exceeded
   THEN
      raise_application_error (-20001, 'Attempt is more than retries_allowed for current exercise');
END;
/

