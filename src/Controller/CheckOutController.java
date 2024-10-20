package Controller;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import View.ViewOrderView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Book;
import model.Model;
import model.Order;
import javafx.scene.Scene;
import Dao.BookDao;
import Dao.BookDaoImplementation;
import Dao.OrderDao;
import Dao.OrderDaoImplementation;
import Dao.UserDao;

public class CheckOutController {

    @FXML
    private TextField nameOnCardField, cardNumberField, securityCodeField, totalItem, totalAmount;

    @FXML
    private ChoiceBox<String> expiryMonthChoiceBox, expiryYearChoiceBox;

    @FXML
    private Label totalItemLabel;

    @FXML
    private Button placeOrderButton, goBackToCartButton, addCardButton;
    private boolean isCardAdded = false;

    private Scene shoppingCartScene;  // Reference to the shopping cart scene
    private int totalItems;           // Total items in the cart
    private double totalAmountValue;  // Total amount of the order
    private Stage primaryStage; 
    private BookDao bookDao;
    private ArrayList<Book> cartItems = new ArrayList<>();
	private HomeSceneController homeSceneController;
	private Scene homeScene;
	private TableView<Book> bookStockTable;
	private OrderDao orderDao;

	private Model model;
	private String username;

	//set primary stage
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    
    public void setBookDao(BookDao bookDao) {
        this.bookDao = bookDao;
    }
	public void setHomeController(HomeSceneController homeSceneController) {
		this.homeSceneController = homeSceneController;
		
	}

	public void setModel(Model model) {
		this.model = model;
		
	}
	public void setUserDao(UserDao userDao) {
    }
    
  // Method to set the username in the label
    public void setUsername(String username) {
        this.username = username;  // Store the username
        
    }

    
    // Set shopping cart scene
    public void setShoppingCartScene(Scene shoppingCartScene) {
        this.shoppingCartScene = shoppingCartScene;
    }
    
    //set Home scene
    public void setHomeScene(Scene homeScene) {
        this.homeScene = homeScene;
    }



    // Set total items and total amount from the shopping cart
    public void setCartDetails(int totalItems, double totalAmountValue) {
        this.totalItems = totalItems;
        this.totalAmountValue = totalAmountValue;
        updateCartSummary();  // Update the summary labels and fields
        
    }

    // Update cart summary on the checkout page
    private void updateCartSummary() {
        totalItem.setText(String.valueOf(totalItems));  // Set total items in the field
        totalAmount.setText(String.format("%.2f", totalAmountValue) + " AUD");  // Set total amount in the field
    }

    @FXML
    public void initialize() throws SQLException {
    	this.bookDao = new BookDaoImplementation();  
        this.orderDao = new OrderDaoImplementation(); 
        // Populate expiry month and year choice boxes
        expiryMonthChoiceBox.getItems().addAll("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12");
        expiryYearChoiceBox.getItems().addAll("2024", "2025", "2026", "2027", "2028", "2029", "2030");
        
    }

     // Place order method
    @FXML
    public void placeOrder() throws SQLException {
        
        // Validate payment details only if the card has not been added yet
        if (!isCardAdded) {
            String nameOnCard = nameOnCardField.getText();
            String cardNumber = cardNumberField.getText();
            String expiryMonth = expiryMonthChoiceBox.getValue();
            String expiryYear = expiryYearChoiceBox.getValue();
            String securityCode = securityCodeField.getText();

            // Validate payment details
            if (!validatePaymentDetails(nameOnCard, cardNumber, expiryMonth, expiryYear, securityCode)) {
                showAlert(Alert.AlertType.ERROR, "Invalid Payment Details", "Please correct the payment details and try again.");
                return;  // Exit the method if the validation fails
            }
        }

        // If card is already added or validated, proceed with placing the order
        showAlert(Alert.AlertType.INFORMATION, "Order Placed", "Your order has been placed successfully!");

        // Update stock, clear cart, and load order details
        updateBookInventory();
        saveOrderDetails();
        totalItem.clear();
        totalAmount.clear();
        clearCartItems();
        loadOrderDetailScene();
    }

