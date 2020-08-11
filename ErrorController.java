package controller;

import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class ErrorController implements Initializable {
    public Text error_title;
    public Text error_text;
    public AnchorPane error_window;

    public void initData(String title, String message) throws Exception{
        System.out.println("in error controller");
        error_title.setText(title);
        error_title.setFill(Paint.valueOf("RED"));
        error_text.setText(message);
        error_text.wrappingWidthProperty().bind(error_window.widthProperty());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
