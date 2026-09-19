package com.japs.backend.backend_BodyFitGym.application.usecases.affiliate;

import com.japs.backend.backend_BodyFitGym.domain.model.Affiliate;
import com.japs.backend.backend_BodyFitGym.domain.port.in.affiliate.IUpdateAffiliateUseCase;
import com.japs.backend.backend_BodyFitGym.domain.port.out.AffiliateRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Component
public class UpdateAffiliateUseCaseImpl implements IUpdateAffiliateUseCase {

    private final AffiliateRepositoryPort affiliateRepositoryPort;

    @Override
    public Affiliate updateAffiliate(Long id, Affiliate affiliate) {

        Affiliate existingAffiliate = affiliateRepositoryPort.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No existe ningún afiliado con el ID: " + id));

        if (!existingAffiliate.getIdentification().equalsIgnoreCase(affiliate.getIdentification())) {
            Affiliate existingAffiliateWithIdentification = affiliateRepositoryPort
                    .findByIdentificationIgnoreCase(affiliate.getIdentification()).orElse(null);
            if (existingAffiliateWithIdentification != null) {
                throw new IllegalStateException("Ya existe un afiliado registrado con la identificación: " + affiliate.getIdentification());
            }
        }

        Affiliate affiliateToUpdate = existingAffiliate.toBuilder()
                .identification(affiliate.getIdentification())
                .firstName(affiliate.getFirstName())
                .middleName(affiliate.getMiddleName())
                .lastName(affiliate.getLastName())
                .secondLastName(affiliate.getSecondLastName())
                .sex(affiliate.getSex())
                .birthDate(affiliate.getBirthDate())
                .address(affiliate.getAddress())
                .neighborhood(affiliate.getNeighborhood())
                .city(affiliate.getCity())
                .status(affiliate.getStatus() != null ? affiliate.getStatus() : existingAffiliate.getStatus())
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

        return affiliateRepositoryPort.save(affiliateToUpdate);
    }
}
