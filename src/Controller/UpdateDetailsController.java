package Controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
    private Button logOutButton;

    @FXML
    private Button backButton;

    private Scene accountScene;  // Reference to the account scene or previous scene

    // Set the account scene to go back to after updating or canceling
    public void setAccountScene(Scene accountScene) {
        this.accountScene = accountScene;
    }

    // Method to handle updating user details
    @FXML
    public void updateDetails() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        System.out.println("Update Clicked");

        if (firstName.isEmpty() || lastName.isEmpty()) {
            System.out.println("Please fill in both fields.");
        } else {
            System.out.println("Updated Details: " + firstName + " " + lastName);
            // Add logic to update user details in the system
        }
    }

    // Method to handle logging out (or canceling)
    @FXML
    public void cancel() {
        System.out.println("Canceling, going back to Account.");
        // Clear fields or close the current window if needed
        firstNameField.clear();
        lastNameField.clear();
        
    }

    // Method to handle the 'Back' button
    @FXML
    public void goBack() {
        System.out.println("Back to Account!");
        Stage primaryStage = (Stage) backButton.getScene().getWindow();

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
}
