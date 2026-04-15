package com.healthguard.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * 提醒计划模型类
 * 存储用户的提醒计划信息
 */
@Entity
@Table(name = "reminder_plans")
public class ReminderPlan {
    @Id
    private String planId; // 计划唯一标识
    private String userId; // 用户ID
    private String planType; // 计划类型
    private LocalDateTime triggerTs; // 触发时间
    private String message; // 提醒消息
    private int priority; // 优先级
    private String status; // 状态

    // Getters and Setters
    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public LocalDateTime getTriggerTs() {
        return triggerTs;
    }

    public void setTriggerTs(LocalDateTime triggerTs) {
        this.triggerTs = triggerTs;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}