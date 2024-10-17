package Controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import View.CheckOutView;
import View.OrderDetailView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Book;
import model.Model;
import javafx.scene.Scene;
import Dao.BookDao;
import Dao.BookDaoImplementation;

public class CheckOutController {

    @FXML
    private TextField nameOnCardField, cardNumberField, securityCodeField, totalItem, totalAmount;

    @FXML
    private ChoiceBox<String> expiryMonthChoiceBox, expiryYearChoiceBox;

    @FXML
    private Label totalItemLabel;

    @FXML
    private Button placeOrderButton, goBackToCartButton, addCardButton;
    private boolean isCardAdded = false;

    private Scene shoppingCartScene;  // Reference to the shopping cart scene
    private int totalItems;           // Total items in the cart
    private double totalAmountValue;  // Total amount of the order
    private Stage primaryStage; 
    private BookDao bookDao;
    private ArrayList<Book> cartItems = new ArrayList<>();
	private HomeSceneController homeSceneController;
	private Scene homeScene;
	private TableView<Book> bookStockTable;

	private Model model;
	
    //set primary stage
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    
    public void setBookDao(BookDao bookDao) {
        this.bookDao = bookDao;
    }
	public void setHomeController(HomeSceneController homeSceneController) {
		this.homeSceneController = homeSceneController;
		
	}

	public void setModel(Model model) {
		this.model = model;
		
	}

    
    // Set shopping cart scene
    public void setShoppingCartScene(Scene shoppingCartScene) {
        this.shoppingCartScene = shoppingCartScene;
    }
    
    //set Home scene
    public void setHomeScene(Scene homeScene) {
        this.homeScene = homeScene;
        System.out.println("Home scene set in OrderDetailController: " + homeScene);  // Debugging log
    }



    // Set total items and total amount from the shopping cart
    public void setCartDetails(int totalItems, double totalAmountValue) {
        this.totalItems = totalItems;
        this.totalAmountValue = totalAmountValue;
        updateCartSummary();  // Update the summary labels and fields
        
    }

    // Update cart summary on the checkout page
    private void updateCartSummary() {
        totalItem.setText(String.valueOf(totalItems));  // Set total items in the field
        totalAmount.setText(String.format("%.2f", totalAmountValue) + " AUD");  // Set total amount in the field
    }

    @FXML
    public void initialize() throws SQLException {
    	this.bookDao = new BookDaoImplementation();
        // Populate expiry month and year choice boxes
        expiryMonthChoiceBox.getItems().addAll("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12");
        expiryYearChoiceBox.getItems().addAll("2024", "2025", "2026", "2027", "2028", "2029", "2030");
        
    }

    // Place order method
    @FXML
    public void placeOrder() {
    	
        // Check if the card was already added
        if (isCardAdded) {
        	if (cartItems.isEmpty()) {
        	    System.out.println("Error: No items in the cart to update.");
        	} else {
        	    for (Book book : cartItems) {
        	        System.out.println("Book in cart: " + book.getTitle() + ", Quantity: " + book.getNoOfCopies());
        	    }
        	}

            // If the card was added, proceed with placing the order
            showAlert(Alert.AlertType.INFORMATION, "Order Placed", "Your order has been placed successfully!");

            //update stock available copies
            updateBookInventory();
            // Load the Order Detail scene
            
            // Clear the cart
            
            totalItem.clear();
            totalAmount.clear();
            clearCartItems();
            loadOrderDetailScene();
            
        } else {
            // If the card has not been added, validate the payment details as usual
            String nameOnCard = nameOnCardField.getText();
            String cardNumber = cardNumberField.getText();
            String expiryMonth = expiryMonthChoiceBox.getValue();
            String expiryYear = expiryYearChoiceBox.getValue();
            String securityCode = securityCodeField.getText();

            // Validate payment details
            if (validatePaymentDetails(nameOnCard, cardNumber, expiryMonth, expiryYear, securityCode)) {
                showAlert(Alert.AlertType.INFORMATION, "Order Placed", "Your order has been placed successfully!");

             // Update stock, clear cart, and load order details
                updateBookInventory();
                totalItem.clear();
                totalAmount.clear();
                clearCartItems();
                loadOrderDetailScene();
                
            } else {
                showAlert(Alert.AlertType.ERROR, "Invalid Payment Details", "Please correct the payment details and try again.");
            }
        }
       
        
    }



    private void clearCartItems() {
        cartItems.clear();  // Clear the cart items list
        homeSceneController.clearCart();  // Clear the cart in the HomeSceneController

        // Optionally show a message indicating the cart was cleared
        System.out.println("Cart items have been cleared after placing the order.");
    }
 // In HomeSceneController
    public Book findBookInStock(Book selectedBook) {
		for (Book book : bookStockTable.getItems()) {
            if (book.getTitle().equals(selectedBook.getTitle()) && book.getAuthor().equals(selectedBook.getAuthor())) {
                return book;
            }
        }
        return null;
    }

 // Update the book stock in HomeSceneController
    private void updateBookInventory() {
        if (bookDao == null) {
            System.out.println("Error: bookDao is not initialized.");
            return;
        }

        if (cartItems == null || cartItems.isEmpty()) {
            System.out.println("Error: No items in the cart to update.");
            return;
        }

        for (Book bookInCart : cartItems) {
            // Find the corresponding book in the inventory
            Book bookInInventory = homeSceneController.findBookInStock(bookInCart);

            if (bookInInventory != null) {
                System.out.println("Updating stock for book: " + bookInInventory.getTitle());

                // Subtract the quantity purchased from the available stock
                int newStock = bookInInventory.getNoOfCopies() - bookInCart.getNoOfCopies();
                bookInInventory.setNoOfCopies(newStock);

                // Update the sold copies
                int newSoldCopies = bookInInventory.getSoldCopies() + bookInCart.getNoOfCopies();
                bookInInventory.setSoldCopies(newSoldCopies);

                // Update the book stock in the database
                try {
                    bookDao.updateBookStockbyUser(bookInInventory);  // Persist changes in the database
                    System.out.println("Stock updated in database for: " + bookInInventory.getTitle());
                } catch (SQLException e) {
                    System.out.println("Failed to update stock for: " + bookInInventory.getTitle());
                    e.printStackTrace();
                }
            } else {
                System.out.println("Book not found in inventory: " + bookInCart.getTitle());
            }
        }
    }

