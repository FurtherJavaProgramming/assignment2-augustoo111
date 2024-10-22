package View;

import java.io.IOException;

import Controller.UpdatePasswordController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class updatePasswordView {

	private Scene myAccount;  // Scene to navigate back to login
	private Stage primaryStage;
    
    public updatePasswordView(Scene myAccount) {
    	this.myAccount = myAccount;
    }
    

    public String getTitle() {
        return "Update Password";
    }

    public Scene getScene() {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("updatePassword.fxml"));

        try {
            GridPane root = loader.load();
            UpdatePasswordController controller = loader.getController();
            controller.setAccountScene(myAccount);
            
			primaryStage.setTitle(getTitle());
            return new Scene(root);
            } catch (IOException e) {
            	e.printStackTrace();
            	return null;
            	}
        }

}
