package com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.mapper;

import com.japs.backend.backend_BodyFitGym.domain.model.Affiliate;
import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.entities.AffiliateEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class AffiliateMapperTest {

    @Test
    void toEntity_devuelveNullConEntradaNull() {
        assertThat(AffiliateMapper.toEntity(null)).isNull();
    }

    @Test
    void toModel_devuelveNullConEntradaNull() {
        assertThat(AffiliateMapper.toModel(null)).isNull();
    }

    @Test
    void mapeoIdaYVuelta_conservaTodosLosCampos() {
        Affiliate original = Affiliate.builder()
                .id(1L)
                .identification("123456")
                .firstName("Juan")
                .middleName("Carlos")
                .lastName("Pérez")
                .secondLastName("Gómez")
                .sex("Masculino")
                .birthDate(LocalDate.of(1990, 5, 20))
                .affiliationDate(LocalDate.of(2020, 1, 15))
                .address("Calle 1")
                .neighborhood("Centro")
                .city("Medellín")
                .status("Activo")
                .postalCode("050001")
                .homePhone("6041234")
                .mobilePhone("3001234567")
                .email("juan@test.com")
                .occupation("Ingeniero")
                .weight(75.5)
                .height(1.78)
                .waist(85.0)
                .leg(55.0)
                .hip(95.0)
                .build();

        Affiliate result = AffiliateMapper.toModel(AffiliateMapper.toEntity(original));

        assertThat(result).usingRecursiveComparison().isEqualTo(original);
    }

    @Test
    void toEntity_mapeaLosCamposClave() {
        Affiliate affiliate = Affiliate.builder()
                .id(7L)
                .identification("999")
                .firstName("Ana")
                .status("Activo")
                .build();

        AffiliateEntity entity = AffiliateMapper.toEntity(affiliate);

        assertThat(entity.getId()).isEqualTo(7L);
        assertThat(entity.getIdentification()).isEqualTo("999");
        assertThat(entity.getFirstName()).isEqualTo("Ana");
        assertThat(entity.getStatus()).isEqualTo("Activo");
    }
}
