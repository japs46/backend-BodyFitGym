package com.japs.backend.backend_BodyFitGym.domain.port.in.affiliatemembership;

import com.japs.backend.backend_BodyFitGym.domain.model.AffiliateMembership;

public interface IUpdateAffiliateMembershipUseCase {

    AffiliateMembership updateAffiliateMembership(Long id, AffiliateMembership affiliateMembership);
}
