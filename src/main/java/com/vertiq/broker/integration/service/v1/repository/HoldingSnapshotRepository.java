package com.vertiq.broker.integration.service.v1.repository;

import com.vertiq.broker.integration.service.v1.entity.HoldingSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface HoldingSnapshotRepository extends JpaRepository<HoldingSnapshot, Long> {
    Optional<HoldingSnapshot> findByHoldingIdAndDatePart(Long holdingId, LocalDate datePart);
}
