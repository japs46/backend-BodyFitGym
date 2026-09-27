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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateProductUseCaseImplTest {

    @Mock
    private ProductRepositoryPort productRepositoryPort;

    @InjectMocks
    private CreateProductUseCaseImpl createProductUseCase;

    private Product newProduct() {
        return Product.builder()
                .name("Proteina Whey 1kg")
                .quantity(20)
                .price(BigDecimal.valueOf(85000))
                .build();
    }

    @Test
    void createProduct_guardaCuandoElNombreNoExiste() {
        Product input = newProduct();
        when(productRepositoryPort.findByNameIgnoreCase("Proteina Whey 1kg")).thenReturn(Optional.empty());
        when(productRepositoryPort.save(input)).thenReturn(input.toBuilder().id(1L).status(ProductStatus.ACTIVO).build());

        Product result = createProductUseCase.createProduct(input);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getStatus()).isEqualTo(ProductStatus.ACTIVO);
        verify(productRepositoryPort).save(input);
    }

    @Test
    void createProduct_lanzaExcepcionCuandoElNombreYaExiste() {
        Product input = newProduct();
        when(productRepositoryPort.findByNameIgnoreCase("Proteina Whey 1kg"))
                .thenReturn(Optional.of(Product.builder().id(99L).name("Proteina Whey 1kg").build()));

        assertThatThrownBy(() -> createProductUseCase.createProduct(input))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Proteina Whey 1kg");

        verify(productRepositoryPort, never()).save(any());
    }

    @Test
    void createProduct_noSobreescribeStatusSiYaViaDefinido() {
        Product input = newProduct().toBuilder().status(ProductStatus.INACTIVO).build();
        when(productRepositoryPort.findByNameIgnoreCase("Proteina Whey 1kg")).thenReturn(Optional.empty());
        when(productRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Product result = createProductUseCase.createProduct(input);

        assertThat(result.getStatus()).isEqualTo(ProductStatus.INACTIVO);
    }
}
