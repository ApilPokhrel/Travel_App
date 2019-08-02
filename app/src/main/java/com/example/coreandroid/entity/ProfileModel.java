package com.example.coreandroid.entity;

import java.io.Serializable;

public class ProfileModel implements Serializable {

    private String type;
    private String name;

    public ProfileModel(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public ProfileModel() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
