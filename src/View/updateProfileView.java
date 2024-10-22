package View;

import java.io.IOException;

import Controller.UpdateDetailsController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class updateProfileView {

	private Scene myAccount;  // Scene to navigate back to login
	private Stage primaryStage;
    
    public updateProfileView(Scene myAccount) {
    	this.myAccount = myAccount;
    }

    public String getTitle() {
        return "Update Profile";
    }

    public Scene getScene() {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("updateProfile.fxml"));

        try {
            GridPane root = loader.load();
            UpdateDetailsController controller = loader.getController();
            controller.setAccountScene(myAccount);
            primaryStage.setTitle(getTitle());
            return new Scene(root);
            } catch (IOException e) {
            	e.printStackTrace();
            	return null;
            	}
        }

}
