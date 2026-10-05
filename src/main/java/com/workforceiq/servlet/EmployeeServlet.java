package com.workforceiq.servlet;

import com.workforceiq.dao.EmployeeDao;
import com.workforceiq.dto.ApiResponse;
import com.workforceiq.model.Employee;
import com.workforceiq.util.JsonUtil;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeServlet extends HttpServlet {
    private final EmployeeDao employeeDao = new EmployeeDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        String pathInfo = req.getPathInfo();

        if (pathInfo != null && pathInfo.length() > 1 && !"/".equals(pathInfo)) {
            try {
                int id = Integer.parseInt(pathInfo.substring(1));
                Employee emp = employeeDao.findById(id);
                if (emp != null) {
                    resp.getWriter().write(JsonUtil.toJson(ApiResponse.ok(emp)));
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().write(JsonUtil.toJson(ApiResponse.fail("Employee not found")));
                }
                return;
            } catch (NumberFormatException e) {
                // fall through
            }
        }

        String search = req.getParameter("search");
        String deptStr = req.getParameter("departmentId");
        Integer deptId = (deptStr != null && !deptStr.isEmpty()) ? Integer.parseInt(deptStr) : null;
        String status = req.getParameter("status");
        String skill = req.getParameter("skill");
        int page = req.getParameter("page") != null ? Integer.parseInt(req.getParameter("page")) : 1;
        int pageSize = req.getParameter("pageSize") != null ? Integer.parseInt(req.getParameter("pageSize")) : 50;

        List<Employee> list = employeeDao.findAll(search, deptId, status, skill, page, pageSize);
        int total = employeeDao.count(search, deptId, status, skill);

        Map<String, Object> result = new HashMap<>();
        result.put("items", list);
        result.put("total", total);
        result.put("page", page);
        result.put("pageSize", pageSize);

        resp.getWriter().write(JsonUtil.toJson(ApiResponse.ok(result)));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        Employee emp = JsonUtil.getGson().fromJson(req.getReader(), Employee.class);
        if (emp.getAvatarUrl() == null || emp.getAvatarUrl().isEmpty()) {
            emp.setAvatarUrl("https://api.dicebear.com/7.x/notionists/svg?seed=" + emp.getName().replaceAll(" ", ""));
        }
        if (employeeDao.create(emp)) {
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(JsonUtil.toJson(ApiResponse.ok(emp)));
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(JsonUtil.toJson(ApiResponse.fail("Failed to create employee")));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && pathInfo.length() > 1) {
            int id = Integer.parseInt(pathInfo.substring(1));
            Employee emp = JsonUtil.getGson().fromJson(req.getReader(), Employee.class);
            emp.setId(id);
            if (employeeDao.update(emp)) {
                resp.getWriter().write(JsonUtil.toJson(ApiResponse.ok(emp)));
                return;
            }
        }
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        resp.getWriter().write(JsonUtil.toJson(ApiResponse.fail("Failed to update employee")));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && pathInfo.length() > 1) {
            int id = Integer.parseInt(pathInfo.substring(1));
            if (employeeDao.delete(id)) {
                resp.getWriter().write(JsonUtil.toJson(ApiResponse.ok("Employee deleted")));
                return;
            }
        }
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        resp.getWriter().write(JsonUtil.toJson(ApiResponse.fail("Failed to delete employee")));
    }
}
