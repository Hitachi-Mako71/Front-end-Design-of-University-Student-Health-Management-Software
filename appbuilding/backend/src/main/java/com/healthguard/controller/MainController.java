package com.healthguard.controller;

import com.healthguard.model.*;
import com.healthguard.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 主控制器类
 * 处理所有API请求，包括用户管理、课表管理、数据采集、睡眠分析、提醒调度和外部API调用
 */
@RestController
@RequestMapping("/")
public class MainController {
    @Autowired
    private UserService userService; // 用户服务
    @Autowired
    private HealthProfileService healthProfileService; // 健康档案服务
    @Autowired
    private ScheduleService scheduleService; // 课表服务
    @Autowired
    private DataCollectorService dataCollectorService; // 数据采集服务
    @Autowired
    private SleepAnalyzerService sleepAnalyzerService; // 睡眠分析服务
    @Autowired
    private ReminderSchedulerService reminderSchedulerService; // 提醒调度服务
    @Autowired
    private ExternalApiService externalApiService; // 外部API服务

    /**
     * 统一成功返回结构
     * @param data 返回数据
     * @param <T> 数据类型
     * @return 响应实体
     */
    private <T> ResponseEntity<?> success(T data) {
        return ResponseEntity.ok(new Response<>(0, "ok", data));
    }

    /**
     * 统一错误返回结构
     * @param code 错误码
     * @param msg 错误信息
     * @return 响应实体
     */
    private ResponseEntity<?> error(int code, String msg) {
        return ResponseEntity.ok(new Response<>(code, msg, null));
    }

