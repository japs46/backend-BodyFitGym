package com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.helper;

import com.japs.backend.backend_BodyFitGym.domain.model.SaleStatus;
import com.japs.backend.backend_BodyFitGym.domain.model.SaleType;
import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.entities.SaleEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class SaleSpecification {

    public static Specification<SaleEntity> affiliateIdEquals(Long affiliateId) {
        return (root, query, cb) ->
                (affiliateId == null) ? null : cb.equal(root.get("affiliateId"), affiliateId);
    }

    public static Specification<SaleEntity> typeEquals(SaleType type) {
        return (root, query, cb) ->
                (type == null) ? null : cb.equal(root.get("type"), type);
    }

    public static Specification<SaleEntity> statusEquals(SaleStatus status) {
        return (root, query, cb) ->
                (status == null) ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<SaleEntity> saleDateBetween(LocalDateTime dateFrom, LocalDateTime dateTo) {
        return (root, query, cb) -> {
            if (dateFrom == null && dateTo == null) {
                return null;
            }
            if (dateFrom != null && dateTo != null) {
                return cb.between(root.get("saleDate"), dateFrom, dateTo);
            }
            if (dateFrom != null) {
                return cb.greaterThanOrEqualTo(root.get("saleDate"), dateFrom);
            }
            return cb.lessThanOrEqualTo(root.get("saleDate"), dateTo);
        };
    }
}
