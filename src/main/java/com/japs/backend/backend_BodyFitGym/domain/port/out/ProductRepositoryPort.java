package com.japs.backend.backend_BodyFitGym.domain.port.out;

import com.japs.backend.backend_BodyFitGym.application.dto.ProductSearchCriteria;
import com.japs.backend.backend_BodyFitGym.domain.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductRepositoryPort {

    Product save(Product product);

    void delete(Long id);

    Optional<Product> findById(Long id);

    Optional<Product> findByNameIgnoreCase(String name);

    Page<Product> search(ProductSearchCriteria productSearchCriteria, Pageable pageable);
}
