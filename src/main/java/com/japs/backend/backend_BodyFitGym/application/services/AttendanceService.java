package com.japs.backend.backend_BodyFitGym.application.services;

import com.japs.backend.backend_BodyFitGym.application.dto.AttendanceSearchCriteria;
import com.japs.backend.backend_BodyFitGym.domain.model.Attendance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AttendanceService {

    Attendance save(Long affiliateId);

    Attendance findById(Long id);

    Page<Attendance> search(AttendanceSearchCriteria attendanceSearchCriteria, Pageable pageable);
}
