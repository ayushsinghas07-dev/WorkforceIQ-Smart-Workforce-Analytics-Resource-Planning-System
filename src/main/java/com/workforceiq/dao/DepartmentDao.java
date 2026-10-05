package com.workforceiq.dao;

import com.workforceiq.config.DatabaseConfig;
import com.workforceiq.model.Department;
import com.workforceiq.model.Skill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDao {
    private static final Logger logger = LoggerFactory.getLogger(DepartmentDao.class);

    public List<Department> findAll() {
        List<Department> list = new ArrayList<>();
        String sql = "SELECT id, name, code, description FROM departments ORDER BY name ASC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Department(rs.getInt("id"), rs.getString("name"), rs.getString("code"), rs.getString("description")));
            }
        } catch (SQLException e) {
            logger.error("Error finding departments", e);
        }
        return list;
    }
}
