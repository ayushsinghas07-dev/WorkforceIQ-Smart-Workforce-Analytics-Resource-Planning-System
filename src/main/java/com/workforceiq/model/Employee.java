package com.workforceiq.model;

import java.util.ArrayList;
import java.util.List;

public class Employee {
    private int id;
    private String name;
    private String email;
    private int departmentId;
    private String departmentName;
    private int roleId;
    private String roleTitle;
    private String hireDate;
    private int weeklyCapacityHours = 40;
    private String status = "Active";
    private String avatarUrl;
    private String location;
    private double utilizationPct;
    private String utilizationStatus;
    private List<EmployeeSkillDetail> skills = new ArrayList<>();

    public static class EmployeeSkillDetail {
        private int skillId;
        private String skillName;
        private String category;
        private int proficiency;
        private double yearsExperience;

        public EmployeeSkillDetail() {}

        public EmployeeSkillDetail(int skillId, String skillName, String category, int proficiency, double yearsExperience) {
            this.skillId = skillId;
            this.skillName = skillName;
            this.category = category;
            this.proficiency = proficiency;
            this.yearsExperience = yearsExperience;
        }

        public int getSkillId() { return skillId; }
        public void setSkillId(int skillId) { this.skillId = skillId; }
        public String getSkillName() { return skillName; }
        public void setSkillName(String skillName) { this.skillName = skillName; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public int getProficiency() { return proficiency; }
        public void setProficiency(int proficiency) { this.proficiency = proficiency; }
        public double getYearsExperience() { return yearsExperience; }
        public void setYearsExperience(double yearsExperience) { this.yearsExperience = yearsExperience; }
    }

    public Employee() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public int getDepartmentId() { return departmentId; }
    public void setDepartmentId(int departmentId) { this.departmentId = departmentId; }
    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    public int getRoleId() { return roleId; }
    public void setRoleId(int roleId) { this.roleId = roleId; }
    public String getRoleTitle() { return roleTitle; }
    public void setRoleTitle(String roleTitle) { this.roleTitle = roleTitle; }
    public String getHireDate() { return hireDate; }
    public void setHireDate(String hireDate) { this.hireDate = hireDate; }
    public int getWeeklyCapacityHours() { return weeklyCapacityHours; }
    public void setWeeklyCapacityHours(int weeklyCapacityHours) { this.weeklyCapacityHours = weeklyCapacityHours; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public double getUtilizationPct() { return utilizationPct; }
    public void setUtilizationPct(double utilizationPct) { this.utilizationPct = utilizationPct; }
    public String getUtilizationStatus() { return utilizationStatus; }
    public void setUtilizationStatus(String utilizationStatus) { this.utilizationStatus = utilizationStatus; }
    public List<EmployeeSkillDetail> getSkills() { return skills; }
    public void setSkills(List<EmployeeSkillDetail> skills) { this.skills = skills; }
}
