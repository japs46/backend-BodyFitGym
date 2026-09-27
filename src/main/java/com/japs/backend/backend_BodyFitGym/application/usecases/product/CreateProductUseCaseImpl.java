package com.japs.backend.backend_BodyFitGym.application.usecases.product;

import com.japs.backend.backend_BodyFitGym.domain.model.Product;
import com.japs.backend.backend_BodyFitGym.domain.model.ProductStatus;
import com.japs.backend.backend_BodyFitGym.domain.port.in.product.ICreateProductUseCase;
import com.japs.backend.backend_BodyFitGym.domain.port.out.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CreateProductUseCaseImpl implements ICreateProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    @Override
    public Product createProduct(Product product) {

        Product existingProductWithName = productRepositoryPort.findByNameIgnoreCase(product.getName()).orElse(null);
        if (existingProductWithName != null) {
            throw new IllegalStateException("Ya existe un producto registrado con el nombre: " + product.getName());
        }

        if (product.getStatus() == null) {
            product.setStatus(ProductStatus.ACTIVO);
        }

        return productRepositoryPort.save(product);
    }
}
