package com.survey.backend.repository;

import com.survey.backend.entity.EmpAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EmpAdminRepository extends JpaRepository<EmpAdmin, Long> {
    Optional<EmpAdmin> findByEmail(String email);
}
