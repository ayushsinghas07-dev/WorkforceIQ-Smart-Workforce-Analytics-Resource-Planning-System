package com.workforceiq.service;

import com.workforceiq.config.DatabaseConfig;
import com.workforceiq.dto.RecommendationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecommendationEngine {
    private static final Logger logger = LoggerFactory.getLogger(RecommendationEngine.class);

    public List<RecommendationDto> recommendCandidates(int skillId, int minProficiency, String startDate, String endDate, int requiredHoursPerWeek) {
        List<RecommendationDto> list = new ArrayList<>();
        String sql = "SELECT e.id AS candidate_id, e.name AS candidate_name, e.avatar_url, d.name AS department_name, r.title AS role_title, " +
                "es.proficiency, es.years_experience, u.utilization_pct, u.allocated_hours " +
                "FROM employees e " +
                "JOIN departments d ON e.department_id = d.id " +
                "JOIN roles r ON e.role_id = r.id " +
                "JOIN employee_skills es ON e.id = es.employee_id " +
                "JOIN v_employee_utilization u ON e.id = u.employee_id " +
                "WHERE e.status = 'Active' AND es.skill_id = ? AND es.proficiency >= ? " +
                "AND e.id NOT IN (" +
                "   SELECT l.employee_id FROM leaves l WHERE l.status = 'Approved' AND l.start_date <= ? AND l.end_date >= ?" +
                ")";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, skillId);
            stmt.setInt(2, minProficiency);
            stmt.setString(3, endDate != null ? endDate : "2026-12-31");
            stmt.setString(4, startDate != null ? startDate : "2026-08-01");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    RecommendationDto rec = new RecommendationDto();
                    rec.setCandidateId(rs.getInt("candidate_id"));
                    rec.setCandidateName(rs.getString("candidate_name"));
                    rec.setAvatarUrl(rs.getString("avatar_url"));
                    rec.setDepartmentName(rs.getString("department_name"));
                    rec.setRoleTitle(rs.getString("role_title"));
                    int prof = rs.getInt("proficiency");
                    rec.setSkillProficiency(prof);
                    double currentUtil = rs.getDouble("utilization_pct");
                    rec.setCurrentUtilizationPct(currentUtil);
                    double allocHours = rs.getDouble("allocated_hours");
                    int availHours = (int) Math.max(0, 40 - allocHours);
                    rec.setAvailableHoursPerWeek(availHours);

                    // Multi-Factor Recommendation Scoring Algorithm:
                    // 1. Skill Match Weight (40 points)
                    double skillScore = 40.0; // matched required skill

                    // 2. Proficiency Weight (20 points)
                    double profScore = Math.min(20.0, (prof / 5.0) * 20.0);

                    // 3. Availability Fit (20 points)
                    double availScore = 0.0;
                    if (availHours >= requiredHoursPerWeek) availScore = 20.0;
                    else if (availHours > 0) availScore = (availHours / (double) requiredHoursPerWeek) * 20.0;

                    // 4. Utilization Balance (20 points) - favors bench/underutilized over overloaded
                    double utilFitScore = 0.0;
                    if (currentUtil == 0) utilFitScore = 20.0; // Bench is ideal candidate
                    else if (currentUtil <= 75.0) utilFitScore = 18.0;
                    else if (currentUtil <= 90.0) utilFitScore = 12.0;
                    else utilFitScore = 4.0;

                    double totalScore = Math.round((skillScore + profScore + availScore + utilFitScore) * 10.0) / 10.0;
                    rec.setMatchScore(totalScore);

                    // Explanatory reasoning tags
                    rec.getMatchReasons().add("Proficiency level " + prof + "/5");
                    if (currentUtil == 0) rec.getMatchReasons().add("Currently on Bench (100% available)");
                    else if (availHours >= requiredHoursPerWeek) rec.getMatchReasons().add("Sufficient free capacity (" + availHours + " hrs/wk)");
                    else rec.getMatchReasons().add("Partial availability (" + availHours + " hrs/wk)");

                    if (prof >= 4) rec.getMatchReasons().add("Senior subject matter expert");
                    rec.getMatchReasons().add("Zero schedule leave conflicts");

                    list.add(rec);
                }
            }
        } catch (SQLException e) {
            logger.error("Error computing candidate recommendations", e);
        }

        // Sort descending by matchScore
        list.sort((a, b) -> Double.compare(b.getMatchScore(), a.getMatchScore()));
        return list;
    }
}
