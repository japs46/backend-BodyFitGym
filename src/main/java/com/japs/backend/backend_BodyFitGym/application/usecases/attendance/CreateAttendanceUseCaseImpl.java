package com.japs.backend.backend_BodyFitGym.application.usecases.attendance;

import com.japs.backend.backend_BodyFitGym.application.dto.AffiliateMembershipSearchCriteria;
import com.japs.backend.backend_BodyFitGym.domain.model.AffiliateMembership;
import com.japs.backend.backend_BodyFitGym.domain.model.Attendance;
import com.japs.backend.backend_BodyFitGym.domain.model.SubscriptionStatus;
import com.japs.backend.backend_BodyFitGym.domain.model.TrackingMode;
import com.japs.backend.backend_BodyFitGym.domain.port.in.attendance.ICreateAttendanceUseCase;
import com.japs.backend.backend_BodyFitGym.domain.port.out.AffiliateMembershipRepositoryPort;
import com.japs.backend.backend_BodyFitGym.domain.port.out.AffiliateRepositoryPort;
import com.japs.backend.backend_BodyFitGym.domain.port.out.AttendanceRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;

/**
 * El check-in de un afiliado. Todas las validaciones se resuelven ANTES de
 * guardar nada y antes de tocar remainingUnits — a diferencia del sistema
 * legado, donde el descuento de visitas restantes ocurría antes de validar
 * si la suscripción estaba congelada o si aún no había iniciado, causando
 * que un check-in rechazado igual consumiera una visita.
 */
@RequiredArgsConstructor
@Component
public class CreateAttendanceUseCaseImpl implements ICreateAttendanceUseCase {

    private final AttendanceRepositoryPort attendanceRepositoryPort;
    private final AffiliateRepositoryPort affiliateRepositoryPort;
    private final AffiliateMembershipRepositoryPort affiliateMembershipRepositoryPort;

    @Override
    public Attendance createAttendance(Long affiliateId) {

        affiliateRepositoryPort.findById(affiliateId)
                .orElseThrow(() -> new NoSuchElementException("No existe ningún afiliado con el ID: " + affiliateId));

        LocalDate today = LocalDate.now();

        if (attendanceRepositoryPort.existsByAffiliateIdOnDate(affiliateId, today)) {
            throw new IllegalStateException("El afiliado ya registró asistencia el día de hoy.");
        }

        AffiliateMembership subscription = findActiveSubscription(affiliateId);

        if (Boolean.TRUE.equals(subscription.getFrozen())) {
            throw new IllegalStateException("La suscripción del afiliado se encuentra congelada.");
        }

        if (today.isBefore(subscription.getStartDate())) {
            throw new IllegalStateException("La suscripción del afiliado inicia el "
                    + subscription.getStartDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ".");
        }

        if (today.isAfter(subscription.getEndDate())) {
            throw new IllegalStateException("La membresía del afiliado ya venció.");
        }

        if (subscription.getTrackingMode() == TrackingMode.ASISTENCIA
                && (subscription.getRemainingUnits() == null || subscription.getRemainingUnits() <= 0)) {
            throw new IllegalStateException("Al afiliado no le quedan visitas disponibles en su membresía.");
        }

        Attendance attendance = Attendance.builder()
                .affiliateId(affiliateId)
                .affiliateMembershipId(subscription.getId())
                .attendanceDate(LocalDateTime.now())
                .build();

        Attendance saved = attendanceRepositoryPort.save(attendance);

        if (subscription.getTrackingMode() == TrackingMode.ASISTENCIA) {
            int remaining = subscription.getRemainingUnits() - 1;
            AffiliateMembership.AffiliateMembershipBuilder updatedSubscription = subscription.toBuilder()
                    .remainingUnits(remaining);
            if (remaining <= 0) {
                updatedSubscription.status(SubscriptionStatus.INACTIVA);
            }
            affiliateMembershipRepositoryPort.save(updatedSubscription.build());
        }

        return saved;
    }

    private AffiliateMembership findActiveSubscription(Long affiliateId) {
        AffiliateMembershipSearchCriteria criteria = AffiliateMembershipSearchCriteria.builder()
                .affiliateId(affiliateId)
                .status(SubscriptionStatus.ACTIVA)
                .build();

        Page<AffiliateMembership> page = affiliateMembershipRepositoryPort.search(criteria, PageRequest.of(0, 5));

        if (page.isEmpty()) {
            throw new IllegalStateException("El afiliado no tiene una membresía activa.");
        }

        return page.getContent().get(0);
    }
}
