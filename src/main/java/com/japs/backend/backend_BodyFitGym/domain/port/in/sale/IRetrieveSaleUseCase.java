package com.japs.backend.backend_BodyFitGym.domain.port.in.sale;

import com.japs.backend.backend_BodyFitGym.application.dto.SaleSearchCriteria;
import com.japs.backend.backend_BodyFitGym.domain.model.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IRetrieveSaleUseCase {

    Sale getSaleById(Long id);

    Page<Sale> search(SaleSearchCriteria saleSearchCriteria, Pageable pageable);
}
