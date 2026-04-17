package com.aiproxy.dto;

import java.time.LocalDate;

public record QuotaStatusResponse(
        long tokensUsed,
        long tokensRemaining,
        long monthlyLimit,
        double usagePercentage,
        LocalDate resetDate,
        String plan,
        int requestsThisMinute,
        int requestsLimit
) {
}

