package com.japs.backend.backend_BodyFitGym.domain.port.in.affiliatemembership;

import com.japs.backend.backend_BodyFitGym.application.dto.AffiliateMembershipSearchCriteria;
import com.japs.backend.backend_BodyFitGym.domain.model.AffiliateMembership;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IRetrieveAffiliateMembershipUseCase {

    AffiliateMembership getAffiliateMembershipById(Long id);

    Page<AffiliateMembership> search(AffiliateMembershipSearchCriteria affiliateMembershipSearchCriteria, Pageable pageable);
}
