package com.aiproxy.service;

import com.aiproxy.dto.GenerationRequest;
import com.aiproxy.dto.GenerationResponse;
import com.aiproxy.enums.UserPlan;
import com.aiproxy.exception.QuotaExceededException;
import com.aiproxy.model.DailyUsage;
import com.aiproxy.model.UserQuota;
import com.aiproxy.repository.DailyUsageRepository;
import com.aiproxy.repository.UserQuotaRepository;

import java.time.LocalDate;

public class QuotaProxyService implements AIGenerationService {

    private final AIGenerationService next;
    private final UserQuotaRepository userQuotaRepository;
    private final DailyUsageRepository dailyUsageRepository;

    public QuotaProxyService(AIGenerationService next, UserQuotaRepository userQuotaRepository, DailyUsageRepository dailyUsageRepository) {
        this.next = next;
        this.userQuotaRepository = userQuotaRepository;
        this.dailyUsageRepository = dailyUsageRepository;
    }

    @Override
    public GenerationResponse generate(GenerationRequest request) throws Exception {
        UserQuota quota = userQuotaRepository.findByUserId(request.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        long tokensUsed = quota.getTokensUsed() == null ? 0 : quota.getTokensUsed();
        long estimatedTokens = (request.prompt() == null ? 0 : request.prompt().length()) / 4L + 20L;
        UserPlan plan = quota.getPlan();

        if (plan != UserPlan.ENTERPRISE && tokensUsed + estimatedTokens > plan.getMonthlyTokens()) {
            throw new QuotaExceededException();
        }

        GenerationResponse response = next.generate(request);

        quota.setTokensUsed(tokensUsed + response.tokensUsed());
        userQuotaRepository.save(quota);

        LocalDate today = LocalDate.now();
        DailyUsage usage = dailyUsageRepository.findByUserIdAndDate(request.userId(), today)
                .orElseGet(() -> new DailyUsage(null, request.userId(), today, 0L, 0));

        long currentTokens = usage.getTokensUsed() == null ? 0 : usage.getTokensUsed();
        int currentRequests = usage.getRequestCount() == null ? 0 : usage.getRequestCount();
        usage.setTokensUsed(currentTokens + response.tokensUsed());
        usage.setRequestCount(currentRequests + 1);
        dailyUsageRepository.save(usage);

        return response;
    }
}

