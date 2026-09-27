package com.japs.backend.backend_BodyFitGym.domain.port.in.attendance;

import com.japs.backend.backend_BodyFitGym.application.dto.AttendanceSearchCriteria;
import com.japs.backend.backend_BodyFitGym.domain.model.Attendance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IRetrieveAttendanceUseCase {

    Attendance getAttendanceById(Long id);

    Page<Attendance> search(AttendanceSearchCriteria attendanceSearchCriteria, Pageable pageable);
}
