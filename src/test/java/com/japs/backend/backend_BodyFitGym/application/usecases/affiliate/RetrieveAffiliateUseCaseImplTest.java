package com.japs.backend.backend_BodyFitGym.application.usecases.affiliate;

import com.japs.backend.backend_BodyFitGym.application.dto.AffiliateSearchCriteria;
import com.japs.backend.backend_BodyFitGym.domain.model.Affiliate;
import com.japs.backend.backend_BodyFitGym.domain.port.out.AffiliateRepositoryPort;
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
class RetrieveAffiliateUseCaseImplTest {

    @Mock
    private AffiliateRepositoryPort affiliateRepositoryPort;

    @InjectMocks
    private RetrieveAffiliateUseCaseImpl retrieveAffiliateUseCase;

    @Test
    void getAffiliateById_lanzaCuandoIdEsNull() {
        assertThatThrownBy(() -> retrieveAffiliateUseCase.getAffiliateById(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void getAffiliateById_lanzaCuandoIdNoEsPositivo() {
        assertThatThrownBy(() -> retrieveAffiliateUseCase.getAffiliateById(0L))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> retrieveAffiliateUseCase.getAffiliateById(-5L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void getAffiliateById_lanzaNoSuchElementCuandoNoExiste() {
        when(affiliateRepositoryPort.findById(50L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> retrieveAffiliateUseCase.getAffiliateById(50L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("50");
    }

    @Test
    void getAffiliateById_retornaCuandoExiste() {
        Affiliate affiliate = Affiliate.builder().id(1L).firstName("Juan").build();
        when(affiliateRepositoryPort.findById(1L)).thenReturn(Optional.of(affiliate));

        Affiliate result = retrieveAffiliateUseCase.getAffiliateById(1L);

        assertThat(result).isSameAs(affiliate);
    }

    @Test
    void search_delegaAlPuerto() {
        AffiliateSearchCriteria criteria = AffiliateSearchCriteria.builder().name("Juan").build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Affiliate> page = new PageImpl<>(List.of(Affiliate.builder().id(1L).build()));
        when(affiliateRepositoryPort.search(criteria, pageable)).thenReturn(page);

        Page<Affiliate> result = retrieveAffiliateUseCase.search(criteria, pageable);

        assertThat(result).isSameAs(page);
        assertThat(result.getTotalElements()).isEqualTo(1);
    }
}
