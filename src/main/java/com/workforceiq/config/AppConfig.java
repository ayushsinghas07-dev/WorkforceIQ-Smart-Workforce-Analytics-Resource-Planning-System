package com.workforceiq.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class AppConfig {
    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);
    private static final Properties properties = new Properties();

    static {
        try {
            if (Files.exists(Paths.get("config.properties"))) {
                try (InputStream is = Files.newInputStream(Paths.get("config.properties"))) {
                    properties.load(is);
                    logger.info("Loaded configuration from config.properties");
                }
            } else {
                try (InputStream is = AppConfig.class.getClassLoader().getResourceAsStream("config.properties")) {
                    if (is != null) {
                        properties.load(is);
                        logger.info("Loaded configuration from classpath resource config.properties");
                    } else {
                        logger.warn("config.properties not found! Using default fallbacks.");
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Failed to load configuration properties", e);
        }
    }

    public static String get(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public static int getInt(String key, int defaultValue) {
        String val = properties.getProperty(key);
        if (val == null) return defaultValue;
        try {
            return Integer.parseInt(val.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
