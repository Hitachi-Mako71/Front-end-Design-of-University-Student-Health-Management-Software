package com.healthguard.service;

import org.springframework.stereotype.Service;

@Service
public class ExternalApiService {

    public NavTimeResult getNavTime(double originLat, double originLng, double destLat, double destLng) {
        // 模拟导航时间计算
        NavTimeResult result = new NavTimeResult();
        result.setDurationMin(15); // 15分钟
        result.setDistanceM(1000); // 1000米
        result.setSource("default");
        return result;
    }

    public WeatherResult getTomorrowWeather(String city) {
        // 模拟天气数据
        WeatherResult result = new WeatherResult();
        result.setWeather("晴");
        result.setTempMax(25);
        result.setTempMin(15);
        result.setAdvice("明天天气晴朗，适合出行！");
        return result;
    }

    public AiHealthTipResult getAiHealthTip(double lateNightScore, Object profile) {
        // 模拟AI健康提示
        AiHealthTipResult result = new AiHealthTipResult();
        if (lateNightScore > 70) {
            result.setTip("昨晚熬夜较严重，今天建议多休息，多喝水，避免剧烈运动。");
        } else if (lateNightScore > 40) {
            result.setTip("昨晚熬夜适中，今天注意保持良好的作息习惯。");
        } else {
            result.setTip("昨晚睡眠良好，今天精力充沛，继续保持！");
        }
        return result;
    }

    // 内部类用于返回导航时间结果
    public static class NavTimeResult {
        private int durationMin;
        private int distanceM;
        private String source;

        // Getters and Setters
        public int getDurationMin() {
            return durationMin;
        }

        public void setDurationMin(int durationMin) {
            this.durationMin = durationMin;
        }

        public int getDistanceM() {
            return distanceM;
        }

        public void setDistanceM(int distanceM) {
            this.distanceM = distanceM;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }
    }

    // 内部类用于返回天气结果
    public static class WeatherResult {
        private String weather;
        private int tempMax;
        private int tempMin;
        private String advice;

        // Getters and Setters
        public String getWeather() {
            return weather;
        }

        public void setWeather(String weather) {
            this.weather = weather;
        }

        public int getTempMax() {
            return tempMax;
        }

        public void setTempMax(int tempMax) {
            this.tempMax = tempMax;
        }

        public int getTempMin() {
            return tempMin;
        }

        public void setTempMin(int tempMin) {
            this.tempMin = tempMin;
        }

        public String getAdvice() {
            return advice;
        }

        public void setAdvice(String advice) {
            this.advice = advice;
        }
    }

    // 内部类用于返回AI健康提示结果
    public static class AiHealthTipResult {
        private String tip;

        // Getters and Setters
        public String getTip() {
            return tip;
        }

        public void setTip(String tip) {
            this.tip = tip;
        }
    }
}