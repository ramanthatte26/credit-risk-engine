package com.Raman.credit_risk_engine.repository;

import com.Raman.credit_risk_engine.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}