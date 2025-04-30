package com.survey.backend.service;

import com.survey.backend.dto.CreateSurveyRequest;
import com.survey.backend.dto.QuestionRequest;
import com.survey.backend.model.Question;
import com.survey.backend.model.SurveyForm;
import com.survey.backend.model.QuestionType;
import com.survey.backend.repository.SurveyFormRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SurveyFormService {

    private final SurveyFormRepository repository;

    public SurveyFormService(SurveyFormRepository repository) {
        this.repository = repository;
    }

    public List<SurveyForm> getAllSurveys() {
        return repository.findAll();
    }
    @Transactional
    public SurveyForm createSurvey(CreateSurveyRequest request) {
        SurveyForm surveyForm = new SurveyForm();
        surveyForm.setTitle(request.getTitle());
        surveyForm.setDescription(request.getDescription());
        surveyForm.setEmails(request.getEmails());
    
        List<Question> questions = new ArrayList<>();
        for (QuestionRequest qr : request.getQuestions()) {
            Question question = new Question();
            question.setText(qr.getText());
            question.setType(QuestionType.fromString(qr.getType()));
    
            // Handle MCQ options
            if (qr.getType().equalsIgnoreCase(QuestionType.MCQ.name())) {
                if (qr.getOptions() != null && !qr.getOptions().isEmpty()) {
                    question.setOptions(qr.getOptions());
                } else {
                    // Default options for MCQs
                    question.setOptions(List.of(
                        "Very Dissatisfied", 
                        "Dissatisfied", 
                        "Neutral", 
                        "Satisfied", 
                        "Very Satisfied"
                    ));
                }
            }
    
            // Link the question to the survey form
            question.setSurveyForm(surveyForm);
            questions.add(question);
        }
    
        surveyForm.setQuestions(questions);
    
        // Save the survey form along with its questions
        return repository.save(surveyForm);
    }
    

    @Transactional
public SurveyForm updateSurvey(Long id, SurveyForm updatedForm) {
    // Find the existing survey form
    SurveyForm existing = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Survey Form not found with id " + id));

    // Update the title and description
    existing.setTitle(updatedForm.getTitle());
    existing.setDescription(updatedForm.getDescription());

    // Update questions if they are present in the request
    if (updatedForm.getQuestions() != null && !updatedForm.getQuestions().isEmpty()) {
        // Clear existing questions
        existing.getQuestions().clear();
        
        // Add new questions
        updatedForm.getQuestions().forEach(q -> {
            // Set the relationship to the parent SurveyForm
            q.setSurveyForm(existing);
            existing.getQuestions().add(q);
        });
    }

    // Save the updated survey form and return it
    return repository.save(existing);
}


    // Fetch a single survey form by ID
    public SurveyForm getSurvey(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Survey not found with id: " + id));
    }

    // Delete a survey by ID
    @Transactional
    public void deleteSurvey(Long id) {
        SurveyForm surveyForm = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Survey not found with id: " + id));
        repository.delete(surveyForm);
    }
}
