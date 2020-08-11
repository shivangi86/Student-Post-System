package controller;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.EditPost;
import model.Post;
import model.UniLinkGUI;

import java.net.URL;
import java.util.ResourceBundle;

public class EditPostController implements Initializable {

    Post post;
    Stage window;
    public Text postTitle;
    public TextArea postDetails;
    String currentUser;
    UniLinkGUI unilink;
    EditPost editPost;

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

    public void closePost(ActionEvent actionEvent) {
        post.setStatus("CLOSE");
    }

    public void deletePost(ActionEvent actionEvent) {
    }

    public void savePost(ActionEvent actionEvent) throws Exception{
        String description = postDetails.getText();
        editPost.updatePostDetails(description,post.getPostId(),post.getType());
    }

    public void initData(Post item, Event event, UniLinkGUI unilink) {
        post = item;
        this.unilink = unilink;
        postTitle.setText(item.getTitle());
        postDetails.setText(item.getDescription());
        currentUser = item.getCreatorId();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        editPost = new EditPost();
    }
}
