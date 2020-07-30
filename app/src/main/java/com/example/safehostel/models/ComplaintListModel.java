package com.example.safehostel.models;

public class ComplaintListModel {
    private String complaintTitle;
    private String complaintDate;
    private String complaintImgUrl1;
    private String complaintImgUrl2;
    private String complaintImgUrl3;
    private String complaintImgUrl4;
    private String complaintDesc;
    private boolean complaintState;

    public ComplaintListModel(String complaintTitle, String complaintDate,
                              String complaintImgUrl1, String complaintImgUrl2,
                              String complaintImgUrl3, String complaintImgUrl4,
                              String complaintDesc, boolean complaintState) {
        this.complaintTitle = complaintTitle;
        this.complaintDate = complaintDate;
        this.complaintImgUrl1 = complaintImgUrl1;
        this.complaintImgUrl2 = complaintImgUrl2;
        this.complaintImgUrl3 = complaintImgUrl3;
        this.complaintImgUrl4 = complaintImgUrl4;
        this.complaintDesc = complaintDesc;
        this.complaintState = complaintState;
    }

    public String getComplaintTitle() {
        return complaintTitle;
    }

    public void setComplaintTitle(String complaintTitle) {
        this.complaintTitle = complaintTitle;
    }

    public String getComplaintDate() {
        return complaintDate;
    }

    public void setComplaintDate(String complaintDate) {
        this.complaintDate = complaintDate;
    }

    public String getComplaintImgUrl1() {
        return complaintImgUrl1;
    }

    public void setComplaintImgUrl1(String complaintImgUrl1) {
        this.complaintImgUrl1 = complaintImgUrl1;
    }

    public String getComplaintImgUrl2() {
        return complaintImgUrl2;
    }

    public void setComplaintImgUrl2(String complaintImgUrl2) {
        this.complaintImgUrl2 = complaintImgUrl2;
    }

    public String getComplaintImgUrl3() {
        return complaintImgUrl3;
    }

    public void setComplaintImgUrl3(String complaintImgUrl3) {
        this.complaintImgUrl3 = complaintImgUrl3;
    }

    public String getComplaintImgUrl4() {
        return complaintImgUrl4;
    }

    public void setComplaintImgUrl4(String complaintImgUrl4) {
        this.complaintImgUrl4 = complaintImgUrl4;
    }

    public String getComplaintDesc() {
        return complaintDesc;
    }

    public void setComplaintDesc(String complaintDesc) {
        this.complaintDesc = complaintDesc;
    }

    public boolean isComplaintState() {
        return complaintState;
    }

    public void setComplaintState(boolean complaintState) {
        this.complaintState = complaintState;
    }
}
