# DBMS Project 1
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

### Run JDBC
#### Using CMD
```java
javac OracleJDBCExample.java
java -cp .\ojdbc8.jar;.\ OracleJDBCExample
```
#### Using Eclipse
Right click on ojdbc8.jar > "Build Path" > "Add to build path"
than just run from eclipse
## Reference
[Connect to Oracle DB via JDBC driver](http://www.mkyong.com/jdbc/connect-to-oracle-db-via-jdbc-driver-java/)
[Drop All Tables](http://www.jochenhebbrecht.be/site/2010-05-10/database/drop-all-tables-in-oracle-db-scheme)
