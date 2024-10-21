package Controller;

import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;
import model.Model;
import Dao.UserDao;

public class UpdateDetailsController {

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private Label userNameLabel;

    @FXML
    private Button updateDetailsButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Button backButton;

    private Scene accountScene;  // Reference to the previous scene
    private Model model;         // The user model managing the current session
    private UserDao userDao;      // DAO to handle user data
    private HomeSceneController homeController;  // Reference to the HomeSceneController for updating account details


    // Setter for Model
    public void setModel(Model model) {
        this.model = model;
        populateFields();  // Populate the fields with current user data when model is set
    }

    // Setter for the previous scene (account tab)
    public void setAccountScene(Scene accountScene) {
        this.accountScene = accountScene;
    }

    // Setter for UserDao (database access object)
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    // Setter for HomeSceneController to refresh the account tab
    public void setHomeController(HomeSceneController homeController) {
        this.homeController = homeController;
    }

    // Populate fields with current user details
    private void populateFields() {
        User currentUser = model.getCurrentUser();
        userNameLabel.setText(currentUser.getUsername());
        firstNameField.setText(currentUser.getFirstName());
        lastNameField.setText(currentUser.getLastName());
    }

    // Method to handle updating user details
    @FXML
    public void updateDetails() throws SQLException {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();

        if (firstName.isEmpty() || lastName.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Please fill in both fields.");
            return;
        }

        // Get the current user from the model
        User currentUser = model.getCurrentUser();
        
        // Update the user information
        currentUser.setFirstName(firstName);
        currentUser.setLastName(lastName);

        // Update the user details in the database
        userDao.updateUserDetails(currentUser);  // DAO call to update the user in the database

        // Show success message
        showAlert(Alert.AlertType.INFORMATION, "Update Successful", "Your details have been updated.");

        // Go back to the previous scene
        goBack();
    }

    // Method to handle canceling (go back without saving)
    @FXML
    public void cancel() {
        firstNameField.clear();
        lastNameField.clear();
        goBack();
    }

    // Method to handle the 'Back' button (navigate to account scene)
    @FXML
    public void goBack() {
        // Refresh the account tab details when going back
        homeController.updateAccountTab(); 
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.setScene(accountScene);  // Set the account scene back
        stage.show();
    }

    // Method to show alerts for user notifications
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
