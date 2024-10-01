package View;

import java.io.IOException;

import Controller.AdminDashboardController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;

public class adminDashboardView {

    private Scene loginScene; 

    public String getTitle() {
        return "Reading Room Book Store Admin";
    }

    public adminDashboardView(Scene loginScene) {
        this.loginScene = loginScene;
        
    }


    public Scene getScene() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("admindashboardview.fxml"));

        try {
            TabPane root = loader.load();
            AdminDashboardController controller = loader.getController();
            controller.setLoginScene(loginScene);
            return new Scene(root);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}