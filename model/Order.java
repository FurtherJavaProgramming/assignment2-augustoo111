package model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class Order {
    private String orderId;
    private LocalDateTime orderDateTime;
    private double totalPrice;
    private List<Book> books;  // List of Book objects

    // Constructor
    public Order(String orderId, List<Book> books, double totalPrice, int totalQuantity, LocalDateTime orderDateTime) {
        this.orderId = orderId;
        this.orderDateTime = orderDateTime;
        this.totalPrice = totalPrice;
        this.books = books;  // List of Book objects
    }

    // Getters
    public String getOrderId() {
        return orderId;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public List<Book> getBooks() {
        return books;
    }

    // Method to return the book titles as a single string
    public String getBookTitles() {
        if (books == null || books.isEmpty()) {
            return "No Books";
        }
        // Concatenate the book titles into a single string
        return books.stream()
                    .map(Book::getTitle)
                    .collect(Collectors.joining(", "));
    }

    // Method to return the total quantity of books in the order
    public int getQuantity() {
        int totalQuantity = 0;

        // Sum the quantity of each book in the list
        if (books != null) {
            for (Book book : books) {
                totalQuantity += book.getNoOfCopies(); 
            }
        }

        return totalQuantity;
    }
}
