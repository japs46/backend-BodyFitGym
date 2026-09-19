package com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.adapter;

import com.japs.backend.backend_BodyFitGym.application.dto.AffiliateSearchCriteria;
import com.japs.backend.backend_BodyFitGym.domain.model.Affiliate;
import com.japs.backend.backend_BodyFitGym.domain.port.out.AffiliateRepositoryPort;
import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.entities.AffiliateEntity;
import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.helper.AffiliateSpecification;
import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.mapper.AffiliateMapper;
import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.repository.AffiliateEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class AffiliateAdapter implements AffiliateRepositoryPort {

    private final AffiliateEntityRepository affiliateEntityRepository;

    @Override
    public Affiliate save(Affiliate affiliate) {
        AffiliateEntity affiliateEntity = AffiliateMapper.toEntity(affiliate);
        return AffiliateMapper.toModel(affiliateEntityRepository.save(affiliateEntity));
    }

    @Override
    public void delete(Long id) {
        affiliateEntityRepository.deleteById(id);
    }

    @Override
    public Optional<Affiliate> findById(Long id) {
        return affiliateEntityRepository.findById(id)
                .map(AffiliateMapper::toModel);
    }

    @Override
    public Optional<Affiliate> findByIdentificationIgnoreCase(String identification) {
        return affiliateEntityRepository.findFirstByIdentificationIgnoreCase(identification)
                .map(AffiliateMapper::toModel);
    }

    @Override
    public Page<Affiliate> search(AffiliateSearchCriteria affiliateSearchCriteria, Pageable pageable) {

        Specification<AffiliateEntity> specification = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        if (affiliateSearchCriteria.getIdentification() != null) {
            specification = specification.and(AffiliateSpecification.identificationStartsWith(affiliateSearchCriteria.getIdentification()));
        }

        if (affiliateSearchCriteria.getName() != null) {
            specification = specification.and(AffiliateSpecification.nameStartsWith(affiliateSearchCriteria.getName()));
        }

        if (affiliateSearchCriteria.getStatus() != null) {
            specification = specification.and(AffiliateSpecification.statusEquals(affiliateSearchCriteria.getStatus()));
        }

        return affiliateEntityRepository.findAll(specification, pageable).map(AffiliateMapper::toModel);
    }
}
