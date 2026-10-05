package com.workforceiq.dto;

import java.util.ArrayList;
import java.util.List;

public class ForecastDto {
    private List<String> weeks = new ArrayList<>();
    private List<Integer> totalCapacityHours = new ArrayList<>();
    private List<Integer> allocatedDemandHours = new ArrayList<>();
    private List<Double> utilizationTrendPct = new ArrayList<>();

    public ForecastDto() {}

    public List<String> getWeeks() { return weeks; }
    public void setWeeks(List<String> weeks) { this.weeks = weeks; }
    public List<Integer> getTotalCapacityHours() { return totalCapacityHours; }
    public void setTotalCapacityHours(List<Integer> totalCapacityHours) { this.totalCapacityHours = totalCapacityHours; }
    public List<Integer> getAllocatedDemandHours() { return allocatedDemandHours; }
    public void setAllocatedDemandHours(List<Integer> allocatedDemandHours) { this.allocatedDemandHours = allocatedDemandHours; }
    public List<Double> getUtilizationTrendPct() { return utilizationTrendPct; }
    public void setUtilizationTrendPct(List<Double> utilizationTrendPct) { this.utilizationTrendPct = utilizationTrendPct; }
}
