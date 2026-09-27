package com.japs.backend.backend_BodyFitGym.infrastructure.entry_points.rest.handler;

import com.japs.backend.backend_BodyFitGym.application.dto.SaleSearchCriteria;
import com.japs.backend.backend_BodyFitGym.application.response.ApiResponse;
import com.japs.backend.backend_BodyFitGym.application.services.SaleService;
import com.japs.backend.backend_BodyFitGym.application.utils.ResponseBuilder;
import com.japs.backend.backend_BodyFitGym.domain.model.Sale;
import com.japs.backend.backend_BodyFitGym.domain.model.SaleStatus;
import com.japs.backend.backend_BodyFitGym.domain.model.SaleType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/sale")
public class SaleController {

    private final SaleService saleService;

    @PostMapping("/save")
    public ResponseEntity<ApiResponse<Sale>> save(@Valid @RequestBody Sale saleRequest) {

        log.info("[SaleController] POST /save - Creando venta para afiliado: {}", saleRequest.getAffiliateId());

        Sale sale = saleService.save(saleRequest);
        ApiResponse<Sale> apiResponse = ResponseBuilder.successMessage("Venta registrada exitosamente", sale);

        log.info("[SaleController] POST /save - Venta creada con ID: {}", sale.getId());
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<ApiResponse<Sale>> cancel(@PathVariable Long id) {

        log.info("[SaleController] PUT /cancel/{} - Anulando venta", id);

        Sale sale = saleService.cancel(id);
        ApiResponse<Sale> apiResponse = ResponseBuilder.successMessage("Venta anulada exitosamente", sale);

        log.info("[SaleController] PUT /cancel/{} - Venta anulada", id);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<ApiResponse<Sale>> findById(@PathVariable Long id) {

        log.info("[SaleController] GET /find-by-id/{} - Buscando venta", id);

        Sale sale = saleService.findById(id);
        ApiResponse<Sale> apiResponse = ResponseBuilder.successMessage("Venta encontrada", sale);

        log.info("[SaleController] GET /find-by-id/{} - Venta encontrada", id);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<Sale>>> search(
            @RequestParam(required = false) Long affiliateId,
            @RequestParam(required = false) SaleType type,
            @RequestParam(required = false) SaleStatus status,
            @RequestParam(required = false) LocalDateTime dateFrom,
            @RequestParam(required = false) LocalDateTime dateTo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("[SaleController] GET /search - Filtros: affiliateId={}, type={}, status={}", affiliateId, type, status);

        SaleSearchCriteria saleSearchCriteria = SaleSearchCriteria.builder()
                .affiliateId(affiliateId)
                .type(type)
                .status(status)
                .dateFrom(dateFrom)
                .dateTo(dateTo)
                .build();
        Pageable pageable = PageRequest.of(page, size);

        Page<Sale> salePage = saleService.search(saleSearchCriteria, pageable);

        ApiResponse<Page<Sale>> apiResponse = ResponseBuilder.successMessage("Ventas encontradas", salePage);

        log.info("[SaleController] GET /search - Resultados encontrados: {}", salePage.getTotalElements());
        return ResponseEntity.ok(apiResponse);
    }
}
