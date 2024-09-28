package View;


import java.io.IOException;

import Controller.HomeSceneController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;

public class HomeScene {
    private Scene loginScene; 

    public String getTitle() {
        return "Reading Room Book Store";
    }

    public HomeScene(Scene loginScene) {
        this.loginScene = loginScene;
        
    }


    public Scene getScene() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("homeview.fxml"));

        try {
            TabPane root = loader.load();
            HomeSceneController controller = loader.getController();
            controller.setLoginScene(loginScene);
            return new Scene(root);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
