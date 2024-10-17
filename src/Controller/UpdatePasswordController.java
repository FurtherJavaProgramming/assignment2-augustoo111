package Controller;

import java.io.IOException;
import java.sql.SQLException;

import Dao.UserDao;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Labeled;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Model;
import model.User;

public class UpdatePasswordController {

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private TextField newPasswordTextField;

    @FXML
    private TextField confirmPasswordTextField;
    
    @FXML
    private Labeled userNameLabel;

    @FXML
    private Button goBackButton;

    @FXML
    private Button updatePasswordButton;

    @FXML
    private RadioButton toggleVisibilityButton1;

    @FXML
    private RadioButton toggleVisibilityButton2;

    private boolean isNewPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;

    private Scene accountScene; // Reference to the account scene
    private Stage primaryStage;
    private Model model;         // The user model managing the current session
    private UserDao userDao;      // DAO to handle user data
    private HomeSceneController homeController;  // Reference to the HomeSceneController for updating account details

    // Setter for Model
    public void setModel(Model model) {
        this.model = model;
        populateFields();
    }

    // Setter for UserDao (database access object)
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
        
    }

    // Setter for HomeSceneController to refresh the account tab
    public void setHomeController(HomeSceneController homeController) {
        this.homeController = homeController;
    }
    private void populateFields() {
        User currentUser = model.getCurrentUser();
		userNameLabel.setText(currentUser.getUsername());
    }

    // Method to toggle the visibility of the new password field
    @FXML
    public void togglePasswordVisibility1() {
        if (isNewPasswordVisible) {
            newPasswordField.setText(newPasswordTextField.getText());
            newPasswordField.setVisible(true);
            newPasswordTextField.setVisible(false);
            toggleVisibilityButton1.setText("Show");
        } else {
            newPasswordTextField.setText(newPasswordField.getText());
            newPasswordTextField.setVisible(true);
            newPasswordField.setVisible(false);
            toggleVisibilityButton1.setText("Hide");
        }
        isNewPasswordVisible = !isNewPasswordVisible;
    }

    // Method to toggle the visibility of the confirm password field
    @FXML
    public void togglePasswordVisibility2() {
        if (isConfirmPasswordVisible) {
            confirmPasswordField.setText(confirmPasswordTextField.getText());
            confirmPasswordField.setVisible(true);
            confirmPasswordTextField.setVisible(false);
            toggleVisibilityButton2.setText("Show");
        } else {
            confirmPasswordTextField.setText(confirmPasswordField.getText());
            confirmPasswordTextField.setVisible(true);
            confirmPasswordField.setVisible(false);
            toggleVisibilityButton2.setText("Hide");
        }
        isConfirmPasswordVisible = !isConfirmPasswordVisible;
    }

    // Method to handle the password update logic
    @FXML
    public void handleUpdatePassword() throws SQLException {
        String newPassword = isNewPasswordVisible ? newPasswordTextField.getText() : newPasswordField.getText();
        String confirmPassword = isConfirmPasswordVisible ? confirmPasswordTextField.getText() : confirmPasswordField.getText();

        if (newPassword.equals(confirmPassword) && !newPassword.isEmpty()) {
        	showAlert(Alert.AlertType.INFORMATION, "Update Successful", "Your password is updated.");
            // Here you would add logic to update the password in the backend
        } else {
        	showAlert(Alert.AlertType.ERROR, "Passwords MisMatch", "Your password is not updated.");
            // Show error message for password mismatch
        }
     // Get the current user from the model
        User currentUser = model.getCurrentUser();
        
        // Update the user information
        currentUser.setPassword(newPassword);

        // Update the user details in the database
        userDao.updatePassword(currentUser);  // DAO call to update the user in the database


    }

    // Set the account scene to go back to
    public void setAccountScene(Scene accountScene) {
        this.accountScene = accountScene;
    }

    // Method to handle the 'Go Back' button
    @FXML
    public void goBack() {
        Stage primaryStage = (Stage) goBackButton.getScene().getWindow();
        primaryStage.setScene(accountScene);
        primaryStage.setTitle("Account");
    }

    // Method to handle the cancel button
    @FXML
    public void handleCancel() {
        newPasswordField.clear();
        confirmPasswordField.clear();
        newPasswordTextField.clear();
        confirmPasswordTextField.clear();
    }
    // Method to show alerts for user notifications
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }



}