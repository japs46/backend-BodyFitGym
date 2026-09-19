package com.japs.backend.backend_BodyFitGym.domain.port.in.membership;

import com.japs.backend.backend_BodyFitGym.application.dto.MembershipSearchCriteria;
import com.japs.backend.backend_BodyFitGym.domain.model.Membership;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IRetrieveMembershipUseCase {

    Membership getMembershipById(Long id);

    Page<Membership> search(MembershipSearchCriteria membershipSearchCriteria, Pageable pageable);
}
