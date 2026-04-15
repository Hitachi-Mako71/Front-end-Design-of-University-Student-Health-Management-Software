package com.healthguard.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * 健康档案模型类
 * 存储用户的健康相关信息
 */
@Entity
@Table(name = "health_profiles")
public class HealthProfile {
    @Id
    private String profileId; // 健康档案唯一标识
    private String userId; // 用户ID
    private boolean hasHypertension; // 是否有高血压
    private boolean hasHeartDisease; // 是否有心脏病
    private boolean hasAnxiety; // 是否有焦虑症
    private boolean hasSleepDisorder; // 是否有睡眠障碍
    private String usualSleepTime; // 通常入睡时间
    private String usualWakeTime; // 通常起床时间
    private int sensitivityLevel; // 敏感程度
    private String extraNotes; // 额外说明

    // Getters and Setters
    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isHasHypertension() {
        return hasHypertension;
    }

    public void setHasHypertension(boolean hasHypertension) {
        this.hasHypertension = hasHypertension;
    }

    public boolean isHasHeartDisease() {
        return hasHeartDisease;
    }

    public void setHasHeartDisease(boolean hasHeartDisease) {
        this.hasHeartDisease = hasHeartDisease;
    }

    public boolean isHasAnxiety() {
        return hasAnxiety;
    }

    public void setHasAnxiety(boolean hasAnxiety) {
        this.hasAnxiety = hasAnxiety;
    }

    public boolean isHasSleepDisorder() {
        return hasSleepDisorder;
    }

    public void setHasSleepDisorder(boolean hasSleepDisorder) {
        this.hasSleepDisorder = hasSleepDisorder;
    }

    public String getUsualSleepTime() {
        return usualSleepTime;
    }

    public void setUsualSleepTime(String usualSleepTime) {
        this.usualSleepTime = usualSleepTime;
    }

    public String getUsualWakeTime() {
        return usualWakeTime;
    }

    public void setUsualWakeTime(String usualWakeTime) {
        this.usualWakeTime = usualWakeTime;
    }

    public int getSensitivityLevel() {
        return sensitivityLevel;
    }

    public void setSensitivityLevel(int sensitivityLevel) {
        this.sensitivityLevel = sensitivityLevel;
    }

    public String getExtraNotes() {
        return extraNotes;
    }

    public void setExtraNotes(String extraNotes) {
        this.extraNotes = extraNotes;
    }
}