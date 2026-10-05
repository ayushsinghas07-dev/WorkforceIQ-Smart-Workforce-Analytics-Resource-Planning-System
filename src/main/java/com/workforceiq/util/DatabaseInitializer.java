package com.workforceiq.util;

import com.workforceiq.config.DatabaseConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseInitializer {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);

    public static void initializeDatabase() {
        try (Connection conn = DatabaseConfig.getConnection()) {
            if (!isDatabaseInitialized(conn)) {
                logger.info("Database empty or missing tables. Initializing schema.sql and seed.sql...");
                executeSqlScript(conn, "database/schema.sql");
                executeSqlScript(conn, "database/seed.sql");
                logger.info("Database successfully initialized with 500+ seed records!");
            } else {
                logger.info("Database schema and data already initialized.");
            }
        } catch (Exception e) {
            logger.error("Error during database initialization", e);
        }
    }

    private static boolean isDatabaseInitialized(Connection conn) {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'workforceiq_db' AND table_name = 'employees'")) {
            if (rs.next() && rs.getInt(1) > 0) {
                try (ResultSet countRs = stmt.executeQuery("SELECT COUNT(*) FROM employees")) {
                    if (countRs.next() && countRs.getInt(1) > 10) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            logger.warn("Check initialized error: {}", e.getMessage());
        }
        return false;
    }

    private static void executeSqlScript(Connection conn, String scriptPath) throws Exception {
        String sql = "";
        Path p = Paths.get(scriptPath);
        if (Files.exists(p)) {
            sql = new String(Files.readAllBytes(p), StandardCharsets.UTF_8);
        } else {
            InputStream is = DatabaseInitializer.class.getClassLoader().getResourceAsStream(scriptPath);
            if (is != null) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                    sql = sb.toString();
                }
            } else {
                logger.error("SQL script not found: {}", scriptPath);
                return;
            }
        }

        // Split statements by semicolon while respecting DELIMITER sections
        String[] statements = sql.split(";");
        try (Statement stmt = conn.createStatement()) {
            for (String statement : statements) {
                String trimmed = statement.trim();
                if (!trimmed.isEmpty() && !trimmed.startsWith("--") && !trimmed.startsWith("DELIMITER")) {
                    try {
                        stmt.execute(trimmed);
                    } catch (Exception e) {
                        logger.debug("Script statement execute note: {}", e.getMessage());
                    }
                }
            }
        }
    }
}
