package com.survey.backend.security;

import com.survey.backend.entity.EmpAdmin;
import com.survey.backend.repository.EmpAdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private EmpAdminRepository adminRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        EmpAdmin admin = adminRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found"));

        // You can customize roles as per your system; here "ADMIN" is used.
        return User.builder()
                .username(admin.getEmail())  // Username is the email here.
                .password(admin.getPassword())  // You already have the password hash in the admin object.
                .roles("ADMIN")  // Define roles; only "ADMIN" role in this case.
                .build();
    }
}
