package Dao;

import model.Order;
import model.Book;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderDaoImplementation implements OrderDao {

    private final String ORDERS_TABLE = "orders";

    
    @Override
    public void setup() throws SQLException {
        try (Connection connection = Database.getConnection();
             Statement stmt = connection.createStatement()) {

            // Create orders table if it doesn't exist
            String createOrdersTable = "CREATE TABLE IF NOT EXISTS " + ORDERS_TABLE + " (" +
                                       "order_id TEXT PRIMARY KEY, " +
                                       "order_date_time TIMESTAMP NOT NULL, " +
                                       "username TEXT NOT NULL, " +  // Add username column
                                       "total_price DOUBLE NOT NULL, " +
                                       "total_quantity INT NOT NULL, " +
                                       "books_purchased TEXT)";
            stmt.executeUpdate(createOrdersTable);
        }
    }


    @Override
    public List<Order> getAllOrders() throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM " + ORDERS_TABLE + " ORDER BY order_date_time DESC";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String orderId = rs.getString("order_id");
                LocalDateTime orderDateTime = rs.getTimestamp("order_date_time").toLocalDateTime();
                double totalPrice = rs.getDouble("total_price");
                int totalQuantity = rs.getInt("total_quantity");  // Get total quantity

                // Fetch associated books as a string and split into an array of titles
                String booksPurchased = rs.getString("books_purchased");
                List<Book> books = new ArrayList<>();
                
                if (booksPurchased != null && !booksPurchased.isEmpty()) {
                    String[] bookTitles = booksPurchased.split(", ");
                    
                    // Calculate the quantity for each book (evenly distributed)
                    int quantityPerBook = totalQuantity / bookTitles.length;

                    for (String title : bookTitles) {
                        // Create Book objects with title and quantity for each book
                        books.add(new Book(title, quantityPerBook, 0.0));  // Assuming price is 0.0 for simplicity
                    }
                }

                // Create an Order object and add it to the list
                Order order = new Order(orderId, books, totalPrice, totalQuantity, orderDateTime);
                orders.add(order);
            }
        }
        return orders;
    }

    private List<Book> parseBooksFromString(String booksPurchased, int totalQuantity) {
        List<Book> books = new ArrayList<>();
        if (booksPurchased != null && !booksPurchased.isEmpty()) {
            String[] bookTitles = booksPurchased.split(", ");
            
            // Calculate the quantity for each book (evenly distributed)
            int quantityPerBook = totalQuantity / bookTitles.length;

            for (String title : bookTitles) {
                // Create a new Book object with title and quantity
                books.add(new Book(title, quantityPerBook, 0.0));  // Assuming 0.0 as price for simplicity
            }
        }
        return books;
    }

    
    @Override
    public void saveOrder(Order order, String username) throws SQLException {
        String query = "INSERT INTO " + ORDERS_TABLE + " (order_id, order_date_time, total_price, total_quantity, books_purchased, username) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            // Set order ID
            preparedStatement.setString(1, order.getOrderId());
            // Set order date and time
            preparedStatement.setTimestamp(2, Timestamp.valueOf(order.getOrderDateTime()));  // Convert LocalDateTime to SQL Timestamp
            // Set total price
            preparedStatement.setDouble(3, order.getTotalPrice());

            // Calculate total quantity from the list of books
            int totalQuantity = order.getBooks().stream()
                                      .mapToInt(Book::getNoOfCopies)
                                      .sum();
            preparedStatement.setInt(4, totalQuantity);

            // Convert the list of books to a comma-separated string
            String booksPurchased = order.getBooks().stream()
                    .map(Book::getTitle)
                    .collect(Collectors.joining(", "));
            preparedStatement.setString(5, booksPurchased);

            // Set the username (ensure this is the correct username of the logged-in user)
            preparedStatement.setString(6, username);  // Store the username

            // Execute the query
            preparedStatement.executeUpdate();
        }
    }


    @Override
    public Order getOrderById(String orderId) throws SQLException {
        String query = "SELECT * FROM " + ORDERS_TABLE + " WHERE order_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, orderId);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    LocalDateTime orderDateTime = resultSet.getTimestamp("order_date_time").toLocalDateTime();
                    double totalPrice = resultSet.getDouble("total_price");
                    int totalQuantity = resultSet.getInt("total_quantity");

                    String booksPurchased = resultSet.getString("books_purchased");
                    List<Book> books = parseBooksFromString(booksPurchased, totalQuantity);

                    return new Order(orderId, books, totalPrice, totalQuantity, orderDateTime);
                }
            }
        }
        return null;
    }
    @Override
    public List<Order> getOrdersByUsername(String username) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM " + ORDERS_TABLE + " WHERE username = ? ORDER BY order_date_time DESC";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);  // Bind the username parameter
            try (ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    String orderId = rs.getString("order_id");
                    LocalDateTime orderDateTime = rs.getTimestamp("order_date_time").toLocalDateTime();
                    double totalPrice = rs.getDouble("total_price");
                    int totalQuantity = rs.getInt("total_quantity");

                    // Fetch associated books as a string
                    String booksPurchased = rs.getString("books_purchased");
                    List<Book> books = parseBooksFromString(booksPurchased, totalQuantity);

                    // Create an Order object
                    Order order = new Order(orderId, books, totalPrice, totalQuantity, orderDateTime);
                    orders.add(order);
                }
            }
        }
        return orders;
    }


    @Override
    public void insertOrder(Order order) throws SQLException {
        String sqlOrder = "INSERT INTO " + ORDERS_TABLE + " (order_id, order_date_time, total_price, total_quantity, books_purchased) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = Database.getConnection();
             PreparedStatement stmtOrder = connection.prepareStatement(sqlOrder)) {

            // Step 1: Insert the order into the orders table
            stmtOrder.setString(1, order.getOrderId());
            stmtOrder.setTimestamp(2, Timestamp.valueOf(order.getOrderDateTime()));
            stmtOrder.setDouble(3, order.getTotalPrice());

            // Calculate total quantity of books in the order
            int totalQuantity = order.getBooks().stream()
                                     .mapToInt(Book::getNoOfCopies)
                                     .sum();
            stmtOrder.setInt(4, totalQuantity);

            // Convert the list of books to a comma-separated string
            String booksPurchasedTitles = order.getBooks().stream()
                                               .map(Book::getTitle)
                                               .collect(Collectors.joining(", "));
            stmtOrder.setString(5, booksPurchasedTitles);  // Store book titles as a string in the orders table

            // Execute the order insert
            stmtOrder.executeUpdate();

            System.out.println("Order inserted successfully!");

        } catch (SQLException e) {
            throw new SQLException("Error inserting order: " + e.getMessage(), e);
        }
    }

}
