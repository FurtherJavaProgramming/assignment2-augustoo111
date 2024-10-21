package Dao;

import model.Order;
import java.sql.SQLException;
import java.util.List;
/**
 * A data access object (DAO) that provides an abstract interface for managing 
 * order data within the persistence layer.
 * The `OrderDao` interface defines operations for handling orders in an e-commerce 
 * application, such as creating, retrieving, and saving orders in the database.
 * By abstracting the underlying database operations, this interface simplifies the 
 * task of managing order data, ensuring that the application code is not tightly 
 * coupled to the database.
 * 
 * @see model.Order
 */

public interface OrderDao {
	void setup() throws SQLException;
    List<Order> getAllOrders() throws SQLException;
    void insertOrder(Order order) throws SQLException;
	List<Order> getOrdersByUsername(String username) throws SQLException;
	void saveOrder(Order order, String username) throws SQLException;
}
