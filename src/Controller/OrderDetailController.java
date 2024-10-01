package Controller;


import View.HomeScene;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;


public class OrderDetailController {

    @FXML
    private TextField orderNumberField;

    @FXML
    private TextField totalItemsField;

    @FXML
    private TextField totalAmountField;
    
    @FXML
    private Button backToHomeButton;

	private Scene accountScene;
	
	public void setAccountScene(Scene accountScene) {
        this.accountScene = accountScene;
    }

    @FXML
    public void initialize() {
    	
        
    }

    // Method to handle "Back to Home" button click
    @FXML
    public void goBackToHome(ActionEvent e) {
    	Button backToHomeButton = (Button) e.getSource();
        Stage primaryStage = (Stage) backToHomeButton.getScene().getWindow();
		primaryStage.setScene(accountScene);
		HomeScene homeScene = new HomeScene(backToHomeButton.getScene());
        primaryStage.setTitle(homeScene.getTitle());
        primaryStage.setScene(homeScene.getScene());
        
    	
    }
}

