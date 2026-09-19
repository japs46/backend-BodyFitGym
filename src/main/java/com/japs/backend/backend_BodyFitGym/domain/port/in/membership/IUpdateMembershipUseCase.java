package com.japs.backend.backend_BodyFitGym.domain.port.in.membership;

import com.japs.backend.backend_BodyFitGym.domain.model.Membership;

public interface IUpdateMembershipUseCase {

    Membership updateMembership(Long id, Membership membership);
}
