----------------------------
-- CREATE TABLES
----------------------------

CREATE TABLE USERID_PASSWORD(
user_id VARCHAR(50) NOT NULL,
role INT NOT NULL,
password VARCHAR(64) NOT NULL,
CONSTRAINT check_user_role CHECK (role IN (1,2,3)),
PRIMARY KEY (user_id)
);

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
PRIMARY KEY (user_id),
FOREIGN KEY (user_id) REFERENCES USERID_PASSWORD ON DELETE CASCADE
);

CREATE TABLE STUDENT(
user_id VARCHAR(50) NOT NULL,
student_name VARCHAR(50) NOT NULL,
year_enrolled INT NOT NULL,
type VARCHAR(20) NOT NULL,
PRIMARY KEY (user_id),
FOREIGN KEY (user_id) REFERENCES USERID_PASSWORD ON DELETE CASCADE
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
FOREIGN KEY (course_id) REFERENCES COURSE ON DELETE CASCADE,
CONSTRAINT check_exercise_mode CHECK (exercise_mode IN (0,1)),
CONSTRAINT check_retries CHECK (retries_allowed IN (-1,1,2,3)),
CONSTRAINT check_diff_min CHECK (difficulty_level_min IN (1,2,3,4,5)),
CONSTRAINT check_diff_max CHECK (difficulty_level_max IN (1,2,3,4,5)),
CONSTRAINT check_diff_range CHECK (difficulty_level_max >= difficulty_level_min),
CONSTRAINT check_exercise_date CHECK (exercise_end > exercise_start)
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
PRIMARY KEY (question_id),
CONSTRAINT check_ques_diff CHECK (type IN (1,2,3,4,5)),
CONSTRAINT check_ques_type CHECK (type IN (0,1))
);

CREATE TABLE CONCRETE_ANSWER(
concrete_answer_id INT,
question_id INT,
type INT NOT NULL,
answer_text VARCHAR(50) NOT NULL,
short_explanation VARCHAR(50) NOT NULL,
FOREIGN KEY (question_id) REFERENCES QUESTION ON DELETE CASCADE,
PRIMARY KEY (concrete_answer_id),
CONSTRAINT check_conc_ans_type CHECK (type IN (0,1)),
UNIQUE (concrete_answer_id, question_id)
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
PRIMARY KEY (parameter_id, parameter_answer_id),
CONSTRAINT check_param_ans_type CHECK (type IN (0,1)),
UNIQUE (parameter_id, parameter_answer_id, question_id) 
);

CREATE TABLE COURSE_STAFF(
course_id VARCHAR(50),
user_id VARCHAR(50) NOT NULL, 
role INT,
FOREIGN KEY (course_id) REFERENCES COURSE ON DELETE CASCADE
);
/
CREATE UNIQUE INDEX professor_dup_at_course
    ON COURSE_STAFF ( CASE WHEN role = 1
                            THEN course_id
                            ELSE NULL
                        END );
/
CREATE UNIQUE INDEX ta_dup_at_course
    ON COURSE_STAFF ( CASE WHEN role = 2
                            THEN user_id
                            ELSE NULL
                        END );
/
CREATE TABLE COURSE_STUDENT(
course_id VARCHAR(50),
user_id VARCHAR(50) NOT NULL, 
FOREIGN KEY (course_id) REFERENCES COURSE ON DELETE CASCADE,
FOREIGN KEY (user_id) REFERENCES STUDENT ON DELETE CASCADE,
UNIQUE (course_id, user_id)
);

CREATE TABLE EXERCISE_QUESTION(
exercise_id INT,
question_id INT, 
FOREIGN KEY (exercise_id) REFERENCES EXERCISE ON DELETE CASCADE,
FOREIGN KEY (question_id) REFERENCES QUESTION ON DELETE CASCADE,
UNIQUE (exercise_id, question_id)
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
  FOREIGN KEY (concrete_answer_id) REFERENCES CONCRETE_ANSWER ON DELETE CASCADE,
  UNIQUE (course_id, user_id, exercise_id, question_id, attempt)
);

CREATE TABLE EXERCISE_TOPIC(
exercise_id INT NOT NULL,
topic_id INT NOT NULL,
FOREIGN KEY (exercise_id) REFERENCES EXERCISE ON DELETE CASCADE,
FOREIGN KEY (topic_id) REFERENCES TOPIC ON DELETE CASCADE,
UNIQUE (exercise_id, topic_id)
);

----------------------------
-- CREATE TRIGGERS
----------------------------

-- Enrolled student meets the course requirement with their level,
CREATE OR REPLACE TRIGGER check_student_course_level
BEFORE INSERT OR UPDATE
ON COURSE_STUDENT
FOR EACH ROW
declare
  icount number(10);
BEGIN
        SELECT count(*)
        into icount
        FROM COURSE CC, STUDENT S
        WHERE CC.course_id = :new.course_id
        AND S.user_id = :new.user_id
        AND CC.COURSE_LEVEL LIKE S.TYPE;
        
        if icount = 0 then
          raise_application_error(-20111, 'ERROR, student level does not match course level');
        end if;
END;
/

-- Check student is enrolled before dropping them
CREATE OR REPLACE TRIGGER check_student_enrolled
BEFORE DELETE
ON COURSE_STUDENT
FOR EACH ROW
declare
  icount number(10);
