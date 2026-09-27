package com.japs.backend.backend_BodyFitGym.application.usecases.sale;

import com.japs.backend.backend_BodyFitGym.domain.model.Affiliate;
import com.japs.backend.backend_BodyFitGym.domain.model.AffiliateMembership;
import com.japs.backend.backend_BodyFitGym.domain.model.Membership;
import com.japs.backend.backend_BodyFitGym.domain.model.Product;
import com.japs.backend.backend_BodyFitGym.domain.model.Sale;
import com.japs.backend.backend_BodyFitGym.domain.model.SaleStatus;
import com.japs.backend.backend_BodyFitGym.domain.model.SaleType;
import com.japs.backend.backend_BodyFitGym.domain.port.out.AffiliateMembershipRepositoryPort;
import com.japs.backend.backend_BodyFitGym.domain.port.out.AffiliateRepositoryPort;
import com.japs.backend.backend_BodyFitGym.domain.port.out.MembershipRepositoryPort;
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
class CreateSaleUseCaseImplTest {

    @Mock
    private SaleRepositoryPort saleRepositoryPort;

    @Mock
    private AffiliateRepositoryPort affiliateRepositoryPort;

    @Mock
    private ProductRepositoryPort productRepositoryPort;

    @Mock
    private AffiliateMembershipRepositoryPort affiliateMembershipRepositoryPort;

    @Mock
    private MembershipRepositoryPort membershipRepositoryPort;

    @InjectMocks
    private CreateSaleUseCaseImpl createSaleUseCase;

    private Product product(int quantity) {
        return Product.builder().id(1L).name("Proteina").quantity(quantity).price(BigDecimal.valueOf(88000)).build();
    }

    private AffiliateMembership subscription(Long affiliateId) {
        return AffiliateMembership.builder().id(2L).affiliateId(affiliateId).membershipId(3L).build();
    }

    private Membership membership() {
        return Membership.builder().id(3L).name("Bimestral").price(BigDecimal.valueOf(100000)).build();
    }

