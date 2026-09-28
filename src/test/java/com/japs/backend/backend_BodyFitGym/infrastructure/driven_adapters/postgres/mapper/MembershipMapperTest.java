package com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.mapper;

import com.japs.backend.backend_BodyFitGym.domain.model.DurationUnit;
import com.japs.backend.backend_BodyFitGym.domain.model.Membership;
import com.japs.backend.backend_BodyFitGym.domain.model.MembershipStatus;
import com.japs.backend.backend_BodyFitGym.domain.model.TrackingMode;
import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.entities.MembershipEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class MembershipMapperTest {

    @Test
    void toEntity_devuelveNullConEntradaNull() {
        assertThat(MembershipMapper.toEntity(null)).isNull();
    }

    @Test
    void toModel_devuelveNullConEntradaNull() {
        assertThat(MembershipMapper.toModel(null)).isNull();
    }

    @Test
    void mapeoIdaYVuelta_conservaTodosLosCampos() {
        Membership original = Membership.builder()
                .id(1L)
                .name("Bimestral")
                .description("Plan bimestral")
                .durationUnit(DurationUnit.MESES)
                .durationQuantity(2)
                .trackingMode(TrackingMode.MENSUALIDAD)
                .price(BigDecimal.valueOf(100000))
                .status(MembershipStatus.ACTIVA)
                .createdAt(LocalDateTime.of(2026, 1, 1, 10, 0))
                .updatedAt(LocalDateTime.of(2026, 1, 2, 10, 0))
                .build();

        Membership result = MembershipMapper.toModel(MembershipMapper.toEntity(original));

        assertThat(result).usingRecursiveComparison().isEqualTo(original);
    }

    @Test
    void toEntity_mapeaLosCamposClave() {
        Membership membership = Membership.builder()
                .id(7L)
                .name("Mensual")
                .durationUnit(DurationUnit.MESES)
                .durationQuantity(1)
                .status(MembershipStatus.ACTIVA)
                .build();

        MembershipEntity entity = MembershipMapper.toEntity(membership);

        assertThat(entity.getId()).isEqualTo(7L);
        assertThat(entity.getName()).isEqualTo("Mensual");
        assertThat(entity.getDurationQuantity()).isEqualTo(1);
        assertThat(entity.getStatus()).isEqualTo(MembershipStatus.ACTIVA);
    }
}
