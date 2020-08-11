package controller;

import javafx.animation.PauseTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Sale;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class CreateSaleController implements Initializable {

    public Button createSale;
    public TextField title;
    public TextArea description;
    public TextField asking_price;
    public TextField minimum_raise;

    String userId;
    String filePath;
    String fileName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        allowOnlyNumbers(asking_price);
        allowOnlyNumbers(minimum_raise);
    }

    public void allowOnlyNumbers(TextField field){
        field.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    field.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    public void initData(String currentUser){
        this.userId = currentUser;
    }

//    textField.textProperty().addListener(new ChangeListener<String>() {
//        @Override
//        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//            if (!newValue.matches("\\d{0,7}([\\.]\\d{0,4})?")) {
//                vehiclePrice_TextField.setText(oldValue);
//            }
//        }
//    });


    public void createSaleOnClick(ActionEvent event) throws Exception{

        boolean validInput = false;

        if(title.getText().equals("")){
            showMessage("ERROR","Title cannot be empty");
           validInput = false;
        }
        else if(description.getText().equals("")){
            showMessage("ERROR","Description cannot be empty");
            validInput = false;
        }
        else if(asking_price.getText().equals("")){
            showMessage("ERROR","Asking price cannot be empty");
            validInput = false;
        }
        else if(minimum_raise.getText().equals("")){
            showMessage("ERROR","Minimum raise cannot be empty");
            validInput = false;
        }
        else{
            validInput = true;
        }


        if(validInput){
            String saleTitle = title.getText();
            String saleDesc = description.getText();
            double askingPrice = Double.parseDouble(asking_price.getText());
            double minimumRaise = Double.parseDouble(minimum_raise.getText());

            // Create new sale entry in database
            Sale sale = new Sale();
            if(filePath!=null){
                sale.createNewSale(userId,saleTitle, saleDesc, askingPrice, minimumRaise, fileName);
            }
            else{
                sale.createNewSale(userId,saleTitle, saleDesc, askingPrice, minimumRaise, "noImage");
            }
            Stage stage =  (Stage) createSale.getScene().getWindow();
            stage.close();
            showMessage("SUCCESSFUL", "Sale Created Successfully");
        }
    }

    private void showMessage(String title, String message) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/message.fxml"));
        Parent root = (Parent) loader.load();
        ErrorController controller = loader.getController();
        controller.initData(title,message);
        Scene newScene = new Scene(root);
        Stage newStage = new Stage();
        newStage.setScene(newScene);

        if(title.equals("SUCCESSFUL")){
            newStage.show();
            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            delay.setOnFinished( event -> newStage.close() );
            delay.play();
        }
        else{
            newStage.showAndWait();
        }
    }


    public void openImage(ActionEvent actionEvent) throws IOException {
        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);

        //Show open file dialog
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            filePath = file.toURI().toString();
            fileName = file.getName();
            // Store file in the images folder
            Path to = Paths.get("C:/Users/HARSH/IdeaProjects/UniLinkUI/images",fileName);
            Files.copy(file.toPath(), to);
        }
    }
}
