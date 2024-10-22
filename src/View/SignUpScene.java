package View;

import java.io.IOException;
import Controller.SignUpController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Model;

public class SignUpScene {
    
    private Scene loginScene;  // Scene to navigate back to login
    private Model model;       // The Model to pass to the SignUpController
    private Stage primaryStage;  // Primary stage for scene management

    // Constructor to initialize loginScene, Model, and PrimaryStage
    public SignUpScene(Scene loginScene, Model model, Stage primaryStage) {
        this.loginScene = loginScene;
        this.model = model;
        this.primaryStage = primaryStage;
    }

    // Title for the Sign Up Scene
    public String getTitle() {
        return "Sign Up";
    }

    // Method to load the scene
    public Scene getScene() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("signupview.fxml"));

        try {
            GridPane root = loader.load();
            primaryStage.setTitle(getTitle());
            
            // Get the controller and set the required fields
            SignUpController controller = loader.getController();
            controller.setLoginScene(loginScene);   // Set the login scene to navigate back
            controller.setModel(model);             // Set the model for database interactions
            controller.setPrimaryStage(primaryStage); // Set the primary stage to manage scene switching

            // Return the constructed Scene
            return new Scene(root);
            
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
