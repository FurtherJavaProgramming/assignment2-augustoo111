package readingroom_2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the login screen (loginview.fxml)
        Parent root = FXMLLoader.load(getClass().getResource("/loginview.fxml"));
        //Parent root = FXMLLoader.load(getClass().getResource("/signupview.fxml"));

        // Set the scene with the login screen and show the stage
        Scene scene = new Scene(root);
        primaryStage.setTitle("Reading Room Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);
    }
}
