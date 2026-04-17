package com.aiproxy.enums;

public enum UserPlan {
    FREE(10, 50_000L),
    PRO(60, 500_000L),
    ENTERPRISE(Integer.MAX_VALUE, Long.MAX_VALUE);

    private final int requestsPerMinute;
    private final long monthlyTokens;

    UserPlan(int requestsPerMinute, long monthlyTokens) {
        this.requestsPerMinute = requestsPerMinute;
        this.monthlyTokens = monthlyTokens;
    }

    public int getRequestsPerMinute() {
        return requestsPerMinute;
    }

    public long getMonthlyTokens() {
        return monthlyTokens;
    }
}

