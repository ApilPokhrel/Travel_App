package com.example.coreandroid.entity;

import java.io.Serializable;

public class OptionModel implements Serializable {
    private String question;
    private String answer;

    public OptionModel(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public OptionModel() {
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
