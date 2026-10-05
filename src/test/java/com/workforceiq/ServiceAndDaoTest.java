package com.workforceiq;

import com.workforceiq.dto.BenchmarkDto;
import com.workforceiq.dto.KpiSummaryDto;
import com.workforceiq.dto.RecommendationDto;
import com.workforceiq.service.AnalyticsService;
import com.workforceiq.service.BenchmarkService;
import com.workforceiq.service.RecommendationEngine;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceAndDaoTest {

    @Test
    @DisplayName("Test BenchmarkService side-by-side timing calculations")
    public void testBenchmarkService() {
        BenchmarkService service = new BenchmarkService();
        BenchmarkDto dto = service.runBenchmark();

        assertNotNull(dto, "Benchmark DTO should not be null");
        assertFalse(dto.getItems().isEmpty(), "Benchmark items list should not be empty");
        assertTrue(dto.getTotalManualTimeMinutes() > 100, "Manual time should be over 100 minutes");
        assertTrue(dto.getTimeSavedMultiplier() > 100, "Speedup factor should be high");
    }

    @Test
    @DisplayName("Test RecommendationEngine candidate match scoring")
    public void testRecommendationEngineScoring() {
        RecommendationEngine engine = new RecommendationEngine();
        List<RecommendationDto> recs = engine.recommendCandidates(1, 3, "2026-08-01", "2026-12-31", 40);

        assertNotNull(recs, "Recommendation list should not be null");
        if (!recs.isEmpty()) {
            RecommendationDto top = recs.get(0);
            assertTrue(top.getMatchScore() > 0, "Top candidate match score should be positive");
            assertNotNull(top.getCandidateName(), "Candidate name should not be null");
        }
    }

    @Test
    @DisplayName("Test AnalyticsService KPI response and caching")
    public void testAnalyticsServiceKpi() {
        AnalyticsService service = new AnalyticsService();
        AnalyticsService.clearCache();

        KpiSummaryDto kpi1 = service.getKpis();
        assertNotNull(kpi1, "KPI summary should not be null");

        KpiSummaryDto kpi2 = service.getKpis();
        assertSame(kpi1, kpi2, "Second call within 60s should return cached instance");
    }

    @Test
    @DisplayName("Test AnalyticsService insights generation")
    public void testAnalyticsInsights() {
        AnalyticsService service = new AnalyticsService();
        List<String> insights = service.getInsights();
        assertNotNull(insights, "Insights list should not be null");
        assertFalse(insights.isEmpty(), "Insights list should contain natural language cards");
    }
}
