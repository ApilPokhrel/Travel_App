package com.example.coreandroid.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class UserModel implements Serializable {

    private String _id;
    private String username;
    private String email;
    private String phone;
    private String role;
    private String password;
    private String gender;
    private boolean is_verified;
    private ArrayList<TokenModel> tokens;
    private ArrayList<OptionModel> options;
    private String address;

    public UserModel() {
        super();
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isIs_verified() {
        return is_verified;
    }

    public void setIs_verified(boolean is_verified) {
        this.is_verified = is_verified;
    }

    public ArrayList<TokenModel> getTokens() {
        return tokens;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setTokens(ArrayList<TokenModel> tokens) {
        this.tokens = tokens;
    }

    public ArrayList<OptionModel> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<OptionModel> options) {
        this.options = options;
    }
}