 // Save the order details to the database
    private void saveOrderDetails() {
        // Check if orderDao is properly initialized
        if (orderDao == null) {
            showAlert(Alert.AlertType.ERROR, "ERROR!", "Unable to save the order. Please try again later.");
            return;
        }

        // Check if cartItems is empty or null
        if (cartItems == null || cartItems.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Order ERROR!", "Your cart is empty. Please add items to the cart before placing an order.");
            return;
        }

        // Ensure the username is not null or empty
        if (username == null || username.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Order ERROR!", "Unable to save the order. User is not logged in.");
            return;
        }

        // Generate a unique Order ID
        String orderId = generateOrderId();  
        // Get the list of books in the cart
        List<Book> booksInOrder = new ArrayList<>(cartItems);  
        // Get the total amount for the order
        double totalPrice = totalAmountValue;  
        // Calculate the total quantity of items in the cart
        int totalQuantity = calculateTotalItems();  
        // Get the current date and time
        LocalDateTime orderDate = LocalDateTime.now();  

        // Create a new Order object with the above details
        Order newOrder = new Order(orderId, booksInOrder, totalPrice, totalQuantity, orderDate);

        // Try saving the order to the database
        try {
            // Use the newOrder object and username to save the order
            orderDao.saveOrder(newOrder, username);  
            showAlert(Alert.AlertType.INFORMATION, "Order Saved", "Your order has been saved successfully.");
        } catch (SQLException e) {
            // Handle SQL exception if saving fails
            showAlert(Alert.AlertType.ERROR, "ERROR!", "There was an error saving your order. Please try again.");
            e.printStackTrace();
        }
    }



    // Method to generate a unique Order ID (could be UUID or some logic)
    private String generateOrderId() {
        Random random = new Random();
        int uniqueNumber = random.nextInt(900000) + 100000;  // Generates a number between 100000 and 999999
        return "ORD" + uniqueNumber;  // Prefix with "ORD" to make it look like an order number
    }

    private void clearCartItems() {
        // Clear the in-memory cart items list
        cartItems.clear();  
        // Clear the cart in the HomeSceneController (UI part)
        homeSceneController.clearCart();  
        // Clear the cart from the database for the current user
        try {
            if (bookDao != null && username != null) {
                bookDao.clearCartItems(username);  // Use the BookDao to remove cart items from the database
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "ERROR!", "Failed to clear cart items from the database.");
            e.printStackTrace();
        }
    }

 // In HomeSceneController
    public Book findBookInStock(Book selectedBook) {
		for (Book book : bookStockTable.getItems()) {
            if (book.getTitle().equals(selectedBook.getTitle()) && book.getAuthor().equals(selectedBook.getAuthor())) {
                return book;
            }
        }
        return null;
    }

 // Update the book stock in HomeSceneController
    private void updateBookInventory() {

        for (Book bookInCart : cartItems) {
            // Find the corresponding book in the inventory
            Book bookInInventory = homeSceneController.findBookInStock(bookInCart);

            if (bookInInventory != null) {
                // Subtract the quantity purchased from the available stock
                int newStock = bookInInventory.getNoOfCopies() - bookInCart.getNoOfCopies();
                bookInInventory.setNoOfCopies(newStock);

                // Update the sold copies
                int newSoldCopies = bookInInventory.getSoldCopies() + bookInCart.getNoOfCopies();
                bookInInventory.setSoldCopies(newSoldCopies);

                // Update the book stock in the database
                try {
                    bookDao.updateBookStockbyUser(bookInInventory);  // Persist changes in the database
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "ERROR!", "Book not found in inventory:" + bookInCart.getTitle());

            }
        }
    }

    public void setCartItems(ArrayList<Book> cartItems) {
        this.cartItems = cartItems;
    }

    @FXML
    public void loadOrderDetailScene() throws SQLException {
        try {
            // Load the Order Detail FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/orderDetail.fxml"));
            GridPane orderDetailPane = loader.load();

            // Get the OrderDetailController from the FXML loader
            OrderDetailController orderDetailController = loader.getController();

            // Set order details in OrderDetailController
            orderDetailController.setOrderDetails(cartItems, totalItems, totalAmountValue);
            String orderNumber = generateOrderId();
			orderDetailController.setOrderNumber(orderNumber);  // Pass the generated order number
            orderDetailController.setPrimaryStage(primaryStage);  // Set primaryStage

            // Pass primaryStage and homeScene to OrderDetailController
            if (primaryStage != null) {
                orderDetailController.setPrimaryStage(primaryStage);  // Set primaryStage
            }

            // Pass homeScene and check if it's null
            if (homeScene != null) {
                orderDetailController.setHomeScene(homeScene);  // Set homeScene
            }

            // Switch to the order detail scene
            Scene orderDetailScene = new Scene(orderDetailPane);
            orderDetailController.setModel(model);
            orderDetailController.setOrderDao(orderDao);
            primaryStage.setScene(orderDetailScene);  // Set the new scene for the primary stage
            primaryStage.show();  // Make sure the stage is displayed

        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "ERROR!", "Unable to load OrderDetailView scene");
            
        }
    }


 // Helper method to validate the payment details
    private boolean validatePaymentDetails(String nameOnCard, String cardNumber, String expiryMonth, String expiryYear, String securityCode) {
        // Validate card number length and security code length
        if (nameOnCard.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "ERROR!", "Name on card cannot be empty.");
            return false;
        }

        if (cardNumber.length() != 16) {
            showAlert(Alert.AlertType.ERROR, "ERROR!", "Card number must be 16 digits.");
            return false;
        }

        if (securityCode.length() != 3) {
            showAlert(Alert.AlertType.ERROR, "ERROR!", "Security code must be 3 digits.");
            return false;
        }

        // Ensure the expiry month and year are selected
        if (expiryMonth == null || expiryYear == null) {
            showAlert(Alert.AlertType.ERROR, "ERROR!", "Please select a valid expiry date.");
            return false;
        }
        return true;
    }

  
    
