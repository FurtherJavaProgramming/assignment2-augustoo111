package View;

import java.io.IOException;

import Controller.HomeSceneController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import model.Model;

public class HomeScene {
    private Scene loginScene;  // Scene to navigate back to login
    private Stage primaryStage;  // The main application stage
    private Model model;  // The model for application data

    // Constructor to pass the login scene, the primary stage, and the model
    public HomeScene(Scene loginScene, Stage primaryStage, Model model) {
        this.loginScene = loginScene;
        this.primaryStage = primaryStage;
        this.model = model;
    }

    public String getTitle() {
        return "Reading Room Book Store";
    }

    public Scene getScene() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("homeview.fxml"));

        try {
            TabPane root = loader.load();

            // Get the controller instance from the loader
            HomeSceneController controller = loader.getController();
            
            // Pass the login scene, primary stage, and model to the controller
            controller.setLoginScene(loginScene);
            controller.setPrimaryStage(primaryStage);
            controller.setModel(model);  // Pass the model to the controller
            
            return new Scene(root);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
