package View;

import java.io.IOException;
import Controller.AdminDashboardController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import model.Model;

public class adminDashboardView {

    private Scene loginScene;
    private Model model;
    private Stage primaryStage;  // Reference to the main stage for scene switching

    // Constructor to initialize with login scene, model, and stage
    public adminDashboardView(Scene loginScene, Model model, Stage primaryStage) {
        this.loginScene = loginScene;
        this.model = model;
        this.primaryStage = primaryStage;
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

            // Set necessary references in the controller
            controller.setLoginScene(loginScene);  // Set login scene for logout functionality
            controller.setModel(model);  // Pass the model to interact with the backend
            controller.setPrimaryStage(primaryStage);  // Set the primary stage for scene switching

            return new Scene(root);  // Return the scene
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
