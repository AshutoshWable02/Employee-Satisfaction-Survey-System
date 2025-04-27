package com.survey.backend.controller;

import com.survey.backend.dto.*;
import com.survey.backend.entity.EmpAdmin;
import com.survey.backend.repository.EmpAdminRepository;
import com.survey.backend.security.JwtUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EmpAdminRepository adminRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Auto-register admin on first run
    @PostConstruct
    public void initAdmin() {
        if (adminRepo.findByEmail("admin@survey.com").isEmpty()) {
            EmpAdmin admin = EmpAdmin.builder()
                    .email("admin@survey.com")
                    .password(passwordEncoder.encode("admin123"))
                    .build();
            adminRepo.save(admin);
            logger.info("Admin account created with email: admin@survey.com");
        }
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        try {
            // Authenticate the admin
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            // Generate the JWT token
            String token = jwtUtil.generateToken(request.getEmail());
            logger.info("Login successful for email: " + request.getEmail());
            return new AuthResponse(token);

        } catch (BadCredentialsException ex) {
            logger.error("Invalid credentials for email: " + request.getEmail());
            throw new RuntimeException("Invalid credentials", ex);
        }
    }
}
