package com.survey.backend.service;

import com.survey.backend.dto.SubmitResponseRequest;
import com.survey.backend.model.Question;
import com.survey.backend.model.SurveyResponse;
import com.survey.backend.repository.QuestionRepository;
import com.survey.backend.repository.SurveyResponseRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SurveyResponseService {

    private final SurveyResponseRepository responseRepo;
    private final QuestionRepository questionRepo;

    public SurveyResponseService(SurveyResponseRepository responseRepo, QuestionRepository questionRepo) {
        this.responseRepo = responseRepo;
        this.questionRepo = questionRepo;
    }

    public void saveSurveyResponse(SubmitResponseRequest request) {
        List<SurveyResponse> responses = new ArrayList<>();

        for (SubmitResponseRequest.AnswerDTO answerDTO : request.getAnswers()) {
            Question question = questionRepo.findById(answerDTO.getQuestionId())
                    .orElseThrow(() -> new RuntimeException("Question not found"));

            SurveyResponse response = new SurveyResponse();
            response.setSurveyId(request.getSurveyId());
            response.setDepartment(request.getDepartment());
            response.setQuestion(question);
            response.setAnswer(answerDTO.getAnswer());

            responses.add(response);
        }

        responseRepo.saveAll(responses);
    }
    public List<SurveyResponse> getResponsesBySurveyId(Long surveyId) {
        return responseRepo.findBySurveyId(surveyId);
    }
    
}
