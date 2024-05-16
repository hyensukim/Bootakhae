package com.bootakhae.productservice.global.constant;

public enum Function {
    ACTIVE_BOWEL("원활한 배변"),
    INTESTINE_HEALTH("장 건강"),
    LIVER_HEALTH("간 건강"),
    EYE_HEALTH("눈 건강"),
    BLOOD_CIRCULATION("혈행 개선"),
    JOINT_HEALTH("관절 건강"),
    IMPROVE_MEMORY("기억력 개선"),
    IMPROVE_FATIGUE("피로 개선"),
    SKIN_HEALTH("피부 건강");

    private final String description;

    Function(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }
}
