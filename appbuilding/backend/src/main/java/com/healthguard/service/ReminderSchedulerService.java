package com.healthguard.service;

import com.healthguard.model.ReminderPlan;
import com.healthguard.model.ReminderLog;
import com.healthguard.repository.ReminderPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ReminderSchedulerService {
    @Autowired
    private ReminderPlanRepository reminderPlanRepository;
    @Autowired
    private SleepAnalyzerService sleepAnalyzerService;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private HealthProfileService healthProfileService;

    public List<ReminderPlan> generateDailyPlan(String userId, LocalDate targetDate) {
        // 获取前一天的睡眠记录
        LocalDate previousDate = targetDate.minusDays(1);
        var sleepRecord = sleepAnalyzerService.getSleepRecord(userId, previousDate);

        // 获取目标日期的课程
        var courses = scheduleService.getTodayCourses(userId);

        // 获取用户健康档案
        var profile = healthProfileService.getHealthProfile(userId);

        List<ReminderPlan> plans = new ArrayList<>();

        // 为每门课程生成提醒
        for (var course : courses) {
            // 模拟导航时长（实际应该调用外部API）
            int navDuration = 15; // 15分钟
            int commuteBuffer = 10; // 10分钟缓冲

            // 计算出发时间
            LocalDateTime classStartTime = targetDate.atTime(8, 0); // 假设第一节课8:00开始
            LocalDateTime departTime = classStartTime.minusMinutes(navDuration + commuteBuffer);

            // 生成晨起健康提醒
            LocalDateTime morningTime = departTime.minusMinutes(45);
            ReminderPlan morningReminder = createReminderPlan(
                    userId, "morning_health", morningTime,
                    "昨晚你熬夜了，今天有" + courses.size() + "节课，记得带好" + course.getCourseName() + "的课本，多喝水，注意身体！",
                    calculatePriority(sleepRecord, profile)
            );
            plans.add(morningReminder);

            // 生成出发提醒
            ReminderPlan departReminder = createReminderPlan(
                    userId, "commute_start", departTime,
                    "该出发去上课了，预计需要" + navDuration + "分钟到达教室",
                    2
            );
            plans.add(departReminder);

            // 生成路口提醒
            var routePoints = scheduleService.getRouteWithAlerts(course.getCourseId());
            for (var point : routePoints) {
                LocalDateTime alertTime = departTime.plusMinutes(5); // 模拟5分钟后到达路口
                ReminderPlan crossroadReminder = createReminderPlan(
                        userId, "crossroads", alertTime,
                        point.getAlertMessage(),
                        2
                );
                plans.add(crossroadReminder);
            }
        }

        // 生成当晚熬夜警告
        LocalDateTime warnTime = targetDate.atTime(23, 0);
        ReminderPlan lateNightWarn = createReminderPlan(
                userId, "late_night_warn", warnTime,
                "已经很晚了，早点休息吧！",
                calculatePriority(sleepRecord, profile)
        );
        plans.add(lateNightWarn);

        return plans;
    }

    public List<ReminderPlan> getPendingReminders(String userId) {
        LocalDateTime now = LocalDateTime.now();
        return reminderPlanRepository.findByUserIdAndStatusAndTriggerTsAfter(userId, "pending", now);
    }

    public ReminderPlan ackReminder(String planId, String action, Integer snoozeMin) {
        // 找到提醒计划
        ReminderPlan plan = reminderPlanRepository.findById(planId).orElse(null);
        if (plan != null) {
            // 更新状态
            plan.setStatus(action);
            reminderPlanRepository.save(plan);

            // 如果是推迟，创建新的提醒
            if ("snoozed".equals(action) && snoozeMin != null) {
                ReminderPlan newPlan = createReminderPlan(
                        plan.getUserId(), plan.getPlanType(),
                        plan.getTriggerTs().plusMinutes(snoozeMin),
                        plan.getMessage(), plan.getPriority()
                );
                return newPlan;
            }
        }
        return plan;
    }

    private ReminderPlan createReminderPlan(String userId, String planType, LocalDateTime triggerTs, String message, int priority) {
        ReminderPlan plan = new ReminderPlan();
        plan.setPlanId(UUID.randomUUID().toString());
        plan.setUserId(userId);
        plan.setPlanType(planType);
        plan.setTriggerTs(triggerTs);
        plan.setMessage(message);
        plan.setPriority(priority);
        plan.setStatus("pending");
        return reminderPlanRepository.save(plan);
    }

    private int calculatePriority(Object sleepRecord, Object profile) {
        // 简化实现，实际应该根据睡眠记录和健康档案计算优先级
        return 2; // 默认中优先级
    }
}