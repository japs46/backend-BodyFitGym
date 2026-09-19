package com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.mapper;

import com.japs.backend.backend_BodyFitGym.domain.model.Affiliate;
import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.entities.AffiliateEntity;

public class AffiliateMapper {

    public static AffiliateEntity toEntity(Affiliate affiliate) {

        if (affiliate == null)
            return null;

        return AffiliateEntity.builder()
                .id(affiliate.getId())
                .identification(affiliate.getIdentification())
                .firstName(affiliate.getFirstName())
                .middleName(affiliate.getMiddleName())
                .lastName(affiliate.getLastName())
                .secondLastName(affiliate.getSecondLastName())
                .sex(affiliate.getSex())
                .birthDate(affiliate.getBirthDate())
                .affiliationDate(affiliate.getAffiliationDate())
                .address(affiliate.getAddress())
                .neighborhood(affiliate.getNeighborhood())
                .city(affiliate.getCity())
                .status(affiliate.getStatus())
                .postalCode(affiliate.getPostalCode())
                .homePhone(affiliate.getHomePhone())
                .mobilePhone(affiliate.getMobilePhone())
                .email(affiliate.getEmail())
                .occupation(affiliate.getOccupation())
                .weight(affiliate.getWeight())
                .height(affiliate.getHeight())
                .waist(affiliate.getWaist())
                .leg(affiliate.getLeg())
                .hip(affiliate.getHip())
                .build();
    }

    public static Affiliate toModel(AffiliateEntity affiliateEntity) {

        if (affiliateEntity == null)
            return null;

        return Affiliate.builder()
                .id(affiliateEntity.getId())
                .identification(affiliateEntity.getIdentification())
                .firstName(affiliateEntity.getFirstName())
                .middleName(affiliateEntity.getMiddleName())
                .lastName(affiliateEntity.getLastName())
                .secondLastName(affiliateEntity.getSecondLastName())
                .sex(affiliateEntity.getSex())
                .birthDate(affiliateEntity.getBirthDate())
                .affiliationDate(affiliateEntity.getAffiliationDate())
                .address(affiliateEntity.getAddress())
                .neighborhood(affiliateEntity.getNeighborhood())
                .city(affiliateEntity.getCity())
                .status(affiliateEntity.getStatus())
                .postalCode(affiliateEntity.getPostalCode())
                .homePhone(affiliateEntity.getHomePhone())
                .mobilePhone(affiliateEntity.getMobilePhone())
                .email(affiliateEntity.getEmail())
                .occupation(affiliateEntity.getOccupation())
                .weight(affiliateEntity.getWeight())
                .height(affiliateEntity.getHeight())
                .waist(affiliateEntity.getWaist())
                .leg(affiliateEntity.getLeg())
                .hip(affiliateEntity.getHip())
                .build();
    }
}
