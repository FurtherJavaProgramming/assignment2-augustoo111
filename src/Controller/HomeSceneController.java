package Controller;

import java.sql.SQLException;
import java.util.ArrayList;

import Dao.BookDao;
import View.CheckOutView;
import View.ViewOrderView;
import View.updatePasswordView;
import View.updateProfileView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Book;
import model.Model;

public class HomeSceneController {
	

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab homeTab, accountTab, cartTab;
    @FXML
    private TableView<Book> bookStockTable; // Table to display all books
    
    @FXML
    private TableColumn<Book, String> titleColumn;
    @FXML
    private TableColumn<Book, String> authorColumn;
    @FXML
    private TableColumn<Book, Double> priceColumn;
    @FXML
    private TableColumn<Book, Integer> soldCopiesColumn;
    @FXML
    private TableColumn<Book, Integer> noOfCopiesColumn;
    @FXML
    private TableView<Book> cartListView;  // Cart table to display added books
    @FXML
    private TableColumn<Book, String> cartTitleColumn;
    @FXML
    private TableColumn<Book, String> cartAuthorColumn;
    @FXML
    private TableColumn<Book, Integer> cartQuantityColumn;
    @FXML
    private TableColumn<Book, Double> cartPriceColumn;

    @FXML
    private TextField firstNameField, lastNameField, selectedBookField, quantityField, cartSelectedBookField, cartQuantityField;

    @FXML
    private Button continueShoppingButton, checkOutButton, addCartButton, updatePasswordButton, 
                   updateDetailsButton, logOutButton, viewOrderButton;

    @FXML
    private Label userNameLabel, cartLabel;

    

	private Scene loginScene;  // Reference to the login scene
	private Stage primaryStage;  // Reference to the primary stage
	private BookDao bookDao;
    

    // Default constructor
    public HomeSceneController() {}

    // Constructor to pass the primary stage and model
    public HomeSceneController(Stage primaryStage, Model model) {
    	this.primaryStage = primaryStage;
    }

 // Setter method to inject the BookDao instance
    public void setBookDao(BookDao bookDao) {
        this.bookDao = bookDao;
        loadBookData();
    }


    // Set the login scene to switch back after logging out
    public void setLoginScene(Scene loginScene) {
        this.loginScene = loginScene;
    }
 // Method to set the username in the label
    public void setUsername(String username) {
        userNameLabel.setText(username);
    }
    
    // Setter method for the Primary Stage
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setModel(Model model) {
    }
    private ArrayList<Book> cartItems = new ArrayList<>();

