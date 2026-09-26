package com.japs.backend.backend_BodyFitGym.domain.port.in.affiliatemembership;

import com.japs.backend.backend_BodyFitGym.domain.model.AffiliateMembership;

public interface ICreateAffiliateMembershipUseCase {

    AffiliateMembership createAffiliateMembership(AffiliateMembership affiliateMembership);
}
