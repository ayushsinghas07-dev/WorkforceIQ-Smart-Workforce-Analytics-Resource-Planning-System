package com.workforceiq.dto;

import java.util.ArrayList;
import java.util.List;

public class BenchmarkDto {
    private List<BenchmarkItem> items = new ArrayList<>();
    private double totalManualTimeMinutes;
    private double totalSystemTimeSeconds;
    private double timeSavedMultiplier;

    public static class BenchmarkItem {
        private String taskName;
        private double manualTimeMinutes;
        private double systemTimeMs;
        private double speedupFactor;

        public BenchmarkItem() {}

        public BenchmarkItem(String taskName, double manualTimeMinutes, double systemTimeMs, double speedupFactor) {
            this.taskName = taskName;
            this.manualTimeMinutes = manualTimeMinutes;
            this.systemTimeMs = systemTimeMs;
            this.speedupFactor = speedupFactor;
        }

        public String getTaskName() { return taskName; }
        public void setTaskName(String taskName) { this.taskName = taskName; }
        public double getManualTimeMinutes() { return manualTimeMinutes; }
        public void setManualTimeMinutes(double manualTimeMinutes) { this.manualTimeMinutes = manualTimeMinutes; }
        public double getSystemTimeMs() { return systemTimeMs; }
        public void setSystemTimeMs(double systemTimeMs) { this.systemTimeMs = systemTimeMs; }
        public double getSpeedupFactor() { return speedupFactor; }
        public void setSpeedupFactor(double speedupFactor) { this.speedupFactor = speedupFactor; }
    }

    public BenchmarkDto() {}

    public List<BenchmarkItem> getItems() { return items; }
    public void setItems(List<BenchmarkItem> items) { this.items = items; }
    public double getTotalManualTimeMinutes() { return totalManualTimeMinutes; }
    public void setTotalManualTimeMinutes(double totalManualTimeMinutes) { this.totalManualTimeMinutes = totalManualTimeMinutes; }
    public double getTotalSystemTimeSeconds() { return totalSystemTimeSeconds; }
    public void setTotalSystemTimeSeconds(double totalSystemTimeSeconds) { this.totalSystemTimeSeconds = totalSystemTimeSeconds; }
    public double getTimeSavedMultiplier() { return timeSavedMultiplier; }
    public void setTimeSavedMultiplier(double timeSavedMultiplier) { this.timeSavedMultiplier = timeSavedMultiplier; }
}
