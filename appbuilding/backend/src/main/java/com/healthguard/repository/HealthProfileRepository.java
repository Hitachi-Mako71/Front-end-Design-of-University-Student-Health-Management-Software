package com.healthguard.repository;

import com.healthguard.model.HealthProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HealthProfileRepository extends JpaRepository<HealthProfile, String> {
    Optional<HealthProfile> findByUserId(String userId);
}