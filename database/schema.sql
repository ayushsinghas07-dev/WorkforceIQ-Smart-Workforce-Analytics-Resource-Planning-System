-- WorkforceIQ Database Schema (MySQL 8 / 9)
CREATE DATABASE IF NOT EXISTS workforceiq_db;
USE workforceiq_db;

-- Cleanup existing triggers, procedures, views, tables
DROP TRIGGER IF EXISTS trg_audit_allocations_insert;
DROP TRIGGER IF EXISTS trg_audit_allocations_update;
DROP TRIGGER IF EXISTS trg_audit_employees_update;
DROP PROCEDURE IF EXISTS sp_find_available_employees;
DROP PROCEDURE IF EXISTS sp_utilization_report;
DROP VIEW IF EXISTS v_employee_utilization;
DROP VIEW IF EXISTS v_project_capacity;
DROP VIEW IF EXISTS v_department_workload;

DROP TABLE IF EXISTS audit_log;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS leaves;
DROP TABLE IF EXISTS timesheets;
DROP TABLE IF EXISTS allocations;
DROP TABLE IF EXISTS project_requirements;
DROP TABLE IF EXISTS projects;
DROP TABLE IF EXISTS employee_skills;
DROP TABLE IF EXISTS employees;
DROP TABLE IF EXISTS skills;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS departments;

