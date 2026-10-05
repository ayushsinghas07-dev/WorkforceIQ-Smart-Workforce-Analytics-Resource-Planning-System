package com.workforceiq.servlet;

import com.workforceiq.dao.AuditLogDao;
import com.workforceiq.dao.UserDao;
import com.workforceiq.dto.ApiResponse;
import com.workforceiq.util.JsonUtil;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class UserServlet extends HttpServlet {
    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        resp.getWriter().write(JsonUtil.toJson(ApiResponse.ok(userDao.findAll())));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        com.workforceiq.model.User user = JsonUtil.getGson().fromJson(req.getReader(), com.workforceiq.model.User.class);
        if (user.getPasswordHash() == null || user.getPasswordHash().isEmpty()) {
            user.setPasswordHash(com.workforceiq.util.PasswordUtil.hashPassword("password123"));
        } else if (!user.getPasswordHash().startsWith("$2a$")) {
            user.setPasswordHash(com.workforceiq.util.PasswordUtil.hashPassword(user.getPasswordHash()));
        }
        if (userDao.create(user)) {
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(JsonUtil.toJson(ApiResponse.ok(user)));
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(JsonUtil.toJson(ApiResponse.fail("Failed to create user account")));
        }
    }
}
