package com.workforceiq.model;

public class Role {
    private int id;
    private String title;
    private int departmentId;
    private String departmentName;
    private String salaryBand;

    public Role() {}

    public Role(int id, String title, int departmentId, String salaryBand) {
        this.id = id;
        this.title = title;
        this.departmentId = departmentId;
        this.salaryBand = salaryBand;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public int getDepartmentId() { return departmentId; }
    public void setDepartmentId(int departmentId) { this.departmentId = departmentId; }
    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    public String getSalaryBand() { return salaryBand; }
    public void setSalaryBand(String salaryBand) { this.salaryBand = salaryBand; }
}
