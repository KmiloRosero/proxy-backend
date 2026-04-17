package com.aiproxy.dto;

public record UpgradePlanRequest(
        String userId,
        String targetPlan
) {
}

