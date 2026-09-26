package com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.mapper;

import com.japs.backend.backend_BodyFitGym.domain.model.AffiliateMembership;
import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.entities.AffiliateMembershipEntity;

public class AffiliateMembershipMapper {

    public static AffiliateMembershipEntity toEntity(AffiliateMembership affiliateMembership) {

        if (affiliateMembership == null)
            return null;

        return AffiliateMembershipEntity.builder()
                .id(affiliateMembership.getId())
                .affiliateId(affiliateMembership.getAffiliateId())
                .membershipId(affiliateMembership.getMembershipId())
                .startDate(affiliateMembership.getStartDate())
                .endDate(affiliateMembership.getEndDate())
                .trackingMode(affiliateMembership.getTrackingMode())
                .remainingUnits(affiliateMembership.getRemainingUnits())
                .status(affiliateMembership.getStatus())
                .frozen(affiliateMembership.getFrozen())
                .frozenAt(affiliateMembership.getFrozenAt())
                .createdAt(affiliateMembership.getCreatedAt())
                .build();
    }

    public static AffiliateMembership toModel(AffiliateMembershipEntity affiliateMembershipEntity) {

        if (affiliateMembershipEntity == null)
            return null;

        return AffiliateMembership.builder()
                .id(affiliateMembershipEntity.getId())
                .affiliateId(affiliateMembershipEntity.getAffiliateId())
                .membershipId(affiliateMembershipEntity.getMembershipId())
                .startDate(affiliateMembershipEntity.getStartDate())
                .endDate(affiliateMembershipEntity.getEndDate())
                .trackingMode(affiliateMembershipEntity.getTrackingMode())
                .remainingUnits(affiliateMembershipEntity.getRemainingUnits())
                .status(affiliateMembershipEntity.getStatus())
                .frozen(affiliateMembershipEntity.getFrozen())
                .frozenAt(affiliateMembershipEntity.getFrozenAt())
                .createdAt(affiliateMembershipEntity.getCreatedAt())
                .build();
    }
}
