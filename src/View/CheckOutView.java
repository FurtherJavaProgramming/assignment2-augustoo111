package View;

import java.io.IOException;

import Controller.CheckOutController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;

public class CheckOutView {

	private Scene shoppingCart;  // Scene to navigate back to login
    
    public CheckOutView(Scene shoppingCart) {
    	this.shoppingCart = shoppingCart;
    }

    public String getTitle() {
        return "Check Out";
    }

    public Scene getScene() {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("checkOutView.fxml"));

        try {
            GridPane root = loader.load();
            CheckOutController controller = loader.getController();
            controller.setShoppingCartScene(shoppingCart);
            return new Scene(root);
            } catch (IOException e) {
            	e.printStackTrace();
            	return null;
            	}
        }

}
