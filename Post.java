package model;

import java.util.ArrayList;

public abstract class Post {

    private String postId;
    private String title;
    private String description;
    private String creatorId;
    private String status;
    private String type;
    private String image;
    private ArrayList<Reply> replies;

    public Post(){}

    public Post(String postId,String type,String title,String description,String creatorId,String status,String image) {
        this.postId=postId;
        this.type=type;
        this.title=title;
        this.description=description;
        this.creatorId=creatorId;
        this.status=status;
        this.image=image;
        this.replies = new ArrayList<Reply>();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<Reply> getReplies() {
        return replies;
    }

    public void setReplies(ArrayList<Reply> replies) {
        this.replies = replies;
    }


    public String getReplyValueAndResponderId(int index){
        return replies.get(index).getResponderId() + "-" + replies.get(index).getValue();
    }


    public String getPostDetails(){
        return postId+":"+title+":"+description+":"+creatorId+":"+status;
    }

    public abstract String getPostDetailsForCreator();

    // Abstract methods to be implemented in subclasses
    public abstract String getReplyDetails();
    public abstract boolean handleReply(Reply reply);

    // Add a reply to this post
    public void addToReplyArray(Reply reply) {
        replies.add(reply);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
