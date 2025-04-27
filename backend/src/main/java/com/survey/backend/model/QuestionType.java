package com.survey.backend.model;

public enum QuestionType {
    TEXT,
    RATING,
    MCQ;

    public static QuestionType fromString(String type) {
        if (type == null) {
            return null;
        }
    
        switch (type.toUpperCase()) {
            case "RADIO": // alias
            case "MCQ":   // direct usage
                return MCQ;
            case "TEXT":
                return TEXT;
            case "RATING":
                return RATING;
            default:
                throw new IllegalArgumentException("Unknown question type: " + type);
        }
    }
    
}
