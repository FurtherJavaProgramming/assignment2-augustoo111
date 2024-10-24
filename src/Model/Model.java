package model;

import java.sql.SQLException;

import Dao.BookDao;
import Dao.OrderDao;
import Dao.UserDao;
import Dao.UserDaoImplementation;

public class Model {
    private static Model instance;
    private UserDao userDao;
    private User currentUser;
    private AdminUser admin;

    private Model() {
        userDao = new UserDaoImplementation();
    }

    public static Model getInstance() {
        if (instance == null) {
            instance = new Model();
        }
        return instance;
    }
    
	public void setup() throws SQLException {
		userDao.setup();
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
