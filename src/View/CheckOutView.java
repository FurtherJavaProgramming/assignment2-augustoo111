package View;

import java.io.IOException;

import Controller.CheckOutController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;  // Add import for Stage

public class CheckOutView {

    private Scene shoppingCart;  // Scene to navigate back to cart
    private int totalItems;      // Number of items in the cart
    private double totalAmount;  // Total amount of the cart
    private Stage primaryStage;  // Reference to the primary stage

    // Constructor for CheckOutView
    public CheckOutView(Scene shoppingCart, int totalItems, double totalAmount, Stage primaryStage) {
        this.shoppingCart = shoppingCart;
        this.totalItems = totalItems;
        this.totalAmount = totalAmount;
        this.primaryStage = primaryStage;  // Pass and store the primary stage
    }

    public String getTitle() {
        return "Check Out";
    }

    public Scene getScene() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("checkOutView.fxml"));

        try {
            GridPane root = loader.load();
            CheckOutController controller = loader.getController();
            
            // Set the shopping cart scene in the controller
            controller.setShoppingCartScene(shoppingCart);
            
            // Set the primary stage in the controller
            controller.setPrimaryStage(primaryStage);
            
            // Pass the totalItems and totalAmount to the controller
            controller.setCartDetails(totalItems, totalAmount);
            
            return new Scene(root);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
