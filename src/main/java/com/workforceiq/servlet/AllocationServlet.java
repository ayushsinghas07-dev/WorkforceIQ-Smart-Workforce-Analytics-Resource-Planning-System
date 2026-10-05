package com.workforceiq.servlet;

import com.workforceiq.dao.AllocationDao;
import com.workforceiq.dto.ApiResponse;
import com.workforceiq.dto.RecommendationDto;
import com.workforceiq.model.Allocation;
import com.workforceiq.service.AnalyticsService;
import com.workforceiq.service.RecommendationEngine;
import com.workforceiq.util.JsonUtil;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class AllocationServlet extends HttpServlet {
    private final AllocationDao allocationDao = new AllocationDao();
    private final RecommendationEngine recommendationEngine = new RecommendationEngine();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        String pathInfo = req.getPathInfo();

        if ("/suggest".equalsIgnoreCase(pathInfo)) {
            String skillStr = req.getParameter("skillId");
            int skillId = skillStr != null ? Integer.parseInt(skillStr) : 1;
            int minProf = req.getParameter("minProficiency") != null ? Integer.parseInt(req.getParameter("minProficiency")) : 3;
            String startDate = req.getParameter("startDate");
            String endDate = req.getParameter("endDate");
            int requiredHours = req.getParameter("requiredHours") != null ? Integer.parseInt(req.getParameter("requiredHours")) : 40;

            List<RecommendationDto> suggestions = recommendationEngine.recommendCandidates(skillId, minProf, startDate, endDate, requiredHours);
            resp.getWriter().write(JsonUtil.toJson(ApiResponse.ok(suggestions)));
            return;
        }

        String projStr = req.getParameter("projectId");
        Integer projId = (projStr != null && !projStr.isEmpty()) ? Integer.parseInt(projStr) : null;
        String empStr = req.getParameter("employeeId");
        Integer empId = (empStr != null && !empStr.isEmpty()) ? Integer.parseInt(empStr) : null;

        List<Allocation> list = allocationDao.findAll(projId, empId, 1, 500);
        resp.getWriter().write(JsonUtil.toJson(ApiResponse.ok(list)));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        Allocation alloc = JsonUtil.getGson().fromJson(req.getReader(), Allocation.class);
        if (allocationDao.createTransactional(alloc)) {
            AnalyticsService.clearCache();
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(JsonUtil.toJson(ApiResponse.ok(alloc)));
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(JsonUtil.toJson(ApiResponse.fail("Failed to create allocation")));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && pathInfo.length() > 1) {
            int id = Integer.parseInt(pathInfo.substring(1));
            if (allocationDao.delete(id)) {
                AnalyticsService.clearCache();
                resp.getWriter().write(JsonUtil.toJson(ApiResponse.ok("Allocation deleted")));
                return;
            }
        }
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        resp.getWriter().write(JsonUtil.toJson(ApiResponse.fail("Failed to delete allocation")));
    }
}
