package model;

import model.database.ConnectionTest;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTable {
    public static void main(String[] args) throws SQLException {

        final String DB_NAME = "testDB";
        final String TABLE_NAME = "USER";

        //use try-with-resources Statement
        try (Connection con = ConnectionTest.getConnection(DB_NAME);
             Statement stmt = con.createStatement();
        ) {
            // User table
            int resultUser = stmt.executeUpdate("CREATE TABLE user ("
                    + "id INT NOT NULL IDENTITY,"
                    + "name VARCHAR(50) UNIQUE NOT NULL,"
                    + "PRIMARY KEY (id))");
            if(resultUser == 0) {
                System.out.println("Table User has been created successfully");
            } else {
                System.out.println("Table User is not created");
            }

            // Post table
            int resultPost = stmt.executeUpdate("CREATE TABLE post ("
                    + "id VARCHAR(10) NOT NULL,"
                    + "type VARCHAR(10),"
                    + "title VARCHAR(50) NOT NULL,"
                    + "description VARCHAR(500) NOT NULL,"
                    + "creator_id VARCHAR(50) NOT NULL,"
                    + "status VARCHAR(10) NOT NULL,"
                    + "image VARCHAR(100),"
                    + "PRIMARY KEY (id),"
                    + "FOREIGN KEY (creator_id) REFERENCES user(name))");
            if(resultPost == 0) {
                System.out.println("Table POST has been created successfully");
            } else {
                System.out.println("Table POST is not created");
            }

            // Sale table
            int resultSale = stmt.executeUpdate("CREATE TABLE sale ("
                    + "id VARCHAR(10) NOT NULL,"
                    + "asking_price DECIMAL,"
                    + "highest_offer DECIMAL NOT NULL,"
                    + "minimum_raise DECIMAL NOT NULL,"
                    + "PRIMARY KEY (id),"
                    + "FOREIGN KEY (id) REFERENCES post(id))");
            if(resultSale == 0) {
                System.out.println("Table SALE has been created successfully");
            } else {
                System.out.println("Table SALE is not created");
            }

            // Event table
            int resultEvent = stmt.executeUpdate("CREATE TABLE event ("
                    + "id VARCHAR(10) NOT NULL,"
                    + "venue VARCHAR(50),"
                    + "date DATE NOT NULL,"
                    + "capacity INT NOT NULL,"
                    + "attendee_count int NOT NULL,"
                    + "PRIMARY KEY (id),"
                    + "FOREIGN KEY (id) REFERENCES post(id))");
            if(resultEvent == 0) {
                System.out.println("Table Event has been created successfully");
            } else {
                System.out.println("Table Event is not created");
            }

            // Job table
            int resultJob = stmt.executeUpdate("CREATE TABLE job ("
                    + "id VARCHAR(10) NOT NULL,"
                    + "proposed_price DECIMAL,"
                    + "lowest_price DECIMAL NOT NULL,"
                    + "PRIMARY KEY (id),"
                    + "FOREIGN KEY (id) REFERENCES post(id))");
            if(resultJob == 0) {
                System.out.println("Table Job has been created successfully");
            } else {
                System.out.println("Table Job is not created");
            }

            // Post_Count table
            int resultPostCount = stmt.executeUpdate("CREATE TABLE post_count ("
                    + "type VARCHAR(10) NOT NULL,"
                    + "total INT NOT NULL)");
            if(resultPostCount == 0) {
                System.out.println("Table Post_Count has been created successfully");
            } else {
                System.out.println("Table Post_Count is not created");
            }


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
