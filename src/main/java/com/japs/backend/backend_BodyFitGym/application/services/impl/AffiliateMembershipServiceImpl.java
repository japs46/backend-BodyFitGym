package com.japs.backend.backend_BodyFitGym.application.services.impl;

import com.japs.backend.backend_BodyFitGym.application.dto.AffiliateMembershipSearchCriteria;
import com.japs.backend.backend_BodyFitGym.application.services.AffiliateMembershipService;
import com.japs.backend.backend_BodyFitGym.domain.model.AffiliateMembership;
import com.japs.backend.backend_BodyFitGym.domain.port.in.affiliatemembership.ICreateAffiliateMembershipUseCase;
import com.japs.backend.backend_BodyFitGym.domain.port.in.affiliatemembership.IDeleteAffiliateMembershipUseCase;
import com.japs.backend.backend_BodyFitGym.domain.port.in.affiliatemembership.IFreezeAffiliateMembershipUseCase;
import com.japs.backend.backend_BodyFitGym.domain.port.in.affiliatemembership.IRetrieveAffiliateMembershipUseCase;
import com.japs.backend.backend_BodyFitGym.domain.port.in.affiliatemembership.IUnfreezeAffiliateMembershipUseCase;
import com.japs.backend.backend_BodyFitGym.domain.port.in.affiliatemembership.IUpdateAffiliateMembershipUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AffiliateMembershipServiceImpl implements AffiliateMembershipService {

    private final ICreateAffiliateMembershipUseCase iCreateAffiliateMembershipUseCase;
    private final IUpdateAffiliateMembershipUseCase iUpdateAffiliateMembershipUseCase;
    private final IDeleteAffiliateMembershipUseCase iDeleteAffiliateMembershipUseCase;
    private final IRetrieveAffiliateMembershipUseCase iRetrieveAffiliateMembershipUseCase;
    private final IFreezeAffiliateMembershipUseCase iFreezeAffiliateMembershipUseCase;
    private final IUnfreezeAffiliateMembershipUseCase iUnfreezeAffiliateMembershipUseCase;

    @Override
    public AffiliateMembership save(AffiliateMembership affiliateMembership) {
        return iCreateAffiliateMembershipUseCase.createAffiliateMembership(affiliateMembership);
    }

    @Override
    public AffiliateMembership update(Long id, AffiliateMembership affiliateMembership) {
        return iUpdateAffiliateMembershipUseCase.updateAffiliateMembership(id, affiliateMembership);
    }

    @Override
    public void delete(Long id) {
        iDeleteAffiliateMembershipUseCase.deleteAffiliateMembership(id);
    }

    @Override
    public AffiliateMembership findById(Long id) {
        return iRetrieveAffiliateMembershipUseCase.getAffiliateMembershipById(id);
    }

    @Override
    public Page<AffiliateMembership> search(AffiliateMembershipSearchCriteria affiliateMembershipSearchCriteria, Pageable pageable) {
        return iRetrieveAffiliateMembershipUseCase.search(affiliateMembershipSearchCriteria, pageable);
    }

    @Override
    public AffiliateMembership freeze(Long id) {
        return iFreezeAffiliateMembershipUseCase.freeze(id);
    }

    @Override
    public AffiliateMembership unfreeze(Long id) {
        return iUnfreezeAffiliateMembershipUseCase.unfreeze(id);
    }
}
