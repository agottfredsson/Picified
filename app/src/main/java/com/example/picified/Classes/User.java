package com.example.picified.Classes;

public class User {
    private String name;
    private String email;
    private String profile_picture;
    private String uid;

    public User(String name, String email, String profile_picture, String uid) {
        this.name = name;
        this.email = email;
        this.profile_picture = profile_picture;
        this.uid = uid;
    }

    public User () {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
