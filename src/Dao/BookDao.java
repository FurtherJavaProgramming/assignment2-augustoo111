package Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import model.Book;

/**
 * A data access object (DAO) is a pattern that provides an abstract interface 
 * to a database or other persistence mechanism. 
 * the DAO maps application calls to the persistence layer and provides some specific data operations 
 * without exposing details of the database. 
 */

public interface BookDao {
    void setup() throws SQLException;
    ArrayList<Book> getAllBooks() throws SQLException;
    void updateBookStock(String title, int newStock) throws SQLException;  // Update by title
    void addBook(Book book) throws SQLException;
    
}
