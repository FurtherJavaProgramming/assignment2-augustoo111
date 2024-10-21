package Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import model.Book;

/**
 * A data access object (DAO) is a pattern that provides an abstract interface 
 * to a database or other persistence mechanism.
 * The DAO maps application calls to the persistence layer and provides specific data operations 
 * related to books and cart items without exposing details of the database.
 * This interface promotes separation of database access logic, making it easier to maintain and scale.
 * @see model.Book
 */

public interface BookDao {
    void setup() throws SQLException;
    ArrayList<Book> getAllBooks() throws SQLException;
    void addBook(Book book) throws SQLException;
	void updateBookStockbyUser(Book book) throws SQLException;
	void updateBookStock(String title, int newStock) throws SQLException;
	void saveCartItem(String username, Book book) throws SQLException;
	ArrayList<Book> loadCartItems(String username) throws SQLException;
    void clearCartItems(String username) throws SQLException;
	
}
