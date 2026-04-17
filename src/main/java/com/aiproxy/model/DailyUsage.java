package com.aiproxy.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDate;

@Entity
@Table(
        name = "daily_usage",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "usage_date"})
)
public class DailyUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "usage_date", nullable = false)
    private LocalDate date;

    @Column(name = "tokens_used", nullable = false)
    private Long tokensUsed;

    @Column(name = "requests_count", nullable = false)
    private Integer requestCount;

    public DailyUsage() {
    }

    public DailyUsage(Long id, String userId, LocalDate date, Long tokensUsed, Integer requestCount) {
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.tokensUsed = tokensUsed;
        this.requestCount = requestCount;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getTokensUsed() {
        return tokensUsed;
    }

    public void setTokensUsed(Long tokensUsed) {
        this.tokensUsed = tokensUsed;
    }

    public Integer getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(Integer requestCount) {
        this.requestCount = requestCount;
    }
}

