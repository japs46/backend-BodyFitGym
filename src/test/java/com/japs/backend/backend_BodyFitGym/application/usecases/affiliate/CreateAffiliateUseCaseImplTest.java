package com.japs.backend.backend_BodyFitGym.application.usecases.affiliate;

import com.japs.backend.backend_BodyFitGym.domain.model.Affiliate;
import com.japs.backend.backend_BodyFitGym.domain.port.out.AffiliateRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateAffiliateUseCaseImplTest {

    @Mock
    private AffiliateRepositoryPort affiliateRepositoryPort;

    @InjectMocks
    private CreateAffiliateUseCaseImpl createAffiliateUseCase;

    private Affiliate newAffiliate() {
        return Affiliate.builder()
                .identification("123456")
                .firstName("Juan")
                .lastName("Pérez")
                .build();
    }

    @Test
    void createAffiliate_guardaCuandoLaIdentificacionNoExiste() {
        Affiliate input = newAffiliate();
        when(affiliateRepositoryPort.findByIdentificationIgnoreCase("123456")).thenReturn(Optional.empty());
        when(affiliateRepositoryPort.save(input)).thenReturn(input.toBuilder().id(1L).build());

        Affiliate result = createAffiliateUseCase.createAffiliate(input);

        assertThat(result.getId()).isEqualTo(1L);
        verify(affiliateRepositoryPort).save(input);
    }

    @Test
    void createAffiliate_lanzaExcepcionCuandoLaIdentificacionYaExiste() {
        Affiliate input = newAffiliate();
        when(affiliateRepositoryPort.findByIdentificationIgnoreCase("123456"))
                .thenReturn(Optional.of(Affiliate.builder().id(99L).identification("123456").build()));

        assertThatThrownBy(() -> createAffiliateUseCase.createAffiliate(input))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("123456");

        verify(affiliateRepositoryPort, never()).save(any());
    }
}
