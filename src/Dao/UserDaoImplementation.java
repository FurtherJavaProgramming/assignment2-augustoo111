package Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.AdminUser;
import model.User;

public class UserDaoImplementation implements UserDao {
    private final String USER_TABLE = "users";
    private final String ADMIN_TABLE = "admin_users";

    @Override
    public void setup() throws SQLException {
        // Create the User table
        try (Connection connection = Database.getConnection(); 
             Statement stmt = connection.createStatement()) {

            // SQL for creating user table
            String userSql = "CREATE TABLE IF NOT EXISTS " + USER_TABLE 
                    + " (firstName TEXT NOT NULL, "
                    + "lastName TEXT NOT NULL, "
                    + "username VARCHAR(10) NOT NULL PRIMARY KEY, "
                    + "password VARCHAR(8) NOT NULL)";
            stmt.executeUpdate(userSql);

            // SQL for creating admin user table
            String adminSql = "CREATE TABLE IF NOT EXISTS " + ADMIN_TABLE 
                    + " (firstName TEXT NOT NULL, "
                    + "lastName TEXT NOT NULL, "
                    + "username VARCHAR(10) NOT NULL PRIMARY KEY, "
                    + "password VARCHAR(8) NOT NULL)";
            stmt.executeUpdate(adminSql);
        }
    }


    @Override
    public User getUser(String username, String password) throws SQLException {
        String sql = "SELECT * FROM " + USER_TABLE + " WHERE username = ? AND password = ?";
        try (Connection connection = Database.getConnection(); 
        		PreparedStatement stmt = connection.prepareStatement(sql);) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("username"),
                        rs.getString("password")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public User createUser(String firstName, String lastName, String username, String password) throws SQLException {
    	setup();
        String sql = "INSERT INTO " + USER_TABLE + " (firstName, lastName, username, password) VALUES (?, ?, ?, ?)";
        try (Connection connection = Database.getConnection(); 
        		PreparedStatement stmt = connection.prepareStatement(sql);) {
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, username);
            stmt.setString(4, password);
            
            stmt.executeUpdate();
            return new User(firstName, lastName, username, password);
        }
    }
    

    @Override
    public AdminUser getAdminUser(String username, String password) throws SQLException {
        String sql = "SELECT * FROM " + ADMIN_TABLE + " WHERE username = ? AND password = ?";
        try (Connection connection = Database.getConnection(); 
        		PreparedStatement stmt = connection.prepareStatement(sql);) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new AdminUser(
                        rs.getString("username"),
                        rs.getString("password")
                    );
                }
            }
        }
        return null;
    }

	@Override
    public User getUserByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection connection = Database.getConnection(); 
        		PreparedStatement stmt = connection.prepareStatement(sql);) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new User(rs.getString("firstName"), rs.getString("lastName"), username, rs.getString("password"));
            } else {
                return null;
            }
        }
	}


	@Override
	public void updateUserDetails(User user) throws SQLException {
	    String sql = "UPDATE users SET firstName = ?, lastName = ? WHERE username = ?";

	    try (Connection conn = Database.getConnection();  
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setString(1, user.getFirstName());
	        pstmt.setString(2, user.getLastName());
	        pstmt.setString(3, user.getUsername());

	        pstmt.executeUpdate();  // Ensure the update is executed.
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw e;  // Ensure that any SQL exception is propagated.
	    }
	}


	@Override
	public void updatePassword(User user) throws SQLException {
	    // Correct SQL syntax for updating a user's password
	    String sql = "UPDATE users SET password = ? WHERE username = ?";
	    
	    try (Connection conn = Database.getConnection();  
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        
	        // Set the password and username values
	        pstmt.setString(1, user.getPassword());
	        pstmt.setString(2, user.getUsername());  // Note that it should be index 2 for username
	        
	        // Execute the update
	        pstmt.executeUpdate();
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw e;  // Ensure that any SQL exception is propagated.
	    }
	}


}
