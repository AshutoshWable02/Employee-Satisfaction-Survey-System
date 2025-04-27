package com.survey.backend.repository;

import com.survey.backend.model.Question;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findBySurveyFormId(Long surveyFormId);
// You can add custom query methods here if needed in the future
}
