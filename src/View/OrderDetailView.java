package View;

import java.io.IOException;

import Controller.OrderDetailController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class OrderDetailView {

    private Scene homeScene;
    private int totalItems;      // Number of items in the cart
    private double totalAmount;  // Total amount of the cart
    private Stage primaryStage;  // Reference to the primary stage

    public OrderDetailView(Scene homeScene) {
        this.homeScene = homeScene;
    }

 // Constructor for CheckOutView
    public OrderDetailView(Scene homeScene, int totalItems, double totalAmount, Stage primaryStage) {
        this.homeScene = homeScene;
        this.totalItems = totalItems;
        this.totalAmount = totalAmount;
        this.primaryStage = primaryStage;  // Pass and store the primary stage
    }

    // Pass totalItems and totalAmount to the OrderDetailController
    public Scene getScene() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("orderDetail.fxml"));

        try {
            GridPane root = loader.load();
            OrderDetailController controller = loader.getController();
            // Set the shopping cart scene in the controller
            controller.setHomeScene(homeScene);
            
            // Set the primary stage in the controller
            controller.setPrimaryStage(primaryStage);
            
            // Pass totalItems and totalAmount to the controller
            controller.setOrderDetails(totalItems, totalAmount);
            
            return new Scene(root);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getTitle() {
        return "Order Details";
    }
}
