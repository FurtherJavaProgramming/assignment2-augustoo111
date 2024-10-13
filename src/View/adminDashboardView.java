package View;

import java.io.IOException;
import Controller.AdminDashboardController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class adminDashboardView {

    private Scene loginScene;
    private Stage primaryStage;  // Reference to the main stage for scene switching
    private String username;

    // Constructor to initialize with login scene, model, and stage
    public adminDashboardView(Scene loginScene, Stage primaryStage, String username) {
        this.loginScene = loginScene;
        this.primaryStage = primaryStage;
        this.username = username;
    }

    // Title for the admin dashboard
    public String getTitle() {
        return "Reading Room Book Store Admin";
    }

    // Load the admin dashboard scene
    public Scene getScene() {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("admindashboardview.fxml"));

        try {
            TabPane root = loader.load();

            // Get the controller from the FXML loader
            AdminDashboardController controller = loader.getController();
            // Pass the username to the controller
            controller.setUsername(username); 

            // Set necessary references in the controller
            controller.setLoginScene(loginScene);  // Set login scene for logout functionality
            controller.setPrimaryStage(primaryStage);  // Set the primary stage for scene switching

            return new Scene(root);  // Return the scene
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
