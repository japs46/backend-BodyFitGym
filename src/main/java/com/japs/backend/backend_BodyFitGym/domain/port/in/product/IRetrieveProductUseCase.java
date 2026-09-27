package com.japs.backend.backend_BodyFitGym.domain.port.in.product;

import com.japs.backend.backend_BodyFitGym.application.dto.ProductSearchCriteria;
import com.japs.backend.backend_BodyFitGym.domain.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IRetrieveProductUseCase {

    Product getProductById(Long id);

    Page<Product> search(ProductSearchCriteria productSearchCriteria, Pageable pageable);
}
