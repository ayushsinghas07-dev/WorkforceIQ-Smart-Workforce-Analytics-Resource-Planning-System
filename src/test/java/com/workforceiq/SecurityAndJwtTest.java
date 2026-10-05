package com.workforceiq;

import com.workforceiq.util.JwtUtil;
import com.workforceiq.util.PasswordUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SecurityAndJwtTest {

    @Test
    @DisplayName("Test BCrypt password hashing and verification")
    public void testPasswordHashing() {
        String plain = "admin123";
        String hashed = PasswordUtil.hashPassword(plain);

        assertNotNull(hashed, "Hashed password should not be null");
        assertNotEquals(plain, hashed, "Hashed password should not equal plain text");
        assertTrue(PasswordUtil.checkPassword(plain, hashed), "Password verification should succeed");
        assertFalse(PasswordUtil.checkPassword("wrongpassword", hashed), "Wrong password verification should fail");
    }

    @Test
    @DisplayName("Test JWT token generation and claim validation")
    public void testJwtTokenCycle() {
        String token = JwtUtil.generateToken(1, "admin", "admin@workforceiq.com", "Admin");
        assertNotNull(token, "Generated JWT token should not be null");

        Claims claims = JwtUtil.validateToken(token);
        assertNotNull(claims, "Parsed claims should not be null");
        assertEquals("admin", claims.getSubject(), "Token subject should match username");
        assertEquals("Admin", claims.get("role"), "Token role claim should match");
    }
}
