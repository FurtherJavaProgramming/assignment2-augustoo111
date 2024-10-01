package Controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class ViewOrdersController {

    @FXML
    private TableView<?> ordersTableView; 

    private Scene accountScene;  

    // Set the account scene to go back to
    public void setAccountScene(Scene accountScene) {
        this.accountScene = accountScene;
    }

    // Method to handle going back to the account page
    @FXML
    public void handleBackToAccount() {
        Stage primaryStage = (Stage) ordersTableView.getScene().getWindow();
        primaryStage.setScene(accountScene);
        primaryStage.setTitle("Account");
        
    }

    // Method to handle exporting orders
    @FXML
    public void exportOrders() {
        System.out.println("Exporting all orders...");
    }
}
