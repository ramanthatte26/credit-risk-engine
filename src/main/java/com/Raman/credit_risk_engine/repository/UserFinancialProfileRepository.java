package com.Raman.credit_risk_engine.repository;

import com.Raman.credit_risk_engine.entity.UserFinancialProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFinancialProfileRepository extends JpaRepository<UserFinancialProfile,Long> {
}
