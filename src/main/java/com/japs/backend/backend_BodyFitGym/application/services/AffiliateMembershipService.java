package com.japs.backend.backend_BodyFitGym.application.services;

import com.japs.backend.backend_BodyFitGym.application.dto.AffiliateMembershipSearchCriteria;
import com.japs.backend.backend_BodyFitGym.domain.model.AffiliateMembership;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AffiliateMembershipService {

    AffiliateMembership save(AffiliateMembership affiliateMembership);

    AffiliateMembership update(Long id, AffiliateMembership affiliateMembership);

    void delete(Long id);

    AffiliateMembership findById(Long id);

    Page<AffiliateMembership> search(AffiliateMembershipSearchCriteria affiliateMembershipSearchCriteria, Pageable pageable);

    AffiliateMembership freeze(Long id);

    AffiliateMembership unfreeze(Long id);
}
