package com.japs.backend.backend_BodyFitGym.application.usecases.membership;

import com.japs.backend.backend_BodyFitGym.application.dto.MembershipSearchCriteria;
import com.japs.backend.backend_BodyFitGym.domain.model.Membership;
import com.japs.backend.backend_BodyFitGym.domain.port.out.MembershipRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RetrieveMembershipUseCaseImplTest {

    @Mock
    private MembershipRepositoryPort membershipRepositoryPort;

    @InjectMocks
    private RetrieveMembershipUseCaseImpl retrieveMembershipUseCase;

    @Test
    void getMembershipById_lanzaCuandoIdEsNull() {
        assertThatThrownBy(() -> retrieveMembershipUseCase.getMembershipById(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void getMembershipById_lanzaCuandoIdNoEsPositivo() {
        assertThatThrownBy(() -> retrieveMembershipUseCase.getMembershipById(0L))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> retrieveMembershipUseCase.getMembershipById(-5L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void getMembershipById_lanzaNoSuchElementCuandoNoExiste() {
        when(membershipRepositoryPort.findById(50L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> retrieveMembershipUseCase.getMembershipById(50L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("50");
    }

    @Test
    void getMembershipById_retornaCuandoExiste() {
        Membership membership = Membership.builder().id(1L).name("Bimestral").build();
        when(membershipRepositoryPort.findById(1L)).thenReturn(Optional.of(membership));

        Membership result = retrieveMembershipUseCase.getMembershipById(1L);

        assertThat(result).isSameAs(membership);
    }

    @Test
    void search_delegaAlPuerto() {
        MembershipSearchCriteria criteria = MembershipSearchCriteria.builder().name("Bimestral").build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Membership> page = new PageImpl<>(List.of(Membership.builder().id(1L).build()));
        when(membershipRepositoryPort.search(criteria, pageable)).thenReturn(page);

        Page<Membership> result = retrieveMembershipUseCase.search(criteria, pageable);

        assertThat(result).isSameAs(page);
        assertThat(result.getTotalElements()).isEqualTo(1);
    }
}
