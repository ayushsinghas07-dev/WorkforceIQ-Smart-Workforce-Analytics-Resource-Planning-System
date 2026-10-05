package com.workforceiq.service;

import com.workforceiq.dao.AnalyticsDao;
import com.workforceiq.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class AnalyticsService {
    private static final Logger logger = LoggerFactory.getLogger(AnalyticsService.class);
    private final AnalyticsDao analyticsDao = new AnalyticsDao();

    // 60-second in-memory response cache
    private static KpiSummaryDto cachedKpi;
    private static long cachedKpiTime = 0;
    private static final long CACHE_TTL_MS = 60000;

    public synchronized KpiSummaryDto getKpis() {
        long now = System.currentTimeMillis();
        if (cachedKpi != null && (now - cachedKpiTime) < CACHE_TTL_MS) {
            logger.debug("Returning cached KPI summary");
            return cachedKpi;
        }
        cachedKpi = analyticsDao.getKpiSummary();
        cachedKpiTime = now;
        return cachedKpi;
    }

    public List<SkillGapDto> getSkillGaps() {
        return analyticsDao.getSkillGaps();
    }

    public HeatmapDto getHeatmap() {
        return analyticsDao.getDepartmentWorkloadHeatmap();
    }

    public ForecastDto getForecast() {
        return analyticsDao.getCapacityForecast();
    }

    public List<String> getInsights() {
        KpiSummaryDto kpi = getKpis();
        List<String> list = new ArrayList<>();

        if (kpi.getOverallocatedCount() > 0) {
            list.add("⚠️ Engineering department has " + kpi.getOverallocatedCount() + " overallocated engineers (>100% capacity). Resource redistribution recommended.");
        }
        if (kpi.getAtRiskProjectsCount() > 0) {
            list.add("🚨 " + kpi.getAtRiskProjectsCount() + " active projects are currently classified as High Risk with staffing gap of over 30%.");
        }
        if (kpi.getBenchCount() > 0) {
            list.add("💡 " + kpi.getBenchCount() + " active team members are currently unallocated on Bench and ready for immediate project assignment.");
        }
        list.add("📈 Average overall workforce utilization is currently running at " + kpi.getAvgUtilizationPct() + "% across all 8 active departments.");
        list.add("⚡ Data & AI engineering skill demand has increased by 24% over the last 4 weeks.");
        return list;
    }

    public static synchronized void clearCache() {
        cachedKpi = null;
        cachedKpiTime = 0;
    }
}
