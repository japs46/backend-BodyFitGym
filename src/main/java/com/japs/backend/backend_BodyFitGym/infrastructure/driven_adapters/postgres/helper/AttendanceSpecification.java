package com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.helper;

import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.entities.AttendanceEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AttendanceSpecification {

    public static Specification<AttendanceEntity> affiliateIdEquals(Long affiliateId) {
        return (root, query, cb) ->
                (affiliateId == null) ? null : cb.equal(root.get("affiliateId"), affiliateId);
    }

    public static Specification<AttendanceEntity> attendanceDateFrom(LocalDate dateFrom) {
        return (root, query, cb) ->
                (dateFrom == null) ? null : cb.greaterThanOrEqualTo(root.get("attendanceDate"), dateFrom.atStartOfDay());
    }

    public static Specification<AttendanceEntity> attendanceDateTo(LocalDate dateTo) {
        return (root, query, cb) ->
                (dateTo == null) ? null : cb.lessThanOrEqualTo(root.get("attendanceDate"), LocalDateTime.of(dateTo, LocalTime.MAX));
    }
}
