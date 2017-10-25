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

Run JDBC
```java
javac OracleJDBCExample.java
java -cp C:\Users\trsho\Dropbox\github\eclipse\dbms-proj1\ojdbc8.jar;C:\Users\trsho\Dropbox\github\eclipse\dbms-proj1\ OracleJDBCExample
```

