package com.japs.backend.backend_BodyFitGym.application.usecases.affiliate;

import com.japs.backend.backend_BodyFitGym.application.dto.AffiliateSearchCriteria;
import com.japs.backend.backend_BodyFitGym.domain.model.Affiliate;
import com.japs.backend.backend_BodyFitGym.domain.port.in.affiliate.IRetrieveAffiliateUseCase;
import com.japs.backend.backend_BodyFitGym.domain.port.out.AffiliateRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Component
public class RetrieveAffiliateUseCaseImpl implements IRetrieveAffiliateUseCase {

    private final AffiliateRepositoryPort affiliateRepositoryPort;

    @Override
    public Affiliate getAffiliateById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID del afiliado es obligatorio.");
        }

        if (id <= 0) {
            throw new IllegalArgumentException("El ID del afiliado debe ser un número positivo.");
        }

        return affiliateRepositoryPort.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No existe ningún afiliado con el ID: " + id));
    }

    @Override
    public Page<Affiliate> search(AffiliateSearchCriteria affiliateSearchCriteria, Pageable pageable) {
        return affiliateRepositoryPort.search(affiliateSearchCriteria, pageable);
    }
}
