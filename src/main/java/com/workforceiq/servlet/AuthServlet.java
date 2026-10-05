package com.workforceiq.servlet;

import com.google.gson.JsonObject;
import com.workforceiq.dto.ApiResponse;
import com.workforceiq.service.AuthService;
import com.workforceiq.util.JsonUtil;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

public class AuthServlet extends HttpServlet {
    private final AuthService authService = new AuthService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        String path = req.getPathInfo();

        if ("/login".equalsIgnoreCase(path) || path == null || "/".equals(path)) {
            JsonObject body = JsonUtil.getGson().fromJson(req.getReader(), JsonObject.class);
            String username = body != null && body.has("username") ? body.get("username").getAsString() : null;
            String password = body != null && body.has("password") ? body.get("password").getAsString() : null;

            Map<String, Object> authData = authService.login(username, password);
            resp.getWriter().write(JsonUtil.toJson(ApiResponse.ok(authData)));
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write(JsonUtil.toJson(ApiResponse.fail("Endpoint not found")));
        }
    }
}
