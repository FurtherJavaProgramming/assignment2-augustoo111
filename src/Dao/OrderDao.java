package Dao;

import model.Order;
import java.sql.SQLException;
import java.util.List;

public interface OrderDao {
	void setup() throws SQLException;
    List<Order> getAllOrders() throws SQLException;
    void insertOrder(Order order) throws SQLException;
	Order getOrderById(String orderId) throws SQLException;
	List<Order> getOrdersByUsername(String username) throws SQLException;
	void saveOrder(Order order, String username) throws SQLException;
}
