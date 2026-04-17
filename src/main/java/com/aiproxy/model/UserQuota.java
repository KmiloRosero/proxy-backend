package com.aiproxy.model;

import com.aiproxy.enums.UserPlan;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_quota")
public class UserQuota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "plan", nullable = false)
    private UserPlan plan;

    @Column(name = "tokens_used", nullable = false)
    private Long tokensUsed;

    @Column(name = "requests_this_minute", nullable = false)
    private Integer requestsThisMinute;

    @Column(name = "last_minute_reset", nullable = false)
    private LocalDateTime lastMinuteReset;

    @Column(name = "monthly_reset_date", nullable = false)
    private LocalDate monthlyResetDate;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public UserQuota() {
    }

    public UserQuota(Long id, String userId, UserPlan plan, Long tokensUsed, Integer requestsThisMinute, LocalDateTime lastMinuteReset, LocalDate monthlyResetDate, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.plan = plan;
        this.tokensUsed = tokensUsed;
        this.requestsThisMinute = requestsThisMinute;
        this.lastMinuteReset = lastMinuteReset;
        this.monthlyResetDate = monthlyResetDate;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public UserPlan getPlan() {
        return plan;
    }

    public void setPlan(UserPlan plan) {
        this.plan = plan;
    }

    public Long getTokensUsed() {
        return tokensUsed;
    }

    public void setTokensUsed(Long tokensUsed) {
        this.tokensUsed = tokensUsed;
    }

    public Integer getRequestsThisMinute() {
        return requestsThisMinute;
    }

    public void setRequestsThisMinute(Integer requestsThisMinute) {
        this.requestsThisMinute = requestsThisMinute;
    }

    public LocalDateTime getLastMinuteReset() {
        return lastMinuteReset;
    }

    public void setLastMinuteReset(LocalDateTime lastMinuteReset) {
        this.lastMinuteReset = lastMinuteReset;
    }

    public LocalDate getMonthlyResetDate() {
        return monthlyResetDate;
    }

    public void setMonthlyResetDate(LocalDate monthlyResetDate) {
        this.monthlyResetDate = monthlyResetDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}

