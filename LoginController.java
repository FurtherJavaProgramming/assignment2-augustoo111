package readingroom_2;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginController implements Initializable {

    @FXML
    private TextField usernameInput;

    @FXML
    private TextField passwordInput;

    @FXML
    private Button loginButton;

    @FXML
    private Button createAccountButton;
    
	private Stage stage;


    // Handle the login button action
    
    @FXML
    public void Initialize(URL location, ResourceBundle resources) {
    	loginButton.setOnAction(event -> {
    		
    	});
    }


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
    
	
}
