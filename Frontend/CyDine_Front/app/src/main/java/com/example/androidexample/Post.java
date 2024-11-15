package com.example.androidexample;

import java.util.ArrayList;

/**
 * Represents a social media post containing an image, caption, likes, and comments.
 */
public class Post {
    private String imageUri; // URI of the image associated with the post
    private String caption; // Caption for the post
    private int likeCount; // Number of likes on the post
    private ArrayList<String> comments; // List of comments on the post

    /**
     * Constructs a new Post with the specified image URI and caption.
     *
     * @param imageUri The URI of the image for the post.
     * @param caption  The caption for the post. If null, an empty string is used.
     */
    public Post(String imageUri, String caption) {
        this.imageUri = imageUri;
        this.caption = caption != null ? caption : "";
        this.likeCount = 0;
        this.comments = new ArrayList<>();
    }

    /**
     * Gets the image URI of the post.
     *
     * @return The image URI.
     */
    public String getImageUri() {
        return imageUri;
    }

    /**
     * Gets the caption of the post.
     *
     * @return The caption.
     */
    public String getCaption() {
        return caption;
    }

    /**
     * Gets the current like count for the post.
     *
     * @return The number of likes.
     */
    public int getLikeCount() {
        return likeCount;
    }

    /**
     * Gets the list of comments on the post.
     *
     * @return An {@link ArrayList} of comments.
     */
    public ArrayList<String> getComments() {
        return comments;
    }

    /**
     * Adds a comment to the post.
     *
     * @param comment The comment to add.
     */
    public void addComment(String comment) {
        comments.add(comment);
    }

    /**
     * Increments the like count for the post by one.
     */
    public void likePost() {
        likeCount++;
    }

    /**
     * Gets all comments as a single, comma-separated string.
     *
     * @return A string containing all comments, separated by commas.
     */
    public String getCommentsAsString() {
        return String.join(", ", comments);
    }
}
