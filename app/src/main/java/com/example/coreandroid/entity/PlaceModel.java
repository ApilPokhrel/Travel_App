package com.example.coreandroid.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class PlaceModel implements Serializable {

    private String _id;
    private String title;
    private String contact;
    private float average;
    private String description;
    private LocationModel location;
    private ArrayList<ProfileModel> profiles;
    private String tag;
    @SerializedName("reviews")
    ArrayList<RatingModel> ratings;

    public PlaceModel(String _id, String title, String contact, float average, String description, LocationModel location, ArrayList<ProfileModel> profiles, String tag, ArrayList<RatingModel> ratings) {
        this._id = _id;
        this.title = title;
        this.contact = contact;
        this.average = average;
        this.description = description;
        this.location = location;
        this.profiles = profiles;
        this.tag = tag;
        this.ratings = ratings;
    }

    public PlaceModel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocationModel getLocation() {
        return location;
    }

    public void setLocation(LocationModel location) {
        this.location = location;
    }

    public ArrayList<ProfileModel> getProfiles() {
        return profiles;
    }

    public void setProfiles(ArrayList<ProfileModel> profiles) {
        this.profiles = profiles;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public float getAverage() {
        return average;
    }

    public void setAverage(float average) {
        this.average = average;
    }

    public ArrayList<RatingModel> getRatings() {
        return ratings;
    }

    public void setRatings(ArrayList<RatingModel> ratings) {
        this.ratings = ratings;
    }
}
