package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    private Order order;
    private List<Book> books;

    @BeforeEach
    public void setUp() {
        // Create a list of Book objects
        books = new ArrayList<>();
        books.add(new Book("JAVA: How to Program", "Deitel and Deitel", 10, 70.0, 100));
        books.add(new Book("Phthon Basics", "Brandon Perry", 1, 68.0, 50));
        books.add(new Book("Clean Code", "Robert Martin", 3, 45.0, 80));

        // Create an Order object with these books
        order = new Order("ORD1122889", books, 113.0, 14, LocalDateTime.now());
    }

    // Test case 1: Test the order ID
    @Test
    public void testGetOrderId() {
        assertEquals("ORD1122889", order.getOrderId(), "Order ID should be ORD1122889");
    }

    // Test case 2: Test the total price
    @Test
    public void testGetTotalPrice() {
        assertEquals(113.0, order.getTotalPrice(), 0.001, "Total price should be 113.0");
    }

    // Test case 3: Test the total quantity of books
    @Test
    public void testGetQuantity() {
        int expectedQuantity = 14;  // 10 + 1 + 3 = 14 total books
        assertEquals(expectedQuantity, order.getQuantity(), "Total quantity of books should be 14");
    }

    // Test case 4: Test the book titles concatenation
    @Test
    public void testGetBookTitles() {
        String expectedTitles = "JAVA: How to Program, Phthon Basics, Clean Code";
        assertEquals(expectedTitles, order.getBookTitles(), "Book titles should match the expected titles");
    }

    // Test case 5: Test that the order date is not null
    @Test
    public void testGetOrderDateTime() {
        assertNotNull(order.getOrderDateTime(), "Order date should not be null");
    }
}
