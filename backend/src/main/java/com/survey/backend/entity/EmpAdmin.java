package com.survey.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.Email;

@Entity
@Table(name = "emp_admin")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmpAdmin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    // Optionally, you could add more fields like first name, last name, etc.
}