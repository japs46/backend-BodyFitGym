package com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.mapper;

import com.japs.backend.backend_BodyFitGym.domain.model.Product;
import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.entities.ProductEntity;

public class ProductMapper {

    public static ProductEntity toEntity(Product product) {

        if (product == null)
            return null;

        return ProductEntity.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .quantity(product.getQuantity())
                .price(product.getPrice())
                .status(product.getStatus())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    public static Product toModel(ProductEntity productEntity) {

        if (productEntity == null)
            return null;

        return Product.builder()
                .id(productEntity.getId())
                .name(productEntity.getName())
                .description(productEntity.getDescription())
                .quantity(productEntity.getQuantity())
                .price(productEntity.getPrice())
                .status(productEntity.getStatus())
                .createdAt(productEntity.getCreatedAt())
                .updatedAt(productEntity.getUpdatedAt())
                .build();
    }
}
