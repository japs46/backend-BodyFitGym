package com.japs.backend.backend_BodyFitGym.application.usecases.sale;

import com.japs.backend.backend_BodyFitGym.domain.model.Product;
import com.japs.backend.backend_BodyFitGym.domain.model.Sale;
import com.japs.backend.backend_BodyFitGym.domain.model.SaleStatus;
import com.japs.backend.backend_BodyFitGym.domain.model.SaleType;
import com.japs.backend.backend_BodyFitGym.domain.port.in.sale.ICancelSaleUseCase;
import com.japs.backend.backend_BodyFitGym.domain.port.out.ProductRepositoryPort;
import com.japs.backend.backend_BodyFitGym.domain.port.out.SaleRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Component
public class CancelSaleUseCaseImpl implements ICancelSaleUseCase {

    private final SaleRepositoryPort saleRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;

    @Override
    public Sale cancelSale(Long id) {
        Sale sale = saleRepositoryPort.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No existe ninguna venta con el ID: " + id));

        if (sale.getStatus() == SaleStatus.ANULADA) {
            throw new IllegalStateException("La venta ya se encuentra anulada.");
        }

        if (sale.getType() == SaleType.PRODUCTO) {
            Product product = productRepositoryPort.findById(sale.getProductId())
                    .orElseThrow(() -> new NoSuchElementException("No existe ningún producto con el ID: " + sale.getProductId()));

            int restoredStock = (product.getQuantity() == null ? 0 : product.getQuantity()) + sale.getQuantity();
            productRepositoryPort.save(product.toBuilder().quantity(restoredStock).build());
        }

        return saleRepositoryPort.save(sale.toBuilder().status(SaleStatus.ANULADA).build());
    }
}
