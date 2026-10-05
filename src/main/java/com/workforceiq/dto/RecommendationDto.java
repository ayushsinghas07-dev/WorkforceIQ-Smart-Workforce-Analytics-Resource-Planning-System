package com.workforceiq.dto;

import java.util.ArrayList;
import java.util.List;

public class RecommendationDto {
    private int candidateId;
    private String candidateName;
    private String avatarUrl;
    private String departmentName;
    private String roleTitle;
    private double matchScore; // 0 to 100%
    private int skillProficiency;
    private double currentUtilizationPct;
    private int availableHoursPerWeek;
    private List<String> matchReasons = new ArrayList<>();

    public RecommendationDto() {}

    public int getCandidateId() { return candidateId; }
    public void setCandidateId(int candidateId) { this.candidateId = candidateId; }
    public String getCandidateName() { return candidateName; }
    public void setCandidateName(String candidateName) { this.candidateName = candidateName; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    public String getRoleTitle() { return roleTitle; }
    public void setRoleTitle(String roleTitle) { this.roleTitle = roleTitle; }
    public double getMatchScore() { return matchScore; }
    public void setMatchScore(double matchScore) { this.matchScore = matchScore; }
    public int getSkillProficiency() { return skillProficiency; }
    public void setSkillProficiency(int skillProficiency) { this.skillProficiency = skillProficiency; }
    public double getCurrentUtilizationPct() { return currentUtilizationPct; }
    public void setCurrentUtilizationPct(double currentUtilizationPct) { this.currentUtilizationPct = currentUtilizationPct; }
    public int getAvailableHoursPerWeek() { return availableHoursPerWeek; }
    public void setAvailableHoursPerWeek(int availableHoursPerWeek) { this.availableHoursPerWeek = availableHoursPerWeek; }
    public List<String> getMatchReasons() { return matchReasons; }
    public void setMatchReasons(List<String> matchReasons) { this.matchReasons = matchReasons; }
}
