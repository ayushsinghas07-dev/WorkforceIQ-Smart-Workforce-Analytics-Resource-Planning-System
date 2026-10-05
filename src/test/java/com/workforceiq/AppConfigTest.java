package com.workforceiq;

import com.workforceiq.config.AppConfig;
import com.workforceiq.exception.ValidationException;
import com.workforceiq.util.ValidationUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AppConfigTest {

    @Test
    @DisplayName("Test AppConfig reads default properties fallback")
    public void testAppConfigDefaults() {
        int port = AppConfig.getInt("server.port", 8080);
        assertTrue(port > 0, "Port should be positive integer");

        String dbUser = AppConfig.get("db.user", "root");
        assertNotNull(dbUser, "Database user should not be null");
    }

    @Test
    @DisplayName("Test ValidationUtil email format verification")
    public void testValidEmail() {
        assertDoesNotThrow(() -> ValidationUtil.validateEmail("alexander.wright@workforceiq.com"));
        assertThrows(ValidationException.class, () -> ValidationUtil.validateEmail("invalid-email-address"));
    }

    @Test
    @DisplayName("Test ValidationUtil integer range constraints")
    public void testRangeValidation() {
        assertDoesNotThrow(() -> ValidationUtil.validateRange(4, 1, 5, "Proficiency"));
        assertThrows(ValidationException.class, () -> ValidationUtil.validateRange(10, 1, 5, "Proficiency"));
    }
}
