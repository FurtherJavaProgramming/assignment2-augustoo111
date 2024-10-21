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
import Dao.BookDao;
import Dao.UserDao;
import Dao.UserDaoImplementation;
import Dao.BookDaoImplementation;
import Dao.OrderDao;
import Dao.OrderDaoImplementation;


public class LoginController {

    private Stage primaryStage;
    private Model model;

    @FXML
    private TextField userName;

    @FXML
    private PasswordField password;

    @FXML
    private Label message;

    @FXML
    private Button login;

    @FXML
    private Button signUp;
    private Scene homeScene;;

    public LoginController() {
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
        
    }
    public void setModel(Model model) {
        this.model = model;
        
        // Call the setup method for each DAO to initialize tables
        try {
            // Set up the database tables
            BookDao bookDao = new BookDaoImplementation();
            bookDao.setup();  // This will create the necessary tables for books
            
            UserDao userDao = new UserDaoImplementation();
            userDao.setup();  // This will create the necessary tables for users
            
            OrderDao orderDao = new OrderDaoImplementation();
            orderDao.setup();  // This will create the necessary tables for orders
            
            // Optionally set these DAOs in the model if needed
            model.setUserDao(userDao);
            model.setBookDao(bookDao);
            model.setOrderDao(orderDao);
            
        } catch (SQLException e) {
            showErrorMessage("Error setting up database: " + e.getMessage());
        }
        
        login.setOnAction(event -> setupLogin());
        signUp.setOnAction(event -> loadSignUpScene());
    }

//    @FXML
//    public void initialize() {
//        // Handle login button action
//        login.setOnAction(event -> setupLogin());
//        // Handle sign-up button action
//        signUp.setOnAction(event -> loadSignUpScene());
//    }

    //Method to load the home scene for regular users
    private void loadHomeScene() throws SQLException {
        try {
            // Load FXML and controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/homeview.fxml"));
            TabPane root = loader.load();

            // Get controller instance from the FXMLLoader
            HomeSceneController homeSceneController = loader.getController();

            // Instantiate the BookDao and set it in the controller
            BookDao bookDao = new BookDaoImplementation();  
            homeSceneController.setBookDao(bookDao);  

         // Instantiate the userDao and set it in the controller
            UserDao userDao = new UserDaoImplementation();
            homeSceneController.setUserDao(userDao);
            
            OrderDao orderDao = new OrderDaoImplementation();
            homeSceneController.setOrderDao(orderDao);
            
            // Set primary stage, loginScene and username
            homeSceneController.setPrimaryStage(primaryStage);
            homeSceneController.setHomeScene(homeScene);
            homeSceneController.setModel(model);
            homeSceneController.setLoginScene(primaryStage.getScene());
            homeSceneController.setUsername(model.getCurrentUser().getUsername());  // Pass the current user's username

            // Set the scene
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            showErrorMessage(e.getMessage());
        }
    }
    
    // Setup method for login actions
    public void setupLogin() {
    	
            if (!userName.getText().isEmpty() && !password.getText().isEmpty()) {
                try {
                    // First, try to fetch the user from the regular users table
                    User user = model.getUserDao().getUser(userName.getText(), password.getText());
                    
                    // If not a regular user, check the admin table
                    if (user == null) {
                        AdminUser adminUser = model.getUserDao().getAdminUser(userName.getText(), password.getText());
                        if (adminUser != null) {
                            model.setCurrentUser(adminUser);  // Save the current user in the model
                            loadAdminDashboardScene();
                        } else {
                            showErrorMessage("Wrong username or password");
                        }
                    } else {
                        model.setCurrentUser(user);  // Save the current user in the model
                        
                        loadHomeScene();
                    }
                } catch (SQLException e) {
                    showErrorMessage(e.getMessage());
                }
            } else {
                showErrorMessage("Empty username or password");
            }

            userName.clear();
            password.clear();
     }


    

    
   

 // Method to load the admin dashboard scene for admins
    private void loadAdminDashboardScene() throws SQLException {
        try {
            // Load the FXML file for the Admin Dashboard
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/admindashboardview.fxml"));
            TabPane root = loader.load();  // Load the root node (TabPane) from FXML

            // Get the controller instance from the loader
            AdminDashboardController adminController = loader.getController();

            // Instantiate the BookDao and set it in the controller
            BookDao bookDao = new BookDaoImplementation();  
            adminController.setBookDao(bookDao);  
            
            // Instantiate the userDao and set it in the controller
            UserDao userDao = new UserDaoImplementation();
            adminController.setUserDao(userDao);
            
            
            

            // Set primary stage, loginScene and username
            adminController.setPrimaryStage(primaryStage);
            adminController.setLoginScene(primaryStage.getScene());
            adminController.setUsername(model.getCurrentUser().getUsername());

            // Create and set the scene
            primaryStage.setScene(new Scene(root));
            primaryStage.show();  // Show the primary stage
        } catch (IOException e) {
            // Handle any IO or SQL exceptions that occur during loading
            showErrorMessage("Failed to load the Admin Dashboard: " + e.getMessage());
        }
        
    }


    // Method to load the sign-up scene
    private void loadSignUpScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/signupview.fxml"));

            GridPane root = loader.load();  // Load the FXML

            // Get the SignUpController and set the primaryStage and model
            SignUpController signUpController = loader.getController();
            // Set the model, primary stage and username
            signUpController.setModel(model);
            signUpController.setPrimaryStage(primaryStage); 
            signUpController.setLoginScene(primaryStage.getScene());

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