package Controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import Dao.BookDao;
import Dao.BookDaoImplementation;
import Dao.OrderDao;
import Dao.OrderDaoImplementation;
import Dao.UserDao;
import Dao.UserDaoImplementation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Model;

public class OrderDetailController {
    
    @FXML
    private TextField orderNumberField;
    @FXML
    private TextField totalItemsField;
    @FXML
    private TextField totalAmountField;
    @FXML
    private Button backToHomeButton;

    private String orderNumber;
    private int totalItems;
    private double totalAmount;
    private Stage primaryStage;
    private Scene homeScene;
    private Model model;
    @FXML
    private Label message;
    private OrderDao orderDao;
    
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setModel(Model model) {
    	this.model = model;
    	}

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
        updateOrderDetails();  // Update the UI
    	}



    public void setHomeScene(Scene homeScene) {
        this.homeScene = homeScene;
    	}

    public void setOrderDao(OrderDao orderDao) throws SQLException {
         orderDao.setup();
        
    	}

    public void setOrderDetails(List<model.Book> cartItem, int totalItems, double totalAmountValue) {
        this.totalItems = totalItems;
        this.totalAmount = totalAmountValue;
        // Update the UI labels with the order information
    }


    private void updateOrderDetails() {
        // Set the order number, total items, and total amount to the UI fields
        if (orderNumberField != null) {
            orderNumberField.setText(orderNumber);
        }

        if (totalItemsField != null) {
            totalItemsField.setText(String.valueOf(totalItems));
        }

        if (totalAmountField != null) {
            totalAmountField.setText(String.format("%.2f AUD", totalAmount));
        }

    }

    
    
    @FXML
    private void goBackToHome() throws SQLException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/homeview.fxml"));
            TabPane root = loader.load();

            HomeSceneController homeSceneController = loader.getController();

            BookDao bookDao = new BookDaoImplementation();
            homeSceneController.setBookDao(bookDao);

            UserDao userDao = new UserDaoImplementation();
            homeSceneController.setUserDao(userDao);

            if (orderDao == null) {
                orderDao = new OrderDaoImplementation();
            }
            homeSceneController.setOrderDao(orderDao);
            homeSceneController.setPrimaryStage(primaryStage);
            homeSceneController.setHomeScene(homeScene);
            homeSceneController.setModel(model);
            homeSceneController.setLoginScene(primaryStage.getScene());
            homeSceneController.setUsername(model.getCurrentUser().getUsername());

            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            showErrorMessage(e.getMessage());
        }
    }

    private void showErrorMessage(String messageText) {
        message.setText(messageText);
        message.setTextFill(Color.RED);
    }

	
}
