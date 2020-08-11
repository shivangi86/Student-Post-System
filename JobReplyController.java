package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import model.Job;
import model.Sale;

import java.net.URL;
import java.util.ResourceBundle;

public class JobReplyController implements Initializable {

    String currentUser;
    Job job;
    public TextField jobReply;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        allowOnlyNumbers(jobReply);
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
        String jobReplyStr = jobReply.getText();
        System.out.println("jobReplyStr : " + jobReplyStr);
        job.handleReply(Double.parseDouble(jobReplyStr),currentUser);
    }

    public void initData(String currentUser, Job item) {
        this.currentUser = currentUser;
        job = item;
    }
}
