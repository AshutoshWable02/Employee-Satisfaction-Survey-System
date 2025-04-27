package com.survey.backend.dto;

import java.util.List;

public class InviteRequest {
    private List<String> emails;
    private String department;

    // Getters and setters
    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}

