package com.japs.backend.backend_BodyFitGym.infrastructure.entry_points.rest.handler;

import com.japs.backend.backend_BodyFitGym.application.dto.AffiliateSearchCriteria;
import com.japs.backend.backend_BodyFitGym.application.response.ApiResponse;
import com.japs.backend.backend_BodyFitGym.application.services.AffiliateService;
import com.japs.backend.backend_BodyFitGym.application.utils.ResponseBuilder;
import com.japs.backend.backend_BodyFitGym.domain.model.Affiliate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/affiliate")
public class AffiliateController {

    private final AffiliateService affiliateService;

    @PostMapping("/save")
    public ResponseEntity<ApiResponse<Affiliate>> save(@Valid @RequestBody Affiliate affiliateRequest) {

        log.info("[AffiliateController] POST /save - Creando afiliado: {}", affiliateRequest.getIdentification());

        Affiliate affiliate = affiliateService.save(affiliateRequest);
        ApiResponse<Affiliate> apiResponse = ResponseBuilder.successMessage("Afiliado registrado exitosamente", affiliate);

        log.info("[AffiliateController] POST /save - Afiliado creado con ID: {}", affiliate.getId());
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<Affiliate>> update(@PathVariable Long id, @Valid @RequestBody Affiliate affiliateRequest) {

        log.info("[AffiliateController] PUT /update/{} - Actualizando afiliado", id);

        Affiliate affiliate = affiliateService.update(id, affiliateRequest);
        ApiResponse<Affiliate> apiResponse = ResponseBuilder.successMessage("Afiliado actualizado exitosamente", affiliate);

        log.info("[AffiliateController] PUT /update/{} - Afiliado actualizado", id);
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {

        log.info("[AffiliateController] DELETE /delete/{} - Eliminando afiliado", id);

        affiliateService.delete(id);
        ApiResponse<Void> apiResponse = ResponseBuilder.successMessage("Afiliado eliminado con éxito");

        log.info("[AffiliateController] DELETE /delete/{} - Afiliado eliminado correctamente", id);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<ApiResponse<Affiliate>> findById(@PathVariable Long id) {

        log.info("[AffiliateController] GET /find-by-id/{} - Buscando afiliado", id);

        Affiliate affiliate = affiliateService.findById(id);
        ApiResponse<Affiliate> apiResponse = ResponseBuilder.successMessage("Afiliado encontrado", affiliate);

        log.info("[AffiliateController] GET /find-by-id/{} - Afiliado encontrado: {}", id, affiliate.getIdentification());
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<Affiliate>>> search(
            @RequestParam(required = false) String identification,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("[AffiliateController] GET /search - Filtros: identification={}, name={}, status={}", identification, name, status);

        AffiliateSearchCriteria affiliateSearchCriteria = AffiliateSearchCriteria.builder()
                .identification(identification)
                .name(name)
                .status(status)
                .build();
        Pageable pageable = PageRequest.of(page, size);

        Page<Affiliate> affiliatePage = affiliateService.search(affiliateSearchCriteria, pageable);

        ApiResponse<Page<Affiliate>> apiResponse = ResponseBuilder.successMessage("Afiliados encontrados", affiliatePage);

        log.info("[AffiliateController] GET /search - Resultados encontrados: {}", affiliatePage.getTotalElements());
        return ResponseEntity.ok(apiResponse);
    }
}
