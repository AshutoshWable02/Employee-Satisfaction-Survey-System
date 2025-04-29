package com.survey.backend.controller;

import com.survey.backend.model.SurveyForm;
import com.survey.backend.service.SurveyFormService;
import com.survey.backend.service.SurveyInviteService;
import com.survey.backend.service.SurveyResponseService;
import com.survey.backend.repository.SurveyFormRepository;
import com.survey.backend.dto.CreateSurveyRequest;
import com.survey.backend.model.Question;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/surveys")
@CrossOrigin(origins = "http://localhost:3000")
public class SurveyFormController {

    private final SurveyFormService service;
    private final SurveyInviteService surveyInviteService;
    // Removed unused field responseRepo
    private final SurveyFormRepository surveyFormRepository;

    public SurveyFormController(SurveyFormService service, SurveyInviteService surveyInviteService, SurveyResponseService responseService, SurveyFormRepository surveyFormRepository) {
        this.service = service;
        this.surveyInviteService = surveyInviteService;
        this.surveyFormRepository = surveyFormRepository;
    }

    @GetMapping
    public ResponseEntity<List<SurveyForm>> getAllSurveys() {
        try {
            List<SurveyForm> surveys = service.getAllSurveys();
            return ResponseEntity.ok(surveys);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    // Create a new survey and send invites
    @PostMapping
    public ResponseEntity<SurveyForm> createSurvey(@RequestBody CreateSurveyRequest request) {
        try {
            SurveyForm createdSurvey = service.createSurvey(request);

            // Send invites
            List<String> emails = request.getEmails();
            if (emails != null && !emails.isEmpty()) {
                surveyInviteService.sendInvites(createdSurvey.getId(), emails); // ✅ new method without department

            }

            return ResponseEntity.status(HttpStatus.CREATED).body(createdSurvey); // HTTP 201 for creation
        } catch (Exception e) {
            e.printStackTrace(); // ✅ This will log the actual error in the console
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        
    }

    // Update an existing survey
    @PutMapping("/{id}")
public ResponseEntity<?> updateSurvey(@PathVariable Long id, @RequestBody SurveyForm updatedForm) {
    Optional<SurveyForm> optionalForm = surveyFormRepository.findById(id);
    if (optionalForm.isEmpty()) {
        return ResponseEntity.notFound().build();
    }

    SurveyForm existingForm = optionalForm.get();

    // Update fields
    existingForm.setTitle(updatedForm.getTitle());
    existingForm.setDescription(updatedForm.getDescription());

    // Update or add new questions
    for (com.survey.backend.model.Question newQ : updatedForm.getQuestions()) {
        // Check if the question already exists (based on ID or another unique property)
        Optional<Question> existingQuestionOpt = existingForm.getQuestions().stream()
                .filter(q -> q.getId().equals(newQ.getId()))
                .findFirst();

        if (existingQuestionOpt.isPresent()) {
            // If the question exists, update its fields
            Question existingQuestion = existingQuestionOpt.get();
            existingQuestion.setText(newQ.getText());
            existingQuestion.setType(newQ.getType());
            existingQuestion.setOptions(newQ.getOptions());
        } else {
            // If the question doesn't exist, add it as a new question
            newQ.setSurveyForm(existingForm); // Maintain bidirectional relationship
            existingForm.getQuestions().add(newQ);
        }
    }

    surveyFormRepository.save(existingForm);
    return ResponseEntity.ok("Survey updated successfully");
}

    // Get details of a specific survey by ID
    @GetMapping("/{id}")
    public ResponseEntity<SurveyForm> getSurvey(@PathVariable Long id) {
        try {
            SurveyForm survey = service.getSurvey(id);
            return ResponseEntity.ok(survey); // HTTP 200 with the survey details
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Delete a survey
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSurvey(@PathVariable Long id) {
        try {
            service.deleteSurvey(id);
            return ResponseEntity.ok("Survey deleted successfully!"); // HTTP 200 on success
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting survey.");
        }
    }
    // Handle exceptions globally for the controller
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
    }
}
