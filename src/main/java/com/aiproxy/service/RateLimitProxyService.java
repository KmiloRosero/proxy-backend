package com.aiproxy.service;

import com.aiproxy.dto.GenerationRequest;
import com.aiproxy.dto.GenerationResponse;
import com.aiproxy.exception.RateLimitExceededException;
import com.aiproxy.model.UserQuota;
import com.aiproxy.repository.UserQuotaRepository;

import java.time.Duration;
import java.time.LocalDateTime;

public class RateLimitProxyService implements AIGenerationService {

    private final AIGenerationService next;
    private final UserQuotaRepository userQuotaRepository;

    public RateLimitProxyService(AIGenerationService next, UserQuotaRepository userQuotaRepository) {
        this.next = next;
        this.userQuotaRepository = userQuotaRepository;
    }

    @Override
    public GenerationResponse generate(GenerationRequest request) throws Exception {
        UserQuota quota = userQuotaRepository.findByUserId(request.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastReset = quota.getLastMinuteReset();

        if (lastReset == null || Duration.between(lastReset, now).toSeconds() >= 60) {
            quota.setRequestsThisMinute(0);
            quota.setLastMinuteReset(now);
            userQuotaRepository.save(quota);
        }

        int limit = quota.getPlan().getRequestsPerMinute();
        int current = quota.getRequestsThisMinute() == null ? 0 : quota.getRequestsThisMinute();
        if (current >= limit) {
            LocalDateTime resetAt = quota.getLastMinuteReset().plusMinutes(1);
            long retryAfter = Math.max(1, Duration.between(now, resetAt).getSeconds());
            throw new RateLimitExceededException((int) retryAfter);
        }

        quota.setRequestsThisMinute(current + 1);
        userQuotaRepository.save(quota);

        return next.generate(request);
    }
}

