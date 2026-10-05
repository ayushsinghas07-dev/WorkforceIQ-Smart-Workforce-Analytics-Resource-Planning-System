package com.workforceiq.dao;

import com.workforceiq.config.DatabaseConfig;
import com.workforceiq.model.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectDao {
    private static final Logger logger = LoggerFactory.getLogger(ProjectDao.class);

    public List<Project> findAll(String search, String status, String priority, int page, int pageSize) {
        List<Project> projects = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT p.id, p.name, p.client, p.description, p.start_date, p.end_date, p.status, p.priority, " +
                "p.budget, p.required_hours, p.cover_image_url, " +
                "v.allocated_hours, v.logged_hours, v.allocated_team_size, v.staffing_pct, v.risk_level " +
                "FROM projects p " +
                "LEFT JOIN v_project_capacity v ON p.id = v.project_id " +
                "WHERE 1=1 "
        );

        List<Object> params = new ArrayList<>();
        if (search != null && !search.trim().isEmpty()) {
            sql.append("AND (p.name LIKE ? OR p.client LIKE ?) ");
            String term = "%" + search.trim() + "%";
            params.add(term); params.add(term);
        }
        if (status != null && !status.trim().isEmpty()) {
            sql.append("AND p.status = ? ");
            params.add(status.trim());
        }
        if (priority != null && !priority.trim().isEmpty()) {
            sql.append("AND p.priority = ? ");
            params.add(priority.trim());
        }

        sql.append("ORDER BY p.id ASC LIMIT ? OFFSET ?");
        params.add(pageSize);
        params.add((page - 1) * pageSize);

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    projects.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding projects", e);
        }
        return projects;
    }

    public Project findById(int id) {
        String sql = "SELECT p.id, p.name, p.client, p.description, p.start_date, p.end_date, p.status, p.priority, " +
                "p.budget, p.required_hours, p.cover_image_url, " +
                "v.allocated_hours, v.logged_hours, v.allocated_team_size, v.staffing_pct, v.risk_level " +
                "FROM projects p " +
                "LEFT JOIN v_project_capacity v ON p.id = v.project_id " +
                "WHERE p.id = ?";
        Project p = null;
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    p = mapRow(rs);
                    p.setRequirements(findRequirements(conn, id));
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding project by id: " + id, e);
        }
        return p;
    }

    public boolean create(Project p) {
        String sql = "INSERT INTO projects (name, client, description, start_date, end_date, status, priority, budget, required_hours, cover_image_url) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, p.getName());
            stmt.setString(2, p.getClient());
            stmt.setString(3, p.getDescription());
            stmt.setDate(4, Date.valueOf(p.getStartDate()));
            stmt.setDate(5, Date.valueOf(p.getEndDate()));
            stmt.setString(6, p.getStatus());
            stmt.setString(7, p.getPriority());
            stmt.setDouble(8, p.getBudget());
            stmt.setInt(9, p.getRequiredHours());
            stmt.setString(10, p.getCoverImageUrl());
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) p.setId(keys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            logger.error("Error creating project", e);
        }
        return false;
    }

    public boolean update(Project p) {
        String sql = "UPDATE projects SET name=?, client=?, description=?, start_date=?, end_date=?, status=?, priority=?, budget=?, required_hours=? WHERE id=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getName());
            stmt.setString(2, p.getClient());
            stmt.setString(3, p.getDescription());
            stmt.setDate(4, Date.valueOf(p.getStartDate()));
            stmt.setDate(5, Date.valueOf(p.getEndDate()));
            stmt.setString(6, p.getStatus());
            stmt.setString(7, p.getPriority());
            stmt.setDouble(8, p.getBudget());
            stmt.setInt(9, p.getRequiredHours());
            stmt.setInt(10, p.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error updating project: " + p.getId(), e);
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM projects WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error deleting project: " + id, e);
        }
        return false;
    }

    private List<Project.RequirementDetail> findRequirements(Connection conn, int projectId) throws SQLException {
        List<Project.RequirementDetail> reqs = new ArrayList<>();
        String sql = "SELECT pr.id, pr.skill_id, s.name AS skill_name, pr.min_proficiency, pr.required_hours, pr.status " +
                "FROM project_requirements pr JOIN skills s ON pr.skill_id = s.id WHERE pr.project_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, projectId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Project.RequirementDetail rd = new Project.RequirementDetail();
                    rd.setId(rs.getInt("id"));
                    rd.setSkillId(rs.getInt("skill_id"));
                    rd.setSkillName(rs.getString("skill_name"));
                    rd.setMinProficiency(rs.getInt("min_proficiency"));
                    rd.setRequiredHours(rs.getInt("required_hours"));
                    rd.setStatus(rs.getString("status"));
                    reqs.add(rd);
                }
            }
        }
        return reqs;
    }

    private Project mapRow(ResultSet rs) throws SQLException {
        Project p = new Project();
        p.setId(rs.getInt("id"));
        p.setName(rs.getString("name"));
        p.setClient(rs.getString("client"));
        p.setDescription(rs.getString("description"));
        p.setStartDate(rs.getString("start_date"));
        p.setEndDate(rs.getString("end_date"));
        p.setStatus(rs.getString("status"));
        p.setPriority(rs.getString("priority"));
        p.setBudget(rs.getDouble("budget"));
        p.setRequiredHours(rs.getInt("required_hours"));
        p.setCoverImageUrl(rs.getString("cover_image_url"));
        p.setAllocatedHours(rs.getDouble("allocated_hours"));
        p.setLoggedHours(rs.getDouble("logged_hours"));
        p.setTeamSize(rs.getInt("allocated_team_size"));
        p.setStaffingPct(rs.getDouble("staffing_pct"));
        p.setRiskLevel(rs.getString("risk_level"));
        return p;
    }
}
