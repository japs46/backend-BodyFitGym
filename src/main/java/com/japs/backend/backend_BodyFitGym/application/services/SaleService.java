package com.japs.backend.backend_BodyFitGym.application.services;

import com.japs.backend.backend_BodyFitGym.application.dto.SaleSearchCriteria;
import com.japs.backend.backend_BodyFitGym.domain.model.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SaleService {

    Sale save(Sale sale);

    Sale cancel(Long id);

    Sale findById(Long id);

    Page<Sale> search(SaleSearchCriteria saleSearchCriteria, Pageable pageable);
}
