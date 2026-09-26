package com.japs.backend.backend_BodyFitGym.application.usecases.affiliatemembership;

import com.japs.backend.backend_BodyFitGym.domain.model.AffiliateMembership;
import com.japs.backend.backend_BodyFitGym.domain.model.SubscriptionStatus;
import com.japs.backend.backend_BodyFitGym.domain.model.TrackingMode;
import com.japs.backend.backend_BodyFitGym.domain.port.out.AffiliateMembershipRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateAffiliateMembershipUseCaseImplTest {

    @Mock
    private AffiliateMembershipRepositoryPort affiliateMembershipRepositoryPort;

    @InjectMocks
    private UpdateAffiliateMembershipUseCaseImpl updateAffiliateMembershipUseCase;

    private AffiliateMembership existing() {
        return AffiliateMembership.builder()
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
    }

    @Test
    void updateAffiliateMembership_lanzaCuandoNoExiste() {
        when(affiliateMembershipRepositoryPort.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> updateAffiliateMembershipUseCase.updateAffiliateMembership(99L, existing()))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("99");

        verify(affiliateMembershipRepositoryPort, never()).save(any());
    }

    @Test
    void updateAffiliateMembership_aplicaCamposProvistosYPreservaElResto() {
        when(affiliateMembershipRepositoryPort.findById(1L)).thenReturn(Optional.of(existing()));
        when(affiliateMembershipRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        AffiliateMembership input = AffiliateMembership.builder()
                .status(SubscriptionStatus.INACTIVA)
                .remainingUnits(10)
                .build();

        AffiliateMembership result = updateAffiliateMembershipUseCase.updateAffiliateMembership(1L, input);

        assertThat(result.getStatus()).isEqualTo(SubscriptionStatus.INACTIVA);
        assertThat(result.getRemainingUnits()).isEqualTo(10);
        // no provistos en el input -> se preservan
        assertThat(result.getStartDate()).isEqualTo(LocalDate.of(2026, 1, 1));
        assertThat(result.getEndDate()).isEqualTo(LocalDate.of(2026, 3, 1));
        // nunca editables desde update -> siempre se preservan
        assertThat(result.getAffiliateId()).isEqualTo(4L);
        assertThat(result.getMembershipId()).isEqualTo(3L);
        assertThat(result.getTrackingMode()).isEqualTo(TrackingMode.MENSUALIDAD);
        assertThat(result.getFrozen()).isFalse();
        assertThat(result.getCreatedAt()).isEqualTo(LocalDate.of(2026, 1, 1));
    }

    @Test
    void updateAffiliateMembership_noSobreescribeAffiliateIdNiMembershipIdAunqueVenganEnElInput() {
        when(affiliateMembershipRepositoryPort.findById(1L)).thenReturn(Optional.of(existing()));
        when(affiliateMembershipRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Intento (inválido por diseño) de reasignar afiliado/membresía vía update
        AffiliateMembership input = AffiliateMembership.builder()
                .affiliateId(999L)
                .membershipId(888L)
                .build();

        ArgumentCaptor<AffiliateMembership> captor = ArgumentCaptor.forClass(AffiliateMembership.class);
        updateAffiliateMembershipUseCase.updateAffiliateMembership(1L, input);
        verify(affiliateMembershipRepositoryPort).save(captor.capture());

        assertThat(captor.getValue().getAffiliateId()).isEqualTo(4L);
        assertThat(captor.getValue().getMembershipId()).isEqualTo(3L);
    }
}
