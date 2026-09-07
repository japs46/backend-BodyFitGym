package com.japs.backend.backend_BodyFitGym.infrastructure.entry_points.rest.handler;

import com.japs.backend.backend_BodyFitGym.application.dto.MembershipSearchCriteria;
import com.japs.backend.backend_BodyFitGym.application.response.ApiResponse;
import com.japs.backend.backend_BodyFitGym.application.services.MembershipService;
import com.japs.backend.backend_BodyFitGym.application.utils.ResponseBuilder;
import com.japs.backend.backend_BodyFitGym.domain.model.DurationUnit;
import com.japs.backend.backend_BodyFitGym.domain.model.Membership;
import com.japs.backend.backend_BodyFitGym.domain.model.MembershipStatus;
import com.japs.backend.backend_BodyFitGym.domain.model.TrackingMode;
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
@RequestMapping("/api/membership")
public class MembershipController {

    private final MembershipService membershipService;

    @PostMapping("/save")
    public ResponseEntity<ApiResponse<Membership>> save(@Valid @RequestBody Membership membershipRequest) {

        log.info("[MembershipController] POST /save - Creando membresía: {}", membershipRequest.getName());

        Membership membership = membershipService.save(membershipRequest);
        ApiResponse<Membership> apiResponse = ResponseBuilder.successMessage("Membresía registrada exitosamente", membership);

        log.info("[MembershipController] POST /save - Membresía creada con ID: {}", membership.getId());
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<Membership>> update(@PathVariable Long id, @Valid @RequestBody Membership membershipRequest) {

        log.info("[MembershipController] PUT /update/{} - Actualizando membresía", id);

        Membership membership = membershipService.update(id, membershipRequest);
        ApiResponse<Membership> apiResponse = ResponseBuilder.successMessage("Membresía actualizada exitosamente", membership);

        log.info("[MembershipController] PUT /update/{} - Membresía actualizada", id);
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {

        log.info("[MembershipController] DELETE /delete/{} - Eliminando membresía", id);

        membershipService.delete(id);
        ApiResponse<Void> apiResponse = ResponseBuilder.successMessage("Membresía eliminada con éxito");

        log.info("[MembershipController] DELETE /delete/{} - Membresía eliminada correctamente", id);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<ApiResponse<Membership>> findById(@PathVariable Long id) {

        log.info("[MembershipController] GET /find-by-id/{} - Buscando membresía", id);

        Membership membership = membershipService.findById(id);
        ApiResponse<Membership> apiResponse = ResponseBuilder.successMessage("Membresía encontrada", membership);

        log.info("[MembershipController] GET /find-by-id/{} - Membresía encontrada: {}", id, membership.getName());
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<Membership>>> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) DurationUnit durationUnit,
            @RequestParam(required = false) TrackingMode trackingMode,
            @RequestParam(required = false) MembershipStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("[MembershipController] GET /search - Filtros: name={}, durationUnit={}, trackingMode={}, status={}",
                name, durationUnit, trackingMode, status);

        MembershipSearchCriteria membershipSearchCriteria = MembershipSearchCriteria.builder()
                .name(name)
                .durationUnit(durationUnit)
                .trackingMode(trackingMode)
                .status(status)
                .build();
        Pageable pageable = PageRequest.of(page, size);

        Page<Membership> membershipPage = membershipService.search(membershipSearchCriteria, pageable);

        ApiResponse<Page<Membership>> apiResponse = ResponseBuilder.successMessage("Membresías encontradas", membershipPage);

        log.info("[MembershipController] GET /search - Resultados encontrados: {}", membershipPage.getTotalElements());
        return ResponseEntity.ok(apiResponse);
    }
}
