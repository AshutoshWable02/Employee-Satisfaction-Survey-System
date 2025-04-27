package com.survey.backend.dto;

import java.util.List;

public class QuestionRequest {
    private String text;
    private String type;  // Ensure this matches the QuestionType Enum
    private List<String> options;

    public QuestionRequest() {} 
    // Getters and setters
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }
}
