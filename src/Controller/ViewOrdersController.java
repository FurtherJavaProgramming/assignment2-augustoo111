package Controller;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.Book;
import model.Model;
import model.Order;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import Dao.OrderDao;
import Dao.UserDao;

public class ViewOrdersController {

    @FXML
    private TableView<Order> ordersTableView; // Table to display orders
    @FXML
    private TableColumn<Order, String> orderIdColumn;
    @FXML
    private TableColumn<Order, String> dateColumn;
    @FXML
    private TableColumn<Order, String> totalPriceColumn;
    @FXML
    private TableColumn<Order, String> booksColumn;
    @FXML
    private TableColumn<Order, String> quantitiesColumn;  // Column for total quantities

    @FXML
    private Button exportOrdersButton;
    @FXML
    private Button exportSelectedOrdersButton;
    @FXML
    private Button backButton;
    @FXML
    private Label accountUserNameLabel;


    private Scene accountScene;  // Reference to the account scene
    private HomeSceneController homeController;
    private ObservableList<Order> ordersList = FXCollections.observableArrayList();  // List of orders to display
	private OrderDao orderDao;
	// Set the account scene to go back to
    public void setAccountScene(Scene accountScene) {
        this.accountScene = accountScene;
    }
    
    public void setHomeController(HomeSceneController homeController) {
        this.homeController = homeController;
    }
    
    public void setModel(Model model) throws SQLException {
        orderDao.setup();
    	
    }
    public void setOrderDao(OrderDao orderDao) {
        this.orderDao = orderDao;
    }
    public void setUserDao(UserDao userDao) {
    }
    
  // Method to set the username in the label
    public void setUsername(String username) {
        if (accountUserNameLabel != null) {
        	accountUserNameLabel.setText(username); // Set the username in the label
        }
        viewOrdersByUser();
    }

    
    @FXML
    public void viewOrdersByUser() {
        String username = accountUserNameLabel.getText();  // Get the username input from a TextField

        try {
            // Call the method to get orders for the given username
            List<Order> userOrders = orderDao.getOrdersByUsername(username);

            // Display the orders in the TableView
            ordersTableView.getItems().setAll(userOrders);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception (e.g., show an error message to the user)
        }
    }

    // Method to handle going back to the account page
    @FXML
    public void handleBackToAccount() {
       
        homeController.updateAccountTab(); 

        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.setScene(accountScene);  // Set the account scene back
        stage.show();
    }

    // Load orders method with sorted by recent order
    public void loadOrders(List<Order> orders) {
        ordersList.clear();
        ordersList.addAll(orders);
        // Sort orders by date (most recent first)
        ordersList.sort((o1, o2) -> o2.getOrderDateTime().compareTo(o1.getOrderDateTime()));
        ordersTableView.setItems(ordersList);
    }

    // Initialize method to set up the table columns
    @FXML
    public void initialize() {
        // Set up the table columns with the properties from the Order class
        orderIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOrderId()));

        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getOrderDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd - HH:mm:ss"))
        ));

        totalPriceColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                String.format("%.2f", cellData.getValue().getTotalPrice())
        ));

        // For the booksColumn, concatenate the book titles into a single string
        booksColumn.setCellValueFactory(cellData -> {
            List<Book> booksPurchased = cellData.getValue().getBooks();
            // Join the titles of books purchased into a string separated by commas
            String bookTitles = booksPurchased.stream()
                    .map(Book::getTitle)  // Get the title of each book
                    .collect(Collectors.joining(", "));  // Join titles with commas
            return new SimpleStringProperty(bookTitles);
        });

        // For the quantitiesColumn, sum the total quantity of books purchased
        quantitiesColumn.setCellValueFactory(cellData -> {
            int totalQuantity = cellData.getValue().getBooks().stream()
                    .mapToInt(Book::getNoOfCopies)  // Sum the quantities of each book
                    .sum();
            return new SimpleStringProperty(String.valueOf(totalQuantity));  // Display total quantity as a string
        });
    }

    // Method to handle exporting all orders
    @FXML
    public void exportOrders() {
        exportToCSV(ordersList, "Export All Orders");
    }

    // Method to handle exporting selected orders
    @FXML
    public void exportSelectedOrders() {
        ObservableList<Order> selectedOrders = ordersTableView.getSelectionModel().getSelectedItems();
        if (selectedOrders.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Orders Selected", "Please select orders to export.");
        } else {
            exportToCSV(selectedOrders, "Export Selected Orders");
        }
    }

    // Method to export orders to a CSV file
    private void exportToCSV(ObservableList<Order> ordersToExport, String exportTitle) {
        // Define the file path where the CSV will be saved in the project directory
        String filePath = "src/main/resources/exports/" + exportTitle.replaceAll(" ", "_") + ".csv";

        try {
            // Ensure that the 'exports' folder exists, create if it doesn't
            Files.createDirectories(Paths.get("src/main/resources/exports"));

            try (FileWriter writer = new FileWriter(filePath)) {
                // Write CSV header
                writer.write("Order ID,Date/Time,Total Price,Books Purchased,Total Quantity\n");

                // Write order data
                for (Order order : ordersToExport) {
                    String booksPurchased = order.getBooks().stream()
                            .map(Book::getTitle)
                            .collect(Collectors.joining(" | "));

                    int totalQuantity = order.getBooks().stream()
                            .mapToInt(Book::getNoOfCopies)
                            .sum();

                    writer.write(order.getOrderId() + "," +
                            order.getOrderDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "," +
                            String.format("%.2f", order.getTotalPrice()) + "," +
                            booksPurchased + "," + totalQuantity + "\n");
                }

                showAlert(Alert.AlertType.INFORMATION, "Export Successful", "Orders have been exported to: " + filePath);
            }

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Export Failed", "Error occurred while exporting orders.");
            e.printStackTrace();
        }
    }

    // Helper method to show alerts
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
