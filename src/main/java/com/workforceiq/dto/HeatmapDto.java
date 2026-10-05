package com.workforceiq.dto;

import java.util.ArrayList;
import java.util.List;

public class HeatmapDto {
    private List<String> weeks = new ArrayList<>();
    private List<DepartmentRow> departments = new ArrayList<>();

    public static class DepartmentRow {
        private int departmentId;
        private String departmentName;
        private List<Double> weeklyUtilizationPct = new ArrayList<>();

        public DepartmentRow() {}

        public int getDepartmentId() { return departmentId; }
        public void setDepartmentId(int departmentId) { this.departmentId = departmentId; }
        public String getDepartmentName() { return departmentName; }
        public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
        public List<Double> getWeeklyUtilizationPct() { return weeklyUtilizationPct; }
        public void setWeeklyUtilizationPct(List<Double> weeklyUtilizationPct) { this.weeklyUtilizationPct = weeklyUtilizationPct; }
    }

    public HeatmapDto() {}

    public List<String> getWeeks() { return weeks; }
    public void setWeeks(List<String> weeks) { this.weeks = weeks; }
    public List<DepartmentRow> getDepartments() { return departments; }
    public void setDepartments(List<DepartmentRow> departments) { this.departments = departments; }
}
