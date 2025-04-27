package com.survey.backend.service;

import com.survey.backend.model.SurveyInvite;
import com.survey.backend.model.SurveyForm;
import com.survey.backend.repository.SurveyFormRepository;
import com.survey.backend.repository.SurveyInviteRepository;
import com.survey.backend.util.PasswordGenerator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SurveyInviteService {

    @Autowired
    private SurveyInviteRepository inviteRepository;

    @Autowired
    private SurveyFormRepository surveyFormRepository;

    @Autowired
    private EmailService emailService;

    private String generateRandomPassword() {
        return PasswordGenerator.generate(8); // Assuming PasswordGenerator has a static method to generate passwords
    }

    @Transactional
    public void sendInvites(Long surveyId, List<String> emails) {
        SurveyForm surveyForm = surveyFormRepository.findById(surveyId).orElse(null);
        if (surveyForm == null) {
            throw new IllegalArgumentException("Survey form not found with the provided surveyId: " + surveyId);
        }
    
        for (String email : emails) {
            String password = generateRandomPassword();
            saveInvitation(surveyId, email, password, surveyForm);
            sendMockEmail(email, surveyId, password);
        }
    }
public SurveyInvite findByEmailAndSurveyId(String email, Long surveyFormId) {
    List<SurveyInvite> invites = inviteRepository.findByEmailAndSurveyForm_Id(email, surveyFormId);
    return invites.isEmpty() ? null : invites.get(0);
}


private void saveInvitation(Long surveyId, String email, String password, SurveyForm surveyForm) {
    SurveyInvite invite = new SurveyInvite();
    invite.setEmail(email);
    invite.setPassword(password);
    invite.setCreatedAt(LocalDateTime.now());
    invite.setSurveyForm(surveyForm);
    inviteRepository.save(invite);
}

private void sendMockEmail(String toEmail, Long surveyId, String password) {
    String surveyLink = "http://localhost:3000/employee/login?surveyId=" + surveyId;

    String subject = "Invitation to Participate in Employee Satisfaction Survey";
    String body = "Dear Employee,\n\n"
            + "You have been selected to participate in an important Employee Satisfaction Survey conducted by our organization.\n\n"
            + "Your feedback is invaluable in helping us improve workplace culture, employee well-being, and overall satisfaction.\n\n"
            + "Please find your personalized access details below:\n\n"
            + "Survey Link: " + surveyLink + "\n"
            + "Access Password: " + password + "\n\n"
            + "⚠️ Please note:\n"
            + "- This password is confidential and should not be shared with anyone.\n"
            + "- Each participant is allowed to submit only one response.\n"
            + "- Your responses will remain completely anonymous.\n\n"
            + "We appreciate your time and honesty. The survey should only take a few minutes to complete.\n\n"
            + "If you encounter any issues or have questions, feel free to reach out to the HR department.\n\n"
            + "Thank you for contributing to our continuous improvement.\n\n"
            + "Warm regards,\n"
            + "SentiMeter Team\n";

    emailService.sendEmail(toEmail, subject, body);
}

}
