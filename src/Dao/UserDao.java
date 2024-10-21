package Dao;

import java.sql.SQLException;
import model.AdminUser;
import model.User;

/**
 * The UserDao interface provides an abstract interface for accessing user data from the database.
 * 
 * This DAO is responsible for handling all operations related to user management
 * This interface allows for separation of database access logic from the rest of the application,
 * promoting cleaner architecture and maintainability.
 * 
 * @see model.User
 * @see model.AdminUser
 */
public interface UserDao {
	void setup() throws SQLException;
	User getUser(String username, String password) throws SQLException;
	User getUserByUsername(String username) throws SQLException;
	User createUser(String firstName, String lastName, String username, String password) throws SQLException;
	AdminUser getAdminUser(String username, String password) throws SQLException;
	void updateUserDetails(User currentUser) throws SQLException;
	void updatePassword(User user) throws SQLException;
}
