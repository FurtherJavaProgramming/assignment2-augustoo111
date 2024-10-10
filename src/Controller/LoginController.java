package Controller;

import java.io.IOException;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.AdminUser;
import model.Model;
import model.User;

public class LoginController {

    private Stage stage;
    private Stage primaryStage;
    private Model model;  // Model class that holds the application's state and DAO reference

    @FXML
    private TextField userName;  // Maps to the TextField in FXML

    @FXML
    private PasswordField password;  // Maps to the PasswordField in FXML

    @FXML
    private Label message;  // Label for displaying login messages

    @FXML
    private Button login;  // Login Button

    @FXML
    private Button signUp;  // Sign Up Button
    
    public LoginController() {
    }

    public LoginController(Stage stage, Model model) {
        this.stage = stage;
        this.model = model;
    }

    // Add a setter method for primaryStage
    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }
    
    public void setModel(Model model) {
        this.model = model;
    }

    @FXML
    public void initialize() {
        // Handle login button action
        login.setOnAction(event -> {
            if (!userName.getText().isEmpty() && !password.getText().isEmpty()) {
                try {
                    // Fetch the user from the database
                    User user = model.getUserDao().getUser(userName.getText(), password.getText());
                    if (user != null) {
                        model.setCurrentUser(user);  // Save the current user in the model

                        // Check if the user is an admin or a regular user
                        if (user instanceof AdminUser) {
                            loadAdminDashboardScene();
                        } else {
                            loadHomeScene();
                        }
                    } else {
                        showErrorMessage("Wrong username or password");
                    }
                } catch (SQLException e) {
                    showErrorMessage(e.getMessage());
                }
            } else {
                showErrorMessage("Empty username or password");
            }

            userName.clear();
            password.clear();
        });

        // Handle sign-up button action
        signUp.setOnAction(event -> loadSignUpScene());
    }

    //Method to load the home scene for regular users
    private void loadHomeScene() {
        try {
            // Load FXML and controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/homeview.fxml"));
            TabPane root = loader.load();

            // Get controller instance from the FXMLLoader
            HomeSceneController homeSceneController = loader.getController();
            
            // Set the model and primary stage
            homeSceneController.setModel(model);
            homeSceneController.setPrimaryStage(primaryStage);

            // Set the scene
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            showErrorMessage(e.getMessage());
        }
    }


    // Method to load the admin dashboard scene for admins
    private void loadAdminDashboardScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/admindashboardview.fxml"));
            AdminDashboardController adminDashboardController = new AdminDashboardController(stage, model);
            loader.setController(adminDashboardController);
            TabPane root = loader.load();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            showErrorMessage(e.getMessage());
        }
    }

    // Method to load the sign-up scene
    private void loadSignUpScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/signupview.fxml"));

            GridPane root = loader.load();  // Load the FXML

            // Get the SignUpController and set the primaryStage and model
            SignUpController signUpController = loader.getController();
            signUpController.setModel(model);
            signUpController.setPrimaryStage(primaryStage);  // Pass the primaryStage
            signUpController.setLoginScene(primaryStage.getScene());  // Set the login scene for navigation back

            Scene signUpScene = new Scene(root);
            primaryStage.setScene(signUpScene);  // Switch to sign-up scene
            primaryStage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Method to display error messages
    private void showErrorMessage(String messageText) {
        message.setText(messageText);
        message.setTextFill(Color.RED);
    }
}
