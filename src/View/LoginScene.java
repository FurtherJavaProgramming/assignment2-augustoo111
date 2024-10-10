package View;

import java.io.IOException;
import Controller.LoginController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Model;

public class LoginScene {

    private Model model;  // Reference to the model
    private Stage primaryStage;  // Reference to the primary stage

    // Constructor to pass the Model and Stage
    public LoginScene(Model model, Stage primaryStage) {
        this.model = model;
        this.primaryStage = primaryStage;
    }

    // Method to get the title of the login scene
    public String getTitle() {
        return "Log In";
    }

    // Method to load the login scene
    public Scene getScene() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("loginview.fxml"));

        try {
            GridPane root = loader.load();

            // Get the controller and set the model and primary stage
            LoginController loginController = loader.getController();
            loginController.setModel(model);
            loginController.setPrimaryStage(primaryStage);

            // Return the scene
            return new Scene(root);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
