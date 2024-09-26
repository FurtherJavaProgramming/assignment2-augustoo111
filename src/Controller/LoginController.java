package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import View.SignUpScene;

public class LoginController {
	
	@FXML
	private TextField userNameTextField;
	
	@FXML
	private TextField passwordTextField;
	
	
	
//	@FXML
//	public void initialize() {
//		// this method has any code you want to run just right before the UI is displayed
//		areaLabel.setText("");
//	}
	 
	@FXML
	public void signUpButton(ActionEvent e) {
		
		Button source = (Button) e.getSource();
		Stage primaryStage = (Stage) source.getScene().getWindow();
		
		SignUpScene s2 = new SignUpScene(source.getScene());
		primaryStage.setTitle(s2.getTitle());
		primaryStage.setScene(s2.getScene());
		// how to access the primary stage in the Controller class?
		// the primary stage is in the Main class
		// Answer: create a set method in the controller class to
		// set the primary stage from Main
	}
	
	@FXML
	public void loginButton(ActionEvent e) {
		
		System.out.println("It is Work");
	}
	
	

}







