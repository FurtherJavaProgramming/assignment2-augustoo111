package Controller;



import View.CheckOutView;
import View.ViewOrderView;
import View.updatePasswordView;
import View.updateProfileView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class HomeSceneController {

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab homeTab;

    @FXML
    private Tab accountTab;

    @FXML
    private Tab cartTab;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    

    @FXML
    private Button continueShoppingButton;

    @FXML
    private Button checkOutButton;


    @FXML
    private Label userNameLabel;

    @FXML
    private TableView<String> cartListView; 

    @FXML
    private TextField selectedBookField;

    @FXML
    private TextField quantityField;

    @FXML
    private Button addToCartButton;

    @FXML
    private Label cartLabel;
    
    @FXML
    private Button updatePasswordButton;
    @FXML
    private Button updateDetailsButton;

    @FXML
    private Button logOutButton;
    @FXML
    private Button viewOrderButton;
    
    

    private Scene loginScene;


    public void setLoginScene(Scene loginScene) {
        this.loginScene = loginScene;
    }


    @FXML
    public void updateDetails(ActionEvent e) {
        System.out.println("Update Details clicked");
        Button source = (Button) e.getSource();
        Stage primaryStage = (Stage) source.getScene().getWindow();
        
        // Similar to CheckOutView, create an instance of updateProfileView
        updateProfileView updateProfile = new updateProfileView(source.getScene());
        
        // Set the title for the stage to reflect the update details page
        primaryStage.setTitle(updateProfile.getTitle());
        
        // Switch to the update details scene
        primaryStage.setScene(updateProfile.getScene());
    }

    
    @FXML
    public void updatePassword(ActionEvent e) {
    	System.out.println("Update Password clicked");
        Button source = (Button) e.getSource();
        Stage primaryStage = (Stage) source.getScene().getWindow();

        // Similar to CheckOutView, create an instance of UpdatePasswordView
        updatePasswordView updatePWView = new updatePasswordView(source.getScene());
        
        // Set the title for the stage to reflect the password update page
        primaryStage.setTitle(updatePWView.getTitle());
        
        // Switch to the update password scene
        primaryStage.setScene(updatePWView.getScene());
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

    @FXML
    public void continueShopping() {
        // Handle action to continue shopping
        System.out.println("Continue shopping clicked");
        if (tabPane != null && homeTab != null) {
            tabPane.getSelectionModel().select(homeTab);
        }
    }


    @FXML
	public void checkOut(ActionEvent e) {
	    Button source = (Button) e.getSource();
	    Stage primaryStage = (Stage) source.getScene().getWindow();            
	    CheckOutView checkoutScene = new CheckOutView(source.getScene());
	    primaryStage.setTitle(checkoutScene.getTitle());
	    primaryStage.setScene(checkoutScene.getScene());
	}
    @FXML
	public void viewOrder(ActionEvent e) {
	    Button source = (Button) e.getSource();
	    Stage primaryStage = (Stage) source.getScene().getWindow();            
	    ViewOrderView viewOrder = new ViewOrderView(source.getScene());
	    primaryStage.setTitle(viewOrder.getTitle());
	    primaryStage.setScene(viewOrder.getScene());
	}

    @FXML
    public void updateQuantity() {
        // Update the quantity of the selected book in the cart
        String selectedBook = cartListView.getSelectionModel().getSelectedItem();
        if (selectedBook != null && !quantityField.getText().isEmpty()) {
            int quantity = Integer.parseInt(quantityField.getText());
            System.out.println("Updated quantity for " + selectedBook + ": " + quantity);
        }
    }

    public void selectShoppingCartTab() {
        if (tabPane != null && cartTab != null) {
            tabPane.getSelectionModel().select(cartTab);  // Select the shopping cart tab
            System.out.println("Switched to shopping cart tab!");
        }
    }
    public void selectAccountTab() {
        if (tabPane != null && accountTab != null) {
            tabPane.getSelectionModel().select(accountTab);  // Select the shopping cart tab
            System.out.println("Switched to Account tab!");
        }
    }
    
}
