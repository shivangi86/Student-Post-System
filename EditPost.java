package model;

import controller.ErrorController;
import javafx.animation.PauseTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.database.ConnectDB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class EditPost {
    // Db connection object
    Connection con;

    public EditPost(){
        con = ConnectDB.connect();
    }

    public void updatePostDetails(String description, String postId, String postType) throws SQLException {
        String query = "UPDATE POST SET DESCRIPTION = '" + description + "' WHERE ID = " +"'" + postId + "';";
        System.out.println("Query : " + query);
        Statement stmt = con.createStatement();
        int result = stmt.executeUpdate(query);
        System.out.println("result  : " + result);
        stmt.close();
    }

    public void updateSaleDetails(String title, String description, String askingPrice, String minimumRaise,String postId) throws Exception{
        Statement stmt = con.createStatement();
        String saveSalePost = "UPDATE POST SET TITLE = '" + title + "', " +
                "DESCRIPTION = '"+description+"' " +
                "WHERE ID =  '" + postId + "';";
        int result = stmt.executeUpdate(saveSalePost);
        String saveSale = "UPDATE SALE SET " +
                "ASKING_PRICE = "+askingPrice+ "," +
                "MINIMUM_RAISE = "+minimumRaise+" WHERE ID = '" + postId + "';";
        System.out.println("saveSale : " +saveSale);
        int result2 = stmt.executeUpdate(saveSalePost);
        System.out.println("result  : " + result);
        System.out.println("result2  : " + result2);
        stmt.close();
        con.commit();
    }

    public void updateEventDetails(String title, String description, String venue, String date, String capacity, String postId) throws Exception{
        String saveEventPost = "UPDATE POST SET TITLE = '" + title + "', " +
                "DESCRIPTION = '"+description+"'" +
                "WHERE ID + '" + postId +  "';";
        String saveEvent = "UPDATE EVENT SET " +
                "VENUE = "+venue+ "," +
                "DATE = "+date+ "," +
                "CAPACITY = "+capacity+"," +
                " WHERE ID = '" + postId + "';";
        Statement stmt = con.createStatement();
        int result = stmt.executeUpdate(saveEventPost);
        int result2 = stmt.executeUpdate(saveEvent);
        System.out.println("result  : " + result);
        stmt.close();
    }

    public void updateJobDetails(String title, String description, String proposedPrice, String postId) throws Exception{
        String saveEventPost = "UPDATE POST SET TITLE = '" + title + "', " +
                "DESCRIPTION = '"+description+"'" +
                "WHERE ID + '" + postId +  "';";
        String saveEvent = "UPDAE EVENT SET " +
                "PROPOSED_PRICE = "+proposedPrice+ " " +
                "WHERE ID = '" + postId + "';";
        Statement stmt = con.createStatement();
        int result = stmt.executeUpdate(saveEventPost);
        int result2 = stmt.executeUpdate(saveEvent);
        System.out.println("result  : " + result);
        stmt.close();
    }


    public void closePost(Post post)  throws SQLException{
        String closePost = "UPDATE POST SET STATUS = 'CLOSE' WHERE ID = '" + post.getPostId() + "';";
        System.out.println("closePost : " + closePost);
        Statement stmt = con.createStatement();
        int result = stmt.executeUpdate(closePost);
        System.out.println("result  : " + result);
        stmt.close();
    }

    public static ArrayList loadEventReplies(Post item) throws Exception{
        ArrayList<String> attendees = new ArrayList<String>();
        Connection con = ConnectDB.connect();
        Statement stmt = con.createStatement();
        String getReplies = "SELECT CREATORID FROM REPLY WHERE POSTID = '"+item.getPostId()+"';";
        ResultSet rs = stmt.executeQuery(getReplies);
        if(rs.next()){
            while(rs.next()){
                attendees.add(rs.getString("creatorid"));
            }
            con.close();
            stmt.close();
            return attendees;
        }
        else {
            con.close();
            stmt.close();
            return null;
        }

    }

    public static Hashtable loadSaleJobReplies(Post item) throws Exception{
        Hashtable<String, String> saleReplies = new Hashtable<String, String>();
        Connection con = ConnectDB.connect();
        Statement stmt = con.createStatement();
        String getReplies = "SELECT * FROM REPLY WHERE POSTID = '"+item.getPostId()+"';";
        ResultSet rs = stmt.executeQuery(getReplies);
        if(rs.next()){
            while(rs.next()){
                saleReplies.put(rs.getString("creatorid"),rs.getString("reply"));
            }
            return saleReplies;
        }
        else{
            return null;
        }
    }

    public void deletePost(Post post) throws Exception {
        System.out.println("Here");
        Statement stmt = con.createStatement();
        if(post.getType().equals("SALE")){
            String deleteSale = "DELETE FROM SALE WHERE ID = '" + post.getPostId() + "';";
            stmt.executeUpdate(deleteSale);
            System.out.println("deleteSale : " + deleteSale);
        }
        if(post.getType().equals("EVENT")){
            String deleteEvent = "DELETE FROM EVENT WHERE ID = '" + post.getPostId() + "';";
            stmt.executeUpdate(deleteEvent);
            System.out.println("deleteEvent : " + deleteEvent);
        }
        if(post.getType().equals("JOB")){
            String deleteJob = "DELETE FROM JOB WHERE ID = '" + post.getPostId() + "';";
            stmt.executeUpdate(deleteJob);
            System.out.println("deleteJob : " + deleteJob);
        }

        String deletePost = "DELETE FROM POST WHERE ID = '" + post.getPostId() + "';";
        int result = stmt.executeUpdate(deletePost);
        System.out.println("result  : " + result);
        stmt.close();
        showMessage("DELETED","Post deleted successfully");

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
}
