package com.workforceiq.dto;

public class SkillGapDto {
    private int skillId;
    private String skillName;
    private String category;
    private int availableHeadcount;
    private int requiredHeadcount;
    private int gapCount;
    private String status; // 'Surplus', 'Balanced', 'Deficit', 'Critical Gap'

    public SkillGapDto() {}

    public int getSkillId() { return skillId; }
    public void setSkillId(int skillId) { this.skillId = skillId; }
    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public int getAvailableHeadcount() { return availableHeadcount; }
    public void setAvailableHeadcount(int availableHeadcount) { this.availableHeadcount = availableHeadcount; }
    public int getRequiredHeadcount() { return requiredHeadcount; }
    public void setRequiredHeadcount(int requiredHeadcount) { this.requiredHeadcount = requiredHeadcount; }
    public int getGapCount() { return gapCount; }
    public void setGapCount(int gapCount) { this.gapCount = gapCount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
