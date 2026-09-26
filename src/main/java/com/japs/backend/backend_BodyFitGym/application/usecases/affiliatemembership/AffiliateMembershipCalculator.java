package com.japs.backend.backend_BodyFitGym.application.usecases.affiliatemembership;

import com.japs.backend.backend_BodyFitGym.domain.model.AffiliateMembership;
import com.japs.backend.backend_BodyFitGym.domain.model.DurationUnit;
import com.japs.backend.backend_BodyFitGym.domain.model.TrackingMode;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Cálculo de fecha fin y unidades restantes de una suscripción. Corrige la
 * aproximación del sistema legado (mes = 30 días) usando aritmética de
 * calendario real, y separa el cálculo por MENSUALIDAD (función pura de
 * fechas) de ASISTENCIA (contador persistido, ver AffiliateMembership).
 */
public class AffiliateMembershipCalculator {

    private AffiliateMembershipCalculator() {
    }

    public static LocalDate calculateEndDate(LocalDate startDate, DurationUnit durationUnit, Integer durationQuantity) {
        return switch (durationUnit) {
            case DIAS -> startDate.plusDays(durationQuantity);
            case MESES -> startDate.plusMonths(durationQuantity);
            case ANIOS -> startDate.plusYears(durationQuantity);
        };
    }

    public static int calculateRemainingUnits(AffiliateMembership affiliateMembership) {
        if (Boolean.TRUE.equals(affiliateMembership.getFrozen())) {
            return affiliateMembership.getRemainingUnits();
        }

        if (affiliateMembership.getTrackingMode() == TrackingMode.MENSUALIDAD) {
            long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), affiliateMembership.getEndDate());
            return (int) Math.max(0, daysLeft);
        }

        return affiliateMembership.getRemainingUnits();
    }
}
