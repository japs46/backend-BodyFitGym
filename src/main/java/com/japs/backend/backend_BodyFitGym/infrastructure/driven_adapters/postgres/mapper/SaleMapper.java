package com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.mapper;

import com.japs.backend.backend_BodyFitGym.domain.model.Sale;
import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.entities.SaleEntity;

public class SaleMapper {

    public static SaleEntity toEntity(Sale sale) {

        if (sale == null)
            return null;

        return SaleEntity.builder()
                .id(sale.getId())
                .affiliateId(sale.getAffiliateId())
                .type(sale.getType())
                .productId(sale.getProductId())
                .affiliateMembershipId(sale.getAffiliateMembershipId())
                .quantity(sale.getQuantity())
                .unitPrice(sale.getUnitPrice())
                .total(sale.getTotal())
                .observation(sale.getObservation())
                .status(sale.getStatus())
                .saleDate(sale.getSaleDate())
                .createdAt(sale.getCreatedAt())
                .build();
    }

    public static Sale toModel(SaleEntity saleEntity) {

        if (saleEntity == null)
            return null;

        return Sale.builder()
                .id(saleEntity.getId())
                .affiliateId(saleEntity.getAffiliateId())
                .type(saleEntity.getType())
                .productId(saleEntity.getProductId())
                .affiliateMembershipId(saleEntity.getAffiliateMembershipId())
                .quantity(saleEntity.getQuantity())
                .unitPrice(saleEntity.getUnitPrice())
                .total(saleEntity.getTotal())
                .observation(saleEntity.getObservation())
                .status(saleEntity.getStatus())
                .saleDate(saleEntity.getSaleDate())
                .createdAt(saleEntity.getCreatedAt())
                .build();
    }
}
