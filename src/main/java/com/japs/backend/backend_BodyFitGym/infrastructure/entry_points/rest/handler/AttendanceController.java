package com.japs.backend.backend_BodyFitGym.infrastructure.entry_points.rest.handler;

import com.japs.backend.backend_BodyFitGym.application.dto.AttendanceSearchCriteria;
import com.japs.backend.backend_BodyFitGym.application.response.ApiResponse;
import com.japs.backend.backend_BodyFitGym.application.services.AttendanceService;
import com.japs.backend.backend_BodyFitGym.application.utils.ResponseBuilder;
import com.japs.backend.backend_BodyFitGym.domain.model.Attendance;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("/save")
    public ResponseEntity<ApiResponse<Attendance>> save(@Valid @RequestBody Attendance attendanceRequest) {

        log.info("[AttendanceController] POST /save - Registrando asistencia del afiliado: {}", attendanceRequest.getAffiliateId());

        Attendance attendance = attendanceService.save(attendanceRequest.getAffiliateId());
        ApiResponse<Attendance> apiResponse = ResponseBuilder.successMessage("Asistencia registrada exitosamente", attendance);

        log.info("[AttendanceController] POST /save - Asistencia creada con ID: {}", attendance.getId());
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<ApiResponse<Attendance>> findById(@PathVariable Long id) {

        log.info("[AttendanceController] GET /find-by-id/{} - Buscando asistencia", id);

        Attendance attendance = attendanceService.findById(id);
        ApiResponse<Attendance> apiResponse = ResponseBuilder.successMessage("Asistencia encontrada", attendance);

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<Attendance>>> search(
            @RequestParam(required = false) Long affiliateId,
            @RequestParam(required = false) LocalDate dateFrom,
            @RequestParam(required = false) LocalDate dateTo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("[AttendanceController] GET /search - Filtros: affiliateId={}, dateFrom={}, dateTo={}", affiliateId, dateFrom, dateTo);

        AttendanceSearchCriteria criteria = AttendanceSearchCriteria.builder()
                .affiliateId(affiliateId)
                .dateFrom(dateFrom)
                .dateTo(dateTo)
                .build();
        Pageable pageable = PageRequest.of(page, size);

        Page<Attendance> resultPage = attendanceService.search(criteria, pageable);

        ApiResponse<Page<Attendance>> apiResponse = ResponseBuilder.successMessage("Asistencias encontradas", resultPage);

        log.info("[AttendanceController] GET /search - Resultados encontrados: {}", resultPage.getTotalElements());
        return ResponseEntity.ok(apiResponse);
    }
}