//    @FXML
    public void showItemTotal() {
        // Calculate total items and total amount
        int totalItemsInCart = calculateTotalItems();  // Method to count total items
        double totalAmountInCart = calculateTotalAmount();  // Method to calculate total amount

        // Load the OrderDetailView and pass the cart details
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/orderDetail.fxml"));
            GridPane orderDetailPane = loader.load();

            // Get controller associated with the view
            OrderDetailController orderDetailController = loader.getController();
            Stage primaryStage = (Stage) placeOrderButton.getScene().getWindow();
            // Pass totalItems and totalAmount to OrderDetailController
            orderDetailController.setOrderDetails(cartItems, totalItemsInCart, totalAmountInCart);

            // Create a new scene and set it to the primary stage
            Scene orderDetailScene = new Scene(orderDetailPane);
            primaryStage.setScene(orderDetailScene);
            primaryStage.setTitle(primaryStage.getTitle());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void viewOrder(ActionEvent e) throws SQLException {
        Stage primaryStage = (Stage) ((Button) e.getSource()).getScene().getWindow();
        ViewOrderView viewOrder = new ViewOrderView(primaryStage.getScene());
        primaryStage.setTitle(viewOrder.getTitle());
        primaryStage.setScene(viewOrder.getScene());
    }

    
    
    // Method to calculate total amount
    private double calculateTotalAmount() {
        double total = 0;
        for (Book book : cartItems) {
            total += book.getTotalPrice();  // Assuming Book has a getTotalPrice() method
        }
        return total;
    }
    
   // Method to calculate the total quantity of books in the cart
    private int calculateTotalItems() {
        int totalQuantity = 0;
        for (Book book : cartItems) {
            totalQuantity += book.getNoOfCopies();  // Sum the quantities of each book
        }
        return totalQuantity;
    }
    

    // Go back to the shopping cart method
    @FXML
    public void goBackToCart() {
        try {
            // Get the current stage and switch back to the shopping cart scene
            Stage primaryStage = (Stage) goBackToCartButton.getScene().getWindow();
            primaryStage.setScene(shoppingCartScene);  // Return to the shopping cart scene
            primaryStage.show();  // Make sure the stage is displayed

            // Assuming HomeSceneController is already set up with shoppingCartScene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/homeview.fxml"));
            loader.load();
            HomeSceneController homeController = loader.getController();
            homeController.selectShoppingCartTab();  // Switch to the cart tab in the home scene
        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "ERROR! ", "Unable to load the HomeSceneController");
        }
    }



 // Add card method with payment validation
    @FXML
    public void addCard() {
        // Get input values from fields
        String nameOnCard = nameOnCardField.getText();
        String cardNumber = cardNumberField.getText();
        String expiryMonth = expiryMonthChoiceBox.getValue();
        String expiryYear = expiryYearChoiceBox.getValue();
        String securityCode = securityCodeField.getText();

        // Validate payment details
        if (validatePaymentDetails(nameOnCard, cardNumber, expiryMonth, expiryYear, securityCode)) {
            // Get the last 4 digits of the card number
            String lastFourDigits = cardNumber.substring(cardNumber.length() - 4);

            // Show confirmation message with the last 4 digits of the card number
            showAlert(Alert.AlertType.INFORMATION, "Card Added", "Card ending in " + lastFourDigits + " has been added.");


            // Mark the card as added only if validation is successful
            isCardAdded = true;

            // Clear input fields after the card has been added
            clearCardInputFields();
        }
    }

    // Helper method to clear the card input fields
    private void clearCardInputFields() {
        nameOnCardField.clear();
        cardNumberField.clear();
        securityCodeField.clear();
        expiryMonthChoiceBox.setValue(null);
        expiryYearChoiceBox.setValue(null);
    }



    // Helper method to show alerts for user notifications
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }




}