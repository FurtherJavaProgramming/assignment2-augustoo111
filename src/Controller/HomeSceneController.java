package Controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Dao.BookDao;
import Dao.OrderDao;
import Dao.UserDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Book;
import model.Model;
import model.Order;
import model.User;

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
    
    //for account tab label
    @FXML
    private Label accountUserNameLabel, firstNameLabel, lastNameLabel;  
    
    @FXML
    private TextField selectedBookField, quantityField, cartSelectedBookField, cartQuantityField;

    @FXML
    private Button continueShoppingButton, checkOutButton, addCartButton, updatePasswordButton, 
                   updateDetailsButton, logOutButton, viewOrderButton;

    @FXML
    private Label userNameLabel, cartLabel;
    
    private ArrayList<Book> cartItems = new ArrayList<>();
	private Stage primaryStage;
	private BookDao bookDao;
	private String username;
	private Model model;
	private UserDao userDao;
	private OrderDao orderDao;

    public HomeSceneController() {
    	
    }

    // Constructor to pass the primary stage and model
    public HomeSceneController(Stage primaryStage, Model model) {
    	this.primaryStage = primaryStage;
    }
    

  // Setter method to inject the BookDao instance
    public void setBookDao(BookDao bookDao) {
        this.bookDao = bookDao;
        loadBookData();
    }
    
    public BookDao getBookDao() {
        return this.bookDao;
    }

    public void setUserDao(UserDao userDao) {
    	this.userDao = userDao;
		
	}
    
 // Method to set the username in the label and load the cart data
    public void setUsername(String username) {
        this.username = username;  // Store the username

        // Set the username in the label if the label is not null
        if (userNameLabel != null) {
            userNameLabel.setText(username);
        }

        // Load cart data if the bookDao is initialized
        if (bookDao != null) {
            loadCartData();  // Only load cart data if the BookDao is already set
        }
    }


    public void setOrderDao(OrderDao orderDao) {
        this.orderDao = orderDao;

        // Call setup to ensure tables are created
        try {
            this.orderDao.setup();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database ERROR!", "Failed to initialize database tables.");
        }
    }
  

    // Set the primary stage
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    // Set the model
    public void setModel(Model model) {
        this.model = model;
        loadCartData();
    }

    //set home scene
    public void setHomeScene(Scene homeScene) {
    	
    }

    // Log out function
    @FXML
    public void logOut() {
    	saveCartItems();
            try {
                // Load the login view
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/loginview.fxml"));
                GridPane loginPane = loader.load();

                // Get the LoginController and pass the model and primaryStage
                LoginController loginController = loader.getController();
                loginController.setPrimaryStage(primaryStage);
                loginController.setModel(model); 

                // Set the login scene
                Scene loginScene = new Scene(loginPane);
                primaryStage.setScene(loginScene);
                primaryStage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        
    }
    

   

	@FXML
    public void initialize() throws SQLException {
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
        cartQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("noOfCopies")); 
        cartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        // Add listener to cartListView to show selected item in fields
        cartListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                cartSelectedBookField.setText(newSelection.getTitle());
                cartQuantityField.setText(String.valueOf(newSelection.getNoOfCopies()));
            }
        });
        

     
		if (username != null) {
            userNameLabel.setText(username);
        }
		if (model != null && model.getCurrentUser() != null) {
	        User currentUser = model.getCurrentUser();
	        
	        // Set the username, first name, and last name in the account tab
	        if (accountUserNameLabel != null) {
	            accountUserNameLabel.setText(currentUser.getUsername());
	        }
	        if (firstNameLabel != null) {
	        	firstNameLabel.setText(currentUser.getFirstName());
	        }
	        if (lastNameLabel != null) {
	        	lastNameLabel.setText(currentUser.getLastName());
	        }
	    }
		
		tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
	        if (newTab == accountTab) {
	            updateAccountTab();
	        }
	    });
		
		
    }
	
	//update account tab after user update user detail
	public void updateAccountTab() {
	    if (model != null && model.getCurrentUser() != null) {
	        User currentUser = model.getCurrentUser();
	        
	        // Set the username, first name, and last name in the account tab
	        accountUserNameLabel.setText(currentUser.getUsername());
	        firstNameLabel.setText(currentUser.getFirstName());
	        lastNameLabel.setText(currentUser.getLastName());
	    }
	}

    // Method to load all books from the DAO and display in the table
    public void loadBookData() {
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
                    showAlert(Alert.AlertType.ERROR, "ERROR!", "No books found in the database.");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "ERROR!", "Unable to load book data from the database.");
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "ERROR!", "BookDao is not initialized.");
        }
    }
        
    // Load the cart items from the database for the current user
    public void loadCartData() {
            try {
                ArrayList<Book> savedCartItems = bookDao.loadCartItems(username);
                
                if (savedCartItems != null && !savedCartItems.isEmpty()) {
                    cartItems.clear();
                    cartItems.addAll(savedCartItems);
                    
                    // Set the cart items into the ListView and refresh the view
                    cartListView.getItems().setAll(cartItems);
                    cartListView.refresh();  // Refresh to ensure items are shown

                } else {
                    cartListView.getItems().clear();  // Clear the ListView if no items are found
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "ERROR!", "Unable to load cart items from the database.");
                e.printStackTrace();
                System.out.println("Error while loading cart items: " + e.getMessage());  // Debugging
            }
        
    }

//save cart items method
    public void saveCartItems() {
            try {
                
                for (Book book : cartItems) {
                    bookDao.saveCartItem(username, book);  // Save each book in the cart to the database
                }
                
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "ERROR!", "Unable to save cart items to the database.");
                e.printStackTrace();
            }
    }
    
    

    
    @FXML
    //method for add to cart button
    public void addToCart() {
        // Get the selected book from the bookStockTable
        Book selectedBook = bookStockTable.getSelectionModel().getSelectedItem();
        String chooseItem = selectedBookField.getText();
        String quantityText = quantityField.getText();
        
        // Check if a book is selected
        if (chooseItem == null || chooseItem.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Book Selected", "Please select a book to add to the cart.");
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
            if (quantity <= 0) {
            	showAlert(Alert.AlertType.ERROR, "Invalid Quantity", 
                        "Quantity must be greater than 0!");
              return;
            }

            // Get the available copies of the selected book
            int availableCopies = selectedBook.getNoOfCopies();

            // Check if the book is already in the cart
            Book bookInCart = findBookInCart(selectedBook);
            
            // If the book is already in the cart, reduce the available copies based on what is in the cart
            int alreadyInCart = bookInCart != null ? bookInCart.getNoOfCopies() : 0;
            int remainingCopies = availableCopies - alreadyInCart;
     
            // Validate the quantity (must be less than or equal to available copies if not in cart)
            if (quantity > availableCopies) {
                showAlert(Alert.AlertType.ERROR, "Exceeds Available Stock", 
                          "You can only add up to " + availableCopies + " copies.");
                return;
            }

            // Validate the quantity (must be positive and less than or equal to remaining available copies)
            if (quantity > remainingCopies) {
                showAlert(Alert.AlertType.ERROR, "Invalid Quantity", 
                		"You already have " + bookInCart.getNoOfCopies() +
                         " in your cart. Your remaining copies (" + remainingCopies + ").");
                return;
            }

         // Calculate the total price for the selected books (unit price * quantity)
            double totalPrice = selectedBook.getPrice() * quantity;

            if (bookInCart != null) {
                // If the book is already in the cart, update the quantity and recalculate the total price
                bookInCart.setNoOfCopies(bookInCart.getNoOfCopies() + quantity);  // Update the quantity
                bookInCart.setTotalPrice(bookInCart.getNoOfCopies() * selectedBook.getPrice());  // Set total price
            } else {
                // If the book is not in the cart, create a new entry with the specified quantity and unit price
                bookInCart = new Book(
                    selectedBook.getTitle(),
                    selectedBook.getAuthor(),
                    quantity,  // Set the quantity in the cart
                    selectedBook.getPrice(),  // Use the unit price here
                    selectedBook.getSoldCopies()
                );
                bookInCart.setTotalPrice(totalPrice);  // Set the total price for the new book
                cartItems.add(bookInCart);  // Add to the cart
            }

            // Refresh the cartListView by resetting its items
            cartListView.getItems().clear();
            cartListView.getItems().addAll(cartItems);
            // Show success message including the total price
            showAlert(Alert.AlertType.INFORMATION, "Book Added to Cart", 
                      "Book \"" + selectedBook.getTitle() + "\" has been added to the cart");

            // Optionally, clear the selected fields for a new selection
            selectedBookField.clear();
            quantityField.clear();

        } catch (NumberFormatException e) {
            // If the quantity entered is not a valid number
            showAlert(Alert.AlertType.ERROR, "Invalid Quantity", "Please enter a valid number for the quantity.");
        }
    }
    

    
    
    // update quantity button
    @FXML
    public void updateQuantity() {
        // Get the selected book from the cartListView
        Book selectedBook = cartListView.getSelectionModel().getSelectedItem();
        
        // Check if a book is selected
        if (selectedBook == null) {
            showAlert(Alert.AlertType.WARNING, "No Book Selected", "Please select a book from the cart to update.");
            return;
        }

        String newQuantityText = cartQuantityField.getText();
        
        // Check if a valid quantity is entered
        if (newQuantityText == null || newQuantityText.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Quantity Entered", "Please enter a quantity.");
            return;
        }

        try {
            int newQuantity = Integer.parseInt(newQuantityText);

            // Validate the new quantity (must be positive)
            if (newQuantity <= 0) {
                showAlert(Alert.AlertType.ERROR, "Invalid Quantity", "Quantity must be greater than 0.");
                return;
            }

            // Check if the new quantity exceeds the available stock
            Book bookInStock = findBookInStock(selectedBook);
            if (bookInStock == null) {
                showAlert(Alert.AlertType.ERROR, "Book Not Found", "The selected book is not available in stock.");
                return;
            }

            int availableCopies = bookInStock.getNoOfCopies();
            if (newQuantity > availableCopies) {
                showAlert(Alert.AlertType.ERROR, "Invalid Quantity", "Quantity exceeds available stock (" + availableCopies + ").");
                return;
            }

            // Update the quantity and total price in the cart
            selectedBook.setNoOfCopies(newQuantity);
            selectedBook.setTotalPrice(newQuantity * selectedBook.getPrice());

            // Refresh the cart list view to show updated quantity and price
            cartListView.refresh();

            // Clear the quantity field after updating
            cartQuantityField.clear();

            showAlert(Alert.AlertType.INFORMATION, "Quantity Updated", "The quantity for \"" + selectedBook.getTitle() + "\" has been updated to " + newQuantity + ".");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Quantity", "Please enter a valid number for the quantity.");
        }
    }
    //remove book from cart method
    @FXML
    public void removeBookFromCart() {
        // Get the selected book from the cartListView
        Book selectedBook = cartListView.getSelectionModel().getSelectedItem();

        // Check if a book is selected from the cart
        if (selectedBook == null) {
            showAlert(Alert.AlertType.WARNING, "No Book Selected", "Please select a book to remove.");
            return;
        }
        // Remove the selected book from the cartItems list
        cartItems.remove(selectedBook);

        // Refresh the cartListView to reflect the removed item
        cartListView.getItems().clear();
        cartListView.getItems().addAll(cartItems);

        // Clear the selected fields after removing
        cartSelectedBookField.clear();
        cartQuantityField.clear();

        showAlert(Alert.AlertType.INFORMATION, "Book Removed", "The book \"" + selectedBook.getTitle() + "\" has been removed from the cart.");
    }
    

    // Helper method to find a book in the cart
    public Book findBookInCart(Book selectedBook) {
        for (Book book : cartItems) {
            if (book.getTitle().equals(selectedBook.getTitle()) && book.getAuthor().equals(selectedBook.getAuthor())) {
                return book;  // Return the book if found in the cart
            }
        }
        return null;  // Return null if the book is not in the cart
    }
    


    // Helper method to find the corresponding book in the stock table
    public Book findBookInStock(Book selectedBook) {
        for (Book book : bookStockTable.getItems()) {
            if (book.getTitle().equals(selectedBook.getTitle()) && book.getAuthor().equals(selectedBook.getAuthor())) {
                return book;
            }
        }
        return null;
    }
    

    

 //update firstname and lastname
    @FXML
    public void updateDetails(ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/updateProfile.fxml"));
            GridPane root = loader.load();

            // Get the controller for the update profile view
            UpdateDetailsController updateDetailsController = loader.getController();

            // Set the current user model and other dependencies
            updateDetailsController.setModel(model);  // Set model for current user
            updateDetailsController.setUserDao(userDao);  // Set DAO for database access

            // Here's the key part: Pass this HomeSceneController instance to UpdateDetailsController
            updateDetailsController.setHomeController(this); 

            // Pass the current scene to allow navigating back
            updateDetailsController.setAccountScene(primaryStage.getScene());  // Set the current account scene

            // Set the new scene for the stage
            Scene updateProfileScene = new Scene(root);
            Stage primaryStage = (Stage) ((Button) e.getSource()).getScene().getWindow();
            primaryStage.setScene(updateProfileScene);
            primaryStage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "ERROR!", "Failed to load the Update Profile view.");
        }
    }


