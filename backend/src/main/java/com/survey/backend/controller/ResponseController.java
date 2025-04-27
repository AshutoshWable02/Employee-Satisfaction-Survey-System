package com.survey.backend.controller;

import com.survey.backend.model.SurveyResponse; // Update to the correct package path
import com.survey.backend.repository.SurveyResponseRepository;
import com.survey.backend.service.SurveyResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/responses") // ✅ This is the key update
@RequiredArgsConstructor
public class ResponseController {

    private final SurveyResponseService responseService;
    private final SurveyResponseRepository responseRepo;

    @GetMapping("/{surveyId}")
    public ResponseEntity<List<SurveyResponse>> getResponses(@PathVariable Long surveyId) {
        List<SurveyResponse> responses = responseService.getResponsesBySurveyId(surveyId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{surveyId}/department/{dept}")
    public ResponseEntity<List<SurveyResponse>> getDeptResponses(
            @PathVariable Long surveyId,
            @PathVariable String dept) {
        List<SurveyResponse> responses = responseRepo.findBySurveyIdAndDepartment(surveyId, dept);
        return ResponseEntity.ok(responses);
    }
}
