package com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.mapper;

import com.japs.backend.backend_BodyFitGym.domain.model.Membership;
import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.entities.MembershipEntity;

public class MembershipMapper {

    public static MembershipEntity toEntity(Membership membership) {

        if (membership == null)
            return null;

        return MembershipEntity.builder()
                .id(membership.getId())
                .name(membership.getName())
                .description(membership.getDescription())
                .durationUnit(membership.getDurationUnit())
                .durationQuantity(membership.getDurationQuantity())
                .trackingMode(membership.getTrackingMode())
                .price(membership.getPrice())
                .status(membership.getStatus())
                .createdAt(membership.getCreatedAt())
                .updatedAt(membership.getUpdatedAt())
                .build();
    }

    public static Membership toModel(MembershipEntity membershipEntity) {

        if (membershipEntity == null)
            return null;

        return Membership.builder()
                .id(membershipEntity.getId())
                .name(membershipEntity.getName())
                .description(membershipEntity.getDescription())
                .durationUnit(membershipEntity.getDurationUnit())
                .durationQuantity(membershipEntity.getDurationQuantity())
                .trackingMode(membershipEntity.getTrackingMode())
                .price(membershipEntity.getPrice())
                .status(membershipEntity.getStatus())
                .createdAt(membershipEntity.getCreatedAt())
                .updatedAt(membershipEntity.getUpdatedAt())
                .build();
    }
}
