package com.workforceiq.dto;

public class KpiSummaryDto {
    private int totalEmployees;
    private int activeProjects;
    private double avgUtilizationPct;
    private int overallocatedCount;
    private int benchCount;
    private int atRiskProjectsCount;
    private double totalBudgetAtRisk;
    private double plannedHoursThisMonth;

    public KpiSummaryDto() {}

    public int getTotalEmployees() { return totalEmployees; }
    public void setTotalEmployees(int totalEmployees) { this.totalEmployees = totalEmployees; }
    public int getActiveProjects() { return activeProjects; }
    public void setActiveProjects(int activeProjects) { this.activeProjects = activeProjects; }
    public double getAvgUtilizationPct() { return avgUtilizationPct; }
    public void setAvgUtilizationPct(double avgUtilizationPct) { this.avgUtilizationPct = avgUtilizationPct; }
    public int getOverallocatedCount() { return overallocatedCount; }
    public void setOverallocatedCount(int overallocatedCount) { this.overallocatedCount = overallocatedCount; }
    public int getBenchCount() { return benchCount; }
    public void setBenchCount(int benchCount) { this.benchCount = benchCount; }
    public int getAtRiskProjectsCount() { return atRiskProjectsCount; }
    public void setAtRiskProjectsCount(int atRiskProjectsCount) { this.atRiskProjectsCount = atRiskProjectsCount; }
    public double getTotalBudgetAtRisk() { return totalBudgetAtRisk; }
    public void setTotalBudgetAtRisk(double totalBudgetAtRisk) { this.totalBudgetAtRisk = totalBudgetAtRisk; }
    public double getPlannedHoursThisMonth() { return plannedHoursThisMonth; }
    public void setPlannedHoursThisMonth(double plannedHoursThisMonth) { this.plannedHoursThisMonth = plannedHoursThisMonth; }
}
