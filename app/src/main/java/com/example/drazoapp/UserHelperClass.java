package com.example.drazoapp;

public class UserHelperClass {
    private String fullName;
    private String pseudo;
    private String email;
    private String phoneNo;
    private String password;
    private String imageURL;

    public UserHelperClass() {}

    public UserHelperClass(String fullName, String pseudo, String email, String phoneNo, String password) {
        this.fullName = fullName;
        this.pseudo = pseudo;
        this.email = email;
        this.phoneNo = phoneNo;
        this.password = password;
        imageURL = "NO PROFILE PICTURE";
    }

    public UserHelperClass(String fullName, String pseudo, String email, String phoneNo, String password, String imageURL) {
        this.fullName = fullName;
        this.pseudo = pseudo;
        this.email = email;
        this.phoneNo = phoneNo;
        this.password = password;
        this.imageURL = imageURL;
    }


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
