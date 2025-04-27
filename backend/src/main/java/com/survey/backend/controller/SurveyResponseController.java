package com.survey.backend.controller;

import com.survey.backend.dto.SubmitResponseRequest;
import com.survey.backend.model.SurveyResponse;
import com.survey.backend.service.SurveyResponseService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employee")
@CrossOrigin(origins = "http://localhost:3000")
public class SurveyResponseController {

    private final SurveyResponseService responseService;

    

    public SurveyResponseController(SurveyResponseService responseService) {
        this.responseService = responseService;
    }

    @PostMapping("/submit-response")
    public ResponseEntity<String> submitSurvey(@RequestBody SubmitResponseRequest request) {
        responseService.saveSurveyResponse(request);
        return ResponseEntity.ok("Survey submitted successfully");
    }
    @GetMapping("/responses/{surveyId}")
public ResponseEntity<List<SurveyResponse>> getResponses(@PathVariable Long surveyId) {
    List<SurveyResponse> responses = responseService.getResponsesBySurveyId(surveyId);
    return ResponseEntity.ok(responses);
}

}
