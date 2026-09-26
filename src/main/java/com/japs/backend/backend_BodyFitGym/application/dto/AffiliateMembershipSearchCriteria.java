package com.japs.backend.backend_BodyFitGym.application.dto;

import com.japs.backend.backend_BodyFitGym.domain.model.SubscriptionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AffiliateMembershipSearchCriteria {

    private Long affiliateId;

    private Long membershipId;

    private SubscriptionStatus status;
}