    @FXML
    public void initialize() {
        // Set up table columns with properties
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        soldCopiesColumn.setCellValueFactory(new PropertyValueFactory<>("soldCopies"));
        noOfCopiesColumn.setCellValueFactory(new PropertyValueFactory<>("noOfCopies"));
     // Add listener to the table to get selected book
        bookStockTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedBookField.setText(newSelection.getTitle());
            }
        });

     
        // Set up table columns for cartListView
        cartTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        cartAuthorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        cartQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("noOfCopies"));  // Using noOfCopies to represent quantity in the cart
        cartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Add listener to cartListView to show selected item in fields
        cartListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedBookField.setText(newSelection.getTitle());
                quantityField.setText(String.valueOf(newSelection.getNoOfCopies()));
            }
        });
    }
    // Method to load all books from the DAO and display in the table
    private void loadBookData() {
        if (bookDao != null) {
            try {
                // Fetch all books from the database
                ArrayList<Book> books = (ArrayList<Book>) bookDao.getAllBooks();

                if (books != null && !books.isEmpty()) {
                    // Clear any existing items in the table
                    bookStockTable.getItems().clear();
                    // Add the list of books to the TableView
                    bookStockTable.getItems().addAll(books);
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error Loading Books", "No books found in the database.");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Error Loading Books", "Unable to load book data from the database.");
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Error Loading Books", "BookDao is not initialized.");
        }
    }

    
    // Method to add a book to the cart
    @FXML
    public void addToCart() {
        // Get the selected book from the bookStockTable
        Book selectedBook = bookStockTable.getSelectionModel().getSelectedItem();
        String chooseItem = selectedBookField.getText();
        String quantityText = quantityField.getText();

        // Check if a book is selected
        if (chooseItem == null || chooseItem.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Book Selected", "Please select a book to update.");
            return;
        }

        // Check if quantity is entered
        if (quantityText == null || quantityText.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Quantity Entered", "Please enter the quantity.");
            return;
        }

        try {
            // Parse the quantity entered by the user
            int quantity = Integer.parseInt(quantityText);

            // Ensure the quantity is non-negative
            if (quantity <= 0) {
                showAlert(Alert.AlertType.ERROR, "Invalid Quantity", "Quantity must be a positive number.");
                return;
            }

            // Create a copy of the selected book with the updated quantity for the cart
            Book bookInCart = new Book(
                selectedBook.getTitle(),
                selectedBook.getAuthor(),
                quantity, // Set the quantity in the cart
                selectedBook.getPrice(),
                selectedBook.getSoldCopies()
            );

            // Add the book to the cart
            cartItems.add(bookInCart);
            // Refresh the cartListView by resetting its items
            cartListView.getItems().clear();
            cartListView.getItems().addAll(cartItems);

            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Book Added to Cart", 
                      "Book \"" + selectedBook.getTitle() + "\" has been added to the cart with quantity " + quantity + ".");

            // Optionally, clear the selected fields for a new selection
            selectedBookField.clear();
            quantityField.clear();

        } catch (NumberFormatException e) {
            // If the quantity entered is not a valid number
            showAlert(Alert.AlertType.ERROR, "Invalid Quantity", "Please enter a valid number for the quantity.");
        }
    }

    
    

    @FXML
    public void updateDetails(ActionEvent e) {
        System.out.println("Update Details clicked");
        Stage primaryStage = (Stage) ((Button) e.getSource()).getScene().getWindow();

        updateProfileView updateProfile = new updateProfileView(primaryStage.getScene());
        primaryStage.setTitle(updateProfile.getTitle());
        primaryStage.setScene(updateProfile.getScene());
    }

    @FXML
    public void updatePassword(ActionEvent e) {
        System.out.println("Update Password clicked");
        Stage primaryStage = (Stage) ((Button) e.getSource()).getScene().getWindow();

        updatePasswordView updatePWView = new updatePasswordView(primaryStage.getScene());
        primaryStage.setTitle(updatePWView.getTitle());
        primaryStage.setScene(updatePWView.getScene());
    }

    @FXML
    public void logOut() {
        
//        Stage primaryStage = (Stage) logOutButton.getScene().getWindow();
//        LoginScene loginScene = new LoginScene(model, primaryStage);
//        primaryStage.setTitle(loginScene.getTitle());
//        primaryStage.setScene(loginScene.getScene());
        if (loginScene != null) {
            // Log out the user, switch back to the login scene
            primaryStage.setScene(loginScene);
            primaryStage.show();  // Make sure the stage is shown
        } else {
            // If the login scene is not set, show an alert
            showAlert(Alert.AlertType.WARNING, "Logout Failed", "Unable to log out. The login scene is not set.");
        }
        
    }

    @FXML
    public void continueShopping() {
        System.out.println("Continue shopping clicked");
        if (tabPane != null && homeTab != null) {
            tabPane.getSelectionModel().select(homeTab);
        }
    }

    @FXML
    public void checkOut(ActionEvent e) {
        Stage primaryStage = (Stage) ((Button) e.getSource()).getScene().getWindow();
        CheckOutView checkoutScene = new CheckOutView(primaryStage.getScene());
        primaryStage.setTitle(checkoutScene.getTitle());
        primaryStage.setScene(checkoutScene.getScene());
    }

    @FXML
    public void viewOrder(ActionEvent e) {
        Stage primaryStage = (Stage) ((Button) e.getSource()).getScene().getWindow();
        ViewOrderView viewOrder = new ViewOrderView(primaryStage.getScene());
        primaryStage.setTitle(viewOrder.getTitle());
        primaryStage.setScene(viewOrder.getScene());
    }

    @FXML
    public void updateQuantity() {
        Book selectedBook = cartListView.getSelectionModel().getSelectedItem();
        if (selectedBook != null && !quantityField.getText().isEmpty()) {
            int quantity = Integer.parseInt(quantityField.getText());
            System.out.println("Updated quantity for " + selectedBook + ": " + quantity);
        }
    }

    public void selectShoppingCartTab() {
        if (tabPane != null && cartTab != null) {
            tabPane.getSelectionModel().select(cartTab);
            System.out.println("Switched to shopping cart tab!");
        }
    }

    public void selectAccountTab() {
        if (tabPane != null && accountTab != null) {
            tabPane.getSelectionModel().select(accountTab);
            System.out.println("Switched to Account tab!");
        }
    }
    // Method to show alerts for user notifications
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
