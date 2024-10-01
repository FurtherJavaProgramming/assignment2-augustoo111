package Controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
    private Button goBackButton;

    @FXML
    private Button toggleVisibilityButton1;

    @FXML
    private Button toggleVisibilityButton2;

    private boolean isNewPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;

    private Scene accountScene; // Reference to the account scene

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
    public void handleUpdatePassword() {
        String newPassword = isNewPasswordVisible ? newPasswordTextField.getText() : newPasswordField.getText();
        String confirmPassword = isConfirmPasswordVisible ? confirmPasswordTextField.getText() : confirmPasswordField.getText();

        if (newPassword.equals(confirmPassword) && !newPassword.isEmpty()) {
            System.out.println("Password updated successfully.");
        } else {
            System.out.println("Passwords do not match.");
        }
    }

    // Set the account scene to go back to
    public void setAccountScene(Scene accountScene) {
        this.accountScene = accountScene;
    }

    // Method to handle the 'Go Back' button
    @FXML
    public void goBack() {
        System.out.println("Back to Account!");
        Stage primaryStage = (Stage) goBackButton.getScene().getWindow();

        // Navigate back to the account scene
        primaryStage.setScene(accountScene);
        primaryStage.setTitle("Account");

        // If needed, reload or refresh content in the account scene
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/homeview.fxml"));
            loader.load();
            HomeSceneController homeController = loader.getController();
            homeController.selectAccountTab();  // Assuming there's a method to select the account tab
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Method to handle the cancel button
    @FXML
    public void handleCancel() {
        System.out.println("Password update canceled.");
        // Clear fields or close the current window if needed
        newPasswordField.clear();
        confirmPasswordField.clear();
        newPasswordTextField.clear();
        confirmPasswordTextField.clear();
    }
}