//update Password
    @FXML
    public void updatePassword(ActionEvent e) {
    	try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/updatePassword.fxml"));
            GridPane root = loader.load();
            // Get the controller for the update profile view
            UpdatePasswordController updatepwd = loader.getController();

            // Set the current user model and other dependencies
            updatepwd.setModel(model);  // Set model for current user
            updatepwd.setUserDao(userDao);  // Set DAO for database access
            // Here's the key part: Pass this HomeSceneController instance to UpdateDetailsController
            updatepwd.setHomeController(this); 
            // Pass the current scene to allow navigating back
            updatepwd.setAccountScene(primaryStage.getScene());  // Set the current account scene

            // Set the new scene for the stage
            Scene updateProfileScene = new Scene(root);
            Stage primaryStage = (Stage) ((Button) e.getSource()).getScene().getWindow();
            primaryStage.setScene(updateProfileScene);
            primaryStage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "ERROR!", "Failed to load the Update Profile view.");
        }
        
    }
    public void setLoginScene(Scene loginScene) {
    }



    @FXML
    public void continueShopping() {
        if (tabPane != null && homeTab != null) {
            tabPane.getSelectionModel().select(homeTab);
        }
    }


  //check out method
    @FXML
    public void checkOut(ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/checkOutView.fxml"));
            GridPane root = loader.load();

            // Get the controller associated with checkOutView.fxml
            CheckOutController checkOutController = loader.getController();

            // Set the primary stage in the CheckOutController
            checkOutController.setPrimaryStage(primaryStage);
            checkOutController.setModel(model);

            // Pass the HomeSceneController (this) to the CheckOutController
            checkOutController.setHomeController(this);  // Pass the HomeSceneController
            
            // Pass the Home Scene
            checkOutController.setHomeScene(primaryStage.getScene()); 
            checkOutController.setShoppingCartScene(primaryStage.getScene()); 
            checkOutController.setUsername(username);

            // Set the cart details (total items and total amount)
            checkOutController.setCartDetails(calculateTotalItems(), calculateTotalAmount());
            checkOutController.setCartItems(cartItems);

            // Create and set the checkout scene
            Scene checkOutScene = new Scene(root);
            primaryStage.setScene(checkOutScene);            


        } catch (IOException ex) {
            ex.printStackTrace();
        }
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

    //view Order method
    @FXML
    public void viewOrder(ActionEvent e) throws SQLException {

        try {
            // Load the FXML file for the view orders screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/viewOrderView.fxml"));
            Scene viewOrderScene = new Scene(loader.load());

            // Get the controller instance for the new scene (ViewOrdersController)
            ViewOrdersController viewOrdersController = loader.getController();

            // Retrieve the orders from your data source (OrderDao)
            List<Order> orders = orderDao.getAllOrders();  // Now this will get orders from the `orders` table
            viewOrdersController.setHomeController(this);
            viewOrdersController.setAccountScene(primaryStage.getScene());
            // Pass the orders to the ViewOrdersController
            viewOrdersController.loadOrders(orders);
            viewOrdersController.setOrderDao(orderDao);
            viewOrdersController.setUsername(model.getCurrentUser().getUsername());

            // Get the current stage and set the new scene
            Stage primaryStage = (Stage) ((Button) e.getSource()).getScene().getWindow();
            primaryStage.setScene(viewOrderScene);
            primaryStage.show();

        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the View Orders screen.");
        }
    }

    


    public void selectShoppingCartTab() {
        if (tabPane != null && cartTab != null) {
            tabPane.getSelectionModel().select(cartTab);
            
        }
        
    }

    public void selectAccountTab() {
        if (tabPane != null && accountTab != null) {
            tabPane.getSelectionModel().select(accountTab);
        }
   }


    public void selectHomeTab() {
        if (tabPane != null && homeTab != null) {
            tabPane.getSelectionModel().select(homeTab);
        }
    }
    
    // Method to show alerts for user notifications
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }



	// Clear cart in HomeSceneController
	public void clearCart() {
	    cartItems.clear();  // Clear the cart items list
	    cartListView.getItems().clear();  // Refresh the UI by clearing the cart view
	    try {
	        if (bookDao != null && username != null) {
	            bookDao.clearCartItems(username);  // Clear the cart in the database for the current user
	        }
	    } catch (SQLException e) {
	        showAlert(Alert.AlertType.ERROR, "Error", "Failed to clear cart on logout.");
	        e.printStackTrace();
	    }
	}

}