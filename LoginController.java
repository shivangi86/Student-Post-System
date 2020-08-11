package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Login;


public class LoginController{

    // This variable will search for login_btn in the fxml and link it together
    public Button login_btn;
    public TextField login_field;
    public Text login_successful;

    public void onLoginClick(ActionEvent event) throws Exception {
        String userId = login_field.getText();
        // Check if userId is entered
        if (userId.equals("")) {
            login_successful.setText("User name cannot be left blank");
        } else {
            Login lg = new Login();
            lg.handleLogin(userId);

            // Navigate to mainController window
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/main.fxml"));
            Parent mainViewParent = loader.load();
            Scene mainViewScene = new Scene(mainViewParent);

            // Access Main Controller and store login information
            UniLinkGUIController controller = loader.getController();
            controller.initData(userId,event);

            // Get current stage information
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

            // Set the main scene to the stage
            window.setScene(mainViewScene);
        }


    }


}
