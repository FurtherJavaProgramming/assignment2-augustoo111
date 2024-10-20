package Dao;

import model.Book;
import java.sql.*;
import java.util.ArrayList;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class BookDaoImplementation implements BookDao {

    private final String BOOK_TABLE = "books";
    private final String CART_TABLE = "cart";

    ArrayList<Book> books = new ArrayList<>();

    @Override
    public void setup() throws SQLException {
        try (Connection connection = Database.getConnection(); Statement stmt = connection.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS " + BOOK_TABLE + " (" +
                         "title TEXT NOT NULL PRIMARY KEY, " +
                         "author TEXT NOT NULL, " +
                         "no_of_copies INTEGER NOT NULL, " +
                         "price REAL NOT NULL, " +
                         "sold_copies INTEGER NOT NULL)";
            stmt.executeUpdate(sql);
            
            String createCartTable = "CREATE TABLE IF NOT EXISTS " + CART_TABLE + " (" +
				                     "username TEXT NOT NULL, " +
				                     "author TEXT NOT NULL, " +
				                     "price REAL NOT NULL, " +
				                     "book_title TEXT NOT NULL, " +
				                     "quantity INTEGER NOT NULL, " +
				                     "FOREIGN KEY (book_title) REFERENCES books(title), " +
				                     "UNIQUE(username, book_title))";  // Add UNIQUE constraint
            stmt.executeUpdate(createCartTable);
            
        }
    }
    public BookDaoImplementation() throws SQLException {
        setup();  // Ensure the setup method is called in the constructor
        addInitialBooks();
    }

    
    public void addInitialBooks() throws SQLException {
        // Creating an ArrayList for the book list
        ArrayList<Book> initialBooks = new ArrayList<>();
        // Adding books to the ArrayList
        initialBooks.add(new Book("Absolute Java", "Savitch", 10, 50, 142));
        initialBooks.add(new Book("JAVA: How to Program", "Deitel and Deitel", 100, 70, 475));
        initialBooks.add(new Book("Computing Concepts with JAVA 8 Essentials", "Horstman", 500, 89, 60));
        initialBooks.add(new Book("Java Software Solutions", "Lewis and Loftus", 500, 99, 12));
        initialBooks.add(new Book("Java Program Design", "Cohoon and Davidson", 2, 29, 86));
        initialBooks.add(new Book("Clean Code", "Robert Martin", 100, 45, 300));
        initialBooks.add(new Book("Gray Hat C#", "Brandon Perry", 300, 68, 178));
        initialBooks.add(new Book("Python Basics", "David Amos", 1000, 49, 79));
        initialBooks.add(new Book("Bayesian Statistics The Fun Way", "Will Kurt", 600, 42, 155));
        
        // Loop through the ArrayList and add each book to the database
        for (Book book : initialBooks) {
            addBook(book);
        }
    }



    @Override
    public ArrayList<Book> getAllBooks() throws SQLException {
        ArrayList<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM " + BOOK_TABLE;
        try (Connection connection = Database.getConnection(); 
             PreparedStatement stmt = connection.prepareStatement(sql); 
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                books.add(new Book(
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getInt("no_of_copies"),
                    rs.getDouble("price"),
                    rs.getInt("sold_copies")
                ));
            }
            
        } catch (SQLException e) {
            throw e;
        }
        return books;
    }
    



    @Override
    public void updateBookStock(String title, int newStock) throws SQLException {
        String sql = "UPDATE " + BOOK_TABLE + " SET no_of_copies = ? WHERE title = ?";
        try (Connection connection = Database.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, newStock);
            stmt.setString(2, title);
            stmt.executeUpdate();
        }
    }

    @Override
    public void addBook(Book book) throws SQLException {
        String sql = "INSERT OR IGNORE INTO " + BOOK_TABLE + " (title, author, no_of_copies, price, sold_copies) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = Database.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setInt(3, book.getNoOfCopies());
            stmt.setDouble(4, book.getPrice());
            stmt.setInt(5, book.getSoldCopies());
            stmt.executeUpdate();
        }
    }

    @Override
    public void updateBookStockbyUser(Book book) throws SQLException {
        String sql = "UPDATE books SET no_of_copies = ?, sold_copies = ? WHERE title = ?";
        try (Connection connection = Database.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Set parameters
            stmt.setInt(1, book.getNoOfCopies());  // Update the number of available copies
            stmt.setInt(2, book.getSoldCopies());  // Update the number of sold copies
            stmt.setString(3, book.getTitle());    // Find the book by title

            // Execute the update
            int rowsAffected = stmt.executeUpdate();

            // Show success message if the update was successful
            if (rowsAffected > 0) {
                showMessage(AlertType.INFORMATION, "Update Successful", 
                            "The database was updated successfully for the book: " + book.getTitle());
            } else {
                // Show error message if no rows were affected (book not found)
                showMessage(AlertType.WARNING, "Update Failed", 
                            "No record found to update for the book: " + book.getTitle());
            }
        } catch (SQLException e) {
            // Show an error message if an exception occurs
            showMessage(AlertType.ERROR, "Update Error", 
                        "An error occurred while updating the book: " + book.getTitle());
            e.printStackTrace();
            throw e;  // Re-throw to handle further up in the call stack
        }
    }

    // Helper method to display messages using JavaFX Alerts
    private void showMessage(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();  // Show the alert and wait for user interaction
    }
    
    
    //save cart item method
    @Override
    public void saveCartItem(String username, Book book) throws SQLException {
        String query = "INSERT INTO " + CART_TABLE + " (username, book_title, author, quantity, price) " +
                       "VALUES (?, ?, ?, ?, ?) " +
                       "ON CONFLICT(username, book_title) DO UPDATE SET quantity = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, book.getTitle());
            pstmt.setString(3, book.getAuthor());
            pstmt.setInt(4, book.getNoOfCopies());
            pstmt.setDouble(5, book.getPrice());
            pstmt.setInt(6, book.getNoOfCopies());  // Update quantity if item already exists in the cart
            pstmt.executeUpdate();
        }
    }
    public ArrayList<Book> loadCartItems(String username) throws SQLException {
        ArrayList<Book> cartItems = new ArrayList<>();
        String query = "SELECT * FROM " + CART_TABLE + " WHERE username = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String title = rs.getString("book_title");
                    String author = rs.getString("author");
                    int quantity = rs.getInt("quantity");
                    double price = rs.getDouble("price");
                    cartItems.add(new Book(title, author, quantity, price, 0));
                }
            }
        }
        return cartItems;
    }


    @Override
    public void clearCartItems(String username) throws SQLException {
        String query = "DELETE FROM " + CART_TABLE + " WHERE username = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.executeUpdate();
        }
    }




}