package com.aiproxy.dto;

import java.time.LocalDate;
import java.util.List;

public record QuotaHistoryResponse(
        List<DailyUsageDto> dailyUsage
) {

    public record DailyUsageDto(
            LocalDate date,
            long tokensUsed,
            int requestCount
    ) {
    }
}

