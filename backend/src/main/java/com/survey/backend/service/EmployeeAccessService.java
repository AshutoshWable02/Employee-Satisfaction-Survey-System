package com.survey.backend.service;

import com.survey.backend.model.EmployeeAccess;
import com.survey.backend.model.SurveyForm;
import com.survey.backend.repository.EmployeeAccessRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class EmployeeAccessService {

    private final EmployeeAccessRepository repository;
    private final EmailService emailService;

    @Value("${app.survey.base-url}")
    private String baseUrl;

    public EmployeeAccessService(EmployeeAccessRepository repository, EmailService emailService) {
        this.repository = repository;
        this.emailService = emailService;
    }

    public void generateAndSendPasswords(SurveyForm survey, List<String> employeeEmails) {
        for (String email : employeeEmails) {
            String password = generateRandomPassword(8);
            EmployeeAccess access = new EmployeeAccess();
            access.setEmail(email);
            access.setPassword(password);
            access.setSurveyForm(survey);
            repository.save(access);

            String link = baseUrl + "/survey/" + survey.getId();
            emailService.sendEmail(email,
                    "Your Survey Form Access",
                    "Please access the form at: " + link + "\nYour password: " + password);
        }
    }

    private String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
