package com.japs.backend.backend_BodyFitGym.application.services.impl;

import com.japs.backend.backend_BodyFitGym.application.dto.MembershipSearchCriteria;
import com.japs.backend.backend_BodyFitGym.application.services.MembershipService;
import com.japs.backend.backend_BodyFitGym.domain.model.Membership;
import com.japs.backend.backend_BodyFitGym.domain.port.in.membership.ICreateMembershipUseCase;
import com.japs.backend.backend_BodyFitGym.domain.port.in.membership.IDeleteMembershipUseCase;
import com.japs.backend.backend_BodyFitGym.domain.port.in.membership.IRetrieveMembershipUseCase;
import com.japs.backend.backend_BodyFitGym.domain.port.in.membership.IUpdateMembershipUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MembershipServiceImpl implements MembershipService {

    private final ICreateMembershipUseCase iCreateMembershipUseCase;
    private final IUpdateMembershipUseCase iUpdateMembershipUseCase;
    private final IDeleteMembershipUseCase iDeleteMembershipUseCase;
    private final IRetrieveMembershipUseCase iRetrieveMembershipUseCase;

    @Override
    public Membership save(Membership membership) {
        return iCreateMembershipUseCase.createMembership(membership);
    }

    @Override
    public Membership update(Long id, Membership membership) {
        return iUpdateMembershipUseCase.updateMembership(id, membership);
    }

    @Override
    public void delete(Long id) {
        iDeleteMembershipUseCase.deleteMembership(id);
    }

    @Override
    public Membership findById(Long id) {
        return iRetrieveMembershipUseCase.getMembershipById(id);
    }

    @Override
    public Page<Membership> search(MembershipSearchCriteria membershipSearchCriteria, Pageable pageable) {
        return iRetrieveMembershipUseCase.search(membershipSearchCriteria, pageable);
    }
}
