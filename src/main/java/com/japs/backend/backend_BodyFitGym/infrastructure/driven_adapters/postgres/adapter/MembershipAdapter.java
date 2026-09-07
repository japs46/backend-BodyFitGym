package com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.adapter;

import com.japs.backend.backend_BodyFitGym.application.dto.MembershipSearchCriteria;
import com.japs.backend.backend_BodyFitGym.domain.model.Membership;
import com.japs.backend.backend_BodyFitGym.domain.port.out.MembershipRepositoryPort;
import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.entities.MembershipEntity;
import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.helper.MembershipSpecification;
import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.mapper.MembershipMapper;
import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.repository.MembershipEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class MembershipAdapter implements MembershipRepositoryPort {

    private final MembershipEntityRepository membershipEntityRepository;

    @Override
    public Membership save(Membership membership) {
        MembershipEntity membershipEntity = MembershipMapper.toEntity(membership);
        return MembershipMapper.toModel(membershipEntityRepository.save(membershipEntity));
    }

    @Override
    public void delete(Long id) {
        membershipEntityRepository.deleteById(id);
    }

    @Override
    public Optional<Membership> findById(Long id) {
        return membershipEntityRepository.findById(id)
                .map(MembershipMapper::toModel);
    }

    @Override
    public Optional<Membership> findByNameIgnoreCase(String name) {
        return membershipEntityRepository.findFirstByNameIgnoreCase(name)
                .map(MembershipMapper::toModel);
    }

    @Override
    public Page<Membership> search(MembershipSearchCriteria membershipSearchCriteria, Pageable pageable) {

        Specification<MembershipEntity> specification = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        if (membershipSearchCriteria.getName() != null) {
            specification = specification.and(MembershipSpecification.nameStartsWith(membershipSearchCriteria.getName()));
        }

        if (membershipSearchCriteria.getDurationUnit() != null) {
            specification = specification.and(MembershipSpecification.durationUnitEquals(membershipSearchCriteria.getDurationUnit()));
        }

        if (membershipSearchCriteria.getTrackingMode() != null) {
            specification = specification.and(MembershipSpecification.trackingModeEquals(membershipSearchCriteria.getTrackingMode()));
        }

        if (membershipSearchCriteria.getStatus() != null) {
            specification = specification.and(MembershipSpecification.statusEquals(membershipSearchCriteria.getStatus()));
        }

        return membershipEntityRepository.findAll(specification, pageable).map(MembershipMapper::toModel);
    }
}
