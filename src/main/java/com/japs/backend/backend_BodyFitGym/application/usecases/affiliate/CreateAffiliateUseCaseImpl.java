package com.japs.backend.backend_BodyFitGym.application.usecases.affiliate;

import com.japs.backend.backend_BodyFitGym.domain.model.Affiliate;
import com.japs.backend.backend_BodyFitGym.domain.port.in.affiliate.ICreateAffiliateUseCase;
import com.japs.backend.backend_BodyFitGym.domain.port.out.AffiliateRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CreateAffiliateUseCaseImpl implements ICreateAffiliateUseCase {

    private final AffiliateRepositoryPort affiliateRepositoryPort;

    @Override
    public Affiliate createAffiliate(Affiliate affiliate) {

        Affiliate existingAffiliateWithIdentification = affiliateRepositoryPort
                .findByIdentificationIgnoreCase(affiliate.getIdentification()).orElse(null);
        if (existingAffiliateWithIdentification != null) {
            throw new IllegalStateException("Ya existe un afiliado registrado con la identificación: " + affiliate.getIdentification());
        }

        return affiliateRepositoryPort.save(affiliate);
    }
}
