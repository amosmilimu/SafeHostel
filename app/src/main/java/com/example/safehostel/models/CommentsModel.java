package com.example.safehostel.models;

public class CommentsModel {
    private String comment;
    private String post;
    private String profile_image;
    private String user;

    public CommentsModel() {
    }

    public CommentsModel(String comment, String post, String profile_image, String user) {
        this.comment = comment;
        this.post = post;
        this.profile_image = profile_image;
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public String getPost() {
        return post;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public String getUser() {
        return user;
    }
}
