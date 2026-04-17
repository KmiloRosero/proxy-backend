package com.aiproxy.controller;

import com.aiproxy.dto.QuotaHistoryResponse;
import com.aiproxy.dto.QuotaStatusResponse;
import com.aiproxy.dto.UpgradePlanRequest;
import com.aiproxy.enums.UserPlan;
import com.aiproxy.model.DailyUsage;
import com.aiproxy.model.UserQuota;
import com.aiproxy.repository.DailyUsageRepository;
import com.aiproxy.repository.UserQuotaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/quota")
@CrossOrigin
public class QuotaController {

    private final UserQuotaRepository userQuotaRepository;
    private final DailyUsageRepository dailyUsageRepository;

    public QuotaController(UserQuotaRepository userQuotaRepository, DailyUsageRepository dailyUsageRepository) {
        this.userQuotaRepository = userQuotaRepository;
        this.dailyUsageRepository = dailyUsageRepository;
    }

    @GetMapping("/status")
    public ResponseEntity<QuotaStatusResponse> status(@RequestParam String userId) {
        UserQuota quota = userQuotaRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(toStatus(quota));
    }

    @GetMapping("/history")
    public ResponseEntity<QuotaHistoryResponse> history(@RequestParam String userId) {
        LocalDate end = LocalDate.now();
        LocalDate start = end.minusDays(6);

        List<QuotaHistoryResponse.DailyUsageDto> items = dailyUsageRepository
                .findByUserIdAndDateBetween(userId, start, end)
                .stream()
                .sorted(Comparator.comparing(DailyUsage::getDate))
                .map(u -> new QuotaHistoryResponse.DailyUsageDto(
                        u.getDate(),
                        u.getTokensUsed() == null ? 0L : u.getTokensUsed(),
                        u.getRequestCount() == null ? 0 : u.getRequestCount()
                ))
                .toList();

        return ResponseEntity.ok(new QuotaHistoryResponse(items));
    }

    @PostMapping("/upgrade")
    public ResponseEntity<QuotaStatusResponse> upgradePlan(@RequestBody UpgradePlanRequest request) {
        UserQuota quota = userQuotaRepository.findByUserId(request.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (quota.getPlan() == UserPlan.FREE) {
            quota.setPlan(UserPlan.PRO);
        }

        UserQuota saved = userQuotaRepository.save(quota);
        return ResponseEntity.ok(toStatus(saved));
    }

    private QuotaStatusResponse toStatus(UserQuota quota) {
        UserPlan plan = quota.getPlan();
        long monthlyLimit = plan.getMonthlyTokens();
        long tokensUsed = quota.getTokensUsed() == null ? 0L : quota.getTokensUsed();
        long tokensRemaining;
        if (monthlyLimit == Long.MAX_VALUE) {
            tokensRemaining = Long.MAX_VALUE;
        } else {
            tokensRemaining = Math.max(0L, monthlyLimit - tokensUsed);
        }

        double usagePercentage;
        if (monthlyLimit == 0L || monthlyLimit == Long.MAX_VALUE) {
            usagePercentage = 0.0;
        } else {
            usagePercentage = (tokensUsed * 100.0) / monthlyLimit;
        }

        int requestsThisMinute = quota.getRequestsThisMinute() == null ? 0 : quota.getRequestsThisMinute();
        int requestsLimit = plan.getRequestsPerMinute();

        return new QuotaStatusResponse(
                tokensUsed,
                tokensRemaining,
                monthlyLimit,
                usagePercentage,
                quota.getMonthlyResetDate(),
                plan.name(),
                requestsThisMinute,
                requestsLimit
        );
    }
}

