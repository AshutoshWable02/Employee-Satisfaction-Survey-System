package com.survey.backend.controller;

import com.survey.backend.dto.CreateSurveyRequest;
import com.survey.backend.model.Question;
import com.survey.backend.model.SurveyForm;
import com.survey.backend.repository.SurveyFormRepository;
import com.survey.backend.service.SurveyFormService;
import com.survey.backend.service.SurveyInviteService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/admin/surveys")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class SurveyFormController {

    private static final Logger logger = LoggerFactory.getLogger(SurveyFormController.class);

    private final SurveyFormService service;
    private final SurveyInviteService surveyInviteService;
    private final SurveyFormRepository surveyFormRepository;

    // 1. Get all surveys
    @GetMapping
    public ResponseEntity<List<SurveyForm>> getAllSurveys() {
        List<SurveyForm> surveys = service.getAllSurveys();
        return ResponseEntity.ok(surveys);
    }

    // 2. Create a new survey and optionally send invites
    @PostMapping
    public ResponseEntity<SurveyForm> createSurvey(@RequestBody CreateSurveyRequest request) {
        SurveyForm createdSurvey = service.createSurvey(request);

        List<String> emails = request.getEmails();
        if (emails != null && !emails.isEmpty()) {
            surveyInviteService.sendInvites(createdSurvey.getId(), emails);
            logger.info("Invites sent for survey ID {} to {} recipients", createdSurvey.getId(), emails.size());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(createdSurvey);
    }

    // 3. Update existing survey (title, description, questions)
    @PutMapping("/{id}")
    public ResponseEntity<String> updateSurvey(@PathVariable Long id, @RequestBody SurveyForm updatedForm) {
        Optional<SurveyForm> optionalForm = surveyFormRepository.findById(id);
        if (optionalForm.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        SurveyForm existingForm = optionalForm.get();
        existingForm.setTitle(updatedForm.getTitle());
        existingForm.setDescription(updatedForm.getDescription());

        Map<Long, Question> existingQuestionsMap = new HashMap<>();
        for (Question q : existingForm.getQuestions()) {
            existingQuestionsMap.put(q.getId(), q);
        }

        for (Question newQ : updatedForm.getQuestions()) {
            if (newQ.getId() != null && existingQuestionsMap.containsKey(newQ.getId())) {
                Question existingQ = existingQuestionsMap.get(newQ.getId());
                existingQ.setText(newQ.getText());
                existingQ.setType(newQ.getType());
                existingQ.setOptions(newQ.getOptions());
            } else {
                newQ.setSurveyForm(existingForm);
                existingForm.getQuestions().add(newQ);
            }
        }

        surveyFormRepository.save(existingForm);
        return ResponseEntity.ok("Survey updated successfully");
    }

    // 4. Get survey by ID
    @GetMapping("/{id}")
    public ResponseEntity<SurveyForm> getSurvey(@PathVariable Long id) {
        try {
            SurveyForm survey = service.getSurvey(id);
            return ResponseEntity.ok(survey);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // 5. Delete a survey by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSurvey(@PathVariable Long id) {
        try {
            service.deleteSurvey(id);
            logger.info("Deleted survey with ID {}", id);
            return ResponseEntity.ok("Survey deleted successfully!");
        } catch (Exception e) {
            logger.error("Failed to delete survey ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting survey.");
        }
    }

    // 6. Global exception fallback for this controller
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        logger.error("Unhandled error in SurveyFormController: ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
    }
}
