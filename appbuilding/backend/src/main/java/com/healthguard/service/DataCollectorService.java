package com.healthguard.service;

import com.healthguard.model.ScreenSession;
import com.healthguard.repository.ScreenSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
public class DataCollectorService {
    @Autowired
    private ScreenSessionRepository screenSessionRepository;
    @Autowired
    private SleepAnalyzerService sleepAnalyzerService;

    public ScreenSession recordScreenOn(String userId) {
        ScreenSession session = new ScreenSession();
        session.setSessionId(UUID.randomUUID().toString());
        session.setUserId(userId);
        session.setStartTs(LocalDateTime.now());
        return screenSessionRepository.save(session);
    }

    public ScreenSession recordScreenOff(String userId, String sessionId) {
        ScreenSession session = screenSessionRepository.findById(sessionId).orElse(null);
        if (session != null) {
            LocalDateTime endTs = LocalDateTime.now();
            session.setEndTs(endTs);
            session.setDurationMs(java.time.Duration.between(session.getStartTs(), endTs).toMillis());
            screenSessionRepository.save(session);

            // 检查是否在夜间时段
            LocalTime now = LocalTime.now();
            LocalTime lateNightStart = LocalTime.of(23, 0);
            if (now.isAfter(lateNightStart) || now.isBefore(LocalTime.of(6, 0))) {
                sleepAnalyzerService.updateNightScore(userId, LocalDate.now());
            }
        }
        return session;
    }

    public ScreenSummary getDailyScreenSummary(String userId, String date) {
        LocalDate targetDate = LocalDate.parse(date);
        LocalDateTime startOfDay = targetDate.atStartOfDay();
        LocalDateTime endOfDay = targetDate.atTime(23, 59, 59);
        LocalDateTime lateNightStart = targetDate.atTime(23, 0);

        List<ScreenSession> sessions = screenSessionRepository.findByUserIdAndStartTsBetween(userId, startOfDay, endOfDay);

        long totalMs = 0;
        long after23Ms = 0;

        for (ScreenSession session : sessions) {
            if (session.getEndTs() != null) {
                totalMs += session.getDurationMs();
                if (session.getStartTs().isAfter(lateNightStart)) {
                    after23Ms += session.getDurationMs();
                }
            }
        }

        ScreenSummary summary = new ScreenSummary();
        summary.setDate(date);
        summary.setTotalMs(totalMs);
        summary.setAfter23Ms(after23Ms);
        summary.setSessionCount(sessions.size());

        return summary;
    }

    // 内部类用于返回亮屏汇总数据
    public static class ScreenSummary {
        private String date;
        private long totalMs;
        private long after23Ms;
        private int sessionCount;

        // Getters and Setters
        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public long getTotalMs() {
            return totalMs;
        }

        public void setTotalMs(long totalMs) {
            this.totalMs = totalMs;
        }

        public long getAfter23Ms() {
            return after23Ms;
        }

        public void setAfter23Ms(long after23Ms) {
            this.after23Ms = after23Ms;
        }

        public int getSessionCount() {
            return sessionCount;
        }

        public void setSessionCount(int sessionCount) {
            this.sessionCount = sessionCount;
        }
    }
}