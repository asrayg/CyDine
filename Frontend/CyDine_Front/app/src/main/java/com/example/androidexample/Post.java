package com.example.androidexample;

import java.util.ArrayList;

public class Post {
    private String imageUri;
    private String caption;
    private int likeCount;
    private ArrayList<String> comments;

    public Post(String imageUri, String caption) {
        this.imageUri = imageUri;
        this.caption = caption;
        this.likeCount = 0;
        this.comments = new ArrayList<>();
    }

    public String getImageUri() { return imageUri; }
    public String getCaption() { return caption; }
    public int getLikeCount() { return likeCount; }
    public ArrayList<String> getComments() { return comments; }

    public void addComment(String comment) {
        comments.add(comment);
    }

    public void likePost() {
        likeCount++;
    }

    public String getCommentsAsString() {
        return String.join(", ", comments); // Join comments into a single string
    }
}
