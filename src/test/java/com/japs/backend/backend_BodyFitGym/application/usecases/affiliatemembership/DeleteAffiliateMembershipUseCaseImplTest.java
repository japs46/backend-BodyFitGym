package com.japs.backend.backend_BodyFitGym.application.usecases.affiliatemembership;

import com.japs.backend.backend_BodyFitGym.domain.model.AffiliateMembership;
import com.japs.backend.backend_BodyFitGym.domain.model.Sale;
import com.japs.backend.backend_BodyFitGym.domain.model.SaleStatus;
import com.japs.backend.backend_BodyFitGym.domain.port.in.sale.ICancelSaleUseCase;
import com.japs.backend.backend_BodyFitGym.domain.port.out.AffiliateMembershipRepositoryPort;
import com.japs.backend.backend_BodyFitGym.domain.port.out.SaleRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
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
    @Mock
    private SaleRepositoryPort saleRepositoryPort;
    @Mock
    private ICancelSaleUseCase iCancelSaleUseCase;

    @InjectMocks
    private DeleteAffiliateMembershipUseCaseImpl deleteAffiliateMembershipUseCase;

    @Test
    void deleteAffiliateMembership_eliminaCuandoExiste() {
        when(affiliateMembershipRepositoryPort.findById(1L))
                .thenReturn(Optional.of(AffiliateMembership.builder().id(1L).build()));
        when(saleRepositoryPort.findByAffiliateMembershipId(1L)).thenReturn(List.of());

        deleteAffiliateMembershipUseCase.deleteAffiliateMembership(1L);

        verify(affiliateMembershipRepositoryPort).delete(1L);
        verify(iCancelSaleUseCase, never()).cancelSale(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void deleteAffiliateMembership_lanzaCuandoNoExiste() {
        when(affiliateMembershipRepositoryPort.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> deleteAffiliateMembershipUseCase.deleteAffiliateMembership(99L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("99");

        verify(affiliateMembershipRepositoryPort, never()).delete(99L);
        verify(saleRepositoryPort, never()).findByAffiliateMembershipId(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void deleteAffiliateMembership_anulaSoloLasVentasNoAnuladas() {
        Sale paidSale = Sale.builder().id(5L).status(SaleStatus.PAGADA).build();
        Sale alreadyCancelledSale = Sale.builder().id(6L).status(SaleStatus.ANULADA).build();

        when(affiliateMembershipRepositoryPort.findById(1L))
                .thenReturn(Optional.of(AffiliateMembership.builder().id(1L).build()));
        when(saleRepositoryPort.findByAffiliateMembershipId(1L))
                .thenReturn(List.of(paidSale, alreadyCancelledSale));

        deleteAffiliateMembershipUseCase.deleteAffiliateMembership(1L);

        verify(iCancelSaleUseCase).cancelSale(5L);
        verify(iCancelSaleUseCase, never()).cancelSale(6L);
        verify(affiliateMembershipRepositoryPort).delete(1L);
    }
}
