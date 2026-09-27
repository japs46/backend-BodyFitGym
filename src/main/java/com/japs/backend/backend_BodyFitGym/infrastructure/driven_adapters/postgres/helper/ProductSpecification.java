package com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.helper;

import com.japs.backend.backend_BodyFitGym.domain.model.ProductStatus;
import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.entities.ProductEntity;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {

    public static Specification<ProductEntity> nameStartsWith(String name) {
        return (root, query, cb) ->
                (name == null || name.isBlank()) ? null : cb.like(cb.lower(root.get("name")), name.toLowerCase() + "%");
    }

    public static Specification<ProductEntity> statusEquals(ProductStatus status) {
        return (root, query, cb) ->
                (status == null) ? null : cb.equal(root.get("status"), status);
    }
}
