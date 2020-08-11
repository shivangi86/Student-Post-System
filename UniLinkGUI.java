package model;

import model.database.ConnectDB;
import model.database.ConnectionTest;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class UniLinkGUI {

    String userId;
    Connection con;

    public ArrayList<Post> posts = new ArrayList<Post>();
    public ArrayList<Sale> sales = new ArrayList<Sale>();
    public ArrayList<Event> events = new ArrayList<Event>();
    public ArrayList<Job> jobs = new ArrayList<Job>();

    public UniLinkGUI(String id){
        this.userId = id;
    }

    // Get all the user data from db
    public void loadData() throws Exception {
        System.out.println("in load data");
        con = ConnectDB.connect();

        if(jobs.size()==0){
            getJobPosts(con);
        }
        if(sales.size()==0){
            getSalePosts(con);
        }
        if(jobs.size()==0){
            getEventPosts(con);
        }


    }

    private void getJobPosts(Connection con)  throws Exception{
        String title=""; String description=""; String creatorId=""; String status="";
        double proposedPrice; double lowestOffer; String image="";
//        String query = "select * from job JOIN post on job.ID = post.ID where post.CREATOR_ID = 'john123';";
        String query = "select * from job JOIN post on job.ID = post.ID;";
        Statement stmt = con.createStatement();
        ResultSet resultSet = stmt.executeQuery(query);
        while(resultSet.next()){
            status = resultSet.getString("status");
            String id     = resultSet.getString("id");
            title         = resultSet.getString("title");
            description   = resultSet.getString("description");
            creatorId     = resultSet.getString("creator_id");
            lowestOffer   = resultSet.getDouble("lowest_price");
            proposedPrice = resultSet.getDouble("proposed_price");
            image        = resultSet.getString("image");
            Job job = new Job(id,title, description, creatorId, status, proposedPrice, lowestOffer,image);
            posts.add(job);
        }
    }


    public void getSalePosts(Connection con) throws Exception{
        String title=""; String description=""; String creatorId=""; String status="";
        double askingPrice; double highestOffer; double minimumRaise; String image="";
//        String query = "select * from sale JOIN post on sale.ID = post.ID where post.CREATOR_ID = 'john123';";
        String query = "select * from sale JOIN post on sale.ID = post.ID;";
        Statement stmt = con.createStatement();
        ResultSet resultSet = stmt.executeQuery(query);
        while(resultSet.next()){
            status = resultSet.getString("status");
            String id    = resultSet.getString("id");
            title        = resultSet.getString("title");
            description  = resultSet.getString("description");
            creatorId    = resultSet.getString("creator_id");
            askingPrice  = resultSet.getDouble("asking_price");
            highestOffer = resultSet.getDouble("highest_offer");
            minimumRaise = resultSet.getDouble("minimum_raise");
            image        = resultSet.getString("image");

            System.out.println("Highest Offer : " + highestOffer);
            Sale sale = new Sale(id,title, description, creatorId, status, askingPrice, highestOffer, minimumRaise, image);
            posts.add(sale);
        }
    }

    public void getEventPosts(Connection con) throws Exception{
        String title=""; String description=""; String creatorId=""; String status="";
        String venue=""; String date=""; int capacity=0; int attendeeCount=0; String image="";
//        String query = "select * from event JOIN post on event.ID = post.ID where post.CREATOR_ID = 'john123';";
        String query = "select * from event JOIN post on event.ID = post.ID;";
        Statement stmt = con.createStatement();
        ResultSet resultSet = stmt.executeQuery(query);
        while(resultSet.next()){
            String id = resultSet.getString("id");
            title = resultSet.getString("title");
            description = resultSet.getString("description");
            creatorId = resultSet.getString("creator_id");
            status = resultSet.getString("status");
            venue = resultSet.getString("VENUE");
            date = resultSet.getString("date");
            capacity = resultSet.getInt("capacity");
            attendeeCount = resultSet.getInt("attendee_count");
            image        = resultSet.getString("image");

            Event event = new Event(id,"EVENT", title, description, creatorId, status, venue, date, capacity, attendeeCount,image);
            posts.add(event);
        }
    }

    public void showEvents() {

    }



}

