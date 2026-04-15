package com.healthguard.repository;

import com.healthguard.model.ReminderPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReminderPlanRepository extends JpaRepository<ReminderPlan, String> {
    List<ReminderPlan> findByUserIdAndStatusAndTriggerTsAfter(String userId, String status, LocalDateTime now);
    List<ReminderPlan> findByUserIdAndTriggerTsBetween(String userId, LocalDateTime start, LocalDateTime end);
}