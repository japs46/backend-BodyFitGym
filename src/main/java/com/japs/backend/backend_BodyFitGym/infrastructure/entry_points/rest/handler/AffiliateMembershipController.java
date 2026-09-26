package com.japs.backend.backend_BodyFitGym.infrastructure.entry_points.rest.handler;

import com.japs.backend.backend_BodyFitGym.application.dto.AffiliateMembershipSearchCriteria;
import com.japs.backend.backend_BodyFitGym.application.response.ApiResponse;
import com.japs.backend.backend_BodyFitGym.application.services.AffiliateMembershipService;
import com.japs.backend.backend_BodyFitGym.application.utils.ResponseBuilder;
import com.japs.backend.backend_BodyFitGym.domain.model.AffiliateMembership;
import com.japs.backend.backend_BodyFitGym.domain.model.SubscriptionStatus;
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
@RequestMapping("/api/affiliate-membership")
public class AffiliateMembershipController {

    private final AffiliateMembershipService affiliateMembershipService;

    @PostMapping("/save")
    public ResponseEntity<ApiResponse<AffiliateMembership>> save(@Valid @RequestBody AffiliateMembership affiliateMembershipRequest) {

        log.info("[AffiliateMembershipController] POST /save - Creando suscripción para afiliado: {}", affiliateMembershipRequest.getAffiliateId());

        AffiliateMembership affiliateMembership = affiliateMembershipService.save(affiliateMembershipRequest);
        ApiResponse<AffiliateMembership> apiResponse = ResponseBuilder.successMessage("Suscripción registrada exitosamente", affiliateMembership);

        log.info("[AffiliateMembershipController] POST /save - Suscripción creada con ID: {}", affiliateMembership.getId());
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<AffiliateMembership>> update(@PathVariable Long id, @Valid @RequestBody AffiliateMembership affiliateMembershipRequest) {

        log.info("[AffiliateMembershipController] PUT /update/{} - Actualizando suscripción", id);

        AffiliateMembership affiliateMembership = affiliateMembershipService.update(id, affiliateMembershipRequest);
        ApiResponse<AffiliateMembership> apiResponse = ResponseBuilder.successMessage("Suscripción actualizada exitosamente", affiliateMembership);

        log.info("[AffiliateMembershipController] PUT /update/{} - Suscripción actualizada", id);
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {

        log.info("[AffiliateMembershipController] DELETE /delete/{} - Eliminando suscripción", id);

        affiliateMembershipService.delete(id);
        ApiResponse<Void> apiResponse = ResponseBuilder.successMessage("Suscripción eliminada con éxito");

        log.info("[AffiliateMembershipController] DELETE /delete/{} - Suscripción eliminada correctamente", id);
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/freeze/{id}")
    public ResponseEntity<ApiResponse<AffiliateMembership>> freeze(@PathVariable Long id) {

        log.info("[AffiliateMembershipController] POST /freeze/{} - Congelando suscripción", id);

        AffiliateMembership affiliateMembership = affiliateMembershipService.freeze(id);
        ApiResponse<AffiliateMembership> apiResponse = ResponseBuilder.successMessage("Suscripción congelada exitosamente", affiliateMembership);

        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/unfreeze/{id}")
    public ResponseEntity<ApiResponse<AffiliateMembership>> unfreeze(@PathVariable Long id) {

        log.info("[AffiliateMembershipController] POST /unfreeze/{} - Reanudando suscripción", id);

        AffiliateMembership affiliateMembership = affiliateMembershipService.unfreeze(id);
        ApiResponse<AffiliateMembership> apiResponse = ResponseBuilder.successMessage("Suscripción reanudada exitosamente", affiliateMembership);

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<ApiResponse<AffiliateMembership>> findById(@PathVariable Long id) {

        log.info("[AffiliateMembershipController] GET /find-by-id/{} - Buscando suscripción", id);

        AffiliateMembership affiliateMembership = affiliateMembershipService.findById(id);
        ApiResponse<AffiliateMembership> apiResponse = ResponseBuilder.successMessage("Suscripción encontrada", affiliateMembership);

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<AffiliateMembership>>> search(
            @RequestParam(required = false) Long affiliateId,
            @RequestParam(required = false) Long membershipId,
            @RequestParam(required = false) SubscriptionStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("[AffiliateMembershipController] GET /search - Filtros: affiliateId={}, membershipId={}, status={}", affiliateId, membershipId, status);

        AffiliateMembershipSearchCriteria criteria = AffiliateMembershipSearchCriteria.builder()
                .affiliateId(affiliateId)
                .membershipId(membershipId)
                .status(status)
                .build();
        Pageable pageable = PageRequest.of(page, size);

        Page<AffiliateMembership> resultPage = affiliateMembershipService.search(criteria, pageable);

        ApiResponse<Page<AffiliateMembership>> apiResponse = ResponseBuilder.successMessage("Suscripciones encontradas", resultPage);

        log.info("[AffiliateMembershipController] GET /search - Resultados encontrados: {}", resultPage.getTotalElements());
        return ResponseEntity.ok(apiResponse);
    }
}
