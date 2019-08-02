package com.example.coreandroid.entity;

public class ResponseModel {

    private String message;
    private int status;

    public ResponseModel(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public ResponseModel() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
