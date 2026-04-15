package com.healthguard.service;

import com.healthguard.model.Course;
import com.healthguard.model.RoutePoint;
import com.healthguard.repository.CourseRepository;
import com.healthguard.repository.RoutePointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ScheduleService {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private RoutePointRepository routePointRepository;

    public String importSchedule(String userId, String semesterLabel, List<Course> courses) {
        String scheduleId = UUID.randomUUID().toString();
        for (Course course : courses) {
            course.setCourseId(UUID.randomUUID().toString());
            course.setUserId(userId);
            course.setScheduleId(scheduleId);
            courseRepository.save(course);
        }
        return scheduleId;
    }

    public List<Course> getTodayCourses(String userId) {
        int weekday = LocalDate.now().getDayOfWeek().getValue();
        return courseRepository.findByUserIdAndWeekday(userId, weekday);
    }

    public String saveRoute(String courseId, List<RoutePoint> routePoints) {
        String routeId = UUID.randomUUID().toString();
        int order = 1;
        for (RoutePoint point : routePoints) {
            point.setPointId(UUID.randomUUID().toString());
            point.setRouteId(routeId);
            point.setCourseId(courseId);
            point.setPointOrder(order++);
            routePointRepository.save(point);
        }
        return routeId;
    }

    public List<RoutePoint> getRouteWithAlerts(String courseId) {
        return routePointRepository.findByCourseId(courseId);
    }
}