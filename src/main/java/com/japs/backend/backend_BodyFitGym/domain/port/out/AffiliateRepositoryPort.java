package com.japs.backend.backend_BodyFitGym.domain.port.out;

import com.japs.backend.backend_BodyFitGym.application.dto.AffiliateSearchCriteria;
import com.japs.backend.backend_BodyFitGym.domain.model.Affiliate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AffiliateRepositoryPort {

    Affiliate save(Affiliate affiliate);

    void delete(Long id);

    Optional<Affiliate> findById(Long id);

    Optional<Affiliate> findByIdentificationIgnoreCase(String identification);

    Page<Affiliate> search(AffiliateSearchCriteria affiliateSearchCriteria, Pageable pageable);
}
