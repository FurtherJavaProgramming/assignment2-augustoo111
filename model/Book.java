package model;


/**
 * This class represents a book with details used for both user and admin views.
 */
public class Book implements ShoppingCart {
    private String title;
    private String author;
    private int noOfCopies;
    private double price;
    private int soldCopies;
    private double totalPrice;

    // Constructor
    public Book(String title, String author, int noOfCopies, double price, int soldCopies) {
        this.title = title;
        this.author = author;
        this.noOfCopies = noOfCopies;
        this.price = price;
        this.soldCopies = soldCopies;
        this.totalPrice = price * noOfCopies;
    }
    
    public Book(String title, int noOfCopies, double price) {
        this.title = title;
        this.noOfCopies = noOfCopies;
        this.price = price;

    }
    

    // Getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getNoOfCopies() {
        return noOfCopies;
    }

    public void setNoOfCopies(int noOfCopies) {
        this.noOfCopies = noOfCopies;
    }

    @Override
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public int getSoldCopies() {
        return soldCopies;
    }

    @Override
    public void setSoldCopies(int soldCopies) {
        this.soldCopies = soldCopies;
    }
    
    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    // Implementing getDetail() method from ShoppingCart interface
    @Override
    public String getDetail() {
        return "BOOK TITLE: " + title + " | AUTHORS: " + author + " | Physical Copies: " + noOfCopies +
               " | Price: " + price + " | Sold Copies: " + soldCopies;
    }

    // toString method override
    @Override
    public String toString() {
        return getDetail();
    }
}