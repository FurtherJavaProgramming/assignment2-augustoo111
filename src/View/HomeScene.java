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

    // Title for the user dashboard
    public String getTitle() {
        return "Reading Room Book Store";
    }

    // Load the home scene
    public Scene getScene() throws SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("homeview.fxml"));

        try {
            TabPane root = loader.load();

            // Get the controller from the FXML loader
            HomeSceneController homeController = loader.getController();

            // Pass the username to the controller
            homeController.setUsername(username); 
            
            // Set necessary references in the controller
            homeController.setLoginScene(loginScene);  // Set login scene for logout functionality
            homeController.setPrimaryStage(primaryStage);  // Set the primary stage for scene switching
            // **Set the Model in the controller**
            homeController.setModel(model);  // Pass the model instance to the controller
            
            homeController.selectShoppingCartTab(); //set shoppingcart tab for scene switching
            homeController.selectHomeTab(); //set Home Tab
            homeController.selectAccountTab(); //selecet account tab

            return new Scene(root);  // Return the scene
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
