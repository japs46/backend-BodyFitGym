package com.japs.backend.backend_BodyFitGym.application.usecases.product;

import com.japs.backend.backend_BodyFitGym.domain.model.Product;
import com.japs.backend.backend_BodyFitGym.domain.port.out.ProductRepositoryPort;
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
class DeleteProductUseCaseImplTest {

    @Mock
    private ProductRepositoryPort productRepositoryPort;

    @InjectMocks
    private DeleteProductUseCaseImpl deleteProductUseCase;

    @Test
    void deleteProduct_eliminaCuandoExiste() {
        when(productRepositoryPort.findById(1L))
                .thenReturn(Optional.of(Product.builder().id(1L).build()));

        deleteProductUseCase.deleteProduct(1L);

        verify(productRepositoryPort).delete(1L);
    }

    @Test
    void deleteProduct_lanzaCuandoNoExiste() {
        when(productRepositoryPort.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> deleteProductUseCase.deleteProduct(99L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("99");

        verify(productRepositoryPort, never()).delete(99L);
    }
}
