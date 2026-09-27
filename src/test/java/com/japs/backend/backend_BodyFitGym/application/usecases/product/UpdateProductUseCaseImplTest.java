package com.japs.backend.backend_BodyFitGym.application.usecases.product;

import com.japs.backend.backend_BodyFitGym.domain.model.Product;
import com.japs.backend.backend_BodyFitGym.domain.model.ProductStatus;
import com.japs.backend.backend_BodyFitGym.domain.port.out.ProductRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateProductUseCaseImplTest {

    @Mock
    private ProductRepositoryPort productRepositoryPort;

    @InjectMocks
    private UpdateProductUseCaseImpl updateProductUseCase;

    private Product existing() {
        return Product.builder()
                .id(1L)
                .name("Proteina Whey 1kg")
                .description("Suplemento proteico")
                .quantity(20)
                .price(BigDecimal.valueOf(85000))
                .status(ProductStatus.ACTIVO)
                .build();
    }

    @Test
    void updateProduct_lanzaCuandoNoExiste() {
        when(productRepositoryPort.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> updateProductUseCase.updateProduct(99L, existing()))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("99");

        verify(productRepositoryPort, never()).save(any());
    }

    @Test
    void updateProduct_lanzaCuandoElNuevoNombreYaEstaEnUso() {
        when(productRepositoryPort.findById(1L)).thenReturn(Optional.of(existing()));
        when(productRepositoryPort.findByNameIgnoreCase("Otro Nombre"))
                .thenReturn(Optional.of(Product.builder().id(2L).name("Otro Nombre").build()));

        Product input = existing().toBuilder().name("Otro Nombre").build();

        assertThatThrownBy(() -> updateProductUseCase.updateProduct(1L, input))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Otro Nombre");

        verify(productRepositoryPort, never()).save(any());
    }

    @Test
    void updateProduct_noRevalidaCuandoElNombreNoCambia() {
        when(productRepositoryPort.findById(1L)).thenReturn(Optional.of(existing()));
        when(productRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Product input = existing().toBuilder().quantity(15).build();
        updateProductUseCase.updateProduct(1L, input);

        verify(productRepositoryPort, never()).findByNameIgnoreCase(any());
    }

    @Test
    void updateProduct_aplicaCambios() {
        when(productRepositoryPort.findById(1L)).thenReturn(Optional.of(existing()));
        when(productRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Product input = existing().toBuilder()
                .quantity(5)
                .price(BigDecimal.valueOf(90000))
                .status(ProductStatus.INACTIVO)
                .build();

        Product result = updateProductUseCase.updateProduct(1L, input);

        assertThat(result.getQuantity()).isEqualTo(5);
        assertThat(result.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(90000));
        assertThat(result.getStatus()).isEqualTo(ProductStatus.INACTIVO);
        assertThat(result.getId()).isEqualTo(1L);
    }
}
