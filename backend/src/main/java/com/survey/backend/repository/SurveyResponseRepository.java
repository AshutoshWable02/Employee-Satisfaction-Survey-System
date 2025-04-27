package com.survey.backend.repository;

import com.survey.backend.model.SurveyResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SurveyResponseRepository extends JpaRepository<SurveyResponse, Long> {
    List<SurveyResponse> findBySurveyId(Long surveyId);
    List<SurveyResponse> findBySurveyIdAndDepartment(Long surveyId, String department);
     @Query("SELECT DISTINCT r.department FROM SurveyResponse r WHERE r.department IS NOT NULL")
    List<String> findDistinctDepartments();
}
