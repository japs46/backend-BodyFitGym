package com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.repository;

import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.entities.AffiliateMembershipEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AffiliateMembershipEntityRepository extends JpaRepository<AffiliateMembershipEntity, Long>, JpaSpecificationExecutor<AffiliateMembershipEntity> {
}
