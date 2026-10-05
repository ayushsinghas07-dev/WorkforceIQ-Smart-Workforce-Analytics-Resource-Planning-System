package com.workforceiq.dao;

import com.workforceiq.config.DatabaseConfig;
import com.workforceiq.model.AuditLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AuditLogDao {
    private static final Logger logger = LoggerFactory.getLogger(AuditLogDao.class);

    public List<AuditLog> findAll(int limit) {
        List<AuditLog> list = new ArrayList<>();
        String sql = "SELECT a.id, a.user_id, u.username, a.action, a.entity_type, a.entity_id, a.details, a.timestamp " +
                "FROM audit_log a LEFT JOIN users u ON a.user_id = u.id ORDER BY a.timestamp DESC LIMIT ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    AuditLog log = new AuditLog();
                    log.setId(rs.getInt("id"));
                    int uid = rs.getInt("user_id");
                    if (!rs.wasNull()) log.setUserId(uid);
                    log.setUsername(rs.getString("username") != null ? rs.getString("username") : "System");
                    log.setAction(rs.getString("action"));
                    log.setEntityType(rs.getString("entity_type"));
                    log.setEntityId(rs.getInt("entity_id"));
                    log.setDetails(rs.getString("details"));
                    log.setTimestamp(rs.getTimestamp("timestamp"));
                    list.add(log);
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding audit logs", e);
        }
        return list;
    }
}
