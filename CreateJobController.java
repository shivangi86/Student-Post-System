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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Job;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class CreateJobController implements Initializable {

    public Button createJob;
    public TextField title;
    public TextArea description;
    public TextField proposed_price;
    String userId;
    String filePath;
    String fileName;

    public void initData(String currentUser) { this.userId = currentUser; }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        allowOnlyNumbers(proposed_price);
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

    public void createJobOnClick(ActionEvent actionEvent) throws Exception{
        System.out.println("Here");
        boolean validInput = false;

        if(title.getText().equals("")){
            showMessage("ERROR","Title cannot be empty");
            validInput = false;
        }
        else if(description.getText().equals("")){
            showMessage("ERROR","Description cannot be empty");
            validInput = false;
        }
        else if(proposed_price.getText().equals("")){
            showMessage("ERROR","Proposed price cannot be empty");
            validInput = false;
        }
        else{
            validInput = true;
        }


        if(validInput){
            String jobTitle = title.getText();
            String jobDescription = description.getText();
            double proposedPrice = Double.parseDouble(proposed_price.getText());

            // Create new job entry in database
            Job job = new Job();
            job.createNewJob(userId, jobTitle, jobDescription, proposedPrice,fileName);
            Stage stage =  (Stage) createJob.getScene().getWindow();
            stage.close();
            showMessage("SUCCESSFUL", "Job Created Successfully");
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
