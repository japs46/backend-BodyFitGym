package com.japs.backend.backend_BodyFitGym.application.usecases.membership;

import com.japs.backend.backend_BodyFitGym.application.dto.MembershipSearchCriteria;
import com.japs.backend.backend_BodyFitGym.domain.model.Membership;
import com.japs.backend.backend_BodyFitGym.domain.port.in.membership.IRetrieveMembershipUseCase;
import com.japs.backend.backend_BodyFitGym.domain.port.out.MembershipRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Component
public class RetrieveMembershipUseCaseImpl implements IRetrieveMembershipUseCase {

    private final MembershipRepositoryPort membershipRepositoryPort;

    @Override
    public Membership getMembershipById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID de la membresía es obligatorio.");
        }

        if (id <= 0) {
            throw new IllegalArgumentException("El ID de la membresía debe ser un número positivo.");
        }

        return membershipRepositoryPort.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No existe ninguna membresía con el ID: " + id));
    }

    @Override
    public Page<Membership> search(MembershipSearchCriteria membershipSearchCriteria, Pageable pageable) {
        return membershipRepositoryPort.search(membershipSearchCriteria, pageable);
    }
}
