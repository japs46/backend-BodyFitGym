package com.japs.backend.backend_BodyFitGym.domain.port.out;

import com.japs.backend.backend_BodyFitGym.application.dto.AffiliateMembershipSearchCriteria;
import com.japs.backend.backend_BodyFitGym.domain.model.AffiliateMembership;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AffiliateMembershipRepositoryPort {

    AffiliateMembership save(AffiliateMembership affiliateMembership);

    void delete(Long id);

    Optional<AffiliateMembership> findById(Long id);

    Page<AffiliateMembership> search(AffiliateMembershipSearchCriteria affiliateMembershipSearchCriteria, Pageable pageable);
}
