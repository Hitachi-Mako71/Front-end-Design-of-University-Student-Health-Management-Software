package com.healthguard.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * 课程模型类
 * 表示用户的课程信息
 */
@Entity
@Table(name = "courses")
public class Course {
    @Id
    private String courseId; // 课程唯一标识
    private String userId; // 用户ID
    private String scheduleId; // 课表ID
    private String courseName; // 课程名称
    private int weekday; // 星期（1-7）
    private int weekStart; // 开始周
    private int weekEnd; // 结束周
    private int periodStart; // 开始节
    private int periodEnd; // 结束节
    private String classroom; // 教室
    private double buildingLat; // 教学楼纬度
    private double buildingLng; // 教学楼经度

    // Getters and Setters
    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getWeekday() {
        return weekday;
    }

    public void setWeekday(int weekday) {
        this.weekday = weekday;
    }

    public int getWeekStart() {
        return weekStart;
    }

    public void setWeekStart(int weekStart) {
        this.weekStart = weekStart;
    }

    public int getWeekEnd() {
        return weekEnd;
    }

    public void setWeekEnd(int weekEnd) {
        this.weekEnd = weekEnd;
    }

    public int getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(int periodStart) {
        this.periodStart = periodStart;
    }

    public int getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(int periodEnd) {
        this.periodEnd = periodEnd;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public double getBuildingLat() {
        return buildingLat;
    }

    public void setBuildingLat(double buildingLat) {
        this.buildingLat = buildingLat;
    }

    public double getBuildingLng() {
        return buildingLng;
    }

    public void setBuildingLng(double buildingLng) {
        this.buildingLng = buildingLng;
    }
}