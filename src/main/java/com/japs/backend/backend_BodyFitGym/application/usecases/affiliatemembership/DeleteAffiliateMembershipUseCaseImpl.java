package com.japs.backend.backend_BodyFitGym.application.usecases.affiliatemembership;

import com.japs.backend.backend_BodyFitGym.domain.model.AffiliateMembership;
import com.japs.backend.backend_BodyFitGym.domain.model.Sale;
import com.japs.backend.backend_BodyFitGym.domain.model.SaleStatus;
import com.japs.backend.backend_BodyFitGym.domain.port.in.affiliatemembership.IDeleteAffiliateMembershipUseCase;
import com.japs.backend.backend_BodyFitGym.domain.port.in.sale.ICancelSaleUseCase;
import com.japs.backend.backend_BodyFitGym.domain.port.out.AffiliateMembershipRepositoryPort;
import com.japs.backend.backend_BodyFitGym.domain.port.out.SaleRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

/**
 * Anula las ventas asociadas antes de borrar la afiliación — al eliminarla
 * físicamente, una venta que quedara con estado PAGADA referenciaría un
 * affiliateMembershipId inexistente.
 */
@RequiredArgsConstructor
@Component
public class DeleteAffiliateMembershipUseCaseImpl implements IDeleteAffiliateMembershipUseCase {

    private final AffiliateMembershipRepositoryPort affiliateMembershipRepositoryPort;
    private final SaleRepositoryPort saleRepositoryPort;
    private final ICancelSaleUseCase iCancelSaleUseCase;

    @Override
    public void deleteAffiliateMembership(Long id) {

        AffiliateMembership existing = affiliateMembershipRepositoryPort.findById(id).orElse(null);
        if (existing == null) {
            throw new NoSuchElementException("No existe ninguna suscripción con el ID: " + id);
        }

        for (Sale sale : saleRepositoryPort.findByAffiliateMembershipId(id)) {
            if (sale.getStatus() != SaleStatus.ANULADA) {
                iCancelSaleUseCase.cancelSale(sale.getId());
            }
        }

        affiliateMembershipRepositoryPort.delete(id);
    }
}
