package com.example.safehostel.models;

public class ProfileModel {
    private String admissionNo;
    private String email;
    private String hostel;
    private String institution;
    private String phoneNo;
    private String profile_image;
    private String role;
    private String user_uid;
    private String username;

    public ProfileModel() {
    }

    public ProfileModel(String admissionNo, String email, String hostel, String institution, String phoneNo, String profile_image, String role, String user_uid, String username) {
        this.admissionNo = admissionNo;
        this.email = email;
        this.hostel = hostel;
        this.institution = institution;
        this.phoneNo = phoneNo;
        this.profile_image = profile_image;
        this.role = role;
        this.user_uid = user_uid;
        this.username = username;
    }

    public String getAdmissionNo() {
        return admissionNo;
    }

    public void setAdmissionNo(String admissionNo) {
        this.admissionNo = admissionNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHostel() {
        return hostel;
    }

    public void setHostel(String hostel) {
        this.hostel = hostel;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUser_uid() {
        return user_uid;
    }

    public void setUser_uid(String user_uid) {
        this.user_uid = user_uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
