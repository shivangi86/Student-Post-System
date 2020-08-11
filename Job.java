package model;

import controller.ErrorController;
import javafx.animation.PauseTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.database.ConnectDB;

import java.io.Serializable;
import java.sql.Connection;

import java.net.SocketTimeoutException;
import java.sql.ResultSet;
import java.sql.Statement;

public class Job extends Post implements Serializable {

    private double proposedPrice;
    private double lowestOffer;
    Connection con;
    String userId;


    public Job(){}

    public Job(String id,
               String title,
               String description,
               String creatorId,
               String status,
               double proposedPrice,
               double lowestOffer,
               String image){
        super(id, "JOB", title, description, creatorId, status,image);
        this.proposedPrice = proposedPrice;
        this.lowestOffer = lowestOffer;
    }

//    public Job(String postId, String title, String description, String creatorId) {
//        super(postId, "JOB", title, description, creatorId, "OPEN");
//        System.out.println("Enter proposed price : ");
//        proposedPrice = in.nextDouble();
//        lowestOffer = proposedPrice;
//    }

    public void createNewJob(String userId,
                             String title,
                             String description,
                             double proposedPrice,
                             String fileName) throws Exception{
        String postId="";

        con = ConnectDB.connect();
        // Create the sale id
        String getCount = "select total from POST_COUNT where TYPE = 'JOB'";
        Statement stmt = con.createStatement();
        ResultSet resultSet = stmt.executeQuery(getCount);
        while(resultSet.next()) {
            int currentSaleCount = resultSet.getInt("total");
            postId = "JOB"+( String.format("%03d", ++currentSaleCount ));
        }

        // Insert post
        String insertPost = "INSERT INTO POST VALUES (" +
                "'" + postId + "'," +
                "'JOB'," +
                "'" + title + "'," +
                "'" + description + "'," +
                "'" + userId + "'," +
                "'OPEN'," +
                "'" + fileName + "')";
        System.out.println("insert post : " + insertPost);

        int resultPost = stmt.executeUpdate(insertPost);
        if(resultPost == 1){
            System.out.println("Post Inserted");
        }
        con.commit();

        // Insert sale
        String insertJob = "INSERT INTO JOB VALUES (" +
                "'" + postId + "'," +
                proposedPrice + "," +
                proposedPrice + ")";

        int resultSale = stmt.executeUpdate(insertJob);
        if(resultSale == 1){
            System.out.println("Job Inserted");
        }
        con.commit();

        // Update job count
        String updateJobCount = "UPDATE POST_COUNT SET TOTAL = TOTAL + 1 WHERE TYPE = 'JOB';";
        int resultUpdateJobCount = stmt.executeUpdate(updateJobCount);
        if(resultUpdateJobCount == 1){
            System.out.println("Sale count updated");
        }

//        con.close();
        stmt.close();

    }


    public double getProposedPrice() {
        return proposedPrice;
    }

    public void setProposedPrice(int proposedPrice) {
        this.proposedPrice = proposedPrice;
    }

    public double getLowestOffer() {
        return lowestOffer;
    }

    public void setLowestOffer(int lowestOffer) {
        this.lowestOffer = lowestOffer;
    }


    // Get all the details from Post class along with Sale class variables
    public String getPostDetails() {
        // Splitting string from Post class method
        String[] postDetails = super.getPostDetails().split(":");

        // Printing details
        System.out.println("\n\n** MY JOB POSTS **");
        System.out.println("ID             : " + postDetails[0]);
        System.out.println("Title          : " + postDetails[1]);
        System.out.println("Description    : " + postDetails[2]);
        System.out.println("Creator ID     : " + postDetails[3]);
        System.out.println("Status         : " + postDetails[4]);
        System.out.println("Lowest offer   : " + lowestOffer);
        System.out.println("==============================================");
        return null;
    }


    // When the creator wants to see his/her posts
    public String getPostDetailsForCreator(){
        // Splitting string from Post class method
        String[] postDetails = super.getPostDetails().split(":");

        // Printing details
        System.out.println("\n\n** MY JOB POSTS **");
        System.out.println("ID             : " + postDetails[0]);
        System.out.println("Title          : " + postDetails[1]);
        System.out.println("Description    : " + postDetails[2]);
        System.out.println("Creator ID     : " + postDetails[3]);
        System.out.println("Status         : " + postDetails[4]);
        System.out.println("Lowest offer   : " + lowestOffer);
        System.out.println("Proposed price : " + proposedPrice);
        System.out.println("==============================================");
        return null;
    }

    // To be done
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

    public void handleReply(Double jobReply, String currentUser) throws Exception{
        // If the offer price is higher than current offer
        if( jobReply > lowestOffer){
            showMessage("Invalid Offer","Offer must be lower than the current lowest offer");
        }
        // If offer price < 0
        else if(jobReply < 0){
            showMessage("Invalid Offer","Offer cannot be less than 0");
        }

        else{
//            reply.setValue(Integer.parseInt(value));
//            super.addToReplyArray(reply);

            con = ConnectDB.connect();
            Statement stmt = con.createStatement();

            // Check if the user has already registered for this event
            String checkAlreadyRegisteredQuery = "SELECT * FROM REPLY WHERE CREATORID = '" + currentUser + "' AND POSTID = '"+ getPostId() +"';";
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
                lowestOffer = jobReply;
                // Update the value in the database
                String updateSaleHighestOfferQuery = "UPDATE JOB SET LOWEST_PRICE = " + jobReply + " WHERE ID = '"+getPostId()+"';";
                System.out.println("updateSaleHighestOfferQuery : " + updateSaleHighestOfferQuery);
                int updateSaleHighestOfferResult = stmt.executeUpdate(updateSaleHighestOfferQuery);

                // Insert the reply in the Reply table
                String insertReplyQuery = "INSERT INTO REPLY (REPLY, POSTID, CREATORID) VALUES" +
                        "(" + jobReply + ", " +
                        "'" + getPostId() + "', " +
                        "'" + currentUser + "');";
                int insertReplyResult = stmt.executeUpdate(insertReplyQuery);
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
