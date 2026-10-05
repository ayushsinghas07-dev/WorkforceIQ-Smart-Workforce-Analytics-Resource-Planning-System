package com.workforceiq.service;

import com.workforceiq.dao.EmployeeDao;
import com.workforceiq.dao.ProjectDao;
import com.workforceiq.model.Employee;
import com.workforceiq.model.Project;

import java.util.List;

public class ReportService {
    private final EmployeeDao employeeDao = new EmployeeDao();
    private final ProjectDao projectDao = new ProjectDao();

    public String exportEmployeesCsv() {
        List<Employee> employees = employeeDao.findAll(null, null, null, null, 1, 1000);
        StringBuilder sb = new StringBuilder();
        sb.append("ID,Name,Email,Department,Role,Status,Capacity (hrs/wk),Utilization %,Utilization Status,Location\n");
        for (Employee e : employees) {
            sb.append(e.getId()).append(",")
              .append("\"").append(e.getName()).append("\",")
              .append("\"").append(e.getEmail()).append("\",")
              .append("\"").append(e.getDepartmentName()).append("\",")
              .append("\"").append(e.getRoleTitle()).append("\",")
              .append(e.getStatus()).append(",")
              .append(e.getWeeklyCapacityHours()).append(",")
              .append(e.getUtilizationPct()).append(",")
              .append(e.getUtilizationStatus()).append(",")
              .append("\"").append(e.getLocation()).append("\"\n");
        }
        return sb.toString();
    }

    public String exportProjectsCsv() {
        List<Project> projects = projectDao.findAll(null, null, null, 1, 1000);
        StringBuilder sb = new StringBuilder();
        sb.append("ID,Project Name,Client,Status,Priority,Budget ($),Required Hours,Allocated Hours,Staffing %,Risk Level\n");
        for (Project p : projects) {
            sb.append(p.getId()).append(",")
              .append("\"").append(p.getName()).append("\",")
              .append("\"").append(p.getClient()).append("\",")
              .append(p.getStatus()).append(",")
              .append(p.getPriority()).append(",")
              .append(p.getBudget()).append(",")
              .append(p.getRequiredHours()).append(",")
              .append(p.getAllocatedHours()).append(",")
              .append(p.getStaffingPct()).append(",")
              .append(p.getRiskLevel()).append("\n");
        }
        return sb.toString();
    }
}
