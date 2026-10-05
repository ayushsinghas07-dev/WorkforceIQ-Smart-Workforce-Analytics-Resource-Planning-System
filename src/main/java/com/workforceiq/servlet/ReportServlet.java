package com.workforceiq.servlet;

import com.workforceiq.service.ReportService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ReportServlet extends HttpServlet {
    private final ReportService reportService = new ReportService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String type = req.getParameter("type");
        if ("projects".equalsIgnoreCase(type)) {
            resp.setContentType("text/csv;charset=UTF-8");
            resp.setHeader("Content-Disposition", "attachment; filename=\"workforceiq_projects_report.csv\"");
            resp.getWriter().write(reportService.exportProjectsCsv());
        } else {
            resp.setContentType("text/csv;charset=UTF-8");
            resp.setHeader("Content-Disposition", "attachment; filename=\"workforceiq_employees_report.csv\"");
            resp.getWriter().write(reportService.exportEmployeesCsv());
        }
    }
}
