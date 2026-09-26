package com.japs.backend.backend_BodyFitGym.application.usecases.affiliatemembership;

import com.japs.backend.backend_BodyFitGym.domain.model.AffiliateMembership;
import com.japs.backend.backend_BodyFitGym.domain.port.out.AffiliateMembershipRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteAffiliateMembershipUseCaseImplTest {

    @Mock
    private AffiliateMembershipRepositoryPort affiliateMembershipRepositoryPort;

    @InjectMocks
    private DeleteAffiliateMembershipUseCaseImpl deleteAffiliateMembershipUseCase;

    @Test
    void deleteAffiliateMembership_eliminaCuandoExiste() {
        when(affiliateMembershipRepositoryPort.findById(1L))
                .thenReturn(Optional.of(AffiliateMembership.builder().id(1L).build()));

        deleteAffiliateMembershipUseCase.deleteAffiliateMembership(1L);

        verify(affiliateMembershipRepositoryPort).delete(1L);
    }

    @Test
    void deleteAffiliateMembership_lanzaCuandoNoExiste() {
        when(affiliateMembershipRepositoryPort.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> deleteAffiliateMembershipUseCase.deleteAffiliateMembership(99L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("99");

        verify(affiliateMembershipRepositoryPort, never()).delete(99L);
    }
}
