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
    private Button signUpButton, goBackButton;  // Buttons for sign-up and going back

    private Scene loginScene;
    private Model model;  // Reference to the model for interaction with DAO

    private Stage primaryStage;  // Main application stage
    private LoginController loginController;

    // Default constructor required for FXML loading
    public SignUpController() {
    }
    public String getTitle() {
        return "Sign Up";
    }


    // Setter method for the Model
    public void setModel(Model model) {
        this.model = model;
    }

    // Setter method for the Primary Stage
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle(getTitle());
    }
    //set login controller
	public void setLoginController(LoginController loginController) {
		this.loginController = loginController;
		
	}

    // Set the login scene to switch back after successful sign-up
    public void setLoginScene(Scene loginScene) {
        this.loginScene = loginScene;
    }

    @FXML
    public void initialize() {
        // Handle sign-up and go back button action
        signUpButton.setOnAction(event -> signUp());
        goBackButton.setOnAction(event -> goBack());
    }

    // Method for handling user sign-up
    private void signUp() {
        // Trim user input to avoid accidental spaces
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String username = userNameField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (!firstName.isEmpty() && !lastName.isEmpty() &&
            !username.isEmpty() && !password.isEmpty() &&
            !confirmPassword.isEmpty()) {

            if (password.equals(confirmPassword)) {
                try {
                    // Check if the model is set
                    if (model == null || model.getUserDao() == null) {
                        showAlert(Alert.AlertType.ERROR, "Error", "System Error", "Model or DAO is not initialized.");
                        return;
                    }

                    // Check if the username already exists
                    User existingUser = model.getUserDao().getUserByUsername(username);
                    if (existingUser != null) {
                        // Username already exists
                        showAlert(Alert.AlertType.WARNING, "Username Already Exists",
                                  "The username is already taken.",
                                  "Please choose another username.");
                        return;  // Exit the method early
                    }

                    // If the username is not taken, proceed to create a new user
                    User newUser = model.getUserDao().createUser(firstName, lastName, username, password);

                    if (newUser != null) {
                        // Success message
                        showAlert(Alert.AlertType.INFORMATION, "Sign-Up Success",
                                  "Sign-up successful!",
                                  "User " + newUser.getUsername() + " has been created.");
                        if (primaryStage != null) {
                            primaryStage.setScene(loginScene);  // Switch to login scene
                        } else {
                            showAlert(Alert.AlertType.ERROR, "Scene Error", "Primary stage not found", "Cannot switch to login scene.");
                        }
                        // Clear fields after successful sign-up
                        clearFields();
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Sign-Up Failed", "Failed to create user", "An unknown error occurred during sign-up.");
                    }
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Sign-Up Failed", "Database error", e.getMessage());
                }
            } else {
                // Password mismatch error
                showAlert(Alert.AlertType.WARNING, "Password Mismatch", "Passwords do not match", "Please ensure that both passwords match.");
            }
        } else {
            // Empty field error
            showAlert(Alert.AlertType.WARNING, "Empty Fields", "All fields must be filled.", "Please fill all fields before signing up.");
        }
    }

    // Method to handle going back to login scene
    private void goBack() {
        Stage stage = (Stage) goBackButton.getScene().getWindow();
        stage.setScene(loginScene);  // Set the account scene back
        
		stage.setTitle(loginController.getTitle());
        stage.show();
        
    	
    }
    

    // Method to clear all input fields
    private void clearFields() {
        firstNameField.clear();
        lastNameField.clear();
        userNameField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
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
