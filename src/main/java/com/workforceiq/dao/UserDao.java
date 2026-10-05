package com.workforceiq.dao;

import com.workforceiq.config.DatabaseConfig;
import com.workforceiq.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

    public User findByEmailOrUsername(String identifier) {
        String sql = "SELECT u.id, u.username, u.email, u.password_hash, u.role, u.employee_id, e.name AS employee_name, u.last_login, u.created_at " +
                "FROM users u LEFT JOIN employees e ON u.employee_id = e.id " +
                "WHERE u.username = ? OR u.email = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, identifier);
            stmt.setString(2, identifier);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding user: " + identifier, e);
        }
        return null;
    }

    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT u.id, u.username, u.email, u.password_hash, u.role, u.employee_id, e.name AS employee_name, u.last_login, u.created_at " +
                "FROM users u LEFT JOIN employees e ON u.employee_id = e.id ORDER BY u.id ASC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                users.add(mapRow(rs));
            }
        } catch (SQLException e) {
            logger.error("Error finding all users", e);
        }
        return users;
    }

    public boolean create(User u) {
        String sql = "INSERT INTO users (username, email, password_hash, role, employee_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, u.getUsername());
            stmt.setString(2, u.getEmail());
            stmt.setString(3, u.getPasswordHash());
            stmt.setString(4, u.getRole() != null ? u.getRole() : "Viewer");
            if (u.getEmployeeId() != null && u.getEmployeeId() > 0) {
                stmt.setInt(5, u.getEmployeeId());
            } else {
                stmt.setNull(5, java.sql.Types.INTEGER);
            }
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) u.setId(keys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            logger.error("Error creating user", e);
        }
        return false;
    }

    public boolean updateLastLogin(int userId) {
        String sql = "UPDATE users SET last_login = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error updating last login for user: " + userId, e);
        }
        return false;
    }

    private User mapRow(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setUsername(rs.getString("username"));
        u.setEmail(rs.getString("email"));
        u.setPasswordHash(rs.getString("password_hash"));
        u.setRole(rs.getString("role"));
        int empId = rs.getInt("employee_id");
        if (!rs.wasNull()) u.setEmployeeId(empId);
        u.setEmployeeName(rs.getString("employee_name"));
        u.setLastLogin(rs.getTimestamp("last_login"));
        u.setCreatedAt(rs.getTimestamp("created_at"));
        return u;
    }
}
