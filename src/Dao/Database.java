package Dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The Database class is responsible for managing database connections.
 * It provides a static method to establish a connection to the SQLite database.
 */

public class Database {
	// URL pattern for database
	private static final String DB_URL = "jdbc:sqlite:application.db";

	public static Connection getConnection() throws SQLException {
		// DriverManager is the basic service for managing a set of JDBC drivers
		// Can also pass username and password
		return DriverManager.getConnection(DB_URL);
	}
}
