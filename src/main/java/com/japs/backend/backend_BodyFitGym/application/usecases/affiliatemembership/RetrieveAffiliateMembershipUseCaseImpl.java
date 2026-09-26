package com.japs.backend.backend_BodyFitGym.application.usecases.affiliatemembership;

import com.japs.backend.backend_BodyFitGym.application.dto.AffiliateMembershipSearchCriteria;
import com.japs.backend.backend_BodyFitGym.domain.model.AffiliateMembership;
import com.japs.backend.backend_BodyFitGym.domain.port.in.affiliatemembership.IRetrieveAffiliateMembershipUseCase;
import com.japs.backend.backend_BodyFitGym.domain.port.out.AffiliateMembershipRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Component
public class RetrieveAffiliateMembershipUseCaseImpl implements IRetrieveAffiliateMembershipUseCase {

    private final AffiliateMembershipRepositoryPort affiliateMembershipRepositoryPort;

    @Override
    public AffiliateMembership getAffiliateMembershipById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID de la suscripción es obligatorio.");
        }

        if (id <= 0) {
            throw new IllegalArgumentException("El ID de la suscripción debe ser un número positivo.");
        }

        AffiliateMembership affiliateMembership = affiliateMembershipRepositoryPort.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No existe ninguna suscripción con el ID: " + id));

        return withComputedRemainingUnits(affiliateMembership);
    }

    @Override
    public Page<AffiliateMembership> search(AffiliateMembershipSearchCriteria affiliateMembershipSearchCriteria, Pageable pageable) {
        return affiliateMembershipRepositoryPort.search(affiliateMembershipSearchCriteria, pageable)
                .map(this::withComputedRemainingUnits);
    }

    // remainingUnits se recalcula en vivo (no se persiste en cada lectura,
    // a diferencia del legado, que sí escribía en cada consulta).
    private AffiliateMembership withComputedRemainingUnits(AffiliateMembership affiliateMembership) {
        int remainingUnits = AffiliateMembershipCalculator.calculateRemainingUnits(affiliateMembership);
        return affiliateMembership.toBuilder().remainingUnits(remainingUnits).build();
    }
}
