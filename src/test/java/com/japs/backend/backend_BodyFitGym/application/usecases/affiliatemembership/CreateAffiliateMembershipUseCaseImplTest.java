package com.japs.backend.backend_BodyFitGym.application.usecases.affiliatemembership;

import com.japs.backend.backend_BodyFitGym.domain.model.Affiliate;
import com.japs.backend.backend_BodyFitGym.domain.model.AffiliateMembership;
import com.japs.backend.backend_BodyFitGym.domain.model.DurationUnit;
import com.japs.backend.backend_BodyFitGym.domain.model.Membership;
import com.japs.backend.backend_BodyFitGym.domain.model.MembershipStatus;
import com.japs.backend.backend_BodyFitGym.domain.model.SubscriptionStatus;
import com.japs.backend.backend_BodyFitGym.domain.model.TrackingMode;
import com.japs.backend.backend_BodyFitGym.domain.port.out.AffiliateMembershipRepositoryPort;
import com.japs.backend.backend_BodyFitGym.domain.port.out.AffiliateRepositoryPort;
import com.japs.backend.backend_BodyFitGym.domain.port.out.MembershipRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
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
class CreateAffiliateMembershipUseCaseImplTest {

    @Mock
    private AffiliateMembershipRepositoryPort affiliateMembershipRepositoryPort;
    @Mock
    private AffiliateRepositoryPort affiliateRepositoryPort;
    @Mock
    private MembershipRepositoryPort membershipRepositoryPort;

    @InjectMocks
    private CreateAffiliateMembershipUseCaseImpl createAffiliateMembershipUseCase;

    private Membership activeMembership() {
        return Membership.builder()
                .id(3L)
                .name("Bimestral")
                .durationUnit(DurationUnit.MESES)
                .durationQuantity(2)
                .trackingMode(TrackingMode.MENSUALIDAD)
                .price(BigDecimal.valueOf(100000))
                .status(MembershipStatus.ACTIVA)
                .build();
    }

    @Test
    void createAffiliateMembership_lanzaCuandoElAfiliadoNoExiste() {
        AffiliateMembership input = AffiliateMembership.builder().affiliateId(99L).membershipId(3L).build();
        when(affiliateRepositoryPort.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> createAffiliateMembershipUseCase.createAffiliateMembership(input))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("99");

        verify(affiliateMembershipRepositoryPort, never()).save(any());
    }

    @Test
    void createAffiliateMembership_lanzaCuandoLaMembresiaNoExiste() {
        AffiliateMembership input = AffiliateMembership.builder().affiliateId(4L).membershipId(77L).build();
        when(affiliateRepositoryPort.findById(4L)).thenReturn(Optional.of(Affiliate.builder().id(4L).build()));
        when(membershipRepositoryPort.findById(77L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> createAffiliateMembershipUseCase.createAffiliateMembership(input))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("77");

        verify(affiliateMembershipRepositoryPort, never()).save(any());
    }

    @Test
    void createAffiliateMembership_lanzaCuandoLaMembresiaNoEstaActiva() {
        AffiliateMembership input = AffiliateMembership.builder().affiliateId(4L).membershipId(3L).build();
        Membership inactiveMembership = activeMembership().toBuilder().status(MembershipStatus.INACTIVA).build();

        when(affiliateRepositoryPort.findById(4L)).thenReturn(Optional.of(Affiliate.builder().id(4L).build()));
        when(membershipRepositoryPort.findById(3L)).thenReturn(Optional.of(inactiveMembership));

        assertThatThrownBy(() -> createAffiliateMembershipUseCase.createAffiliateMembership(input))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Bimestral");

        verify(affiliateMembershipRepositoryPort, never()).save(any());
    }

    @Test
    void createAffiliateMembership_calculaFechaFinYCopiaDatosDeLaMembresia() {
        AffiliateMembership input = AffiliateMembership.builder().affiliateId(4L).membershipId(3L).build();

        when(affiliateRepositoryPort.findById(4L)).thenReturn(Optional.of(Affiliate.builder().id(4L).build()));
        when(membershipRepositoryPort.findById(3L)).thenReturn(Optional.of(activeMembership()));
        when(affiliateMembershipRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        AffiliateMembership result = createAffiliateMembershipUseCase.createAffiliateMembership(input);

        ArgumentCaptor<AffiliateMembership> captor = ArgumentCaptor.forClass(AffiliateMembership.class);
        verify(affiliateMembershipRepositoryPort).save(captor.capture());
        AffiliateMembership saved = captor.getValue();

        LocalDate today = LocalDate.now();
        assertThat(saved.getStartDate()).isEqualTo(today);
        assertThat(saved.getEndDate()).isEqualTo(today.plusMonths(2));
        assertThat(saved.getTrackingMode()).isEqualTo(TrackingMode.MENSUALIDAD);
        assertThat(saved.getRemainingUnits()).isEqualTo(2);
        assertThat(saved.getStatus()).isEqualTo(SubscriptionStatus.ACTIVA);
        assertThat(saved.getFrozen()).isFalse();
        assertThat(saved.getCreatedAt()).isEqualTo(today);
        assertThat(result).isEqualTo(saved);
    }

    @Test
    void createAffiliateMembership_respetaStartDateExplicito() {
        LocalDate customStart = LocalDate.of(2026, 1, 1);
        AffiliateMembership input = AffiliateMembership.builder()
                .affiliateId(4L).membershipId(3L).startDate(customStart).build();

        when(affiliateRepositoryPort.findById(4L)).thenReturn(Optional.of(Affiliate.builder().id(4L).build()));
        when(membershipRepositoryPort.findById(3L)).thenReturn(Optional.of(activeMembership()));
        when(affiliateMembershipRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        AffiliateMembership result = createAffiliateMembershipUseCase.createAffiliateMembership(input);

        assertThat(result.getStartDate()).isEqualTo(customStart);
        assertThat(result.getEndDate()).isEqualTo(LocalDate.of(2026, 3, 1));
    }
}
