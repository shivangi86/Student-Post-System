package model;

import model.database.ConnectionTest;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DeleteTable {
    public static void main(String[] args) throws SQLException {

        final String DB_NAME = "testDB";
        final String TABLE_NAME = "POST";

        //use try-with-resources Statement
        try (Connection con = ConnectionTest.getConnection(DB_NAME);
             Statement stmt = con.createStatement();
        ) {
            stmt.executeUpdate("DROP TABLE sale");
            stmt.executeUpdate("DROP TABLE job");
            stmt.executeUpdate("DROP TABLE event");
            stmt.executeUpdate("DROP TABLE post");
            stmt.executeUpdate("DROP TABLE user");


//            if(result == 0) {
//                System.out.println("Table " + TABLE_NAME + " has been deleted successfully");
//            } else {
//                System.out.println("Table " + TABLE_NAME + " was not deleted");
//            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
