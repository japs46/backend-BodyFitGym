package com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.adapter;

import com.japs.backend.backend_BodyFitGym.application.dto.ProductSearchCriteria;
import com.japs.backend.backend_BodyFitGym.domain.model.Product;
import com.japs.backend.backend_BodyFitGym.domain.port.out.ProductRepositoryPort;
import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.entities.ProductEntity;
import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.helper.ProductSpecification;
import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.mapper.ProductMapper;
import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.repository.ProductEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class ProductAdapter implements ProductRepositoryPort {

    private final ProductEntityRepository productEntityRepository;

    @Override
    public Product save(Product product) {
        ProductEntity productEntity = ProductMapper.toEntity(product);
        return ProductMapper.toModel(productEntityRepository.save(productEntity));
    }

    @Override
    public void delete(Long id) {
        productEntityRepository.deleteById(id);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productEntityRepository.findById(id)
                .map(ProductMapper::toModel);
    }

    @Override
    public Optional<Product> findByNameIgnoreCase(String name) {
        return productEntityRepository.findFirstByNameIgnoreCase(name)
                .map(ProductMapper::toModel);
    }

    @Override
    public Page<Product> search(ProductSearchCriteria productSearchCriteria, Pageable pageable) {

        Specification<ProductEntity> specification = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        if (productSearchCriteria.getName() != null) {
            specification = specification.and(ProductSpecification.nameStartsWith(productSearchCriteria.getName()));
        }

        if (productSearchCriteria.getStatus() != null) {
            specification = specification.and(ProductSpecification.statusEquals(productSearchCriteria.getStatus()));
        }

        return productEntityRepository.findAll(specification, pageable).map(ProductMapper::toModel);
    }
}
