package View;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;

public class LoginScene {

	public String getTitle() {
		return "Log In";
	}
	
	public Scene getScene() {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("loginview.fxml"));
		
		try {
			GridPane root = loader.load();
			return new Scene(root);
		} catch(IOException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
}
