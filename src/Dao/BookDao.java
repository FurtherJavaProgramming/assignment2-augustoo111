package Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import model.Book;

public interface BookDao {
    void setup() throws SQLException;
    ArrayList<Book> getAllBooks() throws SQLException;
    void updateBookStock(String title, int newStock) throws SQLException;  // Update by title
    void addBook(Book book) throws SQLException;
    
}
