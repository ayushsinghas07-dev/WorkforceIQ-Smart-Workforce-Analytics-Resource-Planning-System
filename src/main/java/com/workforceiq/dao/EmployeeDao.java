package com.workforceiq.dao;

import com.workforceiq.config.DatabaseConfig;
import com.workforceiq.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDao {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeDao.class);

    public List<Employee> findAll(String search, Integer departmentId, String status, String skill, int page, int pageSize) {
        List<Employee> employees = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT e.id, e.name, e.email, e.department_id, d.name AS department_name, e.role_id, r.title AS role_title, " +
                "e.hire_date, e.weekly_capacity_hours, e.status, e.avatar_url, e.location, " +
                "u.utilization_pct, u.utilization_status " +
                "FROM employees e " +
                "JOIN departments d ON e.department_id = d.id " +
                "JOIN roles r ON e.role_id = r.id " +
                "LEFT JOIN v_employee_utilization u ON e.id = u.employee_id " +
                "WHERE 1=1 "
        );

        List<Object> params = new ArrayList<>();
        if (search != null && !search.trim().isEmpty()) {
            sql.append("AND (e.name LIKE ? OR e.email LIKE ? OR r.title LIKE ?) ");
            String term = "%" + search.trim() + "%";
            params.add(term); params.add(term); params.add(term);
        }
        if (departmentId != null && departmentId > 0) {
            sql.append("AND e.department_id = ? ");
            params.add(departmentId);
        }
        if (status != null && !status.trim().isEmpty()) {
            sql.append("AND e.status = ? ");
            params.add(status.trim());
        }
        if (skill != null && !skill.trim().isEmpty()) {
            sql.append("AND e.id IN (SELECT es.employee_id FROM employee_skills es JOIN skills s ON es.skill_id = s.id WHERE s.name LIKE ?) ");
            params.add("%" + skill.trim() + "%");
        }

        sql.append("ORDER BY e.id ASC LIMIT ? OFFSET ?");
        params.add(pageSize);
        params.add((page - 1) * pageSize);

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Employee emp = mapRow(rs);
                    employees.add(emp);
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding employees", e);
        }
        return employees;
    }

    public Employee findById(int id) {
        String sql = "SELECT e.id, e.name, e.email, e.department_id, d.name AS department_name, e.role_id, r.title AS role_title, " +
                "e.hire_date, e.weekly_capacity_hours, e.status, e.avatar_url, e.location, " +
                "u.utilization_pct, u.utilization_status " +
                "FROM employees e " +
                "JOIN departments d ON e.department_id = d.id " +
                "JOIN roles r ON e.role_id = r.id " +
                "LEFT JOIN v_employee_utilization u ON e.id = u.employee_id " +
                "WHERE e.id = ?";
        Employee emp = null;
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    emp = mapRow(rs);
                    emp.setSkills(findEmployeeSkills(conn, id));
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding employee by id: " + id, e);
        }
        return emp;
    }

    public int count(String search, Integer departmentId, String status, String skill) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM employees e WHERE 1=1 ");
        List<Object> params = new ArrayList<>();
        if (search != null && !search.trim().isEmpty()) {
            sql.append("AND (e.name LIKE ? OR e.email LIKE ?) ");
            String term = "%" + search.trim() + "%";
            params.add(term); params.add(term);
        }
        if (departmentId != null && departmentId > 0) {
            sql.append("AND e.department_id = ? ");
            params.add(departmentId);
        }
        if (status != null && !status.trim().isEmpty()) {
            sql.append("AND e.status = ? ");
            params.add(status.trim());
        }
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            logger.error("Error counting employees", e);
        }
        return 0;
    }

    public boolean create(Employee emp) {
        String sql = "INSERT INTO employees (name, email, department_id, role_id, hire_date, weekly_capacity_hours, status, avatar_url, location) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, emp.getName());
            stmt.setString(2, emp.getEmail());
            stmt.setInt(3, emp.getDepartmentId());
            stmt.setInt(4, emp.getRoleId());
            stmt.setDate(5, Date.valueOf(emp.getHireDate()));
            stmt.setInt(6, emp.getWeeklyCapacityHours());
            stmt.setString(7, emp.getStatus());
            stmt.setString(8, emp.getAvatarUrl());
            stmt.setString(9, emp.getLocation());
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) emp.setId(keys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            logger.error("Error creating employee", e);
        }
        return false;
    }

    public boolean update(Employee emp) {
        String sql = "UPDATE employees SET name=?, email=?, department_id=?, role_id=?, weekly_capacity_hours=?, status=?, location=? WHERE id=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, emp.getName());
            stmt.setString(2, emp.getEmail());
            stmt.setInt(3, emp.getDepartmentId());
            stmt.setInt(4, emp.getRoleId());
            stmt.setInt(5, emp.getWeeklyCapacityHours());
            stmt.setString(6, emp.getStatus());
            stmt.setString(7, emp.getLocation());
            stmt.setInt(8, emp.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error updating employee: " + emp.getId(), e);
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM employees WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error deleting employee: " + id, e);
        }
        return false;
    }

    private List<Employee.EmployeeSkillDetail> findEmployeeSkills(Connection conn, int employeeId) throws SQLException {
        List<Employee.EmployeeSkillDetail> list = new ArrayList<>();
        String sql = "SELECT es.skill_id, s.name AS skill_name, s.category, es.proficiency, es.years_experience " +
                "FROM employee_skills es JOIN skills s ON es.skill_id = s.id WHERE es.employee_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, employeeId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new Employee.EmployeeSkillDetail(
                            rs.getInt("skill_id"),
                            rs.getString("skill_name"),
                            rs.getString("category"),
                            rs.getInt("proficiency"),
                            rs.getDouble("years_experience")
                    ));
                }
            }
        }
        return list;
    }

    private Employee mapRow(ResultSet rs) throws SQLException {
        Employee emp = new Employee();
        emp.setId(rs.getInt("id"));
        emp.setName(rs.getString("name"));
        emp.setEmail(rs.getString("email"));
        emp.setDepartmentId(rs.getInt("department_id"));
        emp.setDepartmentName(rs.getString("department_name"));
        emp.setRoleId(rs.getInt("role_id"));
        emp.setRoleTitle(rs.getString("role_title"));
        emp.setHireDate(rs.getString("hire_date"));
        emp.setWeeklyCapacityHours(rs.getInt("weekly_capacity_hours"));
        emp.setStatus(rs.getString("status"));
        emp.setAvatarUrl(rs.getString("avatar_url"));
        emp.setLocation(rs.getString("location"));
        emp.setUtilizationPct(rs.getDouble("utilization_pct"));
        emp.setUtilizationStatus(rs.getString("utilization_status"));
        return emp;
    }
}
