package model;

import javafx.beans.property.SimpleStringProperty;
import model.database.ConnectDB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Reply {
//    private SimpleStringProperty postId;
    private SimpleStringProperty value;
    private SimpleStringProperty responderId;


//    public Reply(String postId, double value, String responderId) {
//        this.postId = postId;
//        this.value = value;
//        this.responderId = responderId;
//    }

    public Reply(String responderId, String value){
        this.responderId = new SimpleStringProperty(responderId);
        this.value = new SimpleStringProperty(value);
    }


//    public String getPostId() {
//        return postId;
//    }


//    public void setPostId(String postId) {
//        this.postId = postId;
//    }


    public String getValue() {
        return value.get();
    }


    public void setValue(String value) {
        this.value.set(value);
    }


    public String getResponderId() {
        return responderId.get();
    }


    public void setResponderId(String responderId) {
        this.responderId.set(responderId);
    }



}
