package com.japs.backend.backend_BodyFitGym.domain.port.out;

import com.japs.backend.backend_BodyFitGym.application.dto.AttendanceSearchCriteria;
import com.japs.backend.backend_BodyFitGym.domain.model.Attendance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;

public interface AttendanceRepositoryPort {

    Attendance save(Attendance attendance);

    Optional<Attendance> findById(Long id);

    boolean existsByAffiliateIdOnDate(Long affiliateId, LocalDate date);

    Page<Attendance> search(AttendanceSearchCriteria attendanceSearchCriteria, Pageable pageable);
}
