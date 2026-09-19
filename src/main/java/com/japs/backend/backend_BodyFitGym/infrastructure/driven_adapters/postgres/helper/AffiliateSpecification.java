package com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.helper;

import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.entities.AffiliateEntity;
import org.springframework.data.jpa.domain.Specification;

public class AffiliateSpecification {

    public static Specification<AffiliateEntity> identificationStartsWith(String identification) {
        return (root, query, cb) ->
                (identification == null || identification.isBlank()) ? null
                        : cb.like(cb.lower(root.get("identification")), identification.toLowerCase() + "%");
    }

    public static Specification<AffiliateEntity> nameStartsWith(String name) {
        return (root, query, cb) ->
                (name == null || name.isBlank()) ? null
                        : cb.like(cb.lower(root.get("firstName")), name.toLowerCase() + "%");
    }

    public static Specification<AffiliateEntity> statusEquals(String status) {
        return (root, query, cb) ->
                (status == null || status.isBlank()) ? null
                        : cb.equal(cb.lower(root.get("status")), status.toLowerCase());
    }
}
