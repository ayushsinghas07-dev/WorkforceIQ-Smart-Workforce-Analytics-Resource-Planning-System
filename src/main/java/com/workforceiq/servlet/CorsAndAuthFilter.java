package com.workforceiq.servlet;

import com.workforceiq.dto.ApiResponse;
import com.workforceiq.exception.AppException;
import com.workforceiq.util.JsonUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class CorsAndAuthFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(CorsAndAuthFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        // Set global CORS headers
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            resp.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        try {
            chain.doFilter(request, response);
        } catch (Throwable t) {
            handleException(resp, t);
        }
    }

    private void handleException(HttpServletResponse resp, Throwable t) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        if (t instanceof ServletException && t.getCause() != null) {
            t = t.getCause();
        }

        if (t instanceof AppException) {
            AppException ae = (AppException) t;
            resp.setStatus(ae.getStatusCode());
            resp.getWriter().write(JsonUtil.toJson(ApiResponse.fail(ae.getMessage())));
        } else {
            logger.error("Unhandled servlet exception", t);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(JsonUtil.toJson(ApiResponse.fail("Internal server error: " + t.getMessage())));
        }
    }
}
