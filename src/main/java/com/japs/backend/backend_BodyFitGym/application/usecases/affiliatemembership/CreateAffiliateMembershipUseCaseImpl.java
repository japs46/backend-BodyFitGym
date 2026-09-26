package com.japs.backend.backend_BodyFitGym.application.usecases.affiliatemembership;

import com.japs.backend.backend_BodyFitGym.domain.model.AffiliateMembership;
import com.japs.backend.backend_BodyFitGym.domain.model.Membership;
import com.japs.backend.backend_BodyFitGym.domain.model.MembershipStatus;
import com.japs.backend.backend_BodyFitGym.domain.model.SubscriptionStatus;
import com.japs.backend.backend_BodyFitGym.domain.port.in.affiliatemembership.ICreateAffiliateMembershipUseCase;
import com.japs.backend.backend_BodyFitGym.domain.port.out.AffiliateMembershipRepositoryPort;
import com.japs.backend.backend_BodyFitGym.domain.port.out.AffiliateRepositoryPort;
import com.japs.backend.backend_BodyFitGym.domain.port.out.MembershipRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Component
public class CreateAffiliateMembershipUseCaseImpl implements ICreateAffiliateMembershipUseCase {

    private final AffiliateMembershipRepositoryPort affiliateMembershipRepositoryPort;
    private final AffiliateRepositoryPort affiliateRepositoryPort;
    private final MembershipRepositoryPort membershipRepositoryPort;

    @Override
    public AffiliateMembership createAffiliateMembership(AffiliateMembership affiliateMembership) {

        affiliateRepositoryPort.findById(affiliateMembership.getAffiliateId())
                .orElseThrow(() -> new NoSuchElementException(
                        "No existe ningún afiliado con el ID: " + affiliateMembership.getAffiliateId()));

        Membership membership = membershipRepositoryPort.findById(affiliateMembership.getMembershipId())
                .orElseThrow(() -> new NoSuchElementException(
                        "No existe ninguna membresía con el ID: " + affiliateMembership.getMembershipId()));

        if (membership.getStatus() != MembershipStatus.ACTIVA) {
            throw new IllegalStateException("La membresía '" + membership.getName() + "' no está activa.");
        }

        LocalDate startDate = affiliateMembership.getStartDate() != null
                ? affiliateMembership.getStartDate()
                : LocalDate.now();
        LocalDate endDate = AffiliateMembershipCalculator.calculateEndDate(
                startDate, membership.getDurationUnit(), membership.getDurationQuantity());

        AffiliateMembership toSave = affiliateMembership.toBuilder()
                .startDate(startDate)
                .endDate(endDate)
                .trackingMode(membership.getTrackingMode())
                .remainingUnits(membership.getDurationQuantity())
                .status(SubscriptionStatus.ACTIVA)
                .frozen(false)
                .frozenAt(null)
                .createdAt(LocalDate.now())
                .build();

        return affiliateMembershipRepositoryPort.save(toSave);
    }
}
