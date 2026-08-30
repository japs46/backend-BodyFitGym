package com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.helper;

import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.entities.UserEntity;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    public static Specification<UserEntity> documentStartsWith(String document){
        return (root, query, cb) ->
                (document == null || document.isBlank()) ? null : cb.like(cb.lower(root.get("document")),document.toLowerCase() + "%");
    }

    public static Specification<UserEntity> nameStarrsWith(String name){
        return (root, query, cb) ->
                (name == null || name.isBlank()) ? null : cb.like(cb.lower(root.get("name")), name.toLowerCase() + "%");
    }

    public static Specification<UserEntity> userNameStartsWith(String userName){
        return (root, query, cb) ->
                (userName == null || userName.isBlank()) ? null : cb.like(cb.lower(root.get("userName")), userName.toLowerCase() + "%");
    }
}
