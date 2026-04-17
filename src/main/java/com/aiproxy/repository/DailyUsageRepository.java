package com.aiproxy.repository;

import com.aiproxy.model.DailyUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyUsageRepository extends JpaRepository<DailyUsage, Long> {

    Optional<DailyUsage> findByUserIdAndDate(String userId, LocalDate date);

    List<DailyUsage> findByUserIdAndDateBetween(String userId, LocalDate start, LocalDate end);
}

