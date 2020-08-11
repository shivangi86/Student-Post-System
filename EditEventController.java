package controller;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.EditPost;
import model.Post;
import model.Reply;
import model.UniLinkGUI;

import java.io.File;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class EditEventController implements Initializable {
    Post post;
    Stage window;
    String currentUser;
    public TextField title;
    public TextField description;
    public TextField venue;
    public TextField capacity;
    public TextField attendeeCount;
    public DatePicker date;
    public Label mainTitle;
    public ImageView image;
    public Label status;
    public ListView attendeesList;

    UniLinkGUI unilink;
    EditPost editPost;

    public void initData(Post item, Event event, UniLinkGUI unilink) throws Exception {

        post = item;
        this.unilink = unilink;

        // Load replies from database
        ArrayList<String> attendees = EditPost.loadEventReplies(item);
        if(attendees!=null){
            for (String attendee: attendees) {
                attendeesList.getItems().add(attendee);
            }
        }

        // Set image
        if(item.getImage().equals("noImage")){
            File file = new File("C:/Users/HARSH/IdeaProjects/UniLinkUI/images/No_image_available3.png");
            System.out.println(file);
            Image imagePath = new Image(file.toURI().toString());
            image.setImage(imagePath);
            image.setCache(true);
        }
        else{
            File file = new File("C:/Users/HARSH/IdeaProjects/UniLinkUI/images/"+item.getImage());
            System.out.println(file);
            Image imagePath = new Image(file.toURI().toString());
            image.setImage(imagePath);
            image.setCache(true);
        }

        mainTitle.setText(item.getTitle());
        title.setText(item.getTitle());
        description.setText(item.getDescription());
        venue.setText( ((model.Event)item).getVenue());
        capacity.setText( Integer.toString( ((model.Event)item).getCapacity() ));
        attendeeCount.setText(Integer.toString( ((model.Event)item).getAttendeeCount() ));
        date.setValue(LocalDate.parse(((model.Event)item).getDate()));
        status.setText(item.getStatus());
        currentUser = item.getCreatorId();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        editPost = new EditPost();
    }

    public void backToMain(ActionEvent event) throws Exception{
        // Navigate to mainController window
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("view/main.fxml"));
        Parent mainViewParent = loader.load();
        Scene mainViewScene = new Scene(mainViewParent);

        // Access Main Controller and store login information
        UniLinkGUIController controller = loader.getController();
        controller.initData(currentUser,event,unilink);

        // Get current stage information
        window = (Stage)((Node)event.getSource()).getScene().getWindow();
        // Set the main scene to the stage
        window.setScene(mainViewScene);
    }

    public void closePost(ActionEvent actionEvent) throws Exception{
        post.setStatus("CLOSE");
        status.setText("CLOSE");
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/message.fxml"));
        Parent root = (Parent) loader.load();
        ErrorController controller = loader.getController();
        controller.initData("SUCCESSFUL","SALE CLOSED SUCCESSFULLY");
        Scene newScene = new Scene(root);
        Stage newStage = new Stage();
        newStage.setScene(newScene);
        newStage.show();
        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished( event -> newStage.close() );
        delay.play();

    }

    public void deleteEvent(ActionEvent actionEvent) throws Exception {
        Parent confirmDeleteScreen = FXMLLoader.load(getClass().getClassLoader().getResource("view/confirm_delete.fxml"));
        Stage confirmDeleteWindow = new Stage();
        ConfirmDeleteController confirmDelete = new ConfirmDeleteController();
        confirmDelete.initData(post,editPost);
        Scene scene = new Scene(confirmDeleteScreen);
        confirmDeleteWindow.setScene(scene);
        confirmDeleteWindow.showAndWait();
    }

    public void save(ActionEvent actionEvent) throws Exception {
        editPost.updateSaleDetails(title.getText(), description.getText(), venue.getText(), date.getValue().toString() ,post.getPostId());
    }
}
