package com.japs.backend.backend_BodyFitGym.application.usecases.product;

import com.japs.backend.backend_BodyFitGym.domain.model.Product;
import com.japs.backend.backend_BodyFitGym.domain.port.in.product.IDeleteProductUseCase;
import com.japs.backend.backend_BodyFitGym.domain.port.out.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Component
public class DeleteProductUseCaseImpl implements IDeleteProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    @Override
    public void deleteProduct(Long id) {

        Product existingProduct = productRepositoryPort.findById(id).orElse(null);
        if (existingProduct == null) {
            throw new NoSuchElementException("No existe ningún producto con el ID: " + id);
        }

        productRepositoryPort.delete(id);
    }
}
