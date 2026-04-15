package com.healthguard.service;

import com.healthguard.model.HealthProfile;
import com.healthguard.model.SleepRecord;
import com.healthguard.repository.SleepRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class SleepAnalyzerService {
    @Autowired
    private SleepRecordRepository sleepRecordRepository;
    @Autowired
    private HealthProfileService healthProfileService;
    @Autowired
    private DataCollectorService dataCollectorService;

    public SleepRecord updateNightScore(String userId, LocalDate date) {
        // 获取当日亮屏汇总
        DataCollectorService.ScreenSummary summary = dataCollectorService.getDailyScreenSummary(userId, date.toString());
        long after23Ms = summary.getAfter23Ms();

        // 获取用户健康档案
        HealthProfile profile = healthProfileService.getHealthProfile(userId);

        // 计算熬夜评分
        double score = calculateLateNightScore(after23Ms, profile);

        // 保存睡眠记录
        SleepRecord record = sleepRecordRepository.findByUserIdAndDate(userId, date).orElse(new SleepRecord());
        if (record.getRecordId() == null) {
            record.setRecordId(UUID.randomUUID().toString());
            record.setUserId(userId);
            record.setDate(date);
        }
        record.setLateNightScore(score);
        record.setSleepTs(LocalDateTime.now());
        record.setAwakeDurationMs(after23Ms);

        return sleepRecordRepository.save(record);
    }

    public SleepRecord getSleepRecord(String userId, LocalDate date) {
        return sleepRecordRepository.findByUserIdAndDate(userId, date).orElse(null);
    }

    public void submitSleepQuality(String userId, LocalDate date, int quality) {
        SleepRecord record = sleepRecordRepository.findByUserIdAndDate(userId, date).orElse(null);
        if (record != null) {
            record.setSleepQuality(quality);
            sleepRecordRepository.save(record);
        }
    }

    private double calculateLateNightScore(long after23Ms, HealthProfile profile) {
        // Step 1: 计算23:00后的亮屏时长（小时）
        double after23Hours = after23Ms / 3600000.0;

        // Step 2: 计算睡眠延迟
        int offsetMin = 0;
        if (profile != null && profile.getUsualSleepTime() != null) {
            LocalTime usualSleepTime = LocalTime.parse(profile.getUsualSleepTime(), DateTimeFormatter.ofPattern("HH:mm"));
            LocalTime actualSleepTime = LocalTime.now();
            if (actualSleepTime.isAfter(usualSleepTime)) {
                offsetMin = (int) java.time.Duration.between(usualSleepTime, actualSleepTime).toMinutes();
            }
        }

        // Step 3: 计算健康敏感度系数
        double coeff = 1.0;
        if (profile != null) {
            switch (profile.getSensitivityLevel()) {
                case 1: coeff = 0.8;
                    break;
                case 3: coeff = 1.2;
                    break;
                default: coeff = 1.0;
            }
            if (profile.isHasHypertension()) {
                coeff += 0.3;
            }
            if (profile.isHasHeartDisease()) {
                coeff += 0.2;
            }
        }

        // Step 4: 综合评分（满分100）
        double score = Math.min(100,
                (after23Hours * 40) + // 每小时亮屏40分（权重40%）
                (Math.min(offsetMin / 60.0, 1) * 35) + // 最多延迟1小时满分（权重35%）
                (25 * (coeff - 1)) // 健康加权（权重25%）
        ) * coeff;

        return Math.round(score * 10) / 10.0; // 保留一位小数
    }
}