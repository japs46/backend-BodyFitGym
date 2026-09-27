package com.japs.backend.backend_BodyFitGym.application.usecases.sale;

import com.japs.backend.backend_BodyFitGym.domain.model.AffiliateMembership;
import com.japs.backend.backend_BodyFitGym.domain.model.Membership;
import com.japs.backend.backend_BodyFitGym.domain.model.Product;
import com.japs.backend.backend_BodyFitGym.domain.model.Sale;
import com.japs.backend.backend_BodyFitGym.domain.model.SaleStatus;
import com.japs.backend.backend_BodyFitGym.domain.model.SaleType;
import com.japs.backend.backend_BodyFitGym.domain.port.in.sale.ICreateSaleUseCase;
import com.japs.backend.backend_BodyFitGym.domain.port.out.AffiliateMembershipRepositoryPort;
import com.japs.backend.backend_BodyFitGym.domain.port.out.AffiliateRepositoryPort;
import com.japs.backend.backend_BodyFitGym.domain.port.out.MembershipRepositoryPort;
import com.japs.backend.backend_BodyFitGym.domain.port.out.ProductRepositoryPort;
import com.japs.backend.backend_BodyFitGym.domain.port.out.SaleRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

/**
 * El precio se congela en el momento de la venta (unitPrice/total), tomado del
 * catálogo (Product o Membership) — así una venta histórica no cambia si luego
 * se edita el precio del catálogo.
 */
@RequiredArgsConstructor
@Component
public class CreateSaleUseCaseImpl implements ICreateSaleUseCase {

    private final SaleRepositoryPort saleRepositoryPort;
    private final AffiliateRepositoryPort affiliateRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;
    private final AffiliateMembershipRepositoryPort affiliateMembershipRepositoryPort;
    private final MembershipRepositoryPort membershipRepositoryPort;

    @Override
    public Sale createSale(Sale sale) {

        affiliateRepositoryPort.findById(sale.getAffiliateId())
                .orElseThrow(() -> new NoSuchElementException("No existe ningún afiliado con el ID: " + sale.getAffiliateId()));

        Sale.SaleBuilder builder = sale.toBuilder();

        if (sale.getType() == SaleType.PRODUCTO) {
            resolveProductSale(sale, builder);
        } else {
            resolveAffiliationSale(sale, builder);
        }

        builder.status(SaleStatus.PAGADA)
                .saleDate(LocalDateTime.now());

        return saleRepositoryPort.save(builder.build());
    }

    private void resolveProductSale(Sale sale, Sale.SaleBuilder builder) {
        if (sale.getProductId() == null) {
            throw new IllegalArgumentException("El producto es obligatorio para una venta de producto.");
        }

        if (sale.getAffiliateMembershipId() != null) {
            throw new IllegalArgumentException("Una venta de producto no debe referenciar una afiliación.");
        }

        if (sale.getQuantity() == null || sale.getQuantity() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0.");
        }

        Product product = productRepositoryPort.findById(sale.getProductId())
                .orElseThrow(() -> new NoSuchElementException("No existe ningún producto con el ID: " + sale.getProductId()));

        int availableStock = product.getQuantity() == null ? 0 : product.getQuantity();
        if (availableStock < sale.getQuantity()) {
            throw new IllegalStateException("Stock insuficiente. Disponible: " + availableStock);
        }

        BigDecimal total = product.getPrice().multiply(BigDecimal.valueOf(sale.getQuantity()));

        builder.unitPrice(product.getPrice())
                .total(total);

        productRepositoryPort.save(product.toBuilder()
                .quantity(availableStock - sale.getQuantity())
                .build());
    }

    private void resolveAffiliationSale(Sale sale, Sale.SaleBuilder builder) {
        if (sale.getAffiliateMembershipId() == null) {
            throw new IllegalArgumentException("La afiliación es obligatoria para una venta de afiliación.");
        }

        if (sale.getProductId() != null) {
            throw new IllegalArgumentException("Una venta de afiliación no debe referenciar un producto.");
        }

        AffiliateMembership subscription = affiliateMembershipRepositoryPort.findById(sale.getAffiliateMembershipId())
                .orElseThrow(() -> new NoSuchElementException("No existe ninguna afiliación con el ID: " + sale.getAffiliateMembershipId()));

        if (!subscription.getAffiliateId().equals(sale.getAffiliateId())) {
            throw new IllegalArgumentException("La afiliación no pertenece al afiliado indicado.");
        }

        Membership membership = membershipRepositoryPort.findById(subscription.getMembershipId())
                .orElseThrow(() -> new NoSuchElementException("No existe ninguna membresía con el ID: " + subscription.getMembershipId()));

        builder.quantity(1)
                .unitPrice(membership.getPrice())
                .total(membership.getPrice());
    }
}
