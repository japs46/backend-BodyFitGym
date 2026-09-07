package com.japs.backend.backend_BodyFitGym.domain.port.out;

import com.japs.backend.backend_BodyFitGym.application.dto.MembershipSearchCriteria;
import com.japs.backend.backend_BodyFitGym.domain.model.Membership;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface MembershipRepositoryPort {

    Membership save(Membership membership);

    void delete(Long id);

    Optional<Membership> findById(Long id);

    Optional<Membership> findByNameIgnoreCase(String name);

    Page<Membership> search(MembershipSearchCriteria membershipSearchCriteria, Pageable pageable);
}
