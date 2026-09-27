package com.japs.backend.backend_BodyFitGym.application.usecases.product;

import com.japs.backend.backend_BodyFitGym.domain.model.Product;
import com.japs.backend.backend_BodyFitGym.domain.port.in.product.IUpdateProductUseCase;
import com.japs.backend.backend_BodyFitGym.domain.port.out.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Component
public class UpdateProductUseCaseImpl implements IUpdateProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    @Override
    public Product updateProduct(Long id, Product product) {

        Product existingProduct = productRepositoryPort.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No existe ningún producto con el ID: " + id));

        if (!existingProduct.getName().equalsIgnoreCase(product.getName())) {
            Product existingProductWithName = productRepositoryPort.findByNameIgnoreCase(product.getName()).orElse(null);
            if (existingProductWithName != null) {
                throw new IllegalStateException("Ya existe un producto registrado con el nombre: " + product.getName());
            }
        }

        Product productToUpdate = existingProduct.toBuilder()
                .name(product.getName())
                .description(product.getDescription())
                .quantity(product.getQuantity())
                .price(product.getPrice())
                .status(product.getStatus() != null ? product.getStatus() : existingProduct.getStatus())
                .build();

        return productRepositoryPort.save(productToUpdate);
    }
}
