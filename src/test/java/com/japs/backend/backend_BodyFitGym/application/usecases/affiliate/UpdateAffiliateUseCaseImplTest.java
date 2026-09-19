package com.japs.backend.backend_BodyFitGym.application.usecases.affiliate;

import com.japs.backend.backend_BodyFitGym.domain.model.Affiliate;
import com.japs.backend.backend_BodyFitGym.domain.port.out.AffiliateRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateAffiliateUseCaseImplTest {

    @Mock
    private AffiliateRepositoryPort affiliateRepositoryPort;

    @InjectMocks
    private UpdateAffiliateUseCaseImpl updateAffiliateUseCase;

    private Affiliate existing() {
        return Affiliate.builder()
                .id(1L)
                .identification("123456")
                .firstName("Juan")
                .lastName("Pérez")
                .status("Activo")
                .affiliationDate(LocalDate.of(2020, 1, 15))
                .build();
    }

    @Test
    void updateAffiliate_lanzaExcepcionCuandoNoExiste() {
        when(affiliateRepositoryPort.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> updateAffiliateUseCase.updateAffiliate(99L, existing()))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("99");

        verify(affiliateRepositoryPort, never()).save(any());
    }

    @Test
    void updateAffiliate_lanzaExcepcionCuandoLaNuevaIdentificacionYaEstaEnUso() {
        when(affiliateRepositoryPort.findById(1L)).thenReturn(Optional.of(existing()));
        when(affiliateRepositoryPort.findByIdentificationIgnoreCase("999"))
                .thenReturn(Optional.of(Affiliate.builder().id(2L).identification("999").build()));

        Affiliate input = existing().toBuilder().identification("999").build();

        assertThatThrownBy(() -> updateAffiliateUseCase.updateAffiliate(1L, input))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("999");

        verify(affiliateRepositoryPort, never()).save(any());
    }

    @Test
    void updateAffiliate_noRevalidaCuandoLaIdentificacionNoCambia() {
        when(affiliateRepositoryPort.findById(1L)).thenReturn(Optional.of(existing()));
        when(affiliateRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Affiliate input = existing().toBuilder().firstName("Juan Carlos").build();
        updateAffiliateUseCase.updateAffiliate(1L, input);

        // misma identificación -> no debe consultar unicidad
        verify(affiliateRepositoryPort, never()).findByIdentificationIgnoreCase(anyString());
    }

    @Test
    void updateAffiliate_aplicaCambiosYPreservaIdYFechaAfiliacion() {
        when(affiliateRepositoryPort.findById(1L)).thenReturn(Optional.of(existing()));
        when(affiliateRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Affiliate input = Affiliate.builder()
                .identification("123456")
                .firstName("Juan Carlos")
                .lastName("Pérez")
                .city("Medellín")
                .status("Inactivo")
                .affiliationDate(LocalDate.of(1999, 1, 1)) // debe ignorarse
                .build();

        updateAffiliateUseCase.updateAffiliate(1L, input);

        ArgumentCaptor<Affiliate> captor = ArgumentCaptor.forClass(Affiliate.class);
        verify(affiliateRepositoryPort).save(captor.capture());
        Affiliate saved = captor.getValue();

        assertThat(saved.getId()).isEqualTo(1L);                               // preserva id existente
        assertThat(saved.getAffiliationDate()).isEqualTo(LocalDate.of(2020, 1, 15)); // preserva fecha existente
        assertThat(saved.getFirstName()).isEqualTo("Juan Carlos");            // aplica cambio
        assertThat(saved.getCity()).isEqualTo("Medellín");                    // aplica cambio
        assertThat(saved.getStatus()).isEqualTo("Inactivo");                  // aplica cambio
    }
}
