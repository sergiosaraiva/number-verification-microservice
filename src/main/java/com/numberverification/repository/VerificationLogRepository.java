package com.numberverification.repository;

import com.numberverification.repository.entity.VerificationLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface VerificationLogRepository extends MongoRepository<VerificationLog, String> {
    List<VerificationLog> findByCorrelationId(String correlationId);
    List<VerificationLog> findByTimestampBetween(Instant start, Instant end);
    long countByClientIpAndTimestampAfter(String clientIp, Instant since);
}