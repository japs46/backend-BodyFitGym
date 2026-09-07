package com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.repository;

import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.entities.MembershipEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MembershipEntityRepository extends JpaRepository<MembershipEntity, Long>, JpaSpecificationExecutor<MembershipEntity> {

    // findFirst (no findBy) a propósito: la BD ya no exige nombre único (ver MembershipEntity),
    // así que datos migrados del sistema legado pueden traer nombres duplicados sin romper la consulta.
    Optional<MembershipEntity> findFirstByNameIgnoreCase(String name);
}
