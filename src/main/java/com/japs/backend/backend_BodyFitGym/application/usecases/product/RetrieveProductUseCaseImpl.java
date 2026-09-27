package com.japs.backend.backend_BodyFitGym.application.usecases.product;

import com.japs.backend.backend_BodyFitGym.application.dto.ProductSearchCriteria;
import com.japs.backend.backend_BodyFitGym.domain.model.Product;
import com.japs.backend.backend_BodyFitGym.domain.port.in.product.IRetrieveProductUseCase;
import com.japs.backend.backend_BodyFitGym.domain.port.out.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Component
public class RetrieveProductUseCaseImpl implements IRetrieveProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    @Override
    public Product getProductById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID del producto es obligatorio.");
        }

        if (id <= 0) {
            throw new IllegalArgumentException("El ID del producto debe ser un número positivo.");
        }

        return productRepositoryPort.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No existe ningún producto con el ID: " + id));
    }

    @Override
    public Page<Product> search(ProductSearchCriteria productSearchCriteria, Pageable pageable) {
        return productRepositoryPort.search(productSearchCriteria, pageable);
    }
}
