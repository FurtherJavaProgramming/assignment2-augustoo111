package View;

import java.io.IOException;
import Controller.ViewOrdersController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;

public class ViewOrderView {

    private Scene myAccount;  // Scene to navigate back to account

    public ViewOrderView(Scene myAccount) {
        this.myAccount = myAccount;
    }

    public String getTitle() {
        return "View Order Detail";
    }

    public Scene getScene() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("viewOrderview.fxml"));

        try {
            GridPane root = loader.load();
            ViewOrdersController controller = loader.getController();
            controller.setAccountScene(myAccount);  // Set the reference to go back to account
            return new Scene(root);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
