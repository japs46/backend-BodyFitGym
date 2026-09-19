package com.japs.backend.backend_BodyFitGym.application.usecases.affiliate;

import com.japs.backend.backend_BodyFitGym.domain.model.Affiliate;
import com.japs.backend.backend_BodyFitGym.domain.port.in.affiliate.IDeleteAffiliateUseCase;
import com.japs.backend.backend_BodyFitGym.domain.port.out.AffiliateRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Component
public class DeleteAffiliateUseCaseImpl implements IDeleteAffiliateUseCase {

    private final AffiliateRepositoryPort affiliateRepositoryPort;

    @Override
    public void deleteAffiliate(Long id) {

        Affiliate existingAffiliate = affiliateRepositoryPort.findById(id).orElse(null);
        if (existingAffiliate == null) {
            throw new NoSuchElementException("No existe ningún afiliado con el ID: " + id);
        }

        affiliateRepositoryPort.delete(id);
    }
}
