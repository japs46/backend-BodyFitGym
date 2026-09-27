package com.japs.backend.backend_BodyFitGym.application.usecases.sale;

import com.japs.backend.backend_BodyFitGym.domain.model.Product;
import com.japs.backend.backend_BodyFitGym.domain.model.Sale;
import com.japs.backend.backend_BodyFitGym.domain.model.SaleStatus;
import com.japs.backend.backend_BodyFitGym.domain.model.SaleType;
import com.japs.backend.backend_BodyFitGym.domain.port.out.ProductRepositoryPort;
import com.japs.backend.backend_BodyFitGym.domain.port.out.SaleRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
class CancelSaleUseCaseImplTest {

    @Mock
    private SaleRepositoryPort saleRepositoryPort;

    @Mock
    private ProductRepositoryPort productRepositoryPort;

    @InjectMocks
    private CancelSaleUseCaseImpl cancelSaleUseCase;

    @Test
    void cancelSale_lanzaCuandoLaVentaNoExiste() {
        when(saleRepositoryPort.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cancelSaleUseCase.cancelSale(1L))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void cancelSale_lanzaCuandoYaEstaAnulada() {
        Sale sale = Sale.builder().id(1L).type(SaleType.PRODUCTO).status(SaleStatus.ANULADA).build();
        when(saleRepositoryPort.findById(1L)).thenReturn(Optional.of(sale));

        assertThatThrownBy(() -> cancelSaleUseCase.cancelSale(1L))
                .isInstanceOf(IllegalStateException.class);

        verify(saleRepositoryPort, never()).save(any());
    }

    @Test
    void cancelSale_producto_restauraElStockYAnula() {
        Sale sale = Sale.builder().id(1L).type(SaleType.PRODUCTO).productId(9L).quantity(3)
                .status(SaleStatus.PAGADA).build();
        Product product = Product.builder().id(9L).quantity(12).price(BigDecimal.valueOf(1000)).build();
        when(saleRepositoryPort.findById(1L)).thenReturn(Optional.of(sale));
        when(productRepositoryPort.findById(9L)).thenReturn(Optional.of(product));
        when(saleRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Sale result = cancelSaleUseCase.cancelSale(1L);

        assertThat(result.getStatus()).isEqualTo(SaleStatus.ANULADA);

        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepositoryPort).save(productCaptor.capture());
        assertThat(productCaptor.getValue().getQuantity()).isEqualTo(15);
    }

    @Test
    void cancelSale_afiliacion_noTocaProductosYAnula() {
        Sale sale = Sale.builder().id(2L).type(SaleType.AFILIACION).affiliateMembershipId(5L)
                .status(SaleStatus.PAGADA).build();
        when(saleRepositoryPort.findById(2L)).thenReturn(Optional.of(sale));
        when(saleRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Sale result = cancelSaleUseCase.cancelSale(2L);

        assertThat(result.getStatus()).isEqualTo(SaleStatus.ANULADA);
        verify(productRepositoryPort, never()).findById(any());
        verify(productRepositoryPort, never()).save(any());
    }
}
