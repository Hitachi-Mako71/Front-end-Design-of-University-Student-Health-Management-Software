package com.healthguard.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "reminder_logs")
public class ReminderLog {
    @Id
    private String logId;
    private String planId;
    private String action;
    private Integer snoozeMin;
    private LocalDateTime actionTs;

    // Getters and Setters
    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Integer getSnoozeMin() {
        return snoozeMin;
    }

    public void setSnoozeMin(Integer snoozeMin) {
        this.snoozeMin = snoozeMin;
    }

    public LocalDateTime getActionTs() {
        return actionTs;
    }

    public void setActionTs(LocalDateTime actionTs) {
        this.actionTs = actionTs;
    }
}