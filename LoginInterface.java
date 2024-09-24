package readingroom_2;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class LoginInterface extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create GridPane layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);

        // Create and configure the Username Label and TextField
        Label usernameLabel = new Label("Username:");
        GridPane.setConstraints(usernameLabel, 0, 0);
        TextField usernameInput = new TextField();
        usernameInput.setPromptText("Enter username");
        GridPane.setConstraints(usernameInput, 1, 0);

        // Create and configure the Password Label and PasswordField
        Label passwordLabel = new Label("Password:");
        GridPane.setConstraints(passwordLabel, 0, 1);
        PasswordField passwordInput = new PasswordField();
        passwordInput.setPromptText("Enter password");
        GridPane.setConstraints(passwordInput, 1, 1);

        // Create Login and Create Account buttons
        Button loginButton = new Button("Log In");
        Button createAccountButton = new Button("Create Account");

        // Configure layout for buttons
        HBox buttonLayout = new HBox(10);
        buttonLayout.setAlignment(Pos.CENTER);
        buttonLayout.getChildren().addAll(loginButton, createAccountButton);
        GridPane.setConstraints(buttonLayout, 1, 2);

        // Create message label to display status
        Label messageLabel = new Label();
        GridPane.setConstraints(messageLabel, 1, 3);

        // Add functionality to the buttons
        loginButton.setOnAction(e -> {
            String username = usernameInput.getText();
            String password = passwordInput.getText();

            // Dummy login check logic (replace with real logic)
            if (username.equals("user") && password.equals("password123")) {
                messageLabel.setText("Login successful! Welcome, " + username);
                // Proceed to user dashboard
            } else {
                messageLabel.setText("Invalid credentials. Try again.");
            }
        });

        createAccountButton.setOnAction(e -> {
            String username = usernameInput.getText();
            String password = passwordInput.getText();

            // Dummy create account logic (replace with real logic)
            if (!username.isEmpty() && !password.isEmpty()) {
                messageLabel.setText("Account created successfully for " + username);
                // Proceed to create account logic
            } else {
                messageLabel.setText("Please fill out both fields.");
            }
        });

        // Add all elements to the grid
        grid.getChildren().addAll(usernameLabel, usernameInput, passwordLabel, passwordInput, buttonLayout, messageLabel);

        // Create the scene
        Scene scene = new Scene(grid, 350, 200);
        primaryStage.setTitle("Welcome to login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}


	


