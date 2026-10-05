package com.workforceiq.dao;

import com.workforceiq.config.DatabaseConfig;
import com.workforceiq.model.Skill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SkillDao {
    private static final Logger logger = LoggerFactory.getLogger(SkillDao.class);

    public List<Skill> findAll() {
        List<Skill> list = new ArrayList<>();
        String sql = "SELECT id, name, category, description FROM skills ORDER BY name ASC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Skill(rs.getInt("id"), rs.getString("name"), rs.getString("category"), rs.getString("description")));
            }
        } catch (SQLException e) {
            logger.error("Error finding skills", e);
        }
        return list;
    }
}
