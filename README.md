# DBMS Project 1

## Coding Notes
when sql is not giving back the desired result, check these
1. press "sql commit" in the SQL developer
2. the column index and input value in setInt() and setString(), etc.
3. check if you are using 2 prepare statement at the same time (maybe using a query to determine value of another query)
4. check if the query first, you need ';' at sql developer, but not in the preparedstatement
5. sometimes add more space when concat different lines of queries

## Set up oracle
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

Setting for oracle database
```
database: orcl
name: admin
pass: password
```

### Run the program
#### Populate sample database
1. in sql/DatabaseParameter.java, change it to your database credentials, add it to gitignore once you are done.
2. (optional) Run sql_files/DropAllTable.sql using sql developer if you want to drop all tables 
3. Run sql_files/createSamples.sql to create and insert tables
4. run Java

##### Using CMD
```java
javac LoginInterface.java
java -cp .\ojdbc8.jar;.\ LoginInterface.java
```
##### Using Eclipse
Right click on ojdbc8.jar > "Build Path" > "Add to build path"
than just run ```LoginInterface.java``` from eclipse

## Reference
[Connect to Oracle DB via JDBC driver](http://www.mkyong.com/jdbc/connect-to-oracle-db-via-jdbc-driver-java/)
[Drop All Tables](http://www.jochenhebbrecht.be/site/2010-05-10/database/drop-all-tables-in-oracle-db-scheme)
[csv2sql](http://csv2mysql.patrotsky.com/index.php)
[Auto-increment ID in oracle](http://earlruby.org/2009/01/creating-auto-increment-columns-in-oracle/)

