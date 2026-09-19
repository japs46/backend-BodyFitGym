package com.japs.backend.backend_BodyFitGym.domain.port.in.membership;

import com.japs.backend.backend_BodyFitGym.domain.model.Membership;

public interface ICreateMembershipUseCase {

    Membership createMembership(Membership membership);
}
