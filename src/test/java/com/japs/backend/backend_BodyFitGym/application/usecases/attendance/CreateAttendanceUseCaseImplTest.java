package com.japs.backend.backend_BodyFitGym.application.usecases.attendance;

import com.japs.backend.backend_BodyFitGym.application.dto.AffiliateMembershipSearchCriteria;
import com.japs.backend.backend_BodyFitGym.domain.model.Affiliate;
import com.japs.backend.backend_BodyFitGym.domain.model.AffiliateMembership;
import com.japs.backend.backend_BodyFitGym.domain.model.Attendance;
import com.japs.backend.backend_BodyFitGym.domain.model.SubscriptionStatus;
import com.japs.backend.backend_BodyFitGym.domain.model.TrackingMode;
import com.japs.backend.backend_BodyFitGym.domain.port.out.AffiliateMembershipRepositoryPort;
import com.japs.backend.backend_BodyFitGym.domain.port.out.AffiliateRepositoryPort;
import com.japs.backend.backend_BodyFitGym.domain.port.out.AttendanceRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateAttendanceUseCaseImplTest {

    @Mock
    private AttendanceRepositoryPort attendanceRepositoryPort;
    @Mock
    private AffiliateRepositoryPort affiliateRepositoryPort;
    @Mock
    private AffiliateMembershipRepositoryPort affiliateMembershipRepositoryPort;

    @InjectMocks
    private CreateAttendanceUseCaseImpl createAttendanceUseCase;

    private AffiliateMembership activeSubscription(TrackingMode trackingMode, Integer remainingUnits) {
        return AffiliateMembership.builder()
                .id(10L)
                .affiliateId(4L)
                .membershipId(3L)
                .startDate(LocalDate.now().minusDays(5))
                .endDate(LocalDate.now().plusDays(5))
                .trackingMode(trackingMode)
                .remainingUnits(remainingUnits)
                .status(SubscriptionStatus.ACTIVA)
                .frozen(false)
                .build();
    }

    private void mockAffiliateExists() {
        when(affiliateRepositoryPort.findById(4L)).thenReturn(Optional.of(Affiliate.builder().id(4L).build()));
    }

    private void mockNotAttendedToday() {
        when(attendanceRepositoryPort.existsByAffiliateIdOnDate(eq(4L), any(LocalDate.class))).thenReturn(false);
    }

    private void mockActiveSubscription(AffiliateMembership subscription) {
        when(affiliateMembershipRepositoryPort.search(any(AffiliateMembershipSearchCriteria.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(subscription)));
    }

    @Test
    void createAttendance_lanzaCuandoElAfiliadoNoExiste() {
        when(affiliateRepositoryPort.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> createAttendanceUseCase.createAttendance(99L))
                .isInstanceOf(java.util.NoSuchElementException.class)
                .hasMessageContaining("99");

        verify(attendanceRepositoryPort, never()).save(any());
    }

    @Test
    void createAttendance_lanzaCuandoYaAsistioHoy() {
        mockAffiliateExists();
        when(attendanceRepositoryPort.existsByAffiliateIdOnDate(eq(4L), any(LocalDate.class))).thenReturn(true);

        assertThatThrownBy(() -> createAttendanceUseCase.createAttendance(4L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("hoy");

        verify(attendanceRepositoryPort, never()).save(any());
    }

    @Test
    void createAttendance_lanzaCuandoNoTieneMembresiaActiva() {
        mockAffiliateExists();
        mockNotAttendedToday();
        when(affiliateMembershipRepositoryPort.search(any(AffiliateMembershipSearchCriteria.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of()));

        assertThatThrownBy(() -> createAttendanceUseCase.createAttendance(4L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("no tiene una membresía activa");

        verify(attendanceRepositoryPort, never()).save(any());
    }

    @Test
    void createAttendance_lanzaCuandoLaSuscripcionEstaCongelada() {
        mockAffiliateExists();
        mockNotAttendedToday();
        AffiliateMembership frozen = activeSubscription(TrackingMode.MENSUALIDAD, null).toBuilder().frozen(true).build();
        mockActiveSubscription(frozen);

        assertThatThrownBy(() -> createAttendanceUseCase.createAttendance(4L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("congelada");

        verify(attendanceRepositoryPort, never()).save(any());
        verify(affiliateMembershipRepositoryPort, never()).save(any());
    }

    @Test
    void createAttendance_lanzaCuandoAunNoInicia() {
        mockAffiliateExists();
        mockNotAttendedToday();
        AffiliateMembership future = activeSubscription(TrackingMode.MENSUALIDAD, null).toBuilder()
                .startDate(LocalDate.now().plusDays(3))
                .build();
        mockActiveSubscription(future);

        assertThatThrownBy(() -> createAttendanceUseCase.createAttendance(4L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("inicia");

        verify(attendanceRepositoryPort, never()).save(any());
    }

    @Test
    void createAttendance_lanzaCuandoYaVencio() {
        mockAffiliateExists();
        mockNotAttendedToday();
        AffiliateMembership expired = activeSubscription(TrackingMode.MENSUALIDAD, null).toBuilder()
                .endDate(LocalDate.now().minusDays(1))
                .build();
        mockActiveSubscription(expired);

        assertThatThrownBy(() -> createAttendanceUseCase.createAttendance(4L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("venció");

        verify(attendanceRepositoryPort, never()).save(any());
    }

    @Test
    void createAttendance_lanzaCuandoAsistenciaSinVisitasRestantes() {
        mockAffiliateExists();
        mockNotAttendedToday();
        mockActiveSubscription(activeSubscription(TrackingMode.ASISTENCIA, 0));

        assertThatThrownBy(() -> createAttendanceUseCase.createAttendance(4L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("visitas disponibles");

        verify(attendanceRepositoryPort, never()).save(any());
    }

    @Test
    void createAttendance_mensualidad_guardaAsistenciaYNoTocaLaSuscripcion() {
        mockAffiliateExists();
        mockNotAttendedToday();
        mockActiveSubscription(activeSubscription(TrackingMode.MENSUALIDAD, null));
        when(attendanceRepositoryPort.save(any())).thenAnswer(inv -> {
            Attendance a = inv.getArgument(0);
            return a.toBuilder().id(1L).build();
        });

        Attendance result = createAttendanceUseCase.createAttendance(4L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getAffiliateMembershipId()).isEqualTo(10L);
        verify(affiliateMembershipRepositoryPort, never()).save(any());
    }

    @Test
    void createAttendance_asistencia_decrementaRemainingUnitsYMantieneActiva() {
        mockAffiliateExists();
        mockNotAttendedToday();
        mockActiveSubscription(activeSubscription(TrackingMode.ASISTENCIA, 3));
        when(attendanceRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        createAttendanceUseCase.createAttendance(4L);

        ArgumentCaptor<AffiliateMembership> captor = ArgumentCaptor.forClass(AffiliateMembership.class);
        verify(affiliateMembershipRepositoryPort).save(captor.capture());
        assertThat(captor.getValue().getRemainingUnits()).isEqualTo(2);
        assertThat(captor.getValue().getStatus()).isEqualTo(SubscriptionStatus.ACTIVA);
    }

    @Test
    void createAttendance_asistencia_alAgotarseMarcaInactiva() {
        mockAffiliateExists();
        mockNotAttendedToday();
        mockActiveSubscription(activeSubscription(TrackingMode.ASISTENCIA, 1));
        when(attendanceRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        createAttendanceUseCase.createAttendance(4L);

        ArgumentCaptor<AffiliateMembership> captor = ArgumentCaptor.forClass(AffiliateMembership.class);
        verify(affiliateMembershipRepositoryPort).save(captor.capture());
        assertThat(captor.getValue().getRemainingUnits()).isEqualTo(0);
        assertThat(captor.getValue().getStatus()).isEqualTo(SubscriptionStatus.INACTIVA);
    }
}
