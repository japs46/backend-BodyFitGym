package com.japs.backend.backend_BodyFitGym.application.usecases.sale;

import com.japs.backend.backend_BodyFitGym.application.dto.SaleSearchCriteria;
import com.japs.backend.backend_BodyFitGym.domain.model.Sale;
import com.japs.backend.backend_BodyFitGym.domain.port.in.sale.IRetrieveSaleUseCase;
import com.japs.backend.backend_BodyFitGym.domain.port.out.SaleRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Component
public class RetrieveSaleUseCaseImpl implements IRetrieveSaleUseCase {

    private final SaleRepositoryPort saleRepositoryPort;

    @Override
    public Sale getSaleById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID de la venta es obligatorio.");
        }

        if (id <= 0) {
            throw new IllegalArgumentException("El ID de la venta debe ser un número positivo.");
        }

        return saleRepositoryPort.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No existe ninguna venta con el ID: " + id));
    }

    @Override
    public Page<Sale> search(SaleSearchCriteria saleSearchCriteria, Pageable pageable) {
        return saleRepositoryPort.search(saleSearchCriteria, pageable);
    }
}
