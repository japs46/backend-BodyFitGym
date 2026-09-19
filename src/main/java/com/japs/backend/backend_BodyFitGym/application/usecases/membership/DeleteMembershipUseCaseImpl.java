package com.japs.backend.backend_BodyFitGym.application.usecases.membership;

import com.japs.backend.backend_BodyFitGym.domain.model.Membership;
import com.japs.backend.backend_BodyFitGym.domain.port.in.membership.IDeleteMembershipUseCase;
import com.japs.backend.backend_BodyFitGym.domain.port.out.MembershipRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Component
public class DeleteMembershipUseCaseImpl implements IDeleteMembershipUseCase {

    private final MembershipRepositoryPort membershipRepositoryPort;

    @Override
    public void deleteMembership(Long id) {

        Membership existingMembership = membershipRepositoryPort.findById(id).orElse(null);
        if (existingMembership == null) {
            throw new NoSuchElementException("No existe ninguna membresía con el ID: " + id);
        }

        membershipRepositoryPort.delete(id);
    }
}
