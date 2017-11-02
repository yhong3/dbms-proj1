package sql;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionManager {
	
	public Connection getDBConnection() {
		Connection dbConnection = null;
		try {
			Class.forName(DatabaseParameter.DB_DRIVER);
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}

		try {
			dbConnection = DriverManager.getConnection(
					DatabaseParameter.DB_CONNECTION, 
					DatabaseParameter.DB_USER,
					DatabaseParameter.DB_PASSWORD);
			return dbConnection;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return dbConnection;

	}
}