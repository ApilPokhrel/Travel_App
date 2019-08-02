package com.example.coreandroid.entity;

public class TravelInfo {
    private String description;
    private int img;

    public TravelInfo(String description, int img) {
        this.description = description;
        this.img = img;
    }

    public TravelInfo() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
