package com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.repository;

import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductEntityRepository extends JpaRepository<ProductEntity, Long>, JpaSpecificationExecutor<ProductEntity> {

    // findFirst (no findBy) a propósito: la BD no exige nombre único, así que datos
    // migrados del sistema legado pueden traer nombres duplicados sin romper la consulta.
    Optional<ProductEntity> findFirstByNameIgnoreCase(String name);
}
