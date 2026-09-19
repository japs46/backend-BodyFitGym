package com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.repository;

import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.entities.AffiliateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AffiliateEntityRepository extends JpaRepository<AffiliateEntity, Long>, JpaSpecificationExecutor<AffiliateEntity> {

    // findFirst (no findBy) a propósito: la BD ya no exige identificación única (ver AffiliateEntity),
    // así que datos migrados del sistema legado pueden traer identificaciones duplicadas sin romper la consulta.
    Optional<AffiliateEntity> findFirstByIdentificationIgnoreCase(String identification);
}
