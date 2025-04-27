package com.survey.backend.repository;

import com.survey.backend.model.EmployeeAccess;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeAccessRepository extends JpaRepository<EmployeeAccess, Long> {
    Optional<EmployeeAccess> findByEmailAndSurveyFormId(String email, Long surveyId);
}
