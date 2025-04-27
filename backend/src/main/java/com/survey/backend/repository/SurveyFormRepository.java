package com.survey.backend.repository;

import com.survey.backend.model.SurveyForm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyFormRepository extends JpaRepository<SurveyForm, Long> {
}