    public void setCartItems(ArrayList<Book> cartItems) {
        this.cartItems = cartItems;
        // Print cart items for debugging
        for (Book book : cartItems) {
            System.out.println("Received cart item: " + book.getTitle() + ", Quantity: " + book.getNoOfCopies());
        }
    }

    @FXML
    public void loadOrderDetailScene() {
        try {
            // Load the Order Detail FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/orderDetail.fxml"));
            GridPane orderDetailPane = loader.load();

            // Get the OrderDetailController from the FXML loader
            OrderDetailController orderDetailController = loader.getController();

            // Set order details in OrderDetailController
            orderDetailController.setOrderDetails(totalItems, totalAmountValue);

            // Pass primaryStage and homeScene to OrderDetailController
            if (primaryStage != null) {
                orderDetailController.setPrimaryStage(primaryStage);  // Set primaryStage
                System.out.println("PrimaryStage has been passed successfully to OrderDetailController.");
            } else {
                System.out.println("Error: primaryStage is null when trying to set in OrderDetailController.");
            }

            // Pass homeScene and check if it's null
            if (homeScene != null) {
                orderDetailController.setHomeScene(homeScene);  // Set homeScene
                System.out.println("HomeScene has been passed successfully to OrderDetailController.");
            } else {
                System.out.println("Warning: homeScene is null when trying to set in OrderDetailController.");
            }

            // Switch to the order detail scene
            Scene orderDetailScene = new Scene(orderDetailPane);
            orderDetailController.setModel(model);
            primaryStage.setScene(orderDetailScene);  // Set the new scene for the primary stage
            primaryStage.show();  // Make sure the stage is displayed

        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Error: Unable to load OrderDetailView scene.");
        }
    }



 // Helper method to validate the payment details
    private boolean validatePaymentDetails(String nameOnCard, String cardNumber, String expiryMonth, String expiryYear, String securityCode) {
        // Validate card number length and security code length
        if (nameOnCard.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Name on card cannot be empty.");
            return false;
        }

        if (cardNumber.length() != 16) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Card number must be 16 digits.");
            return false;
        }

        if (securityCode.length() != 3) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Security code must be 3 digits.");
            return false;
        }

        // Ensure the expiry month and year are selected
        if (expiryMonth == null || expiryYear == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please select a valid expiry date.");
            return false;
        }

        // If all validations pass, return true
        return true;
    }

  
    
//    @FXML
    public void showItemTotal() {
        // Calculate total items and total amount
        int totalItemsInCart = calculateTotalItems();  // Method to count total items
        double totalAmountInCart = calculateTotalAmount();  // Method to calculate total amount

        // Load the OrderDetailView and pass the cart details
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/orderDetail.fxml"));
            GridPane orderDetailPane = loader.load();

            // Get controller associated with the view
            OrderDetailController orderDetailController = loader.getController();
            Stage primaryStage = (Stage) placeOrderButton.getScene().getWindow();
            // Pass totalItems and totalAmount to OrderDetailController
            orderDetailController.setOrderDetails(totalItemsInCart, totalAmountInCart);

            // Create a new scene and set it to the primary stage
            Scene orderDetailScene = new Scene(orderDetailPane);
            primaryStage.setScene(orderDetailScene);
            primaryStage.setTitle(primaryStage.getTitle());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    
    
    // Method to calculate total amount
    private double calculateTotalAmount() {
        double total = 0;
        for (Book book : cartItems) {
            total += book.getTotalPrice();  // Assuming Book has a getTotalPrice() method
        }
        return total;
    }
    
   // Method to calculate the total quantity of books in the cart
    private int calculateTotalItems() {
        int totalQuantity = 0;
        for (Book book : cartItems) {
            totalQuantity += book.getNoOfCopies();  // Sum the quantities of each book
        }
        return totalQuantity;
    }
    


    // Go back to the shopping cart method
    @FXML
    public void goBackToCart() {
        Stage primaryStage = (Stage) goBackToCartButton.getScene().getWindow();

        // Load to the existing shoppingCartScene
        primaryStage.setScene(shoppingCartScene);  // Return to the shopping cart scene

        // Optional: Ensure that the cart tab is selected if required
        try {
            // Assuming HomeSceneController is already set up with shoppingCartScene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/homeview.fxml"));
            loader.load();
            HomeSceneController homeController = loader.getController();
            homeController.selectShoppingCartTab();  // Switch to cart tab in the home scene
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    


    // Add card method (for demo purposes)
    @FXML
    public void addCard() {
        // Show confirmation message with the last 4 digits of the card number
        showAlert(Alert.AlertType.INFORMATION, "Card Added", "Card ending in " + 
        cardNumberField.getText().substring(cardNumberField.getText().length() - 4) + " has been added.");
        // Mark the card as added
        isCardAdded = true;
        
        // Clear the input fields after the card has been added
        nameOnCardField.clear();
        cardNumberField.clear();
        securityCodeField.clear();
        // Set the choice boxes to null (no selection)
        expiryMonthChoiceBox.setValue(null);
        expiryYearChoiceBox.setValue(null);        
        
    }


    // Helper method to show alerts for user notifications
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }




}
