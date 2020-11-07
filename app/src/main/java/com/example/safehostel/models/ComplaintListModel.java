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

    public ComplaintListModel() {
    }

    public ComplaintListModel(String title, String date, String imageUrl1, String imageUrl2, String imageUrl3, String imageUrl4, String description, String state, String post_id, String viewers) {
        this.title = title;
        this.date = date;
        this.imageUrl1 = imageUrl1;
        this.imageUrl2 = imageUrl2;
        this.imageUrl3 = imageUrl3;
        this.imageUrl4 = imageUrl4;
        this.description = description;
        this.state = state;
        this.post_id = post_id;
        this.viewers = viewers;
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
}
