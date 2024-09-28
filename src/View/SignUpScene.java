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

    public String getTitle() {
        return "Sign Up";
    }

    public Scene getScene() {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("signupview.fxml"));

        try {
            GridPane root = loader.load();
            SignUpController controller = loader.getController();
            controller.setLoginScene(loginScene);
            return new Scene(root);
            } catch (IOException e) {
            	e.printStackTrace();
            	return null;
            	}
        }
    
}
