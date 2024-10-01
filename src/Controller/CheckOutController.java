package Controller;
import java.io.IOException;

import View.HomeScene;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class CheckOutController {

    @FXML
    private TextField nameOnCardField;

    @FXML
    private TextField cardNumberField;

    @FXML
    private ChoiceBox<String> expiryMonthChoiceBox;

    @FXML
    private ChoiceBox<String> expiryYearChoiceBox;

    @FXML
    private TextField securityCodeField;

    @FXML
    private Label totalItemLabel;

    @FXML
    private Button placeOrderButton;

    @FXML
    private Button goBackToCartButton;

    @FXML
    private Button addCardButton;

    private Scene shoppingCartScene;  
    

    public void setShoppingCartScene(Scene shoppingCartScene) {
        this.shoppingCartScene = shoppingCartScene;
    }

    @FXML
    public void initialize() {
        expiryMonthChoiceBox.getItems().addAll("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12");
        expiryYearChoiceBox.getItems().addAll("2024", "2025", "2026", "2027", "2028", "2029", "2030");
        
    }

    @FXML
    public void placeOrder() {
//        String nameOnCard = nameOnCardField.getText();
//        String cardNumber = cardNumberField.getText();
//        String expiryMonth = expiryMonthChoiceBox.getValue();
//        String expiryYear = expiryYearChoiceBox.getValue();
//        String securityCode = securityCodeField.getText();
//
//        if (validatePaymentDetails(nameOnCard, cardNumber, expiryMonth, expiryYear, securityCode)) {
//            System.out.println("Order placed successfully!");

            // Load the Order Detail scene
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/orderDetail.fxml"));
                GridPane orderDetailPane = loader.load();  // Load the layout
                Scene orderDetailScene = new Scene(orderDetailPane);

                // Change the stage to the new scene (Order Detail)
                Stage primaryStage = (Stage) placeOrderButton.getScene().getWindow();
                primaryStage.setScene(orderDetailScene);
                primaryStage.setTitle("Order Details");

            } catch (IOException ex) {
                ex.printStackTrace();
            }
//        } else {
//            System.out.println("Invalid payment details, please correct and try again.");
//        }
    }

        
    

    @FXML
    public void goBackToCart() {
        System.out.println("Back to cart!");
        Stage primaryStage = (Stage) goBackToCartButton.getScene().getWindow();
        HomeScene homeScene = new HomeScene(shoppingCartScene);
        primaryStage.setScene(shoppingCartScene);
        primaryStage.setTitle(homeScene.getTitle());
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/homeview.fxml"));
            loader.load();
            HomeSceneController homeController = loader.getController();
            homeController.selectShoppingCartTab();  
            } catch (IOException ex) {
            ex.printStackTrace();
            }
    }
   


    

    @FXML
    public void addCard() {
        System.out.println("Card added: " + cardNumberField.getText());
    }

//    private boolean validatePaymentDetails(String nameOnCard, String cardNumber, String expiryMonth, String expiryYear, String securityCode) {
//        if (nameOnCard.isEmpty() || cardNumber.length() != 16 || securityCode.length() != 3 ) {
//            return false;
//        }
//        return true;
//    }
    
}
