package com.japs.backend.backend_BodyFitGym.application.services.impl;

import com.japs.backend.backend_BodyFitGym.application.dto.AffiliateSearchCriteria;
import com.japs.backend.backend_BodyFitGym.application.services.AffiliateService;
import com.japs.backend.backend_BodyFitGym.domain.model.Affiliate;
import com.japs.backend.backend_BodyFitGym.domain.port.in.affiliate.ICreateAffiliateUseCase;
import com.japs.backend.backend_BodyFitGym.domain.port.in.affiliate.IDeleteAffiliateUseCase;
import com.japs.backend.backend_BodyFitGym.domain.port.in.affiliate.IRetrieveAffiliateUseCase;
import com.japs.backend.backend_BodyFitGym.domain.port.in.affiliate.IUpdateAffiliateUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AffiliateServiceImpl implements AffiliateService {

    private final ICreateAffiliateUseCase iCreateAffiliateUseCase;
    private final IUpdateAffiliateUseCase iUpdateAffiliateUseCase;
    private final IDeleteAffiliateUseCase iDeleteAffiliateUseCase;
    private final IRetrieveAffiliateUseCase iRetrieveAffiliateUseCase;

    @Override
    public Affiliate save(Affiliate affiliate) {
        return iCreateAffiliateUseCase.createAffiliate(affiliate);
    }

    @Override
    public Affiliate update(Long id, Affiliate affiliate) {
        return iUpdateAffiliateUseCase.updateAffiliate(id, affiliate);
    }

    @Override
    public void delete(Long id) {
        iDeleteAffiliateUseCase.deleteAffiliate(id);
    }

    @Override
    public Affiliate findById(Long id) {
        return iRetrieveAffiliateUseCase.getAffiliateById(id);
    }

    @Override
    public Page<Affiliate> search(AffiliateSearchCriteria affiliateSearchCriteria, Pageable pageable) {
        return iRetrieveAffiliateUseCase.search(affiliateSearchCriteria, pageable);
    }
}
