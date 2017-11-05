# DBMS Project 1

### Team member
CSC 540 Fall 2017
Team Q

* Yu-Lun Hong (yhong3)
* Ashwin Risbood (arisboo)
* Huy Tu (hqtu)
* Xi Yang (yxi2)

[Github repo](https://github.com/yhong3/dbms-proj1) for good-looking readme

## Run the program
### Set up oracle
Tested on Win 10 with **CMD**, POWERSHELL NOT WORKING

Start SQLPLUS as SYSDBA (in CMD)
```
sqlplus / as sysdba
```
```sql
CREATE USER admin IDENTIFIED BY password;
GRANT ALL PRIVILEGES TO admin;
GRANT EXECUTE ANY PROCEDURE TO admin;
```
(run `alter session set "_ORACLE_SCRIPT"=true;` if sqlplus complaining `oracle invalid common user or role name`)

### Populate sample database
1. in ```sql/DatabaseParameter.java```, change it to your database credentials, add it to gitignore once you are done.
2. (optional) ```Run sql_files/DropAllTable.sql``` using sql developer if you want to drop all tables and triggers 
3. Run ```sql_files/createSchema.sql``` to create and insert tables, triggers, etc.
4. Run ```sql_files/insertSamples.sql``` to insert sample data
5. run Java program

Default local setting for oracle database
```
database: orcl
name: admin
pass: password

In sql/DatabaseParameter.java
DB_DRIVER = "oracle.jdbc.driver.OracleDriver";
DB_CONNECTION = "jdbc:oracle:thin:@localhost:1521:orcl";
DB_USER = "admin";
DB_PASSWORD = "password";
```
#### Run from executable

From any kind of command line, run
```
java -jar Login.jar
```
#### Using source code from CMD
```java
javac Login.java
java -cp .\ojdbc8.jar;.\ Login.java
```
#### Run source code using Eclipse
```java
Right click on ojdbc8.jar > "Build Path" > "Add to build path"
than just run Login.java from eclipse
```

## Reference
* [Connect to Oracle DB via JDBC driver](http://www.mkyong.com/jdbc/connect-to-oracle-db-via-jdbc-driver-java/)
* [Drop All Tables](http://www.jochenhebbrecht.be/site/2010-05-10/database/drop-all-tables-in-oracle-db-scheme)
* [csv2sql](http://csv2mysql.patrotsky.com/index.php)
* [Auto-increment ID in oracle](http://earlruby.org/2009/01/creating-auto-increment-columns-in-oracle/)

## Coding Notes
when sql is not giving back the desired result, check these
1. press "sql commit" in the SQL developer
2. the column index and input value in setInt() and setString(), etc.
3. check if you are using 2 prepare statement at the same time (maybe using a query to determine value of another query)
4. check if the query first, you need ';' at sql developer, but not in the preparedstatement
5. sometimes add more space when concat different lines of queries

show trigger errors
```
show errors trigger PARAM_QUESTION_ANSWER_COUNT
```
