package com.japs.backend.backend_BodyFitGym.application.usecases.affiliatemembership;

import com.japs.backend.backend_BodyFitGym.domain.model.AffiliateMembership;
import com.japs.backend.backend_BodyFitGym.domain.model.DurationUnit;
import com.japs.backend.backend_BodyFitGym.domain.model.TrackingMode;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class AffiliateMembershipCalculatorTest {

    @Test
    void calculateEndDate_dias_sumaDiasExactos() {
        LocalDate start = LocalDate.of(2026, 1, 15);
        LocalDate end = AffiliateMembershipCalculator.calculateEndDate(start, DurationUnit.DIAS, 10);
        assertThat(end).isEqualTo(LocalDate.of(2026, 1, 25));
    }

    @Test
    void calculateEndDate_meses_usaAritmeticaDeCalendarioReal() {
        // 31 de enero + 1 mes -> 28 de febrero (no "31 + 30 días" como el legado)
        LocalDate start = LocalDate.of(2026, 1, 31);
        LocalDate end = AffiliateMembershipCalculator.calculateEndDate(start, DurationUnit.MESES, 1);
        assertThat(end).isEqualTo(LocalDate.of(2026, 2, 28));
    }

    @Test
    void calculateEndDate_anios_sumaAnios() {
        LocalDate start = LocalDate.of(2026, 3, 10);
        LocalDate end = AffiliateMembershipCalculator.calculateEndDate(start, DurationUnit.ANIOS, 1);
        assertThat(end).isEqualTo(LocalDate.of(2027, 3, 10));
    }

    @Test
    void calculateRemainingUnits_congelada_devuelveElValorGuardadoSinRecalcular() {
        AffiliateMembership subscription = AffiliateMembership.builder()
                .trackingMode(TrackingMode.MENSUALIDAD)
                .endDate(LocalDate.now().minusDays(100)) // ya vencida si se recalculara
                .remainingUnits(42)
                .frozen(true)
                .build();

        assertThat(AffiliateMembershipCalculator.calculateRemainingUnits(subscription)).isEqualTo(42);
    }

    @Test
    void calculateRemainingUnits_mensualidadNoCongelada_calculaDiasHastaEndDate() {
        AffiliateMembership subscription = AffiliateMembership.builder()
                .trackingMode(TrackingMode.MENSUALIDAD)
                .endDate(LocalDate.now().plusDays(10))
                .remainingUnits(999) // debe ignorarse, se recalcula
                .frozen(false)
                .build();

        assertThat(AffiliateMembershipCalculator.calculateRemainingUnits(subscription)).isEqualTo(10);
    }

    @Test
    void calculateRemainingUnits_mensualidadVencida_noDaNegativo() {
        AffiliateMembership subscription = AffiliateMembership.builder()
                .trackingMode(TrackingMode.MENSUALIDAD)
                .endDate(LocalDate.now().minusDays(5))
                .remainingUnits(0)
                .frozen(false)
                .build();

        assertThat(AffiliateMembershipCalculator.calculateRemainingUnits(subscription)).isEqualTo(0);
    }

    @Test
    void calculateRemainingUnits_asistenciaNoCongelada_devuelveElContadorGuardado() {
        AffiliateMembership subscription = AffiliateMembership.builder()
                .trackingMode(TrackingMode.ASISTENCIA)
                .endDate(LocalDate.now().plusDays(30))
                .remainingUnits(7)
                .frozen(false)
                .build();

        assertThat(AffiliateMembershipCalculator.calculateRemainingUnits(subscription)).isEqualTo(7);
    }
}
