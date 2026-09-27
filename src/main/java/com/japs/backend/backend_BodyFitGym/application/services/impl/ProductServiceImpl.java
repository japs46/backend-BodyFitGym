package com.japs.backend.backend_BodyFitGym.application.services.impl;

import com.japs.backend.backend_BodyFitGym.application.dto.ProductSearchCriteria;
import com.japs.backend.backend_BodyFitGym.application.services.ProductService;
import com.japs.backend.backend_BodyFitGym.domain.model.Product;
import com.japs.backend.backend_BodyFitGym.domain.port.in.product.ICreateProductUseCase;
import com.japs.backend.backend_BodyFitGym.domain.port.in.product.IDeleteProductUseCase;
import com.japs.backend.backend_BodyFitGym.domain.port.in.product.IRetrieveProductUseCase;
import com.japs.backend.backend_BodyFitGym.domain.port.in.product.IUpdateProductUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ICreateProductUseCase iCreateProductUseCase;
    private final IUpdateProductUseCase iUpdateProductUseCase;
    private final IDeleteProductUseCase iDeleteProductUseCase;
    private final IRetrieveProductUseCase iRetrieveProductUseCase;

    @Override
    public Product save(Product product) {
        return iCreateProductUseCase.createProduct(product);
    }

    @Override
    public Product update(Long id, Product product) {
        return iUpdateProductUseCase.updateProduct(id, product);
    }

    @Override
    public void delete(Long id) {
        iDeleteProductUseCase.deleteProduct(id);
    }

    @Override
    public Product findById(Long id) {
        return iRetrieveProductUseCase.getProductById(id);
    }

    @Override
    public Page<Product> search(ProductSearchCriteria productSearchCriteria, Pageable pageable) {
        return iRetrieveProductUseCase.search(productSearchCriteria, pageable);
    }
}
