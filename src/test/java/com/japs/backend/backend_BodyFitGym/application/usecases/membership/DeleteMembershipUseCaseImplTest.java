package com.japs.backend.backend_BodyFitGym.application.usecases.membership;

import com.japs.backend.backend_BodyFitGym.domain.model.Membership;
import com.japs.backend.backend_BodyFitGym.domain.port.out.MembershipRepositoryPort;
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
class DeleteMembershipUseCaseImplTest {

    @Mock
    private MembershipRepositoryPort membershipRepositoryPort;

    @InjectMocks
    private DeleteMembershipUseCaseImpl deleteMembershipUseCase;

    @Test
    void deleteMembership_eliminaCuandoExiste() {
        when(membershipRepositoryPort.findById(1L))
                .thenReturn(Optional.of(Membership.builder().id(1L).build()));

        deleteMembershipUseCase.deleteMembership(1L);

        verify(membershipRepositoryPort).delete(1L);
    }

    @Test
    void deleteMembership_lanzaCuandoNoExiste() {
        when(membershipRepositoryPort.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> deleteMembershipUseCase.deleteMembership(99L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("99");

        verify(membershipRepositoryPort, never()).delete(99L);
    }
}
