package com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.mapper;

import com.japs.backend.backend_BodyFitGym.domain.model.AffiliateMembership;
import com.japs.backend.backend_BodyFitGym.domain.model.SubscriptionStatus;
import com.japs.backend.backend_BodyFitGym.domain.model.TrackingMode;
import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.entities.AffiliateMembershipEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class AffiliateMembershipMapperTest {

    @Test
    void toEntity_devuelveNullConEntradaNull() {
        assertThat(AffiliateMembershipMapper.toEntity(null)).isNull();
    }

    @Test
    void toModel_devuelveNullConEntradaNull() {
        assertThat(AffiliateMembershipMapper.toModel(null)).isNull();
    }

    @Test
    void mapeoIdaYVuelta_conservaTodosLosCampos() {
        AffiliateMembership original = AffiliateMembership.builder()
                .id(1L)
                .affiliateId(4L)
                .membershipId(3L)
                .startDate(LocalDate.of(2026, 1, 1))
                .endDate(LocalDate.of(2026, 3, 1))
                .trackingMode(TrackingMode.MENSUALIDAD)
                .remainingUnits(60)
                .status(SubscriptionStatus.ACTIVA)
                .frozen(false)
                .frozenAt(null)
                .createdAt(LocalDate.of(2026, 1, 1))
                .build();

        AffiliateMembership result = AffiliateMembershipMapper.toModel(AffiliateMembershipMapper.toEntity(original));

        assertThat(result).usingRecursiveComparison().isEqualTo(original);
    }

    @Test
    void toEntity_mapeaLosCamposClave() {
        AffiliateMembership affiliateMembership = AffiliateMembership.builder()
                .id(2L)
                .affiliateId(10L)
                .membershipId(20L)
                .trackingMode(TrackingMode.ASISTENCIA)
                .status(SubscriptionStatus.INACTIVA)
                .frozen(true)
                .build();

        AffiliateMembershipEntity entity = AffiliateMembershipMapper.toEntity(affiliateMembership);

        assertThat(entity.getId()).isEqualTo(2L);
        assertThat(entity.getAffiliateId()).isEqualTo(10L);
        assertThat(entity.getMembershipId()).isEqualTo(20L);
        assertThat(entity.getTrackingMode()).isEqualTo(TrackingMode.ASISTENCIA);
        assertThat(entity.getStatus()).isEqualTo(SubscriptionStatus.INACTIVA);
        assertThat(entity.getFrozen()).isTrue();
    }
}
