package com.survey.backend.controller;

import com.survey.backend.model.SurveyForm;
import com.survey.backend.model.SurveyInvite;
import com.survey.backend.service.SurveyFormService;
import com.survey.backend.service.SurveyInviteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/employee")
@CrossOrigin(origins = "http://localhost:3000") // Ensure frontend can communicate with backend
public class SurveyAccessController {

    private final SurveyInviteService inviteService;
    private final SurveyFormService formService;

    public SurveyAccessController(SurveyInviteService inviteService, SurveyFormService formService) {
        this.inviteService = inviteService;
        this.formService = formService;
    }

    // Employee authentication for survey access
    @PostMapping("/access-survey")
    public ResponseEntity<?> accessSurvey(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");
        String surveyIdStr = request.get("surveyId");

        if (surveyIdStr == null || surveyIdStr.isEmpty()) {
            return ResponseEntity.badRequest().body("Survey ID is required");
        }

        Long surveyId;
        try {
            surveyId = Long.parseLong(surveyIdStr);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid survey ID format");
        }


        // Verify the invite details
        SurveyInvite invite = inviteService.findByEmailAndSurveyId(email, surveyId);

        if (invite == null || !invite.getPassword().equals(password)) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }

        // If valid, return survey form details
        SurveyForm survey = formService.getSurvey(surveyId);
        survey.setEmails(null); // Hide emails from the frontend

        return ResponseEntity.ok(survey);
    }
}
