package com.workforceiq.model;

public class Allocation {
    private int id;
    private int projectId;
    private String projectName;
    private int employeeId;
    private String employeeName;
    private String employeeAvatar;
    private String departmentName;
    private String startDate;
    private String endDate;
    private int allocatedHoursPerWeek;
    private String roleInProject;
    private String status;
    private String notes;

    public Allocation() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getProjectId() { return projectId; }
    public void setProjectId(int projectId) { this.projectId = projectId; }
    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }
    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }
    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
    public String getEmployeeAvatar() { return employeeAvatar; }
    public void setEmployeeAvatar(String employeeAvatar) { this.employeeAvatar = employeeAvatar; }
    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public int getAllocatedHoursPerWeek() { return allocatedHoursPerWeek; }
    public void setAllocatedHoursPerWeek(int allocatedHoursPerWeek) { this.allocatedHoursPerWeek = allocatedHoursPerWeek; }
    public String getRoleInProject() { return roleInProject; }
    public void setRoleInProject(String roleInProject) { this.roleInProject = roleInProject; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
