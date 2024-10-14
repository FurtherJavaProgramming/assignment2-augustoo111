package Controller;

import java.sql.SQLException;

import View.HomeScene;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class OrderDetailController {

    @FXML
    private TextField orderNumberField;  // Displays the order number

    @FXML
    private TextField totalItemsField;   // Displays total items in the order

    @FXML
    private TextField totalAmountField;  // Displays total amount of the order

    private String orderNumber;  // Unique order number (you can generate this)
    private int totalItems;      // Total items in the order
    private double totalAmount;  // Total order amount
    private Scene accountScene;
    private Stage primaryStage;
    private Scene homeScene;
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    // Set shopping cart scene
    public void setHomeScene(Scene homeScene) {
        this.homeScene = homeScene;
    }
    

    // Method to set order details (totalItems and totalAmount)
    public void setOrderDetails(int totalItems, double totalAmount) {
        this.orderNumber = generateOrderNumber(); // Generate a dummy order number
        this.totalItems = totalItems;
        this.totalAmount = totalAmount;

        // Update the fields with order details
        updateOrderDetails();
    }

    // Helper method to update the order fields with provided details
    private void updateOrderDetails() {
        orderNumberField.setText(orderNumber);  // Update order number
        totalItemsField.setText(String.valueOf(totalItems));  // Update total items
        totalAmountField.setText(String.format("%.2f AUD", totalAmount));  // Update total amount
    }

    private static int orderCounter = 1;  // Order starts from 1

	 // Helper method to generate a unique order number
	 private String generateOrderNumber() {
	     String orderNumber = "ORD" + orderCounter;  // Create an order number with a prefix
	     orderCounter++;  // Increment the counter for the next order
	     return orderNumber;
	 }
	 



    @FXML
    public void goBackToHome(ActionEvent e) throws SQLException {
    	Button backToHomeButton = (Button) e.getSource();
        Stage primaryStage = (Stage) backToHomeButton.getScene().getWindow();
		primaryStage.setScene(accountScene);
		HomeScene homeScene = new HomeScene(backToHomeButton.getScene());
        primaryStage.setTitle(homeScene.getTitle());
        primaryStage.setScene(homeScene.getScene());
        
    	
    }

}