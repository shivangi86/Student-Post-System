package controller;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.Set;

public class EditSaleController implements Initializable {
    Post post;
    Stage window;
    String currentUser;
    public TextField title;
    public TextField description;
    public Label status;
    public TextField askingPrice;
    public TextField minimumRaise;
    public Label mainTitle;
    public ImageView image;
    public Button closeSale;
    public Button deleteSale;
    public Button save;
    public TableView replies;
    public TableColumn reply;
    public TableColumn creatorid;


    UniLinkGUI unilink;
    EditPost editPost;
    private ObservableList<Reply> replyList = FXCollections.observableArrayList();

    public void initData(Post item, Event event, UniLinkGUI unilink) throws Exception {
        System.out.println("In init data");
        post = item;
        this.unilink = unilink;

        reply.setCellValueFactory(
                new PropertyValueFactory<Reply,String>("value")
        );
        creatorid.setCellValueFactory(
                new PropertyValueFactory<Reply,String>("responderId")
        );

        // Load replies from database
        Hashtable<String, String> saleReplies = EditPost.loadSaleJobReplies(item);
        if(saleReplies!=null){
            System.out.println("saleReplies not null");
            Set<String> keys = saleReplies.keySet();
            for (String key: keys) {
                System.out.println("key : " + key);
                System.out.println("value : " + saleReplies.get(key));
                replyList.add(new Reply(key, saleReplies.get(key)));
            }
            replies.setItems(replyList);
        }

        // Set image
        if(item.getImage().equals("noImage")){
            System.out.println("here");
            File file = new File("C:/Users/HARSH/IdeaProjects/UniLinkUI/images/No_image_available3.png");
            System.out.println(file);
            Image imagePath = new Image(file.toURI().toString());
            image.setImage(imagePath);
            image.setCache(true);
        }
        else{
            System.out.println("hereeeee");
            File file = new File("C:/Users/HARSH/IdeaProjects/UniLinkUI/images/"+item.getImage());
            System.out.println(file);
            Image imagePath = new Image(file.toURI().toString());
            image.setImage(imagePath);
            image.setCache(true);
        }

        mainTitle.setText(item.getTitle());
        title.setText(item.getTitle());
        description.setText(item.getDescription());
        status.setText(item.getStatus());
        askingPrice.setText( Double.toString(((model.Sale)item).getAskingPrice() ));
        minimumRaise.setText(Double.toString( ((model.Sale)item).getMinimumRaise() ));
        currentUser = item.getCreatorId();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        editPost = new EditPost();
    }



    public void uploadImage(ActionEvent actionEvent) {
    }

    public void mainMenu(ActionEvent event) throws Exception{
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

    public void closeSale(ActionEvent actionEvent) throws Exception{
        post.setStatus("CLOSE");
        status.setText("CLOSE");
        // Close the sale in database.
        editPost.closePost(post);
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

    public void deleteSale(ActionEvent actionEvent) throws Exception {
        post.setStatus("CLOSE");
        editPost.deletePost(post);
    }

    public void save(ActionEvent actionEvent) throws Exception {
        editPost.updateSaleDetails(title.getText(), description.getText(), askingPrice.getText(), minimumRaise.getText(),post.getPostId());
    }
}
