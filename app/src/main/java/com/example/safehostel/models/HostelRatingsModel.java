package com.example.safehostel.models;

public class HostelRatingsModel {
    private String hostelName;
    private String hostelUpVotes;
    private String hostelDownVotes;

    public HostelRatingsModel(String hostelName, String hostelUpVotes, String hostelDownVotes) {
        this.hostelName = hostelName;
        this.hostelUpVotes = hostelUpVotes;
        this.hostelDownVotes = hostelDownVotes;
    }


    public String getHostelName() {
        return hostelName;
    }

    public void setHostelName(String hostelName) {
        this.hostelName = hostelName;
    }

    public String getHostelUpVotes() {
        return hostelUpVotes;
    }

    public void setHostelUpVotes(String hostelUpVotes) {
        this.hostelUpVotes = hostelUpVotes;
    }

    public String getHostelDownVotes() {
        return hostelDownVotes;
    }

    public void setHostelDownVotes(String hostelDownVotes) {
        this.hostelDownVotes = hostelDownVotes;
    }
}