    // 用户管理接口
    /**
     * 创建用户
     * @param nickname 用户名
     * @return 用户信息
     */
    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@RequestParam String nickname) {
        User user = userService.createUser(nickname);
        return success(user);
    }

    /**
     * 提交健康档案
     * @param userId 用户ID
     * @param profileData 健康档案数据
     * @return 健康档案信息
     */
    @PostMapping("/submitHealthProfile")
    public ResponseEntity<?> submitHealthProfile(@RequestParam String userId, @RequestBody HealthProfile profileData) {
        HealthProfile profile = healthProfileService.submitHealthProfile(userId, profileData);
        return success(profile);
    }

    /**
     * 获取健康档案
     * @param userId 用户ID
     * @return 健康档案信息
     */
    @GetMapping("/getHealthProfile")
    public ResponseEntity<?> getHealthProfile(@RequestParam String userId) {
        HealthProfile profile = healthProfileService.getHealthProfile(userId);
        return success(profile);
    }

    // 课表管理接口
    /**
     * 导入课表
     * @param userId 用户ID
     * @param semesterLabel 学期标签
     * @param courses 课程列表
     * @return 课表导入结果
     */
    @PostMapping("/importSchedule")
    public ResponseEntity<?> importSchedule(@RequestParam String userId, @RequestParam String semesterLabel, @RequestBody List<Course> courses) {
        String scheduleId = scheduleService.importSchedule(userId, semesterLabel, courses);
        return success(new ScheduleResult(scheduleId, courses.size()));
    }

    /**
     * 获取今日课程
     * @param userId 用户ID
     * @return 今日课程列表
     */
    @GetMapping("/getTodayCourses")
    public ResponseEntity<?> getTodayCourses(@RequestParam String userId) {
        List<Course> courses = scheduleService.getTodayCourses(userId);
        return success(courses);
    }

    /**
     * 保存路线
     * @param courseId 课程ID
     * @param routePoints 路线点列表
     * @return 路线保存结果
     */
    @PostMapping("/saveRoute")
    public ResponseEntity<?> saveRoute(@RequestParam String courseId, @RequestBody List<RoutePoint> routePoints) {
        String routeId = scheduleService.saveRoute(courseId, routePoints);
        return success(new RouteResult(routeId, routePoints.size()));
    }

    /**
     * 获取带警示的路线
     * @param courseId 课程ID
     * @return 带警示的路线点列表
     */
    @GetMapping("/getRouteWithAlerts")
    public ResponseEntity<?> getRouteWithAlerts(@RequestParam String courseId) {
        List<RoutePoint> points = scheduleService.getRouteWithAlerts(courseId);
        return success(points);
    }

    // 数据采集接口
    /**
     * 记录亮屏事件
     * @param userId 用户ID
     * @return 屏幕会话信息
     */
    @PostMapping("/recordScreenOn")
    public ResponseEntity<?> recordScreenOn(@RequestParam String userId) {
        ScreenSession session = dataCollectorService.recordScreenOn(userId);
        return success(session);
    }

    /**
     * 记录灭屏事件
     * @param userId 用户ID
     * @param sessionId 会话ID
     * @return 屏幕会话信息
     */
    @PostMapping("/recordScreenOff")
    public ResponseEntity<?> recordScreenOff(@RequestParam String userId, @RequestParam String sessionId) {
        ScreenSession session = dataCollectorService.recordScreenOff(userId, sessionId);
        return success(session);
    }

    /**
     * 获取每日屏幕使用汇总
     * @param userId 用户ID
     * @param date 日期
     * @return 屏幕使用汇总信息
     */
    @GetMapping("/getDailyScreenSummary")
    public ResponseEntity<?> getDailyScreenSummary(@RequestParam String userId, @RequestParam String date) {
        DataCollectorService.ScreenSummary summary = dataCollectorService.getDailyScreenSummary(userId, date);
        return success(summary);
    }

    // 睡眠分析接口
    /**
     * 更新熬夜评分
     * @param userId 用户ID
     * @param date 日期
     * @return 睡眠记录信息
     */
    @PostMapping("/updateNightScore")
    public ResponseEntity<?> updateNightScore(@RequestParam String userId, @RequestParam String date) {
        SleepRecord record = sleepAnalyzerService.updateNightScore(userId, LocalDate.parse(date));
        return success(record);
    }

    /**
     * 获取睡眠记录
     * @param userId 用户ID
     * @param date 日期
     * @return 睡眠记录信息
     */
    @GetMapping("/getSleepRecord")
    public ResponseEntity<?> getSleepRecord(@RequestParam String userId, @RequestParam String date) {
        SleepRecord record = sleepAnalyzerService.getSleepRecord(userId, LocalDate.parse(date));
        return success(record);
    }

    /**
     * 提交睡眠质量评价
     * @param userId 用户ID
     * @param date 日期
     * @param quality 睡眠质量评分
     * @return 操作结果
     */
    @PostMapping("/submitSleepQuality")
    public ResponseEntity<?> submitSleepQuality(@RequestParam String userId, @RequestParam String date, @RequestParam int quality) {
        sleepAnalyzerService.submitSleepQuality(userId, LocalDate.parse(date), quality);
        return success(new SuccessResult(true));
    }

    // 提醒调度接口
    /**
     * 生成每日提醒计划
     * @param userId 用户ID
     * @param targetDate 目标日期
     * @return 提醒计划结果
     */
    @PostMapping("/generateDailyPlan")
    public ResponseEntity<?> generateDailyPlan(@RequestParam String userId, @RequestParam String targetDate) {
        List<ReminderPlan> plans = reminderSchedulerService.generateDailyPlan(userId, LocalDate.parse(targetDate));
        return success(new PlanResult(targetDate, plans.size(), plans));
    }

    /**
     * 获取待处理提醒
     * @param userId 用户ID
     * @return 待处理提醒列表
     */
    @GetMapping("/getPendingReminders")
    public ResponseEntity<?> getPendingReminders(@RequestParam String userId) {
        List<ReminderPlan> reminders = reminderSchedulerService.getPendingReminders(userId);
        return success(reminders);
    }

    /**
     * 响应提醒
     * @param planId 计划ID
     * @param action 操作类型
     * @param snoozeMin 推迟分钟数（可选）
     * @return 响应结果
     */
    @PostMapping("/ackReminder")
    public ResponseEntity<?> ackReminder(@RequestParam String planId, @RequestParam String action, @RequestParam(required = false) Integer snoozeMin) {
        ReminderPlan plan = reminderSchedulerService.ackReminder(planId, action, snoozeMin);
        return success(new AckResult(true, plan != null ? plan.getTriggerTs().toEpochSecond() * 1000 : null));
    }

    // 外部API接口
    /**
     * 获取导航时间
     * @param originLat 起点纬度
     * @param originLng 起点经度
     * @param destLat 终点纬度
     * @param destLng 终点经度
     * @return 导航时间结果
     */
    @GetMapping("/getNavTime")
    public ResponseEntity<?> getNavTime(@RequestParam double originLat, @RequestParam double originLng, @RequestParam double destLat, @RequestParam double destLng) {
        ExternalApiService.NavTimeResult result = externalApiService.getNavTime(originLat, originLng, destLat, destLng);
        return success(result);
    }

    /**
     * 获取明天天气
     * @param city 城市名称
     * @return 天气结果
     */
    @GetMapping("/getTomorrowWeather")
    public ResponseEntity<?> getTomorrowWeather(@RequestParam String city) {
        ExternalApiService.WeatherResult result = externalApiService.getTomorrowWeather(city);
        return success(result);
    }

    /**
     * 获取AI健康提示
     * @param lateNightScore 熬夜评分
     * @param profile 健康档案
     * @return AI健康提示结果
     */
    @PostMapping("/getAiHealthTip")
    public ResponseEntity<?> getAiHealthTip(@RequestParam double lateNightScore, @RequestBody Object profile) {
        ExternalApiService.AiHealthTipResult result = externalApiService.getAiHealthTip(lateNightScore, profile);
        return success(result);
    }

    // 内部类用于返回结构
    private static class Response<T> {
        private int code;
        private String msg;
        private T data;

        public Response(int code, String msg, T data) {
            this.code = code;
            this.msg = msg;
            this.data = data;
        }

        // Getters and Setters
        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }
    }

    private static class ScheduleResult {
        private String scheduleId;
        private int courseCount;

        public ScheduleResult(String scheduleId, int courseCount) {
            this.scheduleId = scheduleId;
            this.courseCount = courseCount;
        }

        // Getters and Setters
        public String getScheduleId() {
            return scheduleId;
        }

        public void setScheduleId(String scheduleId) {
            this.scheduleId = scheduleId;
        }

        public int getCourseCount() {
            return courseCount;
        }

        public void setCourseCount(int courseCount) {
            this.courseCount = courseCount;
        }
    }

    private static class RouteResult {
        private String routeId;
        private int pointCount;

        public RouteResult(String routeId, int pointCount) {
            this.routeId = routeId;
            this.pointCount = pointCount;
        }

        // Getters and Setters
        public String getRouteId() {
            return routeId;
        }

        public void setRouteId(String routeId) {
            this.routeId = routeId;
        }

        public int getPointCount() {
            return pointCount;
        }

        public void setPointCount(int pointCount) {
            this.pointCount = pointCount;
        }
    }

    private static class SuccessResult {
        private boolean success;

        public SuccessResult(boolean success) {
            this.success = success;
        }

        // Getters and Setters
        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }
    }

    private static class PlanResult {
        private String targetDate;
        private int planCount;
        private List<ReminderPlan> plans;

        public PlanResult(String targetDate, int planCount, List<ReminderPlan> plans) {
            this.targetDate = targetDate;
            this.planCount = planCount;
            this.plans = plans;
        }

        // Getters and Setters
        public String getTargetDate() {
            return targetDate;
        }

        public void setTargetDate(String targetDate) {
            this.targetDate = targetDate;
        }

        public int getPlanCount() {
            return planCount;
        }

        public void setPlanCount(int planCount) {
            this.planCount = planCount;
        }

        public List<ReminderPlan> getPlans() {
            return plans;
        }

        public void setPlans(List<ReminderPlan> plans) {
            this.plans = plans;
        }
    }

    private static class AckResult {
        private boolean success;
        private Long newTriggerTs;

        public AckResult(boolean success, Long newTriggerTs) {
            this.success = success;
            this.newTriggerTs = newTriggerTs;
        }

        // Getters and Setters
        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public Long getNewTriggerTs() {
            return newTriggerTs;
        }

        public void setNewTriggerTs(Long newTriggerTs) {
            this.newTriggerTs = newTriggerTs;
        }
    }
}