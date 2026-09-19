package com.japs.backend.backend_BodyFitGym.domain.port.in.affiliate;

import com.japs.backend.backend_BodyFitGym.domain.model.Affiliate;

public interface ICreateAffiliateUseCase {

    Affiliate createAffiliate(Affiliate affiliate);
}
