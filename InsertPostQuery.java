package model;

import model.database.ConnectionTest;

import java.sql.Connection;
import java.sql.Statement;

public class InsertPostQuery {
    public static void main(String[] args) {
        final String DB_NAME = "testDB";
        final String TABLE_NAME = "sale";

        //use try-with-resources Statement
        try (Connection con = ConnectionTest.getConnection(DB_NAME);
             Statement stmt = con.createStatement();
        ) {
            String query = "INSERT INTO " + TABLE_NAME +
                    " VALUES (1, 100, 50, 20, 'iphone.png')," +
                    "        (2, 200, 100, 40, 'ipad.png')";
            //" VALUES (2, 's3089940', 'Tom', 'Bruster')";

            int result = stmt.executeUpdate(query);

            con.commit();

            System.out.println("Insert into table " + TABLE_NAME + " executed successfully");
            System.out.println(result + " row(s) affected");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
