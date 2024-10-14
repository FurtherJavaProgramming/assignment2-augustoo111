package Controller;

import java.io.IOException;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Book;
import javafx.scene.Scene;

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
    
    private ArrayList<Book> cartItems = new ArrayList<>();

	private HomeSceneController homeSceneController;
    //set primary stage
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    // Set shopping cart scene
    public void setShoppingCartScene(Scene shoppingCartScene) {
        this.shoppingCartScene = shoppingCartScene;
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
    public void initialize() {
        // Populate expiry month and year choice boxes
        expiryMonthChoiceBox.getItems().addAll("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12");
        expiryYearChoiceBox.getItems().addAll("2024", "2025", "2026", "2027", "2028", "2029", "2030");
        updateCartSummary();
    }

    // Place order method
    @FXML
    public void placeOrder() {
        // Check if the card was already added
        if (isCardAdded) {
            // If the card was added, proceed with placing the order
            showAlert(Alert.AlertType.INFORMATION, "Order Placed", "Your order has been placed successfully!");

            // Load the Order Detail scene
            loadOrderDetailScen();
            
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

                // Load the Order Detail scene
                loadOrderDetailScen();
            } else {
                showAlert(Alert.AlertType.ERROR, "Invalid Payment Details", "Please correct the payment details and try again.");
            }
        }
        
    }
    public void loadOrderDetailScen() {
    	try {
            // Load the Order Detail FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/orderDetail.fxml"));
            GridPane orderDetailPane = loader.load();

            // Get the OrderDetailController and pass the total items and total amount
            OrderDetailController orderDetailController = loader.getController();
            orderDetailController.setOrderDetails(totalItems, totalAmountValue);  // Pass total items and total amount

            // Switch to the order detail scene
            Scene orderDetailScene = new Scene(orderDetailPane);
            Stage primaryStage = (Stage) placeOrderButton.getScene().getWindow();
            primaryStage.setScene(orderDetailScene);
            primaryStage.setTitle("Order Details");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
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

        // Instead of reloading homeview.fxml, just use the existing shoppingCartScene
        primaryStage.setScene(shoppingCartScene);  // Return to the shopping cart scene
        primaryStage.setTitle("Shopping Cart");

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

	public void setHomeController(HomeSceneController homeSceneController) {
		this.homeSceneController = homeSceneController;
		
	}
}
