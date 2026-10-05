package com.workforceiq.servlet;

import com.workforceiq.dao.DepartmentDao;
import com.workforceiq.dao.SkillDao;
import com.workforceiq.dto.ApiResponse;
import com.workforceiq.util.JsonUtil;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MetaServlet extends HttpServlet {
    private final DepartmentDao departmentDao = new DepartmentDao();
    private final SkillDao skillDao = new SkillDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        Map<String, Object> meta = new HashMap<>();
        meta.put("departments", departmentDao.findAll());
        meta.put("skills", skillDao.findAll());
        resp.getWriter().write(JsonUtil.toJson(ApiResponse.ok(meta)));
    }
}
