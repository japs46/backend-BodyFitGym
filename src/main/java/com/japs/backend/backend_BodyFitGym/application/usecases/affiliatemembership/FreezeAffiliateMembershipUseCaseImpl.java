package com.japs.backend.backend_BodyFitGym.application.usecases.affiliatemembership;

import com.japs.backend.backend_BodyFitGym.domain.model.AffiliateMembership;
import com.japs.backend.backend_BodyFitGym.domain.model.SubscriptionStatus;
import com.japs.backend.backend_BodyFitGym.domain.port.in.affiliatemembership.IFreezeAffiliateMembershipUseCase;
import com.japs.backend.backend_BodyFitGym.domain.port.out.AffiliateMembershipRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Component
public class FreezeAffiliateMembershipUseCaseImpl implements IFreezeAffiliateMembershipUseCase {

    private final AffiliateMembershipRepositoryPort affiliateMembershipRepositoryPort;

    @Override
    public AffiliateMembership freeze(Long id) {

        AffiliateMembership existing = affiliateMembershipRepositoryPort.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No existe ninguna suscripción con el ID: " + id));

        if (Boolean.TRUE.equals(existing.getFrozen())) {
            throw new IllegalStateException("La suscripción ya se encuentra congelada.");
        }

        // Se toma una foto de las unidades restantes AL MOMENTO de congelar,
        // para que quede fija mientras dure el congelamiento.
        int remainingUnits = AffiliateMembershipCalculator.calculateRemainingUnits(existing);

        AffiliateMembership frozen = existing.toBuilder()
                .remainingUnits(remainingUnits)
                .frozen(true)
                .frozenAt(LocalDate.now())
                .status(SubscriptionStatus.INACTIVA)
                .build();

        return affiliateMembershipRepositoryPort.save(frozen);
    }
}
