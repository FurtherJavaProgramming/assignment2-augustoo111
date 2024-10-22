package View;

import java.io.IOException;
import Controller.AdminDashboardController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class adminDashboardView {

    private Stage primaryStage;  // Reference to the main stage for scene switching
    private String username;

    // Constructor to initialize with login scene, model, and stage
    public adminDashboardView(Scene loginScene, Stage primaryStage, String username) {
        this.primaryStage = primaryStage;
        this.username = username;
    }

    // Load the admin dashboard scene
    public void setScene() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("admindashboardview.fxml"));

        try {
            TabPane root = loader.load();

            // Get the controller from the FXML loader
            AdminDashboardController controller = loader.getController();
            controller.setUsername(username);
            controller.setPrimaryStage(primaryStage);  

            // Create a new Scene for the admin dashboard
            Scene scene = new Scene(root);
            // Set the new scene on the primary stage
            primaryStage.setScene(scene);
            // Set the title for the admin dashboard
            primaryStage.setTitle("Admin Dashboard");

            // Show the stage with the updated scene and title
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
