package Controller;


import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AdminDashboardController {

    @FXML
    private TableView<?> stockTableView; // Table to display all books

    @FXML
    private TextField selectedBookField;

    @FXML
    private TextField stockUpdateField;
    @FXML
    private Button logOutButton;

	private Scene loginScene;

    @FXML
    public void initialize() {
        // Initialize the table and other elements
    }
    
    public void setLoginScene(Scene loginScene) {
        this.loginScene = loginScene;
    }
    


    @FXML
    public void updateStock() {
        // Logic to update the stock of the selected book
        String selectedBook = selectedBookField.getText();
        String updatedStock = stockUpdateField.getText();
        System.out.println("Updated stock for " + selectedBook + ": " + updatedStock);
    }
    
    @FXML
    public void logOut() {
        // Handle logout action
        System.out.println("User logged out");
        if (loginScene != null) {
            Stage primaryStage = (Stage) logOutButton.getScene().getWindow();
            primaryStage.setScene(loginScene);
        }
    }

}
