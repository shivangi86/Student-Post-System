package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import model.Sale;

import java.net.URL;
import java.util.ResourceBundle;

public class SaleReplyController implements Initializable {

    String currentUser;
    Sale sale;
    public TextField saleReply;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        allowOnlyNumbers(saleReply);
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

    public void submitReply(ActionEvent actionEvent) throws Exception {
        String saleReplyStr = saleReply.getText();
        sale.handleReply(Double.parseDouble(saleReplyStr), currentUser);
    }

    public void initData(String currentUser, Sale item) {
        this.currentUser = currentUser;
        sale = item;
    }
}
