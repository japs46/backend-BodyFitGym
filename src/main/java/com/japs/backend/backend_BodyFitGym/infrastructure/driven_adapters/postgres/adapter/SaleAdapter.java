package com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.adapter;

import com.japs.backend.backend_BodyFitGym.application.dto.SaleSearchCriteria;
import com.japs.backend.backend_BodyFitGym.domain.model.Sale;
import com.japs.backend.backend_BodyFitGym.domain.port.out.SaleRepositoryPort;
import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.entities.SaleEntity;
import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.helper.SaleSpecification;
import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.mapper.SaleMapper;
import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.repository.SaleEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class SaleAdapter implements SaleRepositoryPort {

    private final SaleEntityRepository saleEntityRepository;

    @Override
    public Sale save(Sale sale) {
        SaleEntity saleEntity = SaleMapper.toEntity(sale);
        return SaleMapper.toModel(saleEntityRepository.save(saleEntity));
    }

    @Override
    public Optional<Sale> findById(Long id) {
        return saleEntityRepository.findById(id)
                .map(SaleMapper::toModel);
    }

    @Override
    public List<Sale> findByAffiliateMembershipId(Long affiliateMembershipId) {
        return saleEntityRepository.findByAffiliateMembershipId(affiliateMembershipId).stream()
                .map(SaleMapper::toModel)
                .toList();
    }

    @Override
    public Page<Sale> search(SaleSearchCriteria saleSearchCriteria, Pageable pageable) {

        Specification<SaleEntity> specification = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        if (saleSearchCriteria.getAffiliateId() != null) {
            specification = specification.and(SaleSpecification.affiliateIdEquals(saleSearchCriteria.getAffiliateId()));
        }

        if (saleSearchCriteria.getType() != null) {
            specification = specification.and(SaleSpecification.typeEquals(saleSearchCriteria.getType()));
        }

        if (saleSearchCriteria.getStatus() != null) {
            specification = specification.and(SaleSpecification.statusEquals(saleSearchCriteria.getStatus()));
        }

        if (saleSearchCriteria.getDateFrom() != null || saleSearchCriteria.getDateTo() != null) {
            specification = specification.and(SaleSpecification.saleDateBetween(
                    saleSearchCriteria.getDateFrom(), saleSearchCriteria.getDateTo()));
        }

        return saleEntityRepository.findAll(specification, pageable).map(SaleMapper::toModel);
    }
}
