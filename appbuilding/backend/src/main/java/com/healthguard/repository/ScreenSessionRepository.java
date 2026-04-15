package com.healthguard.repository;

import com.healthguard.model.ScreenSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ScreenSessionRepository extends JpaRepository<ScreenSession, String> {
    List<ScreenSession> findByUserIdAndStartTsBetween(String userId, LocalDateTime start, LocalDateTime end);
    List<ScreenSession> findByUserIdAndEndTsIsNull(String userId);
}