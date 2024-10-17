package Controller;

import java.io.IOException;
import java.sql.SQLException;

import Dao.BookDao;
import Dao.BookDaoImplementation;
import Dao.UserDao;
import Dao.UserDaoImplementation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Model;

public class OrderDetailController {

    @FXML
    private TextField orderNumberField;  // Displays the order number

    @FXML
    private TextField totalItemsField;   // Displays total items in the order

    @FXML
    private TextField totalAmountField;  // Displays total amount of the order
    
    @FXML
    private Button backToHomeButton;

    private String orderNumber;  // Unique order number (you can generate this)
    private int totalItems;      // Total items in the order
    private double totalAmount;  // Total order amount

    private Stage primaryStage;
    private Scene homeScene;
    private Model model;
    private HomeSceneController homeSceneController;
    @FXML
    private Label message;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        if (primaryStage == null) {
            System.out.println("Warning: primaryStage is null when set in OrderDetailController!");
        } else {
            System.out.println("primaryStage has been set successfully in OrderDetailController.");
        }
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public void setHomeScene(Scene homeScene) {
        this.homeScene = homeScene;
        if (homeScene == null) {
            System.out.println("Warning: homeScene is null when set in OrderDetailController!");
        } else {
            System.out.println("homeScene has been set successfully in OrderDetailController.");
        }
    }

    public void setHomeSceneController(HomeSceneController homeSceneController) {
        this.homeSceneController = homeSceneController;
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
    private void goBackToHome() throws SQLException {
        try {
            // Load FXML and controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/homeview.fxml"));
            TabPane root = loader.load();

            // Get controller instance from the FXMLLoader
            HomeSceneController homeSceneController = loader.getController();

            // Set DAOs in the controller
            BookDao bookDao = new BookDaoImplementation();
            homeSceneController.setBookDao(bookDao);

            UserDao userDao = new UserDaoImplementation();
            homeSceneController.setUserDao(userDao);

            // Set primary stage, loginScene and username
            homeSceneController.setPrimaryStage(primaryStage);
            homeSceneController.setHomeScene(homeScene);
            homeSceneController.setModel(model);  // Ensure model is passed
            homeSceneController.setLoginScene(primaryStage.getScene());
            homeSceneController.setUsername(model.getCurrentUser().getUsername());  // Pass the current user's username

            // Set the scene
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            showErrorMessage(e.getMessage());
        }
    }

    // Method to display error messages
    private void showErrorMessage(String messageText) {
		message.setText(messageText);
        message.setTextFill(Color.RED);
    }
}
