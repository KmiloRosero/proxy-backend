package com.aiproxy.service;

import com.aiproxy.repository.DailyUsageRepository;
import com.aiproxy.repository.UserQuotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProxyChainFactory {

    @Autowired
    private MockAIGenerationService mockAIGenerationService;

    @Autowired
    private UserQuotaRepository userQuotaRepository;

    @Autowired
    private DailyUsageRepository dailyUsageRepository;

    public AIGenerationService buildChain() {
        return new RateLimitProxyService(
                new QuotaProxyService(mockAIGenerationService, userQuotaRepository, dailyUsageRepository),
                userQuotaRepository
        );
    }
}

