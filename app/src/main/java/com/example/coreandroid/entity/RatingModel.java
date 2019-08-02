package com.example.coreandroid.entity;

import java.io.Serializable;

public class RatingModel implements Serializable {

    private String _id;
    private String user;
    private String place;
    private String text;
    private double rating;
    private String createdAt;
    private String updatedAt;


    public RatingModel(String _id, String user, String place, String text, double rating, String createdAt, String updatedAt) {
        this._id = _id;
        this.user = user;
        this.place = place;
        this.text = text;
        this.rating = rating;
    }

    public RatingModel() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
