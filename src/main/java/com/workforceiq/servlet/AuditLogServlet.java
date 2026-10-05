package com.workforceiq.servlet;

import com.workforceiq.dao.AuditLogDao;
import com.workforceiq.dto.ApiResponse;
import com.workforceiq.util.JsonUtil;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AuditLogServlet extends HttpServlet {
    private final AuditLogDao auditLogDao = new AuditLogDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        resp.getWriter().write(JsonUtil.toJson(ApiResponse.ok(auditLogDao.findAll(100))));
    }
}
