package com.japs.backend.backend_BodyFitGym.application.services;

import com.japs.backend.backend_BodyFitGym.application.dto.MembershipSearchCriteria;
import com.japs.backend.backend_BodyFitGym.domain.model.Membership;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MembershipService {

    Membership save(Membership membership);

    Membership update(Long id, Membership membership);

    void delete(Long id);

    Membership findById(Long id);

    Page<Membership> search(MembershipSearchCriteria membershipSearchCriteria, Pageable pageable);
}
