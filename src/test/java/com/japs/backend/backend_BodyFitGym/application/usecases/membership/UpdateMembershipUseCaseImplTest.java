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
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateMembershipUseCaseImplTest {

    @Mock
    private MembershipRepositoryPort membershipRepositoryPort;

    @InjectMocks
    private UpdateMembershipUseCaseImpl updateMembershipUseCase;

    private Membership existing() {
        return Membership.builder()
                .id(1L)
                .name("Bimestral")
                .description("Plan bimestral")
                .durationUnit(DurationUnit.MESES)
                .durationQuantity(2)
                .trackingMode(TrackingMode.MENSUALIDAD)
                .price(BigDecimal.valueOf(100000))
                .status(MembershipStatus.ACTIVA)
                .build();
    }

    @Test
    void updateMembership_lanzaCuandoNoExiste() {
        when(membershipRepositoryPort.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> updateMembershipUseCase.updateMembership(99L, existing()))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("99");

        verify(membershipRepositoryPort, never()).save(any());
    }

    @Test
    void updateMembership_lanzaCuandoElNuevoNombreYaEstaEnUso() {
        when(membershipRepositoryPort.findById(1L)).thenReturn(Optional.of(existing()));
        when(membershipRepositoryPort.findByNameIgnoreCase("Otro Nombre"))
                .thenReturn(Optional.of(Membership.builder().id(2L).name("Otro Nombre").build()));

        Membership input = existing().toBuilder().name("Otro Nombre").build();

        assertThatThrownBy(() -> updateMembershipUseCase.updateMembership(1L, input))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Otro Nombre");

        verify(membershipRepositoryPort, never()).save(any());
    }

    @Test
    void updateMembership_noRevalidaCuandoElNombreNoCambia() {
        when(membershipRepositoryPort.findById(1L)).thenReturn(Optional.of(existing()));
        when(membershipRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Membership input = existing().toBuilder().price(BigDecimal.valueOf(120000)).build();
        updateMembershipUseCase.updateMembership(1L, input);

        verify(membershipRepositoryPort, never()).findByNameIgnoreCase(any());
    }

    @Test
    void updateMembership_aplicaCambios() {
        when(membershipRepositoryPort.findById(1L)).thenReturn(Optional.of(existing()));
        when(membershipRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Membership input = existing().toBuilder()
                .price(BigDecimal.valueOf(150000))
                .durationQuantity(3)
                .status(MembershipStatus.INACTIVA)
                .build();

        Membership result = updateMembershipUseCase.updateMembership(1L, input);

        assertThat(result.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(150000));
        assertThat(result.getDurationQuantity()).isEqualTo(3);
        assertThat(result.getStatus()).isEqualTo(MembershipStatus.INACTIVA);
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void updateMembership_conservaElEstadoExistenteCuandoNoSeEnvia() {
        when(membershipRepositoryPort.findById(1L)).thenReturn(Optional.of(existing()));
        when(membershipRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Membership input = existing().toBuilder().status(null).build();
        Membership result = updateMembershipUseCase.updateMembership(1L, input);

        assertThat(result.getStatus()).isEqualTo(MembershipStatus.ACTIVA);
    }
}
