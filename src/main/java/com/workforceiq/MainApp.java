package com.workforceiq;

import com.workforceiq.config.AppConfig;
import com.workforceiq.config.DatabaseConfig;
import com.workforceiq.servlet.*;
import com.workforceiq.util.DatabaseInitializer;
import jakarta.servlet.DispatcherType;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.EnumSet;

public class MainApp {
    private static final Logger logger = LoggerFactory.getLogger(MainApp.class);

    public static void main(String[] args) {
        logger.info("Starting WorkforceIQ Server...");

        // 1. Initialize Database Schema & Seed Records
        try {
            DatabaseInitializer.initializeDatabase();
        } catch (Exception e) {
            logger.warn("Database initialization check completed: {}", e.getMessage());
        }

        // 2. Configure Embedded Jetty Server
        int port = AppConfig.getInt("server.port", 8080);
        Server server = new Server(port);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        // 3. Register CORS & Error Interceptor Filter
        FilterHolder filterHolder = new FilterHolder(new CorsAndAuthFilter());
        context.addFilter(filterHolder, "/*", EnumSet.of(DispatcherType.REQUEST));

        // 4. Register REST API Servlets
        context.addServlet(new ServletHolder(new AuthServlet()), "/api/auth/*");
        context.addServlet(new ServletHolder(new EmployeeServlet()), "/api/employees/*");
        context.addServlet(new ServletHolder(new ProjectServlet()), "/api/projects/*");
        context.addServlet(new ServletHolder(new AllocationServlet()), "/api/allocations/*");
        context.addServlet(new ServletHolder(new AnalyticsServlet()), "/api/analytics/*");
        context.addServlet(new ServletHolder(new ReportServlet()), "/api/reports/*");
        context.addServlet(new ServletHolder(new MetaServlet()), "/api/meta/*");
        context.addServlet(new ServletHolder(new UserServlet()), "/api/users/*");
        context.addServlet(new ServletHolder(new AuditLogServlet()), "/api/audit-log/*");
        context.addServlet(new ServletHolder(new BenchmarkServlet()), "/api/benchmark/*");

        // 5. Configure Static Resource Handler (Front Landing Page & App SPA)
        try {
            if (Files.exists(Paths.get("src/main/resources/static"))) {
                context.setBaseResource(Resource.newResource(Paths.get("src/main/resources/static").toUri()));
                logger.info("Serving static web files from filesystem path: src/main/resources/static");
            } else {
                URL resourceUrl = MainApp.class.getClassLoader().getResource("static");
                if (resourceUrl != null) {
                    context.setBaseResource(Resource.newResource(resourceUrl.toURI()));
                    logger.info("Serving static web files from classpath resource: static");
                }
            }
        } catch (Exception e) {
            logger.error("Failed to set static resources base", e);
        }

        ServletHolder defaultServlet = new ServletHolder("default", DefaultServlet.class);
        defaultServlet.setInitParameter("dirAllowed", "false");
        defaultServlet.setInitParameter("welcomeServlets", "true");
        defaultServlet.setInitParameter("redirectWelcome", "true");
        context.addServlet(defaultServlet, "/");

        // Graceful shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutting down WorkforceIQ server...");
            try {
                server.stop();
                DatabaseConfig.closePool();
            } catch (Exception e) {
                logger.error("Error during server shutdown", e);
            }
        }));

        // Start server
        try {
            server.start();
            logger.info("=================================================================");
            logger.info("🚀 WorkforceIQ System successfully started on http://localhost:{}", port);
            logger.info("=================================================================");
            server.join();
        } catch (Exception e) {
            logger.error("Fatal error starting Jetty server", e);
            System.exit(1);
        }
    }
}
