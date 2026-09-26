package com.japs.backend.backend_BodyFitGym.domain.port.in.affiliatemembership;

import com.japs.backend.backend_BodyFitGym.domain.model.AffiliateMembership;

public interface IFreezeAffiliateMembershipUseCase {

    AffiliateMembership freeze(Long id);
}
