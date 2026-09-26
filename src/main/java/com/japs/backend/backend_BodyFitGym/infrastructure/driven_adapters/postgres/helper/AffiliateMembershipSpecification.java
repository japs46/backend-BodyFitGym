package com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.helper;

import com.japs.backend.backend_BodyFitGym.domain.model.SubscriptionStatus;
import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.entities.AffiliateMembershipEntity;
import org.springframework.data.jpa.domain.Specification;

public class AffiliateMembershipSpecification {

    public static Specification<AffiliateMembershipEntity> affiliateIdEquals(Long affiliateId) {
        return (root, query, cb) ->
                (affiliateId == null) ? null : cb.equal(root.get("affiliateId"), affiliateId);
    }

    public static Specification<AffiliateMembershipEntity> membershipIdEquals(Long membershipId) {
        return (root, query, cb) ->
                (membershipId == null) ? null : cb.equal(root.get("membershipId"), membershipId);
    }

    public static Specification<AffiliateMembershipEntity> statusEquals(SubscriptionStatus status) {
        return (root, query, cb) ->
                (status == null) ? null : cb.equal(root.get("status"), status);
    }
}
