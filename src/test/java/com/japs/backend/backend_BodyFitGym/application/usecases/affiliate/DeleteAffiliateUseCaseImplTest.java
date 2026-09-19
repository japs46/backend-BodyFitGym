package com.japs.backend.backend_BodyFitGym.application.usecases.affiliate;

import com.japs.backend.backend_BodyFitGym.domain.model.Affiliate;
import com.japs.backend.backend_BodyFitGym.domain.port.out.AffiliateRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteAffiliateUseCaseImplTest {

    @Mock
    private AffiliateRepositoryPort affiliateRepositoryPort;

    @InjectMocks
    private DeleteAffiliateUseCaseImpl deleteAffiliateUseCase;

    @Test
    void deleteAffiliate_eliminaCuandoExiste() {
        when(affiliateRepositoryPort.findById(1L))
                .thenReturn(Optional.of(Affiliate.builder().id(1L).build()));

        deleteAffiliateUseCase.deleteAffiliate(1L);

        verify(affiliateRepositoryPort).delete(1L);
    }

    @Test
    void deleteAffiliate_lanzaExcepcionCuandoNoExiste() {
        when(affiliateRepositoryPort.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> deleteAffiliateUseCase.deleteAffiliate(99L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("99");

        verify(affiliateRepositoryPort, never()).delete(99L);
    }
}
