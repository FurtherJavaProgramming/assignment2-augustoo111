package Dao;

import model.Order;
import model.Book;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The `OrderDaoImplementation` class provides the concrete implementation 
 * of the `OrderDao` interface, handling CRUD operations for orders in a 
 * SQLite database.
 * 
 * This class manages the creation of the orders table, and allows retrieval, 
 * insertion, and fetching of order details by interacting with the underlying 
 * database.
 **/
public class OrderDaoImplementation implements OrderDao {

    private final String ORDERS_TABLE = "orders";

    // Step 1: Create a static instance of OrderDaoImplementation (Singleton)
    private static OrderDaoImplementation instance;

    // Step 2: Make the constructor private to prevent external instantiation
    public OrderDaoImplementation() throws SQLException {
        setup();  // Ensure the setup method is called when the instance is created
    }

    // Step 3: Provide a public static method to get the single instance
    public static OrderDaoImplementation getInstance() throws SQLException {
        if (instance == null) {
            instance = new OrderDaoImplementation();
        }
        return instance;
    }

    // Set up database method
    @Override
    public void setup() throws SQLException {
        try (Connection connection = Database.getConnection();
             Statement stmt = connection.createStatement()) {

            // Create orders table if it doesn't exist
            String createOrdersTable = "CREATE TABLE IF NOT EXISTS " + ORDERS_TABLE + " (" +
                                       "order_id TEXT PRIMARY KEY, " +
                                       "order_date_time DATETIME NOT NULL, " +
                                       "username TEXT NOT NULL, " +  
                                       "total_price DOUBLE NOT NULL, " +
                                       "total_quantity INT NOT NULL, " +
                                       "books_purchased TEXT)";
            stmt.executeUpdate(createOrdersTable);
        }
    }

    // Get all orders method
    @Override
    public List<Order> getAllOrders() throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM " + ORDERS_TABLE + " ORDER BY order_date_time DESC";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String orderId = rs.getString("order_id");

                // Get order date-time as a string and manually parse it
                String orderDateTimeStr = rs.getString("order_date_time");
                LocalDateTime orderDateTime = LocalDateTime.parse(orderDateTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                double totalPrice = rs.getDouble("total_price");
                int totalQuantity = rs.getInt("total_quantity");  // Get total quantity

                // Fetch associated books as a string and split into an array of titles
                String booksPurchased = rs.getString("books_purchased");
                List<Book> books = new ArrayList<>();

                if (booksPurchased != null && !booksPurchased.isEmpty()) {
                    String[] bookTitles = booksPurchased.split(", ");
                    int quantityPerBook = totalQuantity / bookTitles.length;

                    for (String title : bookTitles) {
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

    // Save order method
    @Override
    public void saveOrder(Order order, String username) throws SQLException {
        String query = "INSERT INTO " + ORDERS_TABLE + " (order_id, order_date_time, total_price, total_quantity, books_purchased, username) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            // Set order ID
            preparedStatement.setString(1, order.getOrderId());

            // Store LocalDateTime as a Timestamp in the database
            String formattedDateTime = order.getOrderDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            preparedStatement.setString(2, formattedDateTime);


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

            // Set the username
            preparedStatement.setString(6, username);

            preparedStatement.executeUpdate();
        }
    }

    // Get order by username method
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

                    // Retrieve the date from the database as a Timestamp
                    String orderDateTimeStr = rs.getString("order_date_time");
                    LocalDateTime orderDateTime = LocalDateTime.parse(orderDateTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

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
    

    // Parse books from a comma-separated string
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

}
