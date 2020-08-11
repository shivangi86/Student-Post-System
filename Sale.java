package model;

import controller.ErrorController;
import javafx.animation.PauseTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.database.ConnectDB;
import model.database.ConnectionTest;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Sale extends Post implements Serializable {

    private double askingPrice;
    private double highestOffer;
    private double minimumRaise;
    Connection con;


    public Sale(){}

    public Sale(String postId,
                String title,
                String description,
                String creatorId,
                String status,
                double askingPrice,
                double highestOffer,
                double minimumRaise,
                String image) {
        super(postId, "SALE", title, description, creatorId, status,image);
        this.askingPrice = askingPrice;
        this.highestOffer = highestOffer;
        this.minimumRaise = minimumRaise;
        System.out.println("Highesh Offer : " + highestOffer);
        System.out.println("Minimum Raise : " + minimumRaise);
    }


    public void createNewSale(String userId,
                              String title,
                              String description,
                              double askingPrice,
                              double minimumRaise,
                              String image) throws Exception
    {
        String postId="";

        con = ConnectDB.connect();
        // Create the sale id
        String getCount = "select total from POST_COUNT where TYPE = 'SALE'";
        Statement stmt = con.createStatement();
        ResultSet resultSet = stmt.executeQuery(getCount);
        while(resultSet.next()) {
            int currentSaleCount = resultSet.getInt("total");
            postId = "SAL"+( String.format("%03d", ++currentSaleCount ));
        }

        // Insert post
        String insertPost = "INSERT INTO POST VALUES (" +
                "'" + postId + "'," +
                "'SALE'," +
                "'" + title + "'," +
                "'" + description + "'," +
                "'" + userId + "'," +
                "'OPEN'," +
                "'" + image + "')";
        int resultPost = stmt.executeUpdate(insertPost);
        if(resultPost == 1){
            System.out.println("Post Inserted");
        }
        con.commit();

        // Insert sale
        String insertSale = "INSERT INTO SALE VALUES (" +
                    "'" + postId + "'," +
                    askingPrice + "," +
                    highestOffer + "," +
                    minimumRaise + ")";

        int resultSale = stmt.executeUpdate(insertSale);
        if(resultSale == 1){
            System.out.println("Sale Inserted");
        }
        con.commit();

        // Update sale count
        String updateSaleCount = "UPDATE POST_COUNT SET TOTAL = TOTAL + 1 WHERE TYPE = 'SALE';";
        int resultUpdateSaleCount = stmt.executeUpdate(updateSaleCount);
        if(resultUpdateSaleCount == 1){
            System.out.println("Sale count updated");
        }

        con.close();
        stmt.close();
    }

    public double getHighestOffer() {
        return highestOffer;
    }


    public void setHighestOffer(int highestOffer) {
        this.highestOffer = highestOffer;
    }

    public double getAskingPrice() {
        return askingPrice;
    }

    public void setAskingPrice(double askingPrice) {
        this.askingPrice = askingPrice;
    }

    public double getMinimumRaise() {
        return minimumRaise;
    }


    public void setMinimumRaise(int minimumRaise) {
        this.minimumRaise = minimumRaise;
    }

    // Get all the details from Post class along with Sale class variables
    public String getPostDetails() {
        // Splitting string from Post class method
        String[] postDetails = super.getPostDetails().split(":");
        System.out.println("\n** MY SALE POSTS **");
        System.out.println("ID            : " + postDetails[0]);
        System.out.println("Title         : " + postDetails[1]);
        System.out.println("Description   : " + postDetails[2]);
        System.out.println("Creator ID    : " + postDetails[3]);
        System.out.println("Status        : " + postDetails[4]);
        System.out.println("Minimum Raise : " + "$"+this.minimumRaise);
        System.out.println("Highest Offer : " + "$"+this.highestOffer);
        System.out.println("==============================================");
        return null;
    }


    // When the creator wants to see his/her posts
    public String getPostDetailsForCreator(){
        // Splitting string from Post class method
        String[] postDetails = super.getPostDetails().split(":");
        System.out.println("\n** MY SALE POSTS **");
        System.out.println("ID            : " + postDetails[0]);
        System.out.println("Title         : " + postDetails[1]);
        System.out.println("Description   : " + postDetails[2]);
        System.out.println("Creator ID    : " + postDetails[3]);
        System.out.println("Status        : " + postDetails[4]);
        System.out.println("Minimum Raise : " + "$"+this.minimumRaise);
        System.out.println("Highest Offer : " + "$"+this.highestOffer);
        System.out.println("Asking Price  : " + "$"+askingPrice);
        System.out.println("=================================================");
        return null;
    }


    @Override
    public String getReplyDetails() {
        String replies = "";
        if(super.getReplies().size() == 0){
            System.out.println("No replies on this post yet");
            return null;
        }

        for(int i = 0; i < super.getReplies().size(); i++){
            replies = replies + super.getReplyValueAndResponderId(i);
        }
        return replies;
    }

    @Override
    public boolean handleReply(Reply reply) {
        return false;
    }

    public void handleReply(Double replyValue, String currentUser) throws Exception{
        con = ConnectDB.connect();
        Statement stmt = con.createStatement();
        System.out.println("highestOffer : " + highestOffer);
        System.out.println("minimumRaise : " + minimumRaise);
        if(replyValue <= (highestOffer + minimumRaise)){
            showMessage("Invalid bid","Bid must be higher than the current highest bid");
        }

        else if(replyValue < 0){
            showMessage("Invalid bid","Bid cannot be < 0");
        }

        else{

            // Check if the user has already registered for this event
            String checkAlreadyRegisteredQuery = "SELECT * FROM REPLY WHERE CREATORID = '" + currentUser + "' AND POSTID = '"+ getPostId() +"';";
            System.out.println("checkAlreadyRegisteredQuery : " + checkAlreadyRegisteredQuery);
            ResultSet alreadyRegisteredSet = stmt.executeQuery(checkAlreadyRegisteredQuery);

            // User has already registered for the event
            if(alreadyRegisteredSet.next()){
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/message.fxml"));
                Parent root = (Parent) loader.load();
                ErrorController controller = loader.getController();
                controller.initData("FAILED","You cannot reply to the same job again.");
                Scene newScene = new Scene(root);
                Stage newStage = new Stage();
                newStage.setScene(newScene);
                newStage.show();
                PauseTransition delay = new PauseTransition(Duration.seconds(2));
                delay.setOnFinished( event -> newStage.close() );
                delay.play();
            }
            else{
                // Set to highest offer
                highestOffer = replyValue;

                // Update the value in the database
                String updateSaleHighestOfferQuery = "UPDATE SALE SET HIGHEST_OFFER = " + replyValue + " WHERE ID = '"+getPostId()+"';";
                System.out.println("updateSaleHighestOfferQuery : " + updateSaleHighestOfferQuery);
                stmt = con.createStatement();
                int updateSaleHighestOfferResult = stmt.executeUpdate(updateSaleHighestOfferQuery);

                // Insert the reply in the Reply table
                String insertReplyQuery = "INSERT INTO REPLY (REPLY, POSTID, CREATORID) VALUES" +
                        "(" + replyValue + ", " +
                        "'" + getPostId() + "', " +
                        "'" + currentUser + "');";
                int insertReplyResult = stmt.executeUpdate(insertReplyQuery);

                if(replyValue >= askingPrice){
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/message.fxml"));
                    Parent root = (Parent) loader.load();
                    ErrorController controller = loader.getController();
                    controller.initData("Congratulations!!","The item has been sold to you.");
                    Scene newScene = new Scene(root);
                    Stage newStage = new Stage();
                    newStage.setScene(newScene);
                    newStage.show();
                    PauseTransition delay = new PauseTransition(Duration.seconds(2));
                    delay.setOnFinished( event -> newStage.close() );
                    delay.play();
                    // Update the status
                    this.setStatus("CLOSE");
                    // Update the status in the database
                    String closeSaleQuery = "UPDATE POST SET STATUS = 'CLOSE' WHERE ID = " + getPostId() + ";";
                    int closeSaleResult = stmt.executeUpdate(closeSaleQuery);
                }
                else{
                    showMessage("SUCCESSFUL", "Reply successfully added for the sale");
                }
            }
        }

    }


    public void showMessage(String title, String message) throws Exception{
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

}























