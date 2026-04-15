package com.healthguard.repository;

import com.healthguard.model.SleepRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface SleepRecordRepository extends JpaRepository<SleepRecord, String> {
    Optional<SleepRecord> findByUserIdAndDate(String userId, LocalDate date);
}