package com.workforceiq.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConfig {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);
    private static HikariDataSource dataSource;

    public static synchronized DataSource getDataSource() {
        if (dataSource == null) {
            String jdbcUrl = AppConfig.get("db.url", "jdbc:mysql://localhost:3306/workforceiq_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true");
            String user = AppConfig.get("db.user", "root");
            String password = AppConfig.get("db.password", "12345");
            int maxSize = AppConfig.getInt("db.pool.maxSize", 15);
            int minIdle = AppConfig.getInt("db.pool.minIdle", 5);

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(jdbcUrl);
            config.setUsername(user);
            config.setPassword(password);
            config.setMaximumPoolSize(maxSize);
            config.setMinimumIdle(minIdle);
            config.setDriverClassName("com.mysql.cj.jdbc.Driver");
            config.setConnectionTimeout(10000);
            config.setIdleTimeout(300000);
            config.setMaxLifetime(600000);

            try {
                dataSource = new HikariDataSource(config);
                logger.info("HikariCP DataSource initialized successfully for URL: {}", jdbcUrl);
            } catch (Exception e) {
                logger.error("Failed to initialize HikariCP DataSource", e);
                throw new RuntimeException("Database Connection Error: " + e.getMessage(), e);
            }
        }
        return dataSource;
    }

    public static Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
    }

    public static synchronized void closePool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            logger.info("HikariCP DataSource closed.");
        }
    }
}
