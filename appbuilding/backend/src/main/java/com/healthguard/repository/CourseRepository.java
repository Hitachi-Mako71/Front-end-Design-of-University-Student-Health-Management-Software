package com.healthguard.repository;

import com.healthguard.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, String> {
    List<Course> findByUserId(String userId);
    List<Course> findByUserIdAndWeekday(String userId, int weekday);
}