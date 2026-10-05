package com.workforceiq.servlet;

import com.workforceiq.dto.ApiResponse;
import com.workforceiq.service.AnalyticsService;
import com.workforceiq.util.JsonUtil;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AnalyticsServlet extends HttpServlet {
    private final AnalyticsService analyticsService = new AnalyticsService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        String path = req.getPathInfo();

        if ("/kpis".equalsIgnoreCase(path) || path == null) {
            resp.getWriter().write(JsonUtil.toJson(ApiResponse.ok(analyticsService.getKpis())));
        } else if ("/skill-gaps".equalsIgnoreCase(path)) {
            resp.getWriter().write(JsonUtil.toJson(ApiResponse.ok(analyticsService.getSkillGaps())));
        } else if ("/heatmap".equalsIgnoreCase(path)) {
            resp.getWriter().write(JsonUtil.toJson(ApiResponse.ok(analyticsService.getHeatmap())));
        } else if ("/forecast".equalsIgnoreCase(path)) {
            resp.getWriter().write(JsonUtil.toJson(ApiResponse.ok(analyticsService.getForecast())));
        } else if ("/insights".equalsIgnoreCase(path)) {
            resp.getWriter().write(JsonUtil.toJson(ApiResponse.ok(analyticsService.getInsights())));
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write(JsonUtil.toJson(ApiResponse.fail("Analytics endpoint not found")));
        }
    }
}
