package com.example.safehostel.models;

public class CommentsModel {
    private String commentorName;
    private String commentorImage;
    private String commentorComment;

    public CommentsModel(String commentorName, String commentorImage,
                         String commentorComment) {
        this.commentorName = commentorName;
        this.commentorImage = commentorImage;
        this.commentorComment = commentorComment;
    }

    public String getCommentorName() {
        return commentorName;
    }

    public void setCommentorName(String commentorName) {
        this.commentorName = commentorName;
    }

    public String getCommentorImage() {
        return commentorImage;
    }

    public void setCommentorImage(String commentorImage) {
        this.commentorImage = commentorImage;
    }

    public String getCommentorComment() {
        return commentorComment;
    }

    public void setCommentorComment(String commentorComment) {
        this.commentorComment = commentorComment;
    }
}
