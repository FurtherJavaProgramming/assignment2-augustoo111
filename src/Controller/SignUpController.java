package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SignUpController {
	 @FXML
	 private TextField firstNameField;

	    @FXML
	    private TextField lastNameField;

	    @FXML
	    private TextField userNameField;

	    @FXML
	    private TextField passwordField;

	    @FXML
	    private TextField confirmPasswordField;

		private Scene loginScene;
		
		public void setLoginScene(Scene loginScene) {
			this.loginScene = loginScene;
		}
		
		@FXML
		public void signUp(ActionEvent e) {
			System.out.println("Successfully Sign up");
			
			Button source = (Button) e.getSource();
			Stage primaryStage = (Stage) source.getScene().getWindow();
			primaryStage.setScene(loginScene);
			
		}




}
