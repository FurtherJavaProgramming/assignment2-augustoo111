package model;

import java.sql.SQLException;
import Dao.BookDao;
import Dao.BookDaoImplementation;
import Dao.OrderDao;
import Dao.OrderDaoImplementation;
import Dao.UserDao;
import Dao.UserDaoImplementation;

/**
 * Singleton class for managing the application's core data, including 
 * the current user, admin, and DAOs for user, book, and order operations.
 */

public class Model {
    private static Model instance;
    private UserDao userDao;
    private User currentUser;
    private AdminUser admin;
    private BookDao bookDao;
    private OrderDao orderDao;

    private Model() throws SQLException {
        userDao = new UserDaoImplementation();
        bookDao = new BookDaoImplementation();
        orderDao = new OrderDaoImplementation();
    }

    public static Model getInstance() throws SQLException {
        if (instance == null) {
            instance = new Model();
        }
        return instance;
    }
    
	public void setup() throws SQLException {
		userDao.setup();
		bookDao.setup();
		orderDao.setup();
	}
	
	public UserDao getUserDao() {
		return userDao;
	}
	public AdminUser getAdminUser() {
		return admin;
	}
	
	public void setAdminUser(AdminUser adminUser) {
		admin = adminUser;
	}
	
	public User getCurrentUser() {
		return this.currentUser;
	}
	
	public void setCurrentUser(User user) {
		currentUser = user;
	}

	public UserDao setUserDao(UserDao userDao) {
		return userDao;
	}

	public BookDao setBookDao(BookDao bookDao) {
		return bookDao;
		
	}

	public OrderDao setOrderDao(OrderDao orderDao) {
		return orderDao;
		
	}
}
