package View;

import java.io.IOException;

import Controller.OrderDetailController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;

public class OrderDetailView {
	private Scene myAccount;  // Scene to navigate back to login
    
    public OrderDetailView(Scene myAccount) {
    	this.myAccount = myAccount;
    }

    public String getTitle() {
        return "Order Detail";
    }

    public Scene getScene() {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("orderDetail.fxml"));

        try {
            GridPane root = loader.load();
            OrderDetailController controller = loader.getController();
            controller.setAccountScene(myAccount);
            return new Scene(root);
            } catch (IOException e) {
            	e.printStackTrace();
            	return null;
            	}
        }

}
