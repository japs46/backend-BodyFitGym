package com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.mapper;

import com.japs.backend.backend_BodyFitGym.domain.model.Sale;
import com.japs.backend.backend_BodyFitGym.domain.model.SaleStatus;
import com.japs.backend.backend_BodyFitGym.domain.model.SaleType;
import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.entities.SaleEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class SaleMapperTest {

    @Test
    void toEntity_devuelveNullConEntradaNull() {
        assertThat(SaleMapper.toEntity(null)).isNull();
    }

    @Test
    void toModel_devuelveNullConEntradaNull() {
        assertThat(SaleMapper.toModel(null)).isNull();
    }

    @Test
    void mapeoIdaYVuelta_conservaTodosLosCampos() {
        Sale original = Sale.builder()
                .id(1L)
                .affiliateId(4L)
                .type(SaleType.PRODUCTO)
                .productId(1L)
                .affiliateMembershipId(null)
                .quantity(3)
                .unitPrice(BigDecimal.valueOf(88000))
                .total(BigDecimal.valueOf(264000))
                .observation("Venta de prueba")
                .status(SaleStatus.PAGADA)
                .saleDate(LocalDateTime.of(2026, 1, 1, 10, 0))
                .createdAt(LocalDateTime.of(2026, 1, 1, 10, 0))
                .build();

        Sale result = SaleMapper.toModel(SaleMapper.toEntity(original));

        assertThat(result).usingRecursiveComparison().isEqualTo(original);
    }

    @Test
    void toEntity_mapeaLosCamposClave() {
        Sale sale = Sale.builder()
                .id(2L)
                .affiliateId(4L)
                .type(SaleType.AFILIACION)
                .affiliateMembershipId(2L)
                .status(SaleStatus.PAGADA)
                .build();

        SaleEntity entity = SaleMapper.toEntity(sale);

        assertThat(entity.getId()).isEqualTo(2L);
        assertThat(entity.getAffiliateId()).isEqualTo(4L);
        assertThat(entity.getType()).isEqualTo(SaleType.AFILIACION);
        assertThat(entity.getAffiliateMembershipId()).isEqualTo(2L);
        assertThat(entity.getStatus()).isEqualTo(SaleStatus.PAGADA);
    }
}
