package controller;

import javafx.animation.PauseTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import javafx.util.Duration;
import model.*;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class UniLinkGUIController implements Initializable {

    Stage window;
    UniLinkGUI unilink;
    String currentUser = null;


    public MenuBar menu_bar;
    public Text user_name;
    public ListView<Post> posts;
    public ScrollPane scrollArea;
    public StackPane stackPane;
    public ComboBox type;
    public ComboBox status;
    public ComboBox creator;


    // Load the current user posts
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        type.getItems().setAll("ALL","SALE", "EVENT", "JOB");
        status.getItems().setAll("ALL","OPEN", "CLOSED");
        creator.getItems().setAll("MY POST", "ALL");
    }

    ObservableList<String> list = FXCollections.observableArrayList();


    // Probably delete
    public void initData(String userId, ActionEvent event, UniLinkGUI unilink){
        currentUser = userId;
        // Set username to the userId
        user_name.setText("Welcome " + userId);
         
        this.unilink = unilink;
        // Print data on the main window
        printPosts(event);
    }

    public void initData(String userId, ActionEvent event) throws Exception{
        currentUser = userId;
        // Set username to the userId
        user_name.setText("Welcome " + userId);

        // Setting the current logged in user in the model
        unilink = new UniLinkGUI(currentUser);

        // Load the data of the user from db in model
        unilink.loadData();

        // Print data on the main window
        printPosts(event);

        status.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue<? extends String> selected, String oldValue, String newValue) {
                if (oldValue != null) {
                    switch(oldValue) {
                        case "ALL":  printPosts(event); break;
                        case "OPEN": printOpenPosts(event); break;
                        case "CLOSED": printClosedPosts(event); break;
                    }
                }
                if (newValue != null) {
                    switch(newValue) {
                        case "ALL":  printPosts(event); break;
                        case "OPEN": printOpenPosts(event); break;
                        case "CLOSED": printClosedPosts(event); break;
                    }
                }
            }
        });

        creator.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue<? extends String> selected, String oldValue, String newValue) {
                if (oldValue != null) {
                    switch(oldValue) {
                        case "ALL":  printPosts(event); break;
                        case "MY POSTS": printMyPosts(event); break;
                    }
                }
                if (newValue != null) {
                    switch(newValue) {
                        case "ALL":  printPosts(event); break;
                        case "MY POSTS": printMyPosts(event); break;
                    }
                }
            }
        });

        type.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue<? extends String> selected, String oldValue, String newValue) {
                if (oldValue != null) {
                    switch(oldValue) {
                        case "ALL":  printPosts(event); break;
                        case "SALE": printSales(event); break;
                        case "EVENT": printEvents(event); break;
                        case "JOB": printJobs(event); break;
                    }
                }
                if (newValue != null) {
                    switch(newValue) {
                        case "ALL":  printPosts(event); break;
                        case "SALE": printSales(event); break;
                        case "EVENT": printEvents(event); break;
                        case "JOB": printJobs(event); break;
                    }
                }
            }
        });

    }

    private void printMyPosts(ActionEvent event){
        ObservableList<Post> openData = FXCollections.observableArrayList();
        ArrayList<Post> openPosts = new ArrayList<Post>();
        for (Post post : unilink.posts) {
            if(post.getCreatorId().equals(currentUser)){
                openPosts.add(post);
            }
        }
        openData.addAll(openPosts);
        final ListView<Post> posts = new ListView<Post>(openData);
        posts.setCellFactory(new Callback<ListView<Post>, ListCell<Post>>() {
            public CustomListCell call(ListView<Post> posts) {
                return new CustomListCell();
            }
        });
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        StackPane root = new StackPane();
        stackPane.getChildren().add(posts);
    }

    private void printOpenPosts(ActionEvent event){
        ObservableList<Post> openData = FXCollections.observableArrayList();
        ArrayList<Post> openPosts = new ArrayList<Post>();
        for (Post post : unilink.posts) {
            if(post.getStatus().equals("OPEN")){
                openPosts.add(post);
            }
        }
        openData.addAll(openPosts);
        final ListView<Post> posts = new ListView<Post>(openData);
        posts.setCellFactory(new Callback<ListView<Post>, ListCell<Post>>() {
            public CustomListCell call(ListView<Post> posts) {
                return new CustomListCell();
            }
        });
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        StackPane root = new StackPane();
        stackPane.getChildren().add(posts);
    }

    private void printClosedPosts(ActionEvent event){
        ObservableList<Post> closeData = FXCollections.observableArrayList();
        ArrayList<Post> closePosts = new ArrayList<Post>();
        for (Post post : unilink.posts) {
            if(post.getStatus().equals("CLOSE")){
                closePosts.add(post);
            }
        }
        closeData.addAll(closePosts);
        final ListView<Post> posts = new ListView<Post>(closeData);
        posts.setCellFactory(new Callback<ListView<Post>, ListCell<Post>>() {
            public CustomListCell call(ListView<Post> posts) {
                return new CustomListCell();
            }
        });
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        StackPane root = new StackPane();
        stackPane.getChildren().add(posts);
    }


    private void printSales(ActionEvent event){
        ObservableList<Post> saleData = FXCollections.observableArrayList();
        ArrayList<Post> salePosts = new ArrayList<Post>();
        for (Post post : unilink.posts) {
            if(post.getType().equals("SALE")){
                salePosts.add(post);
            }
        }
        saleData.addAll(salePosts);
        final ListView<Post> posts = new ListView<Post>(saleData);
        posts.setCellFactory(new Callback<ListView<Post>, ListCell<Post>>() {
            public CustomListCell call(ListView<Post> posts) {
                return new CustomListCell();
            }
        });
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        StackPane root = new StackPane();
        stackPane.getChildren().add(posts);
    }


    private void printJobs(ActionEvent event){
        ObservableList<Post> jobData = FXCollections.observableArrayList();
        ArrayList<Post> jobPosts = new ArrayList<Post>();
        for (Post post : unilink.posts) {
            if(post.getType().equals("JOB")){
                jobPosts.add(post);
            }
        }
        jobData.addAll(jobPosts);
        final ListView<Post> posts = new ListView<Post>(jobData);
        posts.setCellFactory(new Callback<ListView<Post>, ListCell<Post>>() {
            public CustomListCell call(ListView<Post> posts) {
                return new CustomListCell();
            }
        });
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        StackPane root = new StackPane();
        stackPane.getChildren().add(posts);
    }



    private void printEvents(ActionEvent event){
        ObservableList<Post> eventData = FXCollections.observableArrayList();
        ArrayList<Post> eventPosts = new ArrayList<Post>();
        for (Post post : unilink.posts) {
            if(post.getType().equals("EVENT")){
                eventPosts.add(post);
            }
        }
        eventData.addAll(eventPosts);
        final ListView<Post> posts = new ListView<Post>(eventData);
        posts.setCellFactory(new Callback<ListView<Post>, ListCell<Post>>() {
            public CustomListCell call(ListView<Post> posts) {
                return new CustomListCell();
            }
        });
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        StackPane root = new StackPane();
        stackPane.getChildren().add(posts);
    }


    private void printPosts(ActionEvent event) {
        ObservableList<Post> data = FXCollections.observableArrayList();
        data.addAll(unilink.posts);

        final ListView<Post> posts = new ListView<Post>(data);

        posts.setCellFactory(new Callback<ListView<Post>, ListCell<Post>>() {
            public CustomListCell call(ListView<Post> posts) {
                return new CustomListCell();
            }
        });

        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        StackPane root = new StackPane();
        stackPane.getChildren().add(posts);
    }

    public void exportDataToFile(ActionEvent actionEvent) throws Exception {
        Stage stage =  (Stage) menu_bar.getScene().getWindow();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(stage);
        String FolderLocation = selectedDirectory.toURI().toString();
        File exportFile = new File(selectedDirectory.getAbsolutePath()+"/export_data.txt");
//        FileLocation  = selectedDirectory+exportFile.toURI().toString();
        System.out.println(exportFile.toURI().toString());
        FileOutputStream writeData = new FileOutputStream(exportFile);
        ObjectOutputStream writeStream = new ObjectOutputStream(writeData);

        writeStream.writeObject(unilink.posts);
        writeStream.flush();
        writeStream.close();
        showMessage("SUCCESSFUL","All the posts have been written to a file");
    }

    public void importDataFromFile(ActionEvent actionEvent) {
    }

    private class CustomListCell extends ListCell<Post>{
        private HBox content;
        private GridPane listGrid;
        private Text title;
        private Text description;
        private Text creatorId;
        private Text status;
        private Text venue;
        private Text date;
        private Text capacity;
        private Text attendeeCount;
        private Button reply;
        private Button moreDetails;
        private Text highestOffer;
        private Text minimumRaise;
        private Text proposedPrice;
        private Text lowestOffer;
        private ImageView imageView;


        public CustomListCell() {
            super();
            title = new Text();
            description = new Text();
            creatorId = new Text();
            status = new Text();
            reply = new Button();
            moreDetails = new Button();
            imageView = new ImageView();
            venue = new Text();
            date = new Text();
            capacity = new Text();
            attendeeCount = new Text();
            highestOffer = new Text() ;
            minimumRaise = new Text();
            proposedPrice = new Text();
            lowestOffer = new Text();
//            listGrid = new GridPane();


//            listGrid.addColumn(0,imageView);

            VBox vBoxTitleDesc = new VBox(title, description, highestOffer, venue);
            vBoxTitleDesc.setSpacing(10);
            vBoxTitleDesc.setAlignment(Pos.TOP_LEFT);
//            listGrid.addColumn(1,vBoxTitleDesc);

            VBox vBoxCreatorStatus = new VBox(creatorId, status,minimumRaise,date);
            vBoxCreatorStatus.setSpacing(10);
            vBoxCreatorStatus.setAlignment(Pos.TOP_LEFT);
//            listGrid.addColumn(2,vBoxCreatorStatus);

            content = new HBox(new Label("[Graphic]"), vBoxTitleDesc,vBoxCreatorStatus, reply, moreDetails);
            content.setAlignment(Pos.TOP_LEFT);
            content.setSpacing(20);
//            content = new HBox(reply, moreDetails);
//            listGrid.addColumn(3,content);
//            listGrid.getColumnConstraints().add(new ColumnConstraints(70));
//            listGrid.getColumnConstraints().add(new ColumnConstraints(190));
//            listGrid.getColumnConstraints().add(new ColumnConstraints(200));
        }

        @Override
        protected void updateItem(Post item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null && !empty) { // <== test for null item and empty parameter
                    System.out.println(item.getPostId());
                    title.setText("Title : " + item.getTitle());
                    description.setText("Description : " + item.getDescription());
                    creatorId.setText("Creator ID : " + item.getCreatorId());
                    status.setText("Status : " + item.getStatus());
                    reply.setText("Reply");


                    getStylesheets().add("view/list-style.css");

                    // If Event is found, set the event details to values from the Posts arraylist
                    if(item.getType().equals("EVENT")){
                        setStyle("-fx-background-color: #E0FFFF;");
                        venue.setText("Venue : " + ((Event)item).getVenue());
                        capacity.setText("Capacity : " + Integer.toString(((Event)item).getCapacity()));
                        highestOffer.setText("Attendee Count : " + Integer.toString(((Event)item).getAttendeeCount()));
                        minimumRaise.setText("Event Date : " +((Event)item).getDate());
                        proposedPrice.setText("");
                        lowestOffer.setText("");
                        if(item.getStatus().equals("OPEN")){
                            reply.setText("Join");
                        }
                        else{
                            reply.setVisible(false);
                        }
                        moreDetailsAndReplyFunctionality(moreDetails,reply,item,currentUser);
                    }
                    // If Sale is found, set the sale details to values from the Posts arraylist
                    if(item.getType().equals("SALE")){
                        setStyle("-fx-background-color: #FFB6C1;");
                        highestOffer.setText("Highest Offer : " + Double.toString(((Sale)item ).getHighestOffer()));
                        minimumRaise.setText("Minimum Raise : " + Double.toString(((Sale)item ).getMinimumRaise()));
                        venue.setText("");
                        date.setText("");
                        capacity.setText("");
                        attendeeCount.setText("");
                        proposedPrice.setText("");
                        lowestOffer.setText("");

                        if(item.getCreatorId().equals(currentUser)){
                            moreDetailsAndReplyFunctionality(moreDetails,reply,item,currentUser);
                        }

                    }
                    // If Job is found, set the job details to values from the Posts arraylist
                    if(item.getType().equals("JOB")){
                        setStyle("-fx-background-color: #FFFFE0;");

                        // FIGURE THIS OUT
//                        highestOffer.setText("Proposed Price : " + Double.toString(((Job)item ).getProposedPrice()));
                        minimumRaise.setText("Lowest Offer : " + Double.toString(((Job)item ).getLowestOffer()));
                        // FIGURE THIS OUT

                        venue.setText("");
                        date.setText("");
                        capacity.setText("");
                        attendeeCount.setText("");
                        if(item.getCreatorId().equals(currentUser)){
                            moreDetailsAndReplyFunctionality(moreDetails,reply,item,currentUser);
                        }
                    }

                    setGraphic(content);

                } else {
                    setGraphic(null);
                }
        }

        private void moreDetailsAndReplyFunctionality(Button moreDetails,Button reply, Post item,String currentUser){
            // Check if the post belongs to the currently logged in user
            if(item.getCreatorId().equals(currentUser)){
                reply.setVisible(false);
                // Set the button details and action listener
                moreDetails.setText("More Details");
                moreDetails.setOnAction(new EventHandler() {
                    @Override
                    public void handle(javafx.event.Event event) {
                        System.out.println("title : " + item.getTitle());
                        FXMLLoader loader = new FXMLLoader();
                        if(item.getType().equals("EVENT")){
                            loader.setLocation(getClass().getClassLoader().getResource("view/edit_event.fxml"));
                        }
                        else if(item.getType().equals("SALE")){
                            loader.setLocation(getClass().getClassLoader().getResource("view/edit_sale.fxml"));
                        }
                        else if(item.getType().equals("JOB")){
                            loader.setLocation(getClass().getClassLoader().getResource("view/edit_job.fxml"));
                        }
                        Parent mainViewParent = null;
                        try {
                            mainViewParent = loader.load();
                            System.out.println("mainViewParent : " + mainViewParent);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Scene mainViewScene = new Scene(mainViewParent);

                        // Initialize the controller
                        if(item.getType().equals("EVENT")){
                            EditEventController controller = loader.getController();
                            try {
                                controller.initData(item,event,unilink);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if(item.getType().equals("SALE")){
                            EditSaleController controller = loader.getController();
                            try {
                                controller.initData(item,event,unilink);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if(item.getType().equals("JOB")){
                            EditJobController controller = loader.getController();
                            try {
                                controller.initData(item,event,unilink);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        // Get current stage information
                        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

                        // Set the main scene to the stage
                        window.setScene(mainViewScene);
                    }
                });
            }
            else
            {
                moreDetails.setVisible(false);
                reply.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        // Check if post is of type Event
                        if(item.getType().equals("EVENT")){
                            // Check if there is place to register i.e if event is open
                            if(item.getStatus().equals("OPEN")){

                                // Add the entry to database
                                Event eventObj = new Event();
                                try {
                                    // If entry successful then display the message to user
                                    if(eventObj.updateAttendeCount(currentUser,item.getPostId()).equals("SUCCESSFUL")){
                                        showMessage("Joined Event", "You have successfully registered for " + item.getTitle());
                                        // Increment the attendee count
                                        ((Event)item).setAttendeeCount(
                                                ((Event)item).getAttendeeCount() + 1
                                        );
                                        // If attendee count is full then close the event
                                        if(((Event) item).getAttendeeCount()==((Event) item).getCapacity()){
                                            item.setStatus("CLOSE");
                                            eventObj.closePost(item.getPostId());
                                            status.setText("CLOSE");
                                        }
                                    }
                                    else if(eventObj.updateAttendeCount(currentUser,item.getPostId()).equals("ALREADY_REGISTERED")){
                                        showMessage("FAILED", "You have already registered for " + item.getTitle() + " and cannot register again.");
                                    }
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }
                        if(item.getType().equals("SALE")){
                            // Check if sale is open
                            if(item.getStatus().equals("OPEN")){
                                try {
                                    openSaleReplyWindow((Sale)item);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        if(item.getType().equals("JOB")){
                            // Check if sale is open
                            if(item.getStatus().equals("OPEN")){
                                try {
                                    openJobReplyWindow((Job)item);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
                moreDetails.setVisible(false);
            }
        }

    }

    private void setEventDetails(Post item) {
    }

    private void openJobReplyWindow(Job item) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("view/job_reply.fxml"));
        // Get the fxml info
        Parent newSaleScreen = loader.load();
        // Create new stage
        Stage newSaleWindow = new Stage();
        // Create new scene
        Scene scene = new Scene(newSaleScreen);
        // Access Main Controller and store login information
        JobReplyController controller = loader.getController();
        controller.initData(currentUser,item);
        // Set scene to the stage
        newSaleWindow.setScene(scene);
        // Wait until the window is closed by the user
        newSaleWindow.showAndWait();
    }

    private void openSaleReplyWindow(Sale item) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("view/sale_reply.fxml"));
        // Get the fxml info
        Parent newSaleScreen = loader.load();
        // Create new stage
        Stage newSaleWindow = new Stage();
        // Create new scene
        Scene scene = new Scene(newSaleScreen);
        // Access Main Controller and store login information
        SaleReplyController controller = loader.getController();
        controller.initData(currentUser,item);
        // Set scene to the stage
        newSaleWindow.setScene(scene);
        // Wait until the window is closed by the user
        newSaleWindow.showAndWait();
    }


    public void logoutClicked(ActionEvent event) throws Exception{
        if(currentUser!=null){
            currentUser=null;
            // Navigate to mainController window
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/login.fxml"));
            Parent loginViewParent = loader.load();
            Scene loginViewScene = new Scene(loginViewParent);
            // Get current stage information
            window = (Stage)((Node)event.getSource()).getScene().getWindow();
            // Set the main scene to the stage
            window.setScene(loginViewScene);
        }
    }


    public void createNewSale(ActionEvent event) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("view/create_new_sale.fxml"));
        // Get the fxml info
        Parent newSaleScreen = loader.load();
        // Create new stage
        Stage newSaleWindow = new Stage();
        // Create new scene
        Scene scene = new Scene(newSaleScreen);
        // Access Main Controller and store login information
        CreateSaleController controller = loader.getController();
        controller.initData(currentUser);
        // Set scene to the stage
        newSaleWindow.setScene(scene);
        // Wait until the window is closed by the user
        newSaleWindow.showAndWait();
    }

    public void createNewEvent(ActionEvent event) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("view/create_new_event.fxml"));
        // Get the fxml info
        Parent newSaleScreen = loader.load();
        // Create new stage
        Stage newSaleWindow = new Stage();
        // Create new scene
        Scene scene = new Scene(newSaleScreen);
        // Access Main Controller and store login information
        CreateEventController controller = loader.getController();
        controller.initData(currentUser);
        // Set scene to the stage
        newSaleWindow.setScene(scene);
        // Wait until the window is closed by the user
        newSaleWindow.showAndWait();
    }

    public void createNewJob(ActionEvent actionEvent) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("view/create_new_job.fxml"));
        // Get the fxml info
        Parent newJobScreen = loader.load();
        // Create new stage
        Stage newJobWindow = new Stage();
        // Create new scene
        Scene scene = new Scene(newJobScreen);
        // Access Main Controller and store login information
        CreateJobController controller = loader.getController();
        controller.initData(currentUser);
        // Set scene to the stage
        newJobWindow.setScene(scene);
        // Wait until the window is closed by the user
        newJobWindow.showAndWait();
    }


    // Show developer information in a separate window
    public void developerInformation(ActionEvent event) throws Exception{
        // Get the fxml info
        Parent developerInfoScreen = FXMLLoader.load(getClass().getClassLoader().getResource("view/developer_info.fxml"));
        // Create new stage
        Stage developerInfoWindow = new Stage();
        // Create new scene
        Scene scene = new Scene(developerInfoScreen);
        // Set scene to the stage
        developerInfoWindow.setScene(scene);
        // Wait until the window is closed by the user
        developerInfoWindow.showAndWait();
    }

    private void showMessage(String title, String message) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/message.fxml"));
        Parent root = (Parent) loader.load();
        ErrorController controller = loader.getController();
        controller.initData(title,message);
        Scene newScene = new Scene(root);
        Stage newStage = new Stage();
        newStage.setScene(newScene);
        newStage.show();
        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished( event -> newStage.close() );
        delay.play();
    }

    public void quitApplication(ActionEvent event){
        // Exit application
        window = (Stage)menu_bar.getScene().getWindow();
        window.close();
    }


}
