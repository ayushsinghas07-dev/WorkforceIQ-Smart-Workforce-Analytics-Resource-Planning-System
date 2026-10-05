package com.workforceiq.model;

import java.util.ArrayList;
import java.util.List;

public class Project {
    private int id;
    private String name;
    private String client;
    private String description;
    private String startDate;
    private String endDate;
    private String status;
    private String priority;
    private double budget;
    private int requiredHours;
    private String coverImageUrl;
    private double allocatedHours;
    private double loggedHours;
    private int teamSize;
    private double staffingPct;
    private String riskLevel;
    private List<RequirementDetail> requirements = new ArrayList<>();

    public static class RequirementDetail {
        private int id;
        private int skillId;
        private String skillName;
        private int minProficiency;
        private int requiredHours;
        private String status;

        public RequirementDetail() {}

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public int getSkillId() { return skillId; }
        public void setSkillId(int skillId) { this.skillId = skillId; }
        public String getSkillName() { return skillName; }
        public void setSkillName(String skillName) { this.skillName = skillName; }
        public int getMinProficiency() { return minProficiency; }
        public void setMinProficiency(int minProficiency) { this.minProficiency = minProficiency; }
        public int getRequiredHours() { return requiredHours; }
        public void setRequiredHours(int requiredHours) { this.requiredHours = requiredHours; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    public Project() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getClient() { return client; }
    public void setClient(String client) { this.client = client; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public double getBudget() { return budget; }
    public void setBudget(double budget) { this.budget = budget; }
    public int getRequiredHours() { return requiredHours; }
    public void setRequiredHours(int requiredHours) { this.requiredHours = requiredHours; }
    public String getCoverImageUrl() { return coverImageUrl; }
    public void setCoverImageUrl(String coverImageUrl) { this.coverImageUrl = coverImageUrl; }
    public double getAllocatedHours() { return allocatedHours; }
    public void setAllocatedHours(double allocatedHours) { this.allocatedHours = allocatedHours; }
    public double getLoggedHours() { return loggedHours; }
    public void setLoggedHours(double loggedHours) { this.loggedHours = loggedHours; }
    public int getTeamSize() { return teamSize; }
    public void setTeamSize(int teamSize) { this.teamSize = teamSize; }
    public double getStaffingPct() { return staffingPct; }
    public void setStaffingPct(double staffingPct) { this.staffingPct = staffingPct; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public List<RequirementDetail> getRequirements() { return requirements; }
    public void setRequirements(List<RequirementDetail> requirements) { this.requirements = requirements; }
}
