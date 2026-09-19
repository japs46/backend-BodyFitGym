package com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.helper;

import com.japs.backend.backend_BodyFitGym.domain.model.DurationUnit;
import com.japs.backend.backend_BodyFitGym.domain.model.MembershipStatus;
import com.japs.backend.backend_BodyFitGym.domain.model.TrackingMode;
import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.entities.MembershipEntity;
import org.springframework.data.jpa.domain.Specification;

public class MembershipSpecification {

    public static Specification<MembershipEntity> nameStartsWith(String name) {
        return (root, query, cb) ->
                (name == null || name.isBlank()) ? null : cb.like(cb.lower(root.get("name")), name.toLowerCase() + "%");
    }

    public static Specification<MembershipEntity> durationUnitEquals(DurationUnit durationUnit) {
        return (root, query, cb) ->
                (durationUnit == null) ? null : cb.equal(root.get("durationUnit"), durationUnit);
    }

    public static Specification<MembershipEntity> trackingModeEquals(TrackingMode trackingMode) {
        return (root, query, cb) ->
                (trackingMode == null) ? null : cb.equal(root.get("trackingMode"), trackingMode);
    }

    public static Specification<MembershipEntity> statusEquals(MembershipStatus status) {
        return (root, query, cb) ->
                (status == null) ? null : cb.equal(root.get("status"), status);
    }
}
