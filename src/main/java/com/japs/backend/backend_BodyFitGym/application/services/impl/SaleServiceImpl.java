package com.japs.backend.backend_BodyFitGym.application.services.impl;

import com.japs.backend.backend_BodyFitGym.application.dto.SaleSearchCriteria;
import com.japs.backend.backend_BodyFitGym.application.services.SaleService;
import com.japs.backend.backend_BodyFitGym.domain.model.Sale;
import com.japs.backend.backend_BodyFitGym.domain.port.in.sale.ICancelSaleUseCase;
import com.japs.backend.backend_BodyFitGym.domain.port.in.sale.ICreateSaleUseCase;
import com.japs.backend.backend_BodyFitGym.domain.port.in.sale.IRetrieveSaleUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SaleServiceImpl implements SaleService {

    private final ICreateSaleUseCase iCreateSaleUseCase;
    private final ICancelSaleUseCase iCancelSaleUseCase;
    private final IRetrieveSaleUseCase iRetrieveSaleUseCase;

    @Override
    public Sale save(Sale sale) {
        return iCreateSaleUseCase.createSale(sale);
    }

    @Override
    public Sale cancel(Long id) {
        return iCancelSaleUseCase.cancelSale(id);
    }

    @Override
    public Sale findById(Long id) {
        return iRetrieveSaleUseCase.getSaleById(id);
    }

    @Override
    public Page<Sale> search(SaleSearchCriteria saleSearchCriteria, Pageable pageable) {
        return iRetrieveSaleUseCase.search(saleSearchCriteria, pageable);
    }
}
