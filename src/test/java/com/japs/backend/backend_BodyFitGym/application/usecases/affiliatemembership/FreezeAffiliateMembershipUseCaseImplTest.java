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
class FreezeAffiliateMembershipUseCaseImplTest {

    @Mock
    private AffiliateMembershipRepositoryPort affiliateMembershipRepositoryPort;

    @InjectMocks
    private FreezeAffiliateMembershipUseCaseImpl freezeAffiliateMembershipUseCase;

    @Test
    void freeze_lanzaCuandoNoExiste() {
        when(affiliateMembershipRepositoryPort.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> freezeAffiliateMembershipUseCase.freeze(99L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("99");

        verify(affiliateMembershipRepositoryPort, never()).save(any());
    }

    @Test
    void freeze_lanzaCuandoYaEstaCongelada() {
        AffiliateMembership alreadyFrozen = AffiliateMembership.builder().id(1L).frozen(true).build();
        when(affiliateMembershipRepositoryPort.findById(1L)).thenReturn(Optional.of(alreadyFrozen));

        assertThatThrownBy(() -> freezeAffiliateMembershipUseCase.freeze(1L))
                .isInstanceOf(IllegalStateException.class);

        verify(affiliateMembershipRepositoryPort, never()).save(any());
    }

    @Test
    void freeze_tomaFotoDeRemainingUnitsYCambiaEstado() {
        AffiliateMembership existing = AffiliateMembership.builder()
                .id(1L)
                .trackingMode(TrackingMode.MENSUALIDAD)
                .endDate(LocalDate.now().plusDays(20))
                .remainingUnits(0) // valor viejo, debe recalcularse antes de congelar
                .frozen(false)
                .status(SubscriptionStatus.ACTIVA)
                .build();
        when(affiliateMembershipRepositoryPort.findById(1L)).thenReturn(Optional.of(existing));
        when(affiliateMembershipRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        AffiliateMembership result = freezeAffiliateMembershipUseCase.freeze(1L);

        ArgumentCaptor<AffiliateMembership> captor = ArgumentCaptor.forClass(AffiliateMembership.class);
        verify(affiliateMembershipRepositoryPort).save(captor.capture());
        AffiliateMembership saved = captor.getValue();

        assertThat(saved.getRemainingUnits()).isEqualTo(20); // snapshot tomado al congelar
        assertThat(saved.getFrozen()).isTrue();
        assertThat(saved.getFrozenAt()).isEqualTo(LocalDate.now());
        assertThat(saved.getStatus()).isEqualTo(SubscriptionStatus.INACTIVA);
        assertThat(result).isEqualTo(saved);
    }
}