BEGIN
        SELECT count(*)
        into icount
        FROM COURSE CC, STUDENT S
        WHERE CC.course_id = :new.course_id
        AND S.user_id = :new.user_id;
        
        if icount = 0 then
          raise_application_error(-20112, 'ERROR, student cannot be dropped as student is not enrolled in this course');
        end if;
END;
/
-- each concrete question id: 1 correct answer and 3 incorrect answer
CREATE OR REPLACE TRIGGER concrete_question_answer_count
BEFORE INSERT OR UPDATE
ON CONCRETE_ANSWER
FOR EACH ROW
declare
  icount number(10);
  ccount number(10);
BEGIN    
      -- inserting inccorect answer
      IF (:new.type = 0) then
        SELECT count(*)
        into icount
        FROM CONCRETE_ANSWER ans
        WHERE ans.QUESTION_ID = :new.question_id AND ans.type = 0;
        
        if icount >= 3 then
          raise_application_error(-20107, 'ERROR, only 3 incorrect answer can be inserted under same question');
        end if; 
      -- inserting ccorect answer
      ELSIF  (:new.type = 1) then
        SELECT count(*)
        into ccount
        FROM CONCRETE_ANSWER ans
        WHERE ans.QUESTION_ID = :new.question_id AND ans.type = 1;
      
        if ccount >=1 then
          raise_application_error(-20108, 'ERROR, only 1 correct answer can be inserted under same question');
        end if;
      end if;
END;
/
-- each parameter_id and question_id: 1 correct answer and 3 incorrect answer
CREATE OR REPLACE TRIGGER param_question_answer_count
BEFORE INSERT OR UPDATE
ON PARAMETER_ANSWER
FOR EACH ROW
declare
  icount number(10);
  ccount number(10);
BEGIN
      -- inserting inccorect answer
      if (:new.type = 0) then
        SELECT count(*)
        into icount
        FROM PARAMETER_ANSWER ans
        WHERE ans.QUESTION_ID = :new.question_id 
        AND ans.PARAMETER_ID = :new.parameter_id
        AND ans.type = 1;

        if icount >= 3 then
          raise_application_error(-20109, 'ERROR, only 3 incorrect answer can be inserted under same question and parameter set ');
        end if;      
      -- inserting ccorect answer
      ELSIF  (:new.type = 1) then
        SELECT count(*)
        into ccount
        FROM PARAMETER_ANSWER ans
        WHERE ans.QUESTION_ID = :new.question_id 
        AND ans.PARAMETER_ID = :new.parameter_id
        AND ans.type = 1;
      
        if ccount >= 1 then
          raise_application_error(-20110, 'ERROR, only 1 correct answer can be inserted under same question and parameter set ');
        end if;
      end if;
END;
/
-- student assigned as TA cannot be enrolled in the class
CREATE OR REPLACE TRIGGER enroll_student_not_ta
BEFORE INSERT OR UPDATE
ON COURSE_STUDENT
FOR EACH ROW
declare
  scount number(1);
BEGIN
      SELECT count(*)
      into scount
      FROM COURSE_STAFF CS
      WHERE CS.user_id = :new.user_id AND CS.course_id = :new.course_id;
      
      if scount != 0 then
        raise_application_error(-20106, 'ERROR, this student is already assigned in this course as a TA');
      end if;
END;
/
-- Check when TA is added
-- TA cannot be enrolled as a student in the course
-- They must be graduate student

CREATE OR REPLACE TRIGGER check_ta_before_add
BEFORE INSERT OR UPDATE
ON COURSE_STAFF
FOR EACH ROW
declare
  scount number(1);
  stype number(1);
BEGIN
    if :new.role = 2 then
      SELECT count(*)
      into scount
      FROM COURSE_STUDENT CS
      WHERE CS.user_id = :new.user_id;
      
      if scount != 0 then
        raise_application_error(-20104, 'ERROR, this TA is already enrolled in this course as a student');
      end if;
    
      SELECT count(*)
      into stype
      FROM STUDENT S
      WHERE S.user_id = :new.user_id AND S.type LIKE 'Grad';
    
      if stype = 0 then
          raise_application_error(-20105, 'ERROR, this TA cannot be assigned since it is not a grad student');
      end if;
    end if;
END;
/
-- Check if the course_staff exist in the system

CREATE OR REPLACE TRIGGER check_course_staff_existed
BEFORE INSERT OR UPDATE
ON COURSE_STAFF
FOR EACH ROW
declare
  scount number(1);
  pcount number(1);
BEGIN
    SELECT count(*)
    into scount
    FROM STUDENT S
    WHERE S.user_id = :new.user_id;
    
    SELECT count(*)
    into pcount
    FROM PROFESSOR P
    WHERE P.user_id = :new.user_id;
    
    if pcount=0 AND scount=0 then
        raise_application_error(-20103, 'ERROR, this user does not exist in the system');
    end if;
END;
/
-- Check if the exercise_id inserted to exercise_question table is in STANDARD MODE

CREATE OR REPLACE TRIGGER check_exercise_question_is_standard
BEFORE INSERT OR UPDATE
ON EXERCISE_QUESTION
FOR EACH ROW
declare
  exmode number(1);
BEGIN
    SELECT EX.exercise_mode
    into exmode 
    FROM EXERCISE EX
    WHERE EX.exercise_id = :new.exercise_id;
    
    if exmode != 0 then
        raise_application_error(-20102, 'ERROR, this exercise_id cannot be inserted into EXERCISE_QUESTION since it is not standard mode');
    end if;
END;
/
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


