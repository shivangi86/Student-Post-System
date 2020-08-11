package model;

import model.database.ConnectDB;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class Event extends Post implements Serializable {

    private String venue;
    private String date;
    private int capacity;
    private int attendeeCount;
    private String status;

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getAttendeeCount() {
        return attendeeCount;
    }

    public void setAttendeeCount(int attendeeCount) {
        this.attendeeCount = attendeeCount;
    }


    Connection con;


    // Constructor invoked when creating new event
    public Event (){}

    // Constructor invoked when getting the current event information from db
    public Event(String postId,
                 String type,
                 String title,
                 String description,
                 String username,
                 String status,
                 String venue,
                 String date,
                 int capacity,
                 int attendeeCount,
                 String image){
        super(postId, type, title, description, username, "OPEN", image);
        this.venue = venue;
        this.date = date;
        this.capacity = capacity;
        this.attendeeCount = attendeeCount;
        this.status = status;
    }

    public void createNewEvent(String userId,
                               String title,
                               String description,
                               String venue,
                               String capacity,
                               LocalDate date,
                               String image) throws Exception{
        String postId="";

        con = ConnectDB.connect();
        // Create the sale id
        String getCount = "select total from POST_COUNT where TYPE = 'EVENT'";
        Statement stmt = con.createStatement();
        ResultSet resultSet = stmt.executeQuery(getCount);
        while(resultSet.next()) {
            int currentEventCount = resultSet.getInt("total");
            postId = "EVE"+( String.format("%03d", ++currentEventCount ));
        }
        System.out.println("post id : " + postId);

        // Insert post
        String insertPost = "INSERT INTO POST VALUES (" +
                "'" + postId + "'," +
                "'EVENT'," +
                "'" + title + "'," +
                "'" + description + "'," +
                "'" + userId + "'," +
                "'OPEN'," +
                "'" + image + "')";
        System.out.println("Post query : " + insertPost);
        int resultPost = stmt.executeUpdate(insertPost);
        if(resultPost == 1){
            System.out.println("Post Inserted");
        }
        con.commit();



        // Insert sale
        String insertEvent = "INSERT INTO EVENT VALUES (" +
                "'" + postId + "'," +
                "'" + venue + "'," +
                "'" +date + "'," +
                capacity + "," +
                attendeeCount + ");";

        System.out.println("Event query : " + insertEvent);
        int resultSale = stmt.executeUpdate(insertEvent);
        if(resultSale == 1){
            System.out.println("Sale Inserted");
        }
        con.commit();

        // Update event count
        String updateSaleCount = "UPDATE POST_COUNT SET TOTAL = TOTAL + 1 WHERE TYPE = 'EVENT';";
        int resultUpdateSaleCount = stmt.executeUpdate(updateSaleCount);
        if(resultUpdateSaleCount == 1){
            System.out.println("Event count updated");
        }

        con.close();
        stmt.close();

    }


    // Get all the details from Post class along with Event class variables
    public String getPostDetails() {
        // Splitting string from Post class method
        String[] postDetails = super.getPostDetails().split(":");

        // Printing details
        System.out.println("\n** MY EVENT POSTS **");
        System.out.println("ID          : " + postDetails[0]);
        System.out.println("Title       : " + postDetails[1]);
        System.out.println("Description : " + postDetails[2]);
        System.out.println("Creator ID  : " + postDetails[3]);
        System.out.println("Status      : " + postDetails[4]);
        System.out.println("Venue       : " + this.venue);
        System.out.println("Date        : " + this.date);
        System.out.println("Capacity    : " + this.capacity);
        System.out.println("Attendees   : " + this.attendeeCount);
        System.out.println("==============================================");
        return null;
    }


    // When the creator wants to see his/her posts
    public String getPostDetailsForCreator(){
        getPostDetails();
        return null;
    }

    @Override
    public String getReplyDetails() {
        return null;
    }

    @Override
    public boolean handleReply(Reply reply) {
        return false;
    }


    public String updateAttendeCount(String currentUser, String postId) throws Exception {
        con = ConnectDB.connect();
        Statement stmt = con.createStatement();

        // Check if the user has already registered for this event
        String checkAlreadyRegisteredQuery = "SELECT * FROM REPLY WHERE CREATORID = '" + currentUser + "' AND POSTID = '"+ postId +"';";
        ResultSet alreadyRegisteredSet = stmt.executeQuery(checkAlreadyRegisteredQuery);

        // User has already registered for the event
        if(alreadyRegisteredSet.next()){
            return "ALREADY_REGISTERED";
        }

        // User has not registered for the event
        else{
            String insertReplyQuery = "INSERT INTO REPLY (POSTID, CREATORID) VALUES (" +
                    "'" + postId + "', " +
                    "'" + currentUser + "');";
            int resultPost = stmt.executeUpdate(insertReplyQuery);
            String updateEventTableQuery = "UPDATE EVENT SET ATTENDEE_COUNT = ATTENDEE_COUNT + 1 WHERE ID = '"+ postId +"';";
            int resultUpdate = stmt.executeUpdate(updateEventTableQuery);

            if(resultPost==1){
                con.close();
                stmt.close();
                return "SUCCESSFUL";
            }
            else{
                con.close();
                stmt.close();
                return "UNSUCCESSFUL";
            }
        }
    }

    public void closePost(String postId) throws Exception {
        con = ConnectDB.connect();
        Statement stmt = con.createStatement();
        String closeEventQuery = "UPDATE POST SET STATUS = CLOSE WHERE ID = '" + postId + "'";
        int closeEventSet = stmt.executeUpdate(closeEventQuery);
    }
}
