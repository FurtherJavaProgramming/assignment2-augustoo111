package Controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Model;
import model.User;
import java.sql.SQLException;

public class SignUpController {

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField userNameField;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField confirmPasswordField;

    @FXML
    private Button signUpButton;  // Button for sign-up

    private Scene loginScene;
    private Model model;  // Reference to the model for interaction with DAO

    private Stage primaryStage;  // Main application stage

    // Default constructor required for FXML loading
    public SignUpController() {}

    // Setter method for the Model
    public void setModel(Model model) {
        this.model = model;
    }

    // Setter method for the Primary Stage
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    // Set the login scene to switch back after successful sign-up
    public void setLoginScene(Scene loginScene) {
        this.loginScene = loginScene;
    }

    @FXML
    public void initialize() {
        // Handle sign-up button action
        signUpButton.setOnAction(event -> {
            if (!firstNameField.getText().isEmpty() && !lastNameField.getText().isEmpty() &&
                !userNameField.getText().isEmpty() && !passwordField.getText().isEmpty() &&
                !confirmPasswordField.getText().isEmpty()) {

                if (passwordField.getText().equals(confirmPasswordField.getText())) {
                    try {
                        // Check if the username already exists
                        User existingUser = model.getUserDao().getUserByUsername(userNameField.getText());
                        if (existingUser != null) {
                            // Username already exists
                            showAlert(Alert.AlertType.WARNING, "Username Already Exists",
                                      "The username is already taken.",
                                      "Please choose another username.");
                            return;  // Exit the method early
                        }

                        // If the username is not taken, proceed to create a new user
                        User newUser = model.getUserDao().createUser(
                            firstNameField.getText(), lastNameField.getText(),
                            userNameField.getText(), passwordField.getText());

                        if (newUser != null) {
                            // Success message
                            showAlert(Alert.AlertType.INFORMATION, "Sign-Up Success",
                                      "Sign-up successful!",
                                      "User " + newUser.getUsername() + " has been created.");
                            if (primaryStage != null) {
                                primaryStage.setScene(loginScene);  // Switch to login scene
                            } else {
                            	 showAlert(Alert.AlertType.ERROR, "Fail to switch Scene!", "Primary stage is not found.", "Cannot switch to login scene");
                            }
                        } else {
                            showAlert(Alert.AlertType.ERROR, "Sign-Up Failed", "Failed to create user.",
                                      "An unknown error occurred during sign-up.");
                        }
                    } catch (SQLException e) {
                        showAlert(Alert.AlertType.ERROR, "Sign-Up Failed", "Database error", e.getMessage());
                        System.out.println(e.getMessage());
                    }
                } else {
                    // Password mismatch error
                    showAlert(Alert.AlertType.WARNING, "Password Mismatch",
                              "Passwords do not match", "Please ensure that both passwords match.");
                }
            } else {
                // Empty field error
                showAlert(Alert.AlertType.WARNING, "Empty Fields",
                          "All fields must be filled.", "Please fill all fields before signing up.");
            }

            // Clear fields after submission
            firstNameField.clear();
            lastNameField.clear();
            userNameField.clear();
            passwordField.clear();
            confirmPasswordField.clear();
        });
        
    }
    
    


    // Method to show alert messages
    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
