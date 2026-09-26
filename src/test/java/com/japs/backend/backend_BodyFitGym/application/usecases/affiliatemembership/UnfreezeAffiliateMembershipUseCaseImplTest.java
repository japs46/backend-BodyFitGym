package com.japs.backend.backend_BodyFitGym.application.usecases.affiliatemembership;

import com.japs.backend.backend_BodyFitGym.domain.model.AffiliateMembership;
import com.japs.backend.backend_BodyFitGym.domain.model.SubscriptionStatus;
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
class UnfreezeAffiliateMembershipUseCaseImplTest {

    @Mock
    private AffiliateMembershipRepositoryPort affiliateMembershipRepositoryPort;

    @InjectMocks
    private UnfreezeAffiliateMembershipUseCaseImpl unfreezeAffiliateMembershipUseCase;

    @Test
    void unfreeze_lanzaCuandoNoExiste() {
        when(affiliateMembershipRepositoryPort.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> unfreezeAffiliateMembershipUseCase.unfreeze(99L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("99");

        verify(affiliateMembershipRepositoryPort, never()).save(any());
    }

    @Test
    void unfreeze_lanzaCuandoNoEstaCongelada() {
        AffiliateMembership notFrozen = AffiliateMembership.builder().id(1L).frozen(false).build();
        when(affiliateMembershipRepositoryPort.findById(1L)).thenReturn(Optional.of(notFrozen));

        assertThatThrownBy(() -> unfreezeAffiliateMembershipUseCase.unfreeze(1L))
                .isInstanceOf(IllegalStateException.class);

        verify(affiliateMembershipRepositoryPort, never()).save(any());
    }

    @Test
    void unfreeze_recalculaFechaFinDesdeHoy_noDesdeLaFechaDeInicioOriginal() {
        // Congelada hace mucho tiempo: si se recalculara desde el inicio original
        // (el bug del legado), la fecha fin quedaría en el pasado.
        AffiliateMembership existing = AffiliateMembership.builder()
                .id(1L)
                .startDate(LocalDate.now().minusYears(1))
                .endDate(LocalDate.now().minusMonths(6)) // snapshot viejo, ya vencido
                .remainingUnits(15)
                .frozen(true)
                .frozenAt(LocalDate.now().minusMonths(3))
                .status(SubscriptionStatus.INACTIVA)
                .build();
        when(affiliateMembershipRepositoryPort.findById(1L)).thenReturn(Optional.of(existing));
        when(affiliateMembershipRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        AffiliateMembership result = unfreezeAffiliateMembershipUseCase.unfreeze(1L);

        ArgumentCaptor<AffiliateMembership> captor = ArgumentCaptor.forClass(AffiliateMembership.class);
        verify(affiliateMembershipRepositoryPort).save(captor.capture());
        AffiliateMembership saved = captor.getValue();

        assertThat(saved.getEndDate()).isEqualTo(LocalDate.now().plusDays(15)); // desde HOY, no desde startDate
        assertThat(saved.getFrozen()).isFalse();
        assertThat(saved.getFrozenAt()).isNull();
        assertThat(saved.getStatus()).isEqualTo(SubscriptionStatus.ACTIVA);
        assertThat(result).isEqualTo(saved);
    }
}
