package com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.repository;

import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.entities.AttendanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AttendanceEntityRepository extends JpaRepository<AttendanceEntity, Long>, JpaSpecificationExecutor<AttendanceEntity> {

    boolean existsByAffiliateIdAndAttendanceDateBetween(Long affiliateId, LocalDateTime startOfDay, LocalDateTime endOfDay);
}
