package com.japs.backend.backend_BodyFitGym.domain.port.in.affiliate;

import com.japs.backend.backend_BodyFitGym.application.dto.AffiliateSearchCriteria;
import com.japs.backend.backend_BodyFitGym.domain.model.Affiliate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IRetrieveAffiliateUseCase {

    Affiliate getAffiliateById(Long id);

    Page<Affiliate> search(AffiliateSearchCriteria affiliateSearchCriteria, Pageable pageable);
}
