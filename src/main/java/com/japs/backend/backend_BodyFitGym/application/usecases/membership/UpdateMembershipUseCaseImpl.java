package com.japs.backend.backend_BodyFitGym.application.usecases.membership;

import com.japs.backend.backend_BodyFitGym.domain.model.Membership;
import com.japs.backend.backend_BodyFitGym.domain.port.in.membership.IUpdateMembershipUseCase;
import com.japs.backend.backend_BodyFitGym.domain.port.out.MembershipRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Component
public class UpdateMembershipUseCaseImpl implements IUpdateMembershipUseCase {

    private final MembershipRepositoryPort membershipRepositoryPort;

    @Override
    public Membership updateMembership(Long id, Membership membership) {

        Membership existingMembership = membershipRepositoryPort.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No existe ninguna membresía con el ID: " + id));

        if (!existingMembership.getName().equalsIgnoreCase(membership.getName())) {
            Membership existingMembershipWithName = membershipRepositoryPort.findByNameIgnoreCase(membership.getName()).orElse(null);
            if (existingMembershipWithName != null) {
                throw new IllegalStateException("Ya existe una membresía registrada con el nombre: " + membership.getName());
            }
        }

        Membership membershipToUpdate = existingMembership.toBuilder()
                .name(membership.getName())
                .description(membership.getDescription())
                .durationUnit(membership.getDurationUnit())
                .durationQuantity(membership.getDurationQuantity())
                .trackingMode(membership.getTrackingMode())
                .price(membership.getPrice())
                .status(membership.getStatus() != null ? membership.getStatus() : existingMembership.getStatus())
                .build();

        return membershipRepositoryPort.save(membershipToUpdate);
    }
}
