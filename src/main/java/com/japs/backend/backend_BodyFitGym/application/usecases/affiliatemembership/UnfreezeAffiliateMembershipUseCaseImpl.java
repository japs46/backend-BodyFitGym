package com.japs.backend.backend_BodyFitGym.application.usecases.affiliatemembership;

import com.japs.backend.backend_BodyFitGym.domain.model.AffiliateMembership;
import com.japs.backend.backend_BodyFitGym.domain.model.SubscriptionStatus;
import com.japs.backend.backend_BodyFitGym.domain.port.in.affiliatemembership.IUnfreezeAffiliateMembershipUseCase;
import com.japs.backend.backend_BodyFitGym.domain.port.out.AffiliateMembershipRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Component
public class UnfreezeAffiliateMembershipUseCaseImpl implements IUnfreezeAffiliateMembershipUseCase {

    private final AffiliateMembershipRepositoryPort affiliateMembershipRepositoryPort;

    @Override
    public AffiliateMembership unfreeze(Long id) {

        AffiliateMembership existing = affiliateMembershipRepositoryPort.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No existe ninguna suscripción con el ID: " + id));

        if (!Boolean.TRUE.equals(existing.getFrozen())) {
            throw new IllegalStateException("La suscripción no se encuentra congelada.");
        }

        // A diferencia del legado (que recalculaba fechaFin = fechaInicio ORIGINAL +
        // diasRestantes, pudiendo quedar en el pasado tras un congelamiento largo),
        // aquí se recalcula desde HOY.
        LocalDate newEndDate = LocalDate.now().plusDays(existing.getRemainingUnits());

        AffiliateMembership unfrozen = existing.toBuilder()
                .endDate(newEndDate)
                .frozen(false)
                .frozenAt(null)
                .status(SubscriptionStatus.ACTIVA)
                .build();

        return affiliateMembershipRepositoryPort.save(unfrozen);
    }
}
