package Controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Book;

import java.sql.SQLException;
import java.util.ArrayList;
import Dao.BookDao;

public class AdminDashboardController {

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
    private TextField selectedBookField, stockUpdateField;  // Field for updating stock quantity

    @FXML
    private Button logOutButton, updateStockButton;  // Buttons
    

    private Scene loginScene;  // Reference to the login scene
    private Stage primaryStage;  // Reference to the primary stage
    private BookDao bookDao;


 // Setter method to inject the BookDao instance
    public void setBookDao(BookDao bookDao) {
        this.bookDao = bookDao;
        loadBookData();
    }
    
    // Set the login scene to switch back after logging out
    public void setLoginScene(Scene loginScene) {
        this.loginScene = loginScene;
    }
    
    // Setter method for the Primary Stage
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    

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




    // Method to handle logout and switch back to the login scene
    @FXML
    public void logOut() {
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
    public void updateStock() {
        // Get the selected book title and the entered stock value
        String selectedBookTitle = selectedBookField.getText();
        String updatedStockValue = stockUpdateField.getText();

        // Check if a book is selected
        if (selectedBookTitle == null || selectedBookTitle.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Book Selected", "Please select a book to update.");
            return;
        }

        // Check if stock value is entered
        if (updatedStockValue == null || updatedStockValue.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Stock Entered", "Please enter the new stock quantity.");
            return;
        }

        try {
            // Parse the stock value entered by the user
            int newStock = Integer.parseInt(updatedStockValue);

            // Ensure the stock value is non-negative
            if (newStock < 0) {
                showAlert(Alert.AlertType.ERROR, "Invalid Stock Value", "Stock quantity cannot be negative.");
                return;
            }

            // Update the stock in the database
            bookDao.updateBookStock(selectedBookTitle, newStock);

            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Stock Updated", "Stock for \"" + selectedBookTitle + "\" has been successfully updated to " + newStock + ".");

            // Refresh the table to show the updated stock
            loadBookData();

        } catch (NumberFormatException e) {
            // If the stock value entered is not a valid number
            showAlert(Alert.AlertType.ERROR, "Invalid Stock Value", "Please enter a valid number for the stock quantity.");
        } catch (SQLException e) {
            // Handle SQL exceptions
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while updating the stock in the database.");
            e.printStackTrace();
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
