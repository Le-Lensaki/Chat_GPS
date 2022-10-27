package com.example.chat_gps2.fragment.Model;

public class User {
    String id;
    String imageURL;
    String phone;
    String status;
    String username;
    Double Latitude;
    Double Longitude;

    public Double getLatitude() {
        return Latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public User() {
    }

    public User(String id, String imageURL, String phone, String status, String username) {
        this.id = id;
        this.imageURL = imageURL;
        this.phone = phone;
        this.status = status;
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getPhone() {
        return phone;
    }

    public String getStatus() {
        return status;
    }

    public String getUsername() {
        return username;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
