package com.japs.backend.backend_BodyFitGym.application.usecases.affiliatemembership;

import com.japs.backend.backend_BodyFitGym.domain.model.AffiliateMembership;
import com.japs.backend.backend_BodyFitGym.domain.port.in.affiliatemembership.IUpdateAffiliateMembershipUseCase;
import com.japs.backend.backend_BodyFitGym.domain.port.out.AffiliateMembershipRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Component
public class UpdateAffiliateMembershipUseCaseImpl implements IUpdateAffiliateMembershipUseCase {

    private final AffiliateMembershipRepositoryPort affiliateMembershipRepositoryPort;

    // No permite reasignar affiliateId/membershipId: para eso se crea una
    // suscripción nueva, igual que en el legado. Solo se ajustan fechas,
    // unidades restantes y estado.
    @Override
    public AffiliateMembership updateAffiliateMembership(Long id, AffiliateMembership affiliateMembership) {

        AffiliateMembership existing = affiliateMembershipRepositoryPort.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No existe ninguna suscripción con el ID: " + id));

        AffiliateMembership toUpdate = existing.toBuilder()
                .startDate(affiliateMembership.getStartDate() != null ? affiliateMembership.getStartDate() : existing.getStartDate())
                .endDate(affiliateMembership.getEndDate() != null ? affiliateMembership.getEndDate() : existing.getEndDate())
                .remainingUnits(affiliateMembership.getRemainingUnits() != null ? affiliateMembership.getRemainingUnits() : existing.getRemainingUnits())
                .status(affiliateMembership.getStatus() != null ? affiliateMembership.getStatus() : existing.getStatus())
                .build();

        return affiliateMembershipRepositoryPort.save(toUpdate);
    }
}
