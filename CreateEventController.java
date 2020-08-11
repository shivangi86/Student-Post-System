package controller;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Event;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

public class CreateEventController {
    public Button createEvent;
    public TextField title;
    public TextArea description;
    public TextField venue;
    public DatePicker date;
    public TextField capacity;
    public Button image;

    String userId;
    String filePath;
    String fileName;

    public void initData(String currentUser){
        this.userId = currentUser;
    }

    public void createEventOnClick(ActionEvent event) throws Exception{
        boolean validInput = false;

        if(title.getText().equals("")){
            showMessage("ERROR","Title cannot be empty");
            validInput = false;
        }
        else if(description.getText().equals("")){
            showMessage("ERROR","Description cannot be empty");
            validInput = false;
        }
        else if(venue.getText().equals("")){
            showMessage("ERROR","Asking price cannot be empty");
            validInput = false;
        }

        else if(date.equals("")){
            showMessage("ERROR","Minimum raise cannot be empty");
            validInput = false;
        }
        else{
            validInput = true;
        }


        if(validInput){
            String eventTitle = title.getText();
            String eventDesc = description.getText();
            String eventVenue = venue.getText();
            String eventCapacity = capacity.getText();
            LocalDate eventDate = date.getValue();

            // Create new sale entry in database
            Event eventPost = new Event();
            eventPost.createNewEvent(userId, eventTitle, eventDesc, eventVenue, eventCapacity, eventDate, fileName);
            Stage stage =  (Stage) createEvent.getScene().getWindow();
            stage.close();
            showMessage("SUCCESSFUL", "Event Created Successfully");
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
            Path to = Paths.get("C:/Users/HARSH/IdeaProjects/UniLinkUI/images", fileName);
            Files.copy(file.toPath(), to);
        }
    }
}
