package com.survey.backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class SubmitResponseRequest {
    private Long surveyId;
    private String department;
    private List<AnswerDTO> answers;

    @Data
    public static class AnswerDTO {
        private Long questionId;
        private String answer;
    }
}
