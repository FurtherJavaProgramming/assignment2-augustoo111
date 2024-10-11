package Dao;

import model.Book;
import java.sql.*;
import java.util.ArrayList;

public class BookDaoImplementation implements BookDao {

    private final String BOOK_TABLE = "books";
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

}
