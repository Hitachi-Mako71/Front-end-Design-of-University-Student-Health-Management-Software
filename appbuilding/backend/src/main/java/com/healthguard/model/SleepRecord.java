package com.healthguard.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 睡眠记录模型类
 * 存储用户的睡眠相关信息
 */
@Entity
@Table(name = "sleep_records")
public class SleepRecord {
    @Id
    private String recordId; // 记录唯一标识
    private String userId; // 用户ID
    private LocalDate date; // 日期
    private double lateNightScore; // 熬夜评分
    private LocalDateTime sleepTs; // 入睡时间
    private long awakeDurationMs; // 清醒时长（毫秒）
    private Integer sleepQuality; // 睡眠质量评分

    // Getters and Setters
    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getLateNightScore() {
        return lateNightScore;
    }

    public void setLateNightScore(double lateNightScore) {
        this.lateNightScore = lateNightScore;
    }

    public LocalDateTime getSleepTs() {
        return sleepTs;
    }

    public void setSleepTs(LocalDateTime sleepTs) {
        this.sleepTs = sleepTs;
    }

    public long getAwakeDurationMs() {
        return awakeDurationMs;
    }

    public void setAwakeDurationMs(long awakeDurationMs) {
        this.awakeDurationMs = awakeDurationMs;
    }

    public Integer getSleepQuality() {
        return sleepQuality;
    }

    public void setSleepQuality(Integer sleepQuality) {
        this.sleepQuality = sleepQuality;
    }
}