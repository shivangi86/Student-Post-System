package controller;

import javafx.event.ActionEvent;
import model.EditPost;
import model.Post;

public class ConfirmDeleteController {
    Post post;
    EditPost editPost;

    public void confirmDelete(ActionEvent actionEvent) throws Exception {
        post.setStatus("CLOSE");
        editPost.deletePost(post);
    }

    public void initData(Post post, EditPost editPost) {
        this.post=post;
        this.editPost=editPost;
    }
}
