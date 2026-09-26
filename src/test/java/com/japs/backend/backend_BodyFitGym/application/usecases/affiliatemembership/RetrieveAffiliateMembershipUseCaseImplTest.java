package com.japs.backend.backend_BodyFitGym.application.usecases.affiliatemembership;

import com.japs.backend.backend_BodyFitGym.application.dto.AffiliateMembershipSearchCriteria;
import com.japs.backend.backend_BodyFitGym.domain.model.AffiliateMembership;
import com.japs.backend.backend_BodyFitGym.domain.model.TrackingMode;
import com.japs.backend.backend_BodyFitGym.domain.port.out.AffiliateMembershipRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RetrieveAffiliateMembershipUseCaseImplTest {

    @Mock
    private AffiliateMembershipRepositoryPort affiliateMembershipRepositoryPort;

    @InjectMocks
    private RetrieveAffiliateMembershipUseCaseImpl retrieveAffiliateMembershipUseCase;

    @Test
    void getAffiliateMembershipById_lanzaCuandoIdEsNull() {
        assertThatThrownBy(() -> retrieveAffiliateMembershipUseCase.getAffiliateMembershipById(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void getAffiliateMembershipById_lanzaCuandoIdNoEsPositivo() {
        assertThatThrownBy(() -> retrieveAffiliateMembershipUseCase.getAffiliateMembershipById(0L))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> retrieveAffiliateMembershipUseCase.getAffiliateMembershipById(-1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void getAffiliateMembershipById_lanzaNoSuchElementCuandoNoExiste() {
        when(affiliateMembershipRepositoryPort.findById(50L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> retrieveAffiliateMembershipUseCase.getAffiliateMembershipById(50L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("50");
    }

    @Test
    void getAffiliateMembershipById_recalculaRemainingUnitsEnVivo() {
        AffiliateMembership stored = AffiliateMembership.builder()
                .id(1L)
                .trackingMode(TrackingMode.MENSUALIDAD)
                .endDate(LocalDate.now().plusDays(15))
                .remainingUnits(999) // valor viejo/desactualizado en BD
                .frozen(false)
                .build();
        when(affiliateMembershipRepositoryPort.findById(1L)).thenReturn(Optional.of(stored));

        AffiliateMembership result = retrieveAffiliateMembershipUseCase.getAffiliateMembershipById(1L);

        assertThat(result.getRemainingUnits()).isEqualTo(15);
    }

    @Test
    void search_delegaAlPuertoYRecalculaCadaElemento() {
        AffiliateMembershipSearchCriteria criteria = AffiliateMembershipSearchCriteria.builder().affiliateId(4L).build();
        Pageable pageable = PageRequest.of(0, 10);
        AffiliateMembership item = AffiliateMembership.builder()
                .id(1L)
                .trackingMode(TrackingMode.MENSUALIDAD)
                .endDate(LocalDate.now().plusDays(5))
                .remainingUnits(0)
                .frozen(false)
                .build();
        Page<AffiliateMembership> page = new PageImpl<>(List.of(item));
        when(affiliateMembershipRepositoryPort.search(criteria, pageable)).thenReturn(page);

        Page<AffiliateMembership> result = retrieveAffiliateMembershipUseCase.search(criteria, pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getRemainingUnits()).isEqualTo(5);
    }
}