    @Test
    void createSale_lanzaCuandoElAfiliadoNoExiste() {
        Sale sale = Sale.builder().affiliateId(4L).type(SaleType.PRODUCTO).productId(1L).quantity(1).build();
        when(affiliateRepositoryPort.findById(4L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> createSaleUseCase.createSale(sale))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("4");

        verify(saleRepositoryPort, never()).save(any());
    }

    @Test
    void createSale_producto_lanzaCuandoFaltaProductId() {
        Sale sale = Sale.builder().affiliateId(4L).type(SaleType.PRODUCTO).quantity(1).build();
        when(affiliateRepositoryPort.findById(4L)).thenReturn(Optional.of(Affiliate.builder().id(4L).build()));

        assertThatThrownBy(() -> createSaleUseCase.createSale(sale))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createSale_producto_lanzaCuandoTambienTraeAffiliateMembershipId() {
        Sale sale = Sale.builder().affiliateId(4L).type(SaleType.PRODUCTO).productId(1L).affiliateMembershipId(2L).quantity(1).build();
        when(affiliateRepositoryPort.findById(4L)).thenReturn(Optional.of(Affiliate.builder().id(4L).build()));

        assertThatThrownBy(() -> createSaleUseCase.createSale(sale))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createSale_producto_lanzaCuandoLaCantidadNoEsPositiva() {
        Sale sale = Sale.builder().affiliateId(4L).type(SaleType.PRODUCTO).productId(1L).quantity(0).build();
        when(affiliateRepositoryPort.findById(4L)).thenReturn(Optional.of(Affiliate.builder().id(4L).build()));

        assertThatThrownBy(() -> createSaleUseCase.createSale(sale))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createSale_producto_lanzaNoSuchElementCuandoElProductoNoExiste() {
        Sale sale = Sale.builder().affiliateId(4L).type(SaleType.PRODUCTO).productId(1L).quantity(1).build();
        when(affiliateRepositoryPort.findById(4L)).thenReturn(Optional.of(Affiliate.builder().id(4L).build()));
        when(productRepositoryPort.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> createSaleUseCase.createSale(sale))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void createSale_producto_lanzaCuandoNoHayStockSuficiente() {
        Sale sale = Sale.builder().affiliateId(4L).type(SaleType.PRODUCTO).productId(1L).quantity(5).build();
        when(affiliateRepositoryPort.findById(4L)).thenReturn(Optional.of(Affiliate.builder().id(4L).build()));
        when(productRepositoryPort.findById(1L)).thenReturn(Optional.of(product(3)));

        assertThatThrownBy(() -> createSaleUseCase.createSale(sale))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("3");

        verify(productRepositoryPort, never()).save(any());
        verify(saleRepositoryPort, never()).save(any());
    }

    @Test
    void createSale_producto_calculaTotalDescuentaStockYFuerzaEstado() {
        Sale sale = Sale.builder().affiliateId(4L).type(SaleType.PRODUCTO).productId(1L).quantity(3)
                .status(SaleStatus.PENDIENTE).build();
        when(affiliateRepositoryPort.findById(4L)).thenReturn(Optional.of(Affiliate.builder().id(4L).build()));
        when(productRepositoryPort.findById(1L)).thenReturn(Optional.of(product(15)));
        when(saleRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Sale result = createSaleUseCase.createSale(sale);

        assertThat(result.getUnitPrice()).isEqualByComparingTo("88000");
        assertThat(result.getTotal()).isEqualByComparingTo("264000");
        assertThat(result.getStatus()).isEqualTo(SaleStatus.PAGADA);
        assertThat(result.getSaleDate()).isNotNull();

        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepositoryPort).save(productCaptor.capture());
        assertThat(productCaptor.getValue().getQuantity()).isEqualTo(12);
    }

    @Test
    void createSale_afiliacion_lanzaCuandoFaltaAffiliateMembershipId() {
        Sale sale = Sale.builder().affiliateId(4L).type(SaleType.AFILIACION).build();
        when(affiliateRepositoryPort.findById(4L)).thenReturn(Optional.of(Affiliate.builder().id(4L).build()));

        assertThatThrownBy(() -> createSaleUseCase.createSale(sale))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createSale_afiliacion_lanzaCuandoTambienTraeProductId() {
        Sale sale = Sale.builder().affiliateId(4L).type(SaleType.AFILIACION).affiliateMembershipId(2L).productId(1L).build();
        when(affiliateRepositoryPort.findById(4L)).thenReturn(Optional.of(Affiliate.builder().id(4L).build()));

        assertThatThrownBy(() -> createSaleUseCase.createSale(sale))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createSale_afiliacion_lanzaNoSuchElementCuandoLaAfiliacionNoExiste() {
        Sale sale = Sale.builder().affiliateId(4L).type(SaleType.AFILIACION).affiliateMembershipId(2L).build();
        when(affiliateRepositoryPort.findById(4L)).thenReturn(Optional.of(Affiliate.builder().id(4L).build()));
        when(affiliateMembershipRepositoryPort.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> createSaleUseCase.createSale(sale))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void createSale_afiliacion_lanzaCuandoLaAfiliacionNoPerteneceAlAfiliado() {
        Sale sale = Sale.builder().affiliateId(4L).type(SaleType.AFILIACION).affiliateMembershipId(2L).build();
        when(affiliateRepositoryPort.findById(4L)).thenReturn(Optional.of(Affiliate.builder().id(4L).build()));
        when(affiliateMembershipRepositoryPort.findById(2L)).thenReturn(Optional.of(subscription(9L)));

        assertThatThrownBy(() -> createSaleUseCase.createSale(sale))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createSale_afiliacion_fuerzaCantidadUnoYTomaPrecioDeLaMembresia() {
        Sale sale = Sale.builder().affiliateId(4L).type(SaleType.AFILIACION).affiliateMembershipId(2L).quantity(9).build();
        when(affiliateRepositoryPort.findById(4L)).thenReturn(Optional.of(Affiliate.builder().id(4L).build()));
        when(affiliateMembershipRepositoryPort.findById(2L)).thenReturn(Optional.of(subscription(4L)));
        when(membershipRepositoryPort.findById(3L)).thenReturn(Optional.of(membership()));
        when(saleRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Sale result = createSaleUseCase.createSale(sale);

        assertThat(result.getQuantity()).isEqualTo(1);
        assertThat(result.getUnitPrice()).isEqualByComparingTo("100000");
        assertThat(result.getTotal()).isEqualByComparingTo("100000");
        assertThat(result.getStatus()).isEqualTo(SaleStatus.PAGADA);
        verify(productRepositoryPort, never()).save(any());
    }
}
