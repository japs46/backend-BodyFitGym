package com.japs.backend.backend_BodyFitGym.application.services;

import com.japs.backend.backend_BodyFitGym.application.dto.AffiliateSearchCriteria;
import com.japs.backend.backend_BodyFitGym.domain.model.Affiliate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AffiliateService {

    Affiliate save(Affiliate affiliate);

    Affiliate update(Long id, Affiliate affiliate);

    void delete(Long id);

    Affiliate findById(Long id);

    Page<Affiliate> search(AffiliateSearchCriteria affiliateSearchCriteria, Pageable pageable);
}
