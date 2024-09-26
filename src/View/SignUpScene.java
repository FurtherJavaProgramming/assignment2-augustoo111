package View;

import java.io.IOException;
import Controller.SignUpController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;

public class SignUpScene {

    private Scene loginScene;  // Scene to navigate back to login

    public SignUpScene(Scene loginScene) {
        this.loginScene = loginScene;
    }

    // Method to get the window title
    public String getTitle() {
        return "Sign Up";
    }

    // Method to load the SignUp scene
    public Scene getScene() {
        // Load the FXML file for the SignUp view
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/signupview.fxml"));

        try {
            // Load the FXML file and retrieve the root pane
            GridPane root = loader.load();

            // Get the controller associated with the FXML
            SignUpController controller = loader.getController();

            // Pass the login scene to the controller for navigation back
            controller.setLoginScene(loginScene);

            // Create and return a new Scene with the loaded AnchorPane
            return new Scene(root);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
