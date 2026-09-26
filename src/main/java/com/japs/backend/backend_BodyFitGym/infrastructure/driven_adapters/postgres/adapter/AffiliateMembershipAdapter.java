package com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.adapter;

import com.japs.backend.backend_BodyFitGym.application.dto.AffiliateMembershipSearchCriteria;
import com.japs.backend.backend_BodyFitGym.domain.model.AffiliateMembership;
import com.japs.backend.backend_BodyFitGym.domain.port.out.AffiliateMembershipRepositoryPort;
import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.entities.AffiliateMembershipEntity;
import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.helper.AffiliateMembershipSpecification;
import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.mapper.AffiliateMembershipMapper;
import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.repository.AffiliateMembershipEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class AffiliateMembershipAdapter implements AffiliateMembershipRepositoryPort {

    private final AffiliateMembershipEntityRepository affiliateMembershipEntityRepository;

    @Override
    public AffiliateMembership save(AffiliateMembership affiliateMembership) {
        AffiliateMembershipEntity entity = AffiliateMembershipMapper.toEntity(affiliateMembership);
        return AffiliateMembershipMapper.toModel(affiliateMembershipEntityRepository.save(entity));
    }

    @Override
    public void delete(Long id) {
        affiliateMembershipEntityRepository.deleteById(id);
    }

    @Override
    public Optional<AffiliateMembership> findById(Long id) {
        return affiliateMembershipEntityRepository.findById(id)
                .map(AffiliateMembershipMapper::toModel);
    }

    @Override
    public Page<AffiliateMembership> search(AffiliateMembershipSearchCriteria affiliateMembershipSearchCriteria, Pageable pageable) {

        Specification<AffiliateMembershipEntity> specification = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        if (affiliateMembershipSearchCriteria.getAffiliateId() != null) {
            specification = specification.and(AffiliateMembershipSpecification.affiliateIdEquals(affiliateMembershipSearchCriteria.getAffiliateId()));
        }

        if (affiliateMembershipSearchCriteria.getMembershipId() != null) {
            specification = specification.and(AffiliateMembershipSpecification.membershipIdEquals(affiliateMembershipSearchCriteria.getMembershipId()));
        }

        if (affiliateMembershipSearchCriteria.getStatus() != null) {
            specification = specification.and(AffiliateMembershipSpecification.statusEquals(affiliateMembershipSearchCriteria.getStatus()));
        }

        return affiliateMembershipEntityRepository.findAll(specification, pageable).map(AffiliateMembershipMapper::toModel);
    }
}
