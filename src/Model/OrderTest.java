package model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class OrderTest {

    private Order order;
    private List<Book> books;

    @Before
    public void setUp() {
        // Create a list of Book objects
        books = new ArrayList<>();
        books.add(new Book("JAVA: How to Program", "Deitel and Deitel", 10, 70.0, 100));
        books.add(new Book("Python Basics", "Brandon Perry", 1, 68.0, 50));
        books.add(new Book("Clean Code", "Robert Martin", 3, 45.0, 80));

        // Create an Order object with these books
        order = new Order("ORD1122889", books, 183.0, 14, LocalDateTime.now());
    }

    @After
    public void tearDown() {
        books.clear();  // Clean up the books list after each test
    }

    // Test case 1: Test the order ID
    @Test
    public void testGetOrderId() {
        String orderID = "ORD1122889";
        assertEquals("Order ID should be ORD1122889", orderID, order.getOrderId());
    }

    // Test case 2: Test the total price
    @Test
    public void testGetTotalPrice() {
        double price = 183.0;
        assertEquals("Total price should be 183.0", price, order.getTotalPrice(), 0.001);
    }

    // Test case 3: Test the total quantity of books
    @Test
    public void testGetQuantity() {
        int expectedQuantity = 14;  // 10 + 1 + 3 = 14 total books
        assertEquals("Total quantity of books should be 14", expectedQuantity, order.getQuantity());
    }

    // Test case 4: Test the book titles concatenation
    @Test
    public void testGetBookTitles() {
        String expectedTitles = "JAVA: How to Program, Python Basics, Clean Code";  // Corrected title
        assertEquals("Book titles should match the expected titles", expectedTitles, order.getBookTitles());
    }

    // Test case 5: Test that the order date is not null
    @Test
    public void testGetOrderDateTime() {
        assertNotNull("Order date should not be null", order.getOrderDateTime());
    }
}
