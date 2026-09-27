package com.japs.backend.backend_BodyFitGym.domain.port.in.sale;

import com.japs.backend.backend_BodyFitGym.domain.model.Sale;

public interface ICancelSaleUseCase {

    Sale cancelSale(Long id);
}
