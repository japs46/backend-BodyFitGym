package com.japs.backend.backend_BodyFitGym.application.services;

import com.japs.backend.backend_BodyFitGym.application.dto.ProductSearchCriteria;
import com.japs.backend.backend_BodyFitGym.domain.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    Product save(Product product);

    Product update(Long id, Product product);

    void delete(Long id);

    Product findById(Long id);

    Page<Product> search(ProductSearchCriteria productSearchCriteria, Pageable pageable);
}
