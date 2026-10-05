package com.workforceiq.servlet;

import com.workforceiq.dao.ProjectDao;
import com.workforceiq.dto.ApiResponse;
import com.workforceiq.model.Project;
import com.workforceiq.util.JsonUtil;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectServlet extends HttpServlet {
    private final ProjectDao projectDao = new ProjectDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        String pathInfo = req.getPathInfo();

        if (pathInfo != null && pathInfo.length() > 1 && !"/".equals(pathInfo)) {
            try {
                int id = Integer.parseInt(pathInfo.substring(1));
                Project p = projectDao.findById(id);
                if (p != null) {
                    resp.getWriter().write(JsonUtil.toJson(ApiResponse.ok(p)));
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().write(JsonUtil.toJson(ApiResponse.fail("Project not found")));
                }
                return;
            } catch (NumberFormatException e) {
                // fall through
            }
        }

        String search = req.getParameter("search");
        String status = req.getParameter("status");
        String priority = req.getParameter("priority");
        int page = req.getParameter("page") != null ? Integer.parseInt(req.getParameter("page")) : 1;
        int pageSize = req.getParameter("pageSize") != null ? Integer.parseInt(req.getParameter("pageSize")) : 50;

        List<Project> list = projectDao.findAll(search, status, priority, page, pageSize);

        Map<String, Object> result = new HashMap<>();
        result.put("items", list);
        result.put("total", list.size());
        result.put("page", page);
        result.put("pageSize", pageSize);

        resp.getWriter().write(JsonUtil.toJson(ApiResponse.ok(result)));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        Project p = JsonUtil.getGson().fromJson(req.getReader(), Project.class);
        if (p.getCoverImageUrl() == null || p.getCoverImageUrl().isEmpty()) {
            p.setCoverImageUrl("https://images.unsplash.com/photo-1551288049-bebda4e38f71?w=800");
        }
        if (projectDao.create(p)) {
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(JsonUtil.toJson(ApiResponse.ok(p)));
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(JsonUtil.toJson(ApiResponse.fail("Failed to create project")));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && pathInfo.length() > 1) {
            int id = Integer.parseInt(pathInfo.substring(1));
            Project p = JsonUtil.getGson().fromJson(req.getReader(), Project.class);
            p.setId(id);
            if (projectDao.update(p)) {
                resp.getWriter().write(JsonUtil.toJson(ApiResponse.ok(p)));
                return;
            }
        }
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        resp.getWriter().write(JsonUtil.toJson(ApiResponse.fail("Failed to update project")));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && pathInfo.length() > 1) {
            int id = Integer.parseInt(pathInfo.substring(1));
            if (projectDao.delete(id)) {
                resp.getWriter().write(JsonUtil.toJson(ApiResponse.ok("Project deleted")));
                return;
            }
        }
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        resp.getWriter().write(JsonUtil.toJson(ApiResponse.fail("Failed to delete project")));
    }
}
