package com.example.safehostel.models;

public class ComplaintListModel {
    private String title;
    private String date;
    private String imageUrl1;
    private String imageUrl2;
    private String imageUrl3;
    private String imageUrl4;
    private String description;
    private String state;
    private String post_id;
    private String viewers;
    private String user_id;
    private String user_image;
    private Boolean solved;
    private String by;

    public ComplaintListModel() {
    }


    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getImageUrl1() {
        return imageUrl1;
    }

    public String getImageUrl2() {
        return imageUrl2;
    }

    public String getImageUrl3() {
        return imageUrl3;
    }

    public String getImageUrl4() {
        return imageUrl4;
    }

    public String getDescription() {
        return description;
    }

    public String getState() {
        return state;
    }

    public String getPost_id() {
        return post_id;
    }

    public String getViewers() {
        return viewers;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUser_image() {
        return user_image;
    }

    public Boolean getSolved() {
        return solved;
    }

    public String getBy() {
        return by;
    }
}
