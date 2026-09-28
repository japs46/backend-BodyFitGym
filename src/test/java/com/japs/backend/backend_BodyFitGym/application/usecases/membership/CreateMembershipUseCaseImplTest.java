package com.japs.backend.backend_BodyFitGym.application.usecases.membership;

import com.japs.backend.backend_BodyFitGym.domain.model.DurationUnit;
import com.japs.backend.backend_BodyFitGym.domain.model.Membership;
import com.japs.backend.backend_BodyFitGym.domain.model.MembershipStatus;
import com.japs.backend.backend_BodyFitGym.domain.model.TrackingMode;
import com.japs.backend.backend_BodyFitGym.domain.port.out.MembershipRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateMembershipUseCaseImplTest {

    @Mock
    private MembershipRepositoryPort membershipRepositoryPort;

    @InjectMocks
    private CreateMembershipUseCaseImpl createMembershipUseCase;

    private Membership newMembership() {
        return Membership.builder()
                .name("Bimestral")
                .durationUnit(DurationUnit.MESES)
                .durationQuantity(2)
                .trackingMode(TrackingMode.MENSUALIDAD)
                .price(BigDecimal.valueOf(100000))
                .build();
    }

    @Test
    void createMembership_guardaCuandoElNombreNoExiste() {
        Membership input = newMembership();
        when(membershipRepositoryPort.findByNameIgnoreCase("Bimestral")).thenReturn(Optional.empty());
        when(membershipRepositoryPort.save(input)).thenReturn(input.toBuilder().id(1L).status(MembershipStatus.ACTIVA).build());

        Membership result = createMembershipUseCase.createMembership(input);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getStatus()).isEqualTo(MembershipStatus.ACTIVA);
        verify(membershipRepositoryPort).save(input);
    }

    @Test
    void createMembership_lanzaExcepcionCuandoElNombreYaExiste() {
        Membership input = newMembership();
        when(membershipRepositoryPort.findByNameIgnoreCase("Bimestral"))
                .thenReturn(Optional.of(Membership.builder().id(99L).name("Bimestral").build()));

        assertThatThrownBy(() -> createMembershipUseCase.createMembership(input))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Bimestral");

        verify(membershipRepositoryPort, never()).save(any());
    }

    @Test
    void createMembership_noSobreescribeStatusSiYaViaDefinido() {
        Membership input = newMembership().toBuilder().status(MembershipStatus.INACTIVA).build();
        when(membershipRepositoryPort.findByNameIgnoreCase("Bimestral")).thenReturn(Optional.empty());
        when(membershipRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Membership result = createMembershipUseCase.createMembership(input);

        assertThat(result.getStatus()).isEqualTo(MembershipStatus.INACTIVA);
    }
}
