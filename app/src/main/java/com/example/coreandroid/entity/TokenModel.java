package com.example.coreandroid.entity;

import java.io.Serializable;

public class TokenModel implements Serializable {

    private String token;
    private String access;

    public TokenModel(String token, String access) {
        this.token = token;
        this.access = access;
    }

    public TokenModel() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }
}