-- 1. Departments Table
CREATE TABLE departments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    code VARCHAR(20) NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. Roles Table
CREATE TABLE roles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    department_id INT NOT NULL,
    salary_band VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (department_id) REFERENCES departments(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. Skills Table
CREATE TABLE skills (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    category VARCHAR(50) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. Employees Table
CREATE TABLE employees (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    department_id INT NOT NULL,
    role_id INT NOT NULL,
    hire_date DATE NOT NULL,
    weekly_capacity_hours INT NOT NULL DEFAULT 40,
    status ENUM('Active', 'On Leave', 'Terminated') NOT NULL DEFAULT 'Active',
    avatar_url VARCHAR(255),
    location VARCHAR(100) DEFAULT 'Remote',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (department_id) REFERENCES departments(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5. Employee Skills Matrix (Junction Table)
CREATE TABLE employee_skills (
    employee_id INT NOT NULL,
    skill_id INT NOT NULL,
    proficiency INT NOT NULL DEFAULT 3 CHECK (proficiency BETWEEN 1 AND 5),
    years_experience DECIMAL(4,1) DEFAULT 1.0,
    PRIMARY KEY (employee_id, skill_id),
    FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE,
    FOREIGN KEY (skill_id) REFERENCES skills(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 6. Projects Table
CREATE TABLE projects (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    client VARCHAR(100) NOT NULL,
    description TEXT,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    status ENUM('Planning', 'Active', 'On Hold', 'Completed') NOT NULL DEFAULT 'Active',
    priority ENUM('Low', 'Medium', 'High', 'Critical') NOT NULL DEFAULT 'Medium',
    budget DECIMAL(12,2) DEFAULT 0.00,
    required_hours INT NOT NULL DEFAULT 160,
    cover_image_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 7. Project Requirements Table
CREATE TABLE project_requirements (
    id INT AUTO_INCREMENT PRIMARY KEY,
    project_id INT NOT NULL,
    skill_id INT NOT NULL,
    min_proficiency INT NOT NULL DEFAULT 3,
    required_hours INT NOT NULL DEFAULT 40,
    status ENUM('Open', 'Filled') NOT NULL DEFAULT 'Open',
    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
    FOREIGN KEY (skill_id) REFERENCES skills(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 8. Allocations Table
CREATE TABLE allocations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    project_id INT NOT NULL,
    employee_id INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    allocated_hours_per_week INT NOT NULL DEFAULT 40,
    role_in_project VARCHAR(100),
    status ENUM('Proposed', 'Approved', 'Rejected') NOT NULL DEFAULT 'Approved',
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
    FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 9. Timesheets Table
CREATE TABLE timesheets (
    id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id INT NOT NULL,
    project_id INT NOT NULL,
    work_date DATE NOT NULL,
    hours_logged DECIMAL(4,1) NOT NULL DEFAULT 8.0,
    task_description TEXT,
    status ENUM('Submitted', 'Approved', 'Rejected') NOT NULL DEFAULT 'Approved',
    FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE,
    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 10. Leaves Table
CREATE TABLE leaves (
    id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id INT NOT NULL,
    leave_type ENUM('Vacation', 'Sick', 'Parental', 'Personal') NOT NULL DEFAULT 'Vacation',
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    status ENUM('Pending', 'Approved', 'Rejected') NOT NULL DEFAULT 'Approved',
    reason TEXT,
    FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 11. Users Table (Role-based access)
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('Admin', 'Manager', 'Viewer') NOT NULL DEFAULT 'Viewer',
    employee_id INT NULL,
    last_login TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 12. Audit Log Table
CREATE TABLE audit_log (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NULL,
    action VARCHAR(50) NOT NULL,
    entity_type VARCHAR(50) NOT NULL,
    entity_id INT NOT NULL,
    details TEXT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Indexes for maximum query performance
CREATE INDEX idx_emp_dept ON employees(department_id);
CREATE INDEX idx_emp_role ON employees(role_id);
CREATE INDEX idx_emp_status ON employees(status);
CREATE INDEX idx_alloc_emp ON allocations(employee_id);
CREATE INDEX idx_alloc_proj ON allocations(project_id);
CREATE INDEX idx_alloc_dates ON allocations(start_date, end_date);
CREATE INDEX idx_timesheet_emp_date ON timesheets(employee_id, work_date);
CREATE INDEX idx_proj_status ON projects(status);

-- -------------------------------------------------------------
-- VIEWS
-- -------------------------------------------------------------

-- View 1: Employee Utilization View
CREATE VIEW v_employee_utilization AS
SELECT 
    e.id AS employee_id,
    e.name AS employee_name,
    e.email AS employee_email,
    e.avatar_url,
    e.location,
    d.id AS department_id,
    d.name AS department_name,
    r.title AS role_title,
    e.weekly_capacity_hours AS capacity_hours,
    COALESCE(SUM(CASE WHEN a.status = 'Approved' AND a.start_date <= CURDATE() AND a.end_date >= CURDATE() THEN a.allocated_hours_per_week ELSE 0 END), 0) AS allocated_hours,
    ROUND((COALESCE(SUM(CASE WHEN a.status = 'Approved' AND a.start_date <= CURDATE() AND a.end_date >= CURDATE() THEN a.allocated_hours_per_week ELSE 0 END), 0) / e.weekly_capacity_hours) * 100, 1) AS utilization_pct,
    CASE 
        WHEN COALESCE(SUM(CASE WHEN a.status = 'Approved' AND a.start_date <= CURDATE() AND a.end_date >= CURDATE() THEN a.allocated_hours_per_week ELSE 0 END), 0) > e.weekly_capacity_hours THEN 'Overallocated'
        WHEN COALESCE(SUM(CASE WHEN a.status = 'Approved' AND a.start_date <= CURDATE() AND a.end_date >= CURDATE() THEN a.allocated_hours_per_week ELSE 0 END), 0) >= (e.weekly_capacity_hours * 0.75) THEN 'Optimal'
        WHEN COALESCE(SUM(CASE WHEN a.status = 'Approved' AND a.start_date <= CURDATE() AND a.end_date >= CURDATE() THEN a.allocated_hours_per_week ELSE 0 END), 0) > 0 THEN 'Underutilized'
        ELSE 'Bench'
    END AS utilization_status
FROM employees e
JOIN departments d ON e.department_id = d.id
JOIN roles r ON e.role_id = r.id
LEFT JOIN allocations a ON e.id = a.employee_id
WHERE e.status = 'Active'
GROUP BY e.id, e.name, e.email, e.avatar_url, e.location, d.id, d.name, r.title, e.weekly_capacity_hours;

-- View 2: Project Capacity & Staffing View
CREATE VIEW v_project_capacity AS
SELECT 
    p.id AS project_id,
    p.name AS project_name,
    p.client,
    p.status AS project_status,
    p.priority,
    p.budget,
    p.required_hours,
    p.start_date,
    p.end_date,
    p.cover_image_url,
    COALESCE(SUM(DISTINCT a.allocated_hours_per_week * DATEDIFF(LEAST(a.end_date, p.end_date), GREATEST(a.start_date, p.start_date)) / 7), 0) AS allocated_hours,
    COALESCE(SUM(t.hours_logged), 0) AS logged_hours,
    COUNT(DISTINCT a.employee_id) AS allocated_team_size,
    ROUND(LEAST(100, (COALESCE(SUM(DISTINCT a.allocated_hours_per_week * DATEDIFF(LEAST(a.end_date, p.end_date), GREATEST(a.start_date, p.start_date)) / 7), 0) / GREATEST(1, p.required_hours)) * 100), 1) AS staffing_pct,
    CASE 
        WHEN p.status = 'Active' AND COALESCE(SUM(DISTINCT a.allocated_hours_per_week * DATEDIFF(LEAST(a.end_date, p.end_date), GREATEST(a.start_date, p.start_date)) / 7), 0) < (p.required_hours * 0.70) THEN 'High Risk'
        WHEN p.status = 'Active' AND COALESCE(SUM(DISTINCT a.allocated_hours_per_week * DATEDIFF(LEAST(a.end_date, p.end_date), GREATEST(a.start_date, p.start_date)) / 7), 0) < p.required_hours THEN 'Medium Risk'
        ELSE 'On Track'
    END AS risk_level
FROM projects p
LEFT JOIN allocations a ON p.id = a.project_id AND a.status = 'Approved'
LEFT JOIN timesheets t ON p.id = t.project_id AND t.status = 'Approved'
GROUP BY p.id, p.name, p.client, p.status, p.priority, p.budget, p.required_hours, p.start_date, p.end_date, p.cover_image_url;

-- View 3: Department Workload View
CREATE VIEW v_department_workload AS
SELECT 
    d.id AS department_id,
    d.name AS department_name,
    d.code AS department_code,
    COUNT(DISTINCT e.id) AS total_employees,
    SUM(e.weekly_capacity_hours) AS total_capacity_hours,
    SUM(u.allocated_hours) AS total_allocated_hours,
    ROUND(AVG(u.utilization_pct), 1) AS avg_utilization_pct,
    SUM(CASE WHEN u.utilization_status = 'Overallocated' THEN 1 ELSE 0 END) AS overallocated_count,
    SUM(CASE WHEN u.utilization_status = 'Optimal' THEN 1 ELSE 0 END) AS optimal_count,
    SUM(CASE WHEN u.utilization_status = 'Underutilized' THEN 1 ELSE 0 END) AS underutilized_count,
    SUM(CASE WHEN u.utilization_status = 'Bench' THEN 1 ELSE 0 END) AS bench_count
FROM departments d
LEFT JOIN employees e ON d.id = e.department_id AND e.status = 'Active'
LEFT JOIN v_employee_utilization u ON e.id = u.employee_id
GROUP BY d.id, d.name, d.code;

-- -------------------------------------------------------------
-- STORED PROCEDURES
-- -------------------------------------------------------------

DELIMITER //

CREATE PROCEDURE sp_find_available_employees(
    IN p_skill_id INT,
    IN p_min_proficiency INT,
    IN p_start_date DATE,
    IN p_end_date DATE,
    IN p_max_utilization INT
)
BEGIN
    SELECT 
        e.id AS employee_id,
        e.name AS employee_name,
        e.email,
        e.avatar_url,
        d.name AS department_name,
        r.title AS role_title,
        es.proficiency,
        es.years_experience,
        u.utilization_pct,
        (40 - u.allocated_hours) AS available_hours
    FROM employees e
    JOIN departments d ON e.department_id = d.id
    JOIN roles r ON e.role_id = r.id
    JOIN employee_skills es ON e.id = es.employee_id
    JOIN v_employee_utilization u ON e.id = u.employee_id
    WHERE e.status = 'Active'
      AND (p_skill_id IS NULL OR es.skill_id = p_skill_id)
      AND (p_min_proficiency IS NULL OR es.proficiency >= p_min_proficiency)
      AND u.utilization_pct <= p_max_utilization
      AND e.id NOT IN (
          SELECT l.employee_id 
          FROM leaves l 
          WHERE l.status = 'Approved' 
            AND l.start_date <= p_end_date 
            AND l.end_date >= p_start_date
      )
    ORDER BY es.proficiency DESC, u.utilization_pct ASC, es.years_experience DESC;
END //

CREATE PROCEDURE sp_utilization_report(
    IN p_start_date DATE,
    IN p_end_date DATE,
    IN p_department_id INT
)
BEGIN
    SELECT 
        u.employee_id,
        u.employee_name,
        u.department_name,
        u.role_title,
        u.capacity_hours,
        u.allocated_hours,
        u.utilization_pct,
        u.utilization_status,
        COUNT(DISTINCT a.project_id) AS active_projects_count
    FROM v_employee_utilization u
    LEFT JOIN allocations a ON u.employee_id = a.employee_id AND a.status = 'Approved'
    WHERE (p_department_id IS NULL OR p_department_id = 0 OR u.department_id = p_department_id)
    GROUP BY u.employee_id, u.employee_name, u.department_name, u.role_title, u.capacity_hours, u.allocated_hours, u.utilization_pct, u.utilization_status
    ORDER BY u.utilization_pct DESC;
END //

DELIMITER ;

-- -------------------------------------------------------------
-- TRIGGERS FOR AUDIT LOGGING
-- -------------------------------------------------------------

DELIMITER //

CREATE TRIGGER trg_audit_allocations_insert
AFTER INSERT ON allocations
FOR EACH ROW
BEGIN
    INSERT INTO audit_log(user_id, action, entity_type, entity_id, details)
    VALUES (1, 'CREATE', 'ALLOCATION', NEW.id, CONCAT('Allocated employee ID ', NEW.employee_id, ' to project ID ', NEW.project_id, ' (', NEW.allocated_hours_per_week, ' hrs/wk)'));
END //

CREATE TRIGGER trg_audit_allocations_update
AFTER UPDATE ON allocations
FOR EACH ROW
BEGIN
    INSERT INTO audit_log(user_id, action, entity_type, entity_id, details)
    VALUES (1, 'UPDATE', 'ALLOCATION', NEW.id, CONCAT('Updated allocation ID ', NEW.id, ' hours to ', NEW.allocated_hours_per_week, ' hrs/wk'));
END //

CREATE TRIGGER trg_audit_employees_update
AFTER UPDATE ON employees
FOR EACH ROW
BEGIN
    INSERT INTO audit_log(user_id, action, entity_type, entity_id, details)
    VALUES (1, 'UPDATE', 'EMPLOYEE', NEW.id, CONCAT('Updated employee ID ', NEW.id, ' status to ', NEW.status));
END //

DELIMITER ;
