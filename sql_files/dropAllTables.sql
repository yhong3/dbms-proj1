--------------------------------------------------------
--  DROP ALL EXISTING TABLES
--------------------------------------------------------

BEGIN

    FOR c IN (SELECT table_name FROM user_tables) LOOP
        EXECUTE IMMEDIATE ('DROP TABLE "' || c.table_name || '" CASCADE CONSTRAINTS');
    END LOOP;

    FOR s IN (SELECT sequence_name FROM user_sequences) LOOP
        EXECUTE IMMEDIATE ('DROP SEQUENCE ' || s.sequence_name);
    END LOOP;

END;
/
--------------------------------------------------------
--  DROP ALL TRIGGERS
--------------------------------------------------------
BEGIN  
  FOR i in (select trigger_name,owner 
              from dba_triggers 
             where trigger_name like '%_BI%' and owner = 'admin' ) LOOP  
    EXECUTE IMMEDIATE 'DROP TRIGGER '||i.owner||'.'||i.trigger_name;  
  END LOOP;  
END; 
/

--------------------------------------------------------
--  SHOW ALL TRIGGERS
--------------------------------------------------------
SELECT TRIGGER_NAME FROM USER_TRIGGERS ;
/
