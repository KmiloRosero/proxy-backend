package com.aiproxy.scheduler;

import com.aiproxy.model.UserQuota;
import com.aiproxy.repository.UserQuotaRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class QuotaResetScheduler {

    private final UserQuotaRepository userQuotaRepository;

    public QuotaResetScheduler(UserQuotaRepository userQuotaRepository) {
        this.userQuotaRepository = userQuotaRepository;
    }

    @Scheduled(fixedDelay = 60000)
    public void resetRateLimits() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime threshold = now.minusMinutes(1);

        List<UserQuota> toReset = userQuotaRepository.findByLastMinuteResetBefore(threshold);
        if (toReset.isEmpty()) {
            return;
        }

        for (UserQuota quota : toReset) {
            quota.setRequestsThisMinute(0);
            quota.setLastMinuteReset(now);
        }

        userQuotaRepository.saveAll(toReset);
    }

    @Scheduled(cron = "0 0 0 1 * *")
    public void resetMonthlyQuota() {
        LocalDate firstDayNextMonth = LocalDate.now().withDayOfMonth(1).plusMonths(1);

        List<UserQuota> all = userQuotaRepository.findAll();
        if (all.isEmpty()) {
            return;
        }

        for (UserQuota quota : all) {
            quota.setTokensUsed(0L);
            quota.setMonthlyResetDate(firstDayNextMonth);
        }

        userQuotaRepository.saveAll(all);
    }
}

