package com.japs.backend.backend_BodyFitGym.application.usecases.product;

import com.japs.backend.backend_BodyFitGym.application.dto.ProductSearchCriteria;
import com.japs.backend.backend_BodyFitGym.domain.model.Product;
import com.japs.backend.backend_BodyFitGym.domain.port.out.ProductRepositoryPort;
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
class RetrieveProductUseCaseImplTest {

    @Mock
    private ProductRepositoryPort productRepositoryPort;

    @InjectMocks
    private RetrieveProductUseCaseImpl retrieveProductUseCase;

    @Test
    void getProductById_lanzaCuandoIdEsNull() {
        assertThatThrownBy(() -> retrieveProductUseCase.getProductById(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void getProductById_lanzaCuandoIdNoEsPositivo() {
        assertThatThrownBy(() -> retrieveProductUseCase.getProductById(0L))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> retrieveProductUseCase.getProductById(-5L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void getProductById_lanzaNoSuchElementCuandoNoExiste() {
        when(productRepositoryPort.findById(50L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> retrieveProductUseCase.getProductById(50L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("50");
    }

    @Test
    void getProductById_retornaCuandoExiste() {
        Product product = Product.builder().id(1L).name("Proteina").build();
        when(productRepositoryPort.findById(1L)).thenReturn(Optional.of(product));

        Product result = retrieveProductUseCase.getProductById(1L);

        assertThat(result).isSameAs(product);
    }

    @Test
    void search_delegaAlPuerto() {
        ProductSearchCriteria criteria = ProductSearchCriteria.builder().name("Proteina").build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> page = new PageImpl<>(List.of(Product.builder().id(1L).build()));
        when(productRepositoryPort.search(criteria, pageable)).thenReturn(page);

        Page<Product> result = retrieveProductUseCase.search(criteria, pageable);

        assertThat(result).isSameAs(page);
        assertThat(result.getTotalElements()).isEqualTo(1);
    }
}
