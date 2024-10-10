package Controller;

import View.CheckOutView;
import View.LoginScene;
import View.ViewOrderView;
import View.updatePasswordView;
import View.updateProfileView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Model;

public class HomeSceneController {

    private Model model;
    private Stage primaryStage;
    private Scene loginScene;

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab homeTab, accountTab, cartTab;

    @FXML
    private TextField firstNameField, lastNameField, selectedBookField, quantityField;

    @FXML
    private Button continueShoppingButton, checkOutButton, addToCartButton, updatePasswordButton, 
                   updateDetailsButton, logOutButton, viewOrderButton;

    @FXML
    private Label userNameLabel, cartLabel;

    @FXML
    private TableView<String> cartListView;

    // Default constructor
    public HomeSceneController() {}

    // Constructor to pass the primary stage and model
    public HomeSceneController(Stage primaryStage, Model model) {
        this.primaryStage = primaryStage;
        this.model = model;
    }

    // Setters for login scene, primary stage, and model
    public void setLoginScene(Scene loginScene) {
        this.loginScene = loginScene;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    @FXML
    public void updateDetails(ActionEvent e) {
        System.out.println("Update Details clicked");
        Stage primaryStage = (Stage) ((Button) e.getSource()).getScene().getWindow();

        updateProfileView updateProfile = new updateProfileView(primaryStage.getScene());
        primaryStage.setTitle(updateProfile.getTitle());
        primaryStage.setScene(updateProfile.getScene());
    }

    @FXML
    public void updatePassword(ActionEvent e) {
        System.out.println("Update Password clicked");
        Stage primaryStage = (Stage) ((Button) e.getSource()).getScene().getWindow();

        updatePasswordView updatePWView = new updatePasswordView(primaryStage.getScene());
        primaryStage.setTitle(updatePWView.getTitle());
        primaryStage.setScene(updatePWView.getScene());
    }

    @FXML
    public void logOut() {
        System.out.println("User logged out");

        Stage primaryStage = (Stage) logOutButton.getScene().getWindow();
        LoginScene loginScene = new LoginScene(model, primaryStage);
        primaryStage.setTitle(loginScene.getTitle());
        primaryStage.setScene(loginScene.getScene());
    }

    @FXML
    public void continueShopping() {
        System.out.println("Continue shopping clicked");
        if (tabPane != null && homeTab != null) {
            tabPane.getSelectionModel().select(homeTab);
        }
    }

    @FXML
    public void checkOut(ActionEvent e) {
        Stage primaryStage = (Stage) ((Button) e.getSource()).getScene().getWindow();
        CheckOutView checkoutScene = new CheckOutView(primaryStage.getScene());
        primaryStage.setTitle(checkoutScene.getTitle());
        primaryStage.setScene(checkoutScene.getScene());
    }

    @FXML
    public void viewOrder(ActionEvent e) {
        Stage primaryStage = (Stage) ((Button) e.getSource()).getScene().getWindow();
        ViewOrderView viewOrder = new ViewOrderView(primaryStage.getScene());
        primaryStage.setTitle(viewOrder.getTitle());
        primaryStage.setScene(viewOrder.getScene());
    }

    @FXML
    public void updateQuantity() {
        String selectedBook = cartListView.getSelectionModel().getSelectedItem();
        if (selectedBook != null && !quantityField.getText().isEmpty()) {
            int quantity = Integer.parseInt(quantityField.getText());
            System.out.println("Updated quantity for " + selectedBook + ": " + quantity);
        }
    }

    public void selectShoppingCartTab() {
        if (tabPane != null && cartTab != null) {
            tabPane.getSelectionModel().select(cartTab);
            System.out.println("Switched to shopping cart tab!");
        }
    }

    public void selectAccountTab() {
        if (tabPane != null && accountTab != null) {
            tabPane.getSelectionModel().select(accountTab);
            System.out.println("Switched to Account tab!");
        }
    }
}
