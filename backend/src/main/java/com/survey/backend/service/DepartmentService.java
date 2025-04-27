package com.survey.backend.service;

import com.survey.backend.repository.SurveyResponseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final SurveyResponseRepository surveyResponseRepository;

    public List<String> getAllDepartments() {
        return surveyResponseRepository.findDistinctDepartments();
    }
}
