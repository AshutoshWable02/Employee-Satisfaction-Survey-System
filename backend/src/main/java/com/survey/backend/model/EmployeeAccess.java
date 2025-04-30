package com.survey.backend.model;

import jakarta.persistence.*;

@Entity
public class EmployeeAccess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private boolean used = false; // marks if already submitted the survey

    @ManyToOne
    @JoinColumn(name = "survey_form_id")
    private SurveyForm surveyForm;

    public SurveyForm getSurveyForm() {
        return surveyForm;
    }

    public void setSurveyForm(SurveyForm surveyForm) {
        this.surveyForm = surveyForm;
    }

        public Long getId() {
            return id;
        }
    
        public void setId(Long id) {
            this.id = id;
        }
    
        public String getEmail() {
            return email;
        }
    
        public void setEmail(String email) {
            this.email = email;
        }
    
        public String getPassword() {
            return password;
        }
    
        public void setPassword(String password) {
            this.password = password;
        }
    
        public boolean isUsed() {
            return used;
        }
    }