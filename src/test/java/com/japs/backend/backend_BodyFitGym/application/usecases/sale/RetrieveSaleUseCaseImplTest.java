package com.japs.backend.backend_BodyFitGym.application.usecases.sale;

import com.japs.backend.backend_BodyFitGym.application.dto.SaleSearchCriteria;
import com.japs.backend.backend_BodyFitGym.domain.model.Sale;
import com.japs.backend.backend_BodyFitGym.domain.port.out.SaleRepositoryPort;
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
class RetrieveSaleUseCaseImplTest {

    @Mock
    private SaleRepositoryPort saleRepositoryPort;

    @InjectMocks
    private RetrieveSaleUseCaseImpl retrieveSaleUseCase;

    @Test
    void getSaleById_lanzaCuandoIdEsNull() {
        assertThatThrownBy(() -> retrieveSaleUseCase.getSaleById(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void getSaleById_lanzaCuandoIdNoEsPositivo() {
        assertThatThrownBy(() -> retrieveSaleUseCase.getSaleById(0L))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> retrieveSaleUseCase.getSaleById(-5L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void getSaleById_lanzaNoSuchElementCuandoNoExiste() {
        when(saleRepositoryPort.findById(50L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> retrieveSaleUseCase.getSaleById(50L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("50");
    }

    @Test
    void getSaleById_retornaCuandoExiste() {
        Sale sale = Sale.builder().id(1L).build();
        when(saleRepositoryPort.findById(1L)).thenReturn(Optional.of(sale));

        Sale result = retrieveSaleUseCase.getSaleById(1L);

        assertThat(result).isSameAs(sale);
    }

    @Test
    void search_delegaAlPuerto() {
        SaleSearchCriteria criteria = SaleSearchCriteria.builder().affiliateId(4L).build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Sale> page = new PageImpl<>(List.of(Sale.builder().id(1L).build()));
        when(saleRepositoryPort.search(criteria, pageable)).thenReturn(page);

        Page<Sale> result = retrieveSaleUseCase.search(criteria, pageable);

        assertThat(result).isSameAs(page);
        assertThat(result.getTotalElements()).isEqualTo(1);
    }
}
