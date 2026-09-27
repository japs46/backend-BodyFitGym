package com.japs.backend.backend_BodyFitGym.domain.port.in.product;

import com.japs.backend.backend_BodyFitGym.domain.model.Product;

public interface ICreateProductUseCase {

    Product createProduct(Product product);
}
