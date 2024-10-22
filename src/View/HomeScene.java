package View;

import java.io.IOException;
import java.sql.SQLException;
import Controller.HomeSceneController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import model.Model;

public class HomeScene {

    private Scene loginScene;
    private Stage primaryStage;  // Reference to the main stage for scene switching
    private String username;  // Username to display
    private Model model;  // Reference to the Model instance
    
    // Constructor to initialize with login scene, model, stage, and username
    public HomeScene(Scene loginScene, Model model, Stage primaryStage, String username) {
        this.loginScene = loginScene;
        this.model = model;  // Store the model
        this.primaryStage = primaryStage;
        this.username = username;  // Store the username
    }

    // Method to set the scene and update the title dynamically
    public void setScene() throws SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("homeview.fxml"));

        try {
            TabPane root = loader.load();

            // Get the controller from the FXML loader
            HomeSceneController homeController = loader.getController();
            
            // Set necessary references in the controller
            homeController.setUsername(username); 
            homeController.setLoginScene(loginScene);
            homeController.setPrimaryStage(primaryStage);
            homeController.setModel(model);

            // Optionally select default tabs (if necessary)
            homeController.selectShoppingCartTab();
            homeController.selectHomeTab();
            homeController.selectAccountTab();

            // Create the scene for the home view
            Scene homeScene = new Scene(root);

            // Set the new scene on the primary stage
            primaryStage.setScene(homeScene);
            // Show the updated stage with the new scene and title
            primaryStage.show();
        } catch (IOException e) {
            // Handle IOException with a meaningful error message
            System.err.println("Error loading homeview.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
