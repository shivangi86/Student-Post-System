package model;

import common.SessionDetails;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import main.Main;
import model.database.ConnectDB;

import java.net.URL;
import java.sql.*;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class Login{

    // Db connection object
    Connection con;

    public void handleLogin(String userId) throws Exception {

        // Connect to Database
        con = ConnectDB.connect();

        // Check if user exists
        String getUser = "select * from user where name =" + "'" + userId + "'";
        Statement stmt = con.createStatement();
        ResultSet resultSet = stmt.executeQuery(getUser);

        // If exists navigate to main window
        if (resultSet.next()) {
            // Return to the controller which will navigate to next window
            return;
            //UniLinkGUIController.changeScene("view/main.fxml");
//          login_successful.setText("Welcome " + resultSet.getString("name"));
        }
        // Else set preferences
        else {
            // Add to db
            String query = "INSERT INTO user(name) VALUES ('" + userId + "')";
            System.out.println("Insert query : " + query);
            int result = stmt.executeUpdate(query);
            System.out.println("Result  : " + result);
            con.commit();
        }
    }

//    /*
//    * Connect to database when the login view is rendered
//    * */
//    @Override
//    public void initialize(URL location, ResourceBundle resources) {
//        ConnectionTest contest = new ConnectionTest();
//        this.con = contest.connect();
//        System.out.println("Connection : " + con);
//
//    }

}
