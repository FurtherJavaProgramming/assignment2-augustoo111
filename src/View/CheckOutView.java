package View;

import java.io.IOException;
import Controller.CheckOutController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Model;

public class CheckOutView {

    private Scene shoppingCart;  // Scene to navigate back to cart
    private int totalItems;      // Number of items in the cart
    private double totalAmount;  // Total amount of the cart
    private Stage primaryStage;  // Reference to the primary stage
    private Scene homeScene;     // Home scene for navigation
    private Model model;         // Model instance for passing

    // Constructor for CheckOutView
    public CheckOutView(Scene shoppingCart, int totalItems, double totalAmount, Stage primaryStage, Scene homeScene, Model model) {
        this.shoppingCart = shoppingCart;
        this.totalItems = totalItems;
        this.totalAmount = totalAmount;
        this.primaryStage = primaryStage;  // Pass and store the primary stage
        this.homeScene = homeScene;        // Initialize homeScene
        this.model = model;                // Initialize model
    }

    // Set the scene and update the title
    public void setScene() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/checkOutView.fxml"));

        try {
            GridPane root = loader.load();
            CheckOutController controller = loader.getController();
            
            // Set the shopping cart scene in the controller
            controller.setShoppingCartScene(shoppingCart);
            
            // Set the primary stage in the controller
            controller.setPrimaryStage(primaryStage);
            
            // Set the model in the controller
            controller.setModel(model);
            
            // Set the homeScene in the controller
            if (homeScene != null) {
                controller.setHomeScene(homeScene);  // Pass homeScene if initialized
            } else {
                System.out.println("Warning: homeScene is null in CheckOutView!");
            }

            // Pass the totalItems and totalAmount to the controller
            controller.setCartDetails(totalItems, totalAmount);
            
            // Create the scene
            Scene checkOutScene = new Scene(root);
            
            // Set the new scene on the primary stage
            primaryStage.setScene(checkOutScene);
            // Show the updated stage with the new scene and title
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
