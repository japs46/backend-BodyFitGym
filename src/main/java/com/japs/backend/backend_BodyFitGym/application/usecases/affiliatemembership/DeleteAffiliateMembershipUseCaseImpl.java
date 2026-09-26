package com.japs.backend.backend_BodyFitGym.application.usecases.affiliatemembership;

import com.japs.backend.backend_BodyFitGym.domain.model.AffiliateMembership;
import com.japs.backend.backend_BodyFitGym.domain.port.in.affiliatemembership.IDeleteAffiliateMembershipUseCase;
import com.japs.backend.backend_BodyFitGym.domain.port.out.AffiliateMembershipRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Component
public class DeleteAffiliateMembershipUseCaseImpl implements IDeleteAffiliateMembershipUseCase {

    private final AffiliateMembershipRepositoryPort affiliateMembershipRepositoryPort;

    @Override
    public void deleteAffiliateMembership(Long id) {

        AffiliateMembership existing = affiliateMembershipRepositoryPort.findById(id).orElse(null);
        if (existing == null) {
            throw new NoSuchElementException("No existe ninguna suscripción con el ID: " + id);
        }

        affiliateMembershipRepositoryPort.delete(id);
    }
}
