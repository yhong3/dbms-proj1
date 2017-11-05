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
-- courses staff can only have one instructor
-- student is already enrolled in the class cannot be insert as a TA 
