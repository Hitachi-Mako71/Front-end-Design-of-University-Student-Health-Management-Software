package com.healthguard.repository;

import com.healthguard.model.RoutePoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoutePointRepository extends JpaRepository<RoutePoint, String> {
    List<RoutePoint> findByCourseId(String courseId);
}