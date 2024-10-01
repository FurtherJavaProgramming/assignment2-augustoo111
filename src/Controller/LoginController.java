package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


import View.HomeScene;
import View.SignUpScene;
import View.adminDashboardView;

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
	// Admin credentials
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "reading_admin";

	 
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
		String enteredUsername = userNameTextField.getText();
        String enteredPassword = passwordTextField.getText();

        Button source = (Button) e.getSource();
        Stage primaryStage = (Stage) source.getScene().getWindow();
     // Check if the user is an admin
        if (isAdmin(enteredUsername, enteredPassword)) {
            // If the user is an admin, navigate to the Admin Dashboard
            adminDashboardView adminDashboardScene = new adminDashboardView(source.getScene());
            primaryStage.setTitle(adminDashboardScene.getTitle());
            primaryStage.setScene(adminDashboardScene.getScene());
        } else {
            // If the user is not an admin, navigate to the normal Home scene
            HomeScene homeScene = new HomeScene(source.getScene());
            primaryStage.setTitle(homeScene.getTitle());
            primaryStage.setScene(homeScene.getScene());
        }
            
	}
	//method to chat admin credentials
	private boolean isAdmin(String username, String password) {
        return ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password);
    }

	
	

}







