package com.aiproxy.repository;

import com.aiproxy.model.UserQuota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserQuotaRepository extends JpaRepository<UserQuota, Long> {

    Optional<UserQuota> findByUserId(String userId);

    java.util.List<UserQuota> findByLastMinuteResetBefore(LocalDateTime threshold);
}

