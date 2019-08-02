package com.example.coreandroid.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class LocationModel implements Serializable {

    private String address;
    private ArrayList<Double> coordinates;


    public LocationModel(String address, ArrayList<Double> coordinates) {
        this.address = address;
        this.coordinates = coordinates;
    }

    public LocationModel() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ArrayList<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(ArrayList<Double> coordinates) {
        this.coordinates = coordinates;
    }
}
