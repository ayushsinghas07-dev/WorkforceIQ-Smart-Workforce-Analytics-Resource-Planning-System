package com.workforceiq.dao;

import com.workforceiq.config.DatabaseConfig;
import com.workforceiq.exception.ConflictException;
import com.workforceiq.model.Allocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AllocationDao {
    private static final Logger logger = LoggerFactory.getLogger(AllocationDao.class);

    public List<Allocation> findAll(Integer projectId, Integer employeeId, int page, int pageSize) {
        List<Allocation> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT a.id, a.project_id, p.name AS project_name, a.employee_id, e.name AS employee_name, e.avatar_url, " +
                "d.name AS department_name, a.start_date, a.end_date, a.allocated_hours_per_week, a.role_in_project, " +
                "a.status, a.notes " +
                "FROM allocations a " +
                "JOIN projects p ON a.project_id = p.id " +
                "JOIN employees e ON a.employee_id = e.id " +
                "JOIN departments d ON e.department_id = d.id " +
                "WHERE 1=1 "
        );

        List<Object> params = new ArrayList<>();
        if (projectId != null && projectId > 0) {
            sql.append("AND a.project_id = ? ");
            params.add(projectId);
        }
        if (employeeId != null && employeeId > 0) {
            sql.append("AND a.employee_id = ? ");
            params.add(employeeId);
        }

        sql.append("ORDER BY a.start_date DESC LIMIT ? OFFSET ?");
        params.add(pageSize);
        params.add((page - 1) * pageSize);

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding allocations", e);
        }
        return list;
    }

    public boolean createTransactional(Allocation alloc) {
        try (Connection conn = DatabaseConfig.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // Check capacity overlap rule
                int existingHours = getEmployeeAllocatedHoursInRange(conn, alloc.getEmployeeId(), alloc.getStartDate(), alloc.getEndDate());
                if (existingHours + alloc.getAllocatedHoursPerWeek() > 60) {
                    throw new ConflictException("Allocation rejected: Employee exceeds max weekly capacity boundary (total " + (existingHours + alloc.getAllocatedHoursPerWeek()) + " hrs/wk).");
                }

                // Check leave overlap rule
                if (hasLeaveConflict(conn, alloc.getEmployeeId(), alloc.getStartDate(), alloc.getEndDate())) {
                    throw new ConflictException("Allocation rejected: Target employee has an approved leave during this date range.");
                }

                String sql = "INSERT INTO allocations (project_id, employee_id, start_date, end_date, allocated_hours_per_week, role_in_project, status, notes) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setInt(1, alloc.getProjectId());
                    stmt.setInt(2, alloc.getEmployeeId());
                    stmt.setDate(3, Date.valueOf(alloc.getStartDate()));
                    stmt.setDate(4, Date.valueOf(alloc.getEndDate()));
                    stmt.setInt(5, alloc.getAllocatedHoursPerWeek());
                    stmt.setString(6, alloc.getRoleInProject());
                    stmt.setString(7, alloc.getStatus() != null ? alloc.getStatus() : "Approved");
                    stmt.setString(8, alloc.getNotes());
                    stmt.executeUpdate();
                    try (ResultSet keys = stmt.getGeneratedKeys()) {
                        if (keys.next()) alloc.setId(keys.getInt(1));
                    }
                }
                conn.commit();
                return true;
            } catch (Exception e) {
                conn.rollback();
                if (e instanceof ConflictException) throw (ConflictException) e;
                logger.error("Transaction failed creating allocation", e);
                return false;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            logger.error("Database error creating allocation", e);
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM allocations WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error deleting allocation: " + id, e);
        }
        return false;
    }

    private int getEmployeeAllocatedHoursInRange(Connection conn, int employeeId, String startDate, String endDate) throws SQLException {
        String sql = "SELECT COALESCE(SUM(allocated_hours_per_week), 0) FROM allocations WHERE employee_id = ? AND status = 'Approved' AND start_date <= ? AND end_date >= ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, employeeId);
            stmt.setDate(2, Date.valueOf(endDate));
            stmt.setDate(3, Date.valueOf(startDate));
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }

    private boolean hasLeaveConflict(Connection conn, int employeeId, String startDate, String endDate) throws SQLException {
        String sql = "SELECT COUNT(*) FROM leaves WHERE employee_id = ? AND status = 'Approved' AND start_date <= ? AND end_date >= ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, employeeId);
            stmt.setDate(2, Date.valueOf(endDate));
            stmt.setDate(3, Date.valueOf(startDate));
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    private Allocation mapRow(ResultSet rs) throws SQLException {
        Allocation a = new Allocation();
        a.setId(rs.getInt("id"));
        a.setProjectId(rs.getInt("project_id"));
        a.setProjectName(rs.getString("project_name"));
        a.setEmployeeId(rs.getInt("employee_id"));
        a.setEmployeeName(rs.getString("employee_name"));
        a.setEmployeeAvatar(rs.getString("avatar_url"));
        a.setDepartmentName(rs.getString("department_name"));
        a.setStartDate(rs.getString("start_date"));
        a.setEndDate(rs.getString("end_date"));
        a.setAllocatedHoursPerWeek(rs.getInt("allocated_hours_per_week"));
        a.setRoleInProject(rs.getString("role_in_project"));
        a.setStatus(rs.getString("status"));
        a.setNotes(rs.getString("notes"));
        return a;
    }
}
