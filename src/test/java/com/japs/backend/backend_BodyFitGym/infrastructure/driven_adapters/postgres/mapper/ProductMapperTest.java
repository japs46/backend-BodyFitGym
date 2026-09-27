package com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.mapper;

import com.japs.backend.backend_BodyFitGym.domain.model.Product;
import com.japs.backend.backend_BodyFitGym.domain.model.ProductStatus;
import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.entities.ProductEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ProductMapperTest {

    @Test
    void toEntity_devuelveNullConEntradaNull() {
        assertThat(ProductMapper.toEntity(null)).isNull();
    }

    @Test
    void toModel_devuelveNullConEntradaNull() {
        assertThat(ProductMapper.toModel(null)).isNull();
    }

    @Test
    void mapeoIdaYVuelta_conservaTodosLosCampos() {
        Product original = Product.builder()
                .id(1L)
                .name("Proteina Whey 1kg")
                .description("Suplemento proteico")
                .quantity(20)
                .price(BigDecimal.valueOf(85000))
                .status(ProductStatus.ACTIVO)
                .createdAt(LocalDateTime.of(2026, 1, 1, 10, 0))
                .updatedAt(LocalDateTime.of(2026, 1, 2, 10, 0))
                .build();

        Product result = ProductMapper.toModel(ProductMapper.toEntity(original));

        assertThat(result).usingRecursiveComparison().isEqualTo(original);
    }

    @Test
    void toEntity_mapeaLosCamposClave() {
        Product product = Product.builder()
                .id(7L)
                .name("Shaker")
                .quantity(10)
                .status(ProductStatus.ACTIVO)
                .build();

        ProductEntity entity = ProductMapper.toEntity(product);

        assertThat(entity.getId()).isEqualTo(7L);
        assertThat(entity.getName()).isEqualTo("Shaker");
        assertThat(entity.getQuantity()).isEqualTo(10);
        assertThat(entity.getStatus()).isEqualTo(ProductStatus.ACTIVO);
    }
}
