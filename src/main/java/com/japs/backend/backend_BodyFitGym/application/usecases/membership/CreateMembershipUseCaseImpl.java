package com.japs.backend.backend_BodyFitGym.application.usecases.membership;

import com.japs.backend.backend_BodyFitGym.domain.model.Membership;
import com.japs.backend.backend_BodyFitGym.domain.model.MembershipStatus;
import com.japs.backend.backend_BodyFitGym.domain.port.in.membership.ICreateMembershipUseCase;
import com.japs.backend.backend_BodyFitGym.domain.port.out.MembershipRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CreateMembershipUseCaseImpl implements ICreateMembershipUseCase {

    private final MembershipRepositoryPort membershipRepositoryPort;

    @Override
    public Membership createMembership(Membership membership) {

        Membership existingMembershipWithName = membershipRepositoryPort.findByNameIgnoreCase(membership.getName()).orElse(null);
        if (existingMembershipWithName != null) {
            throw new IllegalStateException("Ya existe una membresía registrada con el nombre: " + membership.getName());
        }

        if (membership.getStatus() == null) {
            membership.setStatus(MembershipStatus.ACTIVA);
        }

        return membershipRepositoryPort.save(membership);
    }
}
