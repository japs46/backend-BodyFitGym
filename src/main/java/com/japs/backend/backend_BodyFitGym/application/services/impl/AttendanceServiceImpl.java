package com.japs.backend.backend_BodyFitGym.application.services.impl;

import com.japs.backend.backend_BodyFitGym.application.dto.AttendanceSearchCriteria;
import com.japs.backend.backend_BodyFitGym.application.services.AttendanceService;
import com.japs.backend.backend_BodyFitGym.domain.model.Attendance;
import com.japs.backend.backend_BodyFitGym.domain.port.in.attendance.ICreateAttendanceUseCase;
import com.japs.backend.backend_BodyFitGym.domain.port.in.attendance.IRetrieveAttendanceUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AttendanceServiceImpl implements AttendanceService {

    private final ICreateAttendanceUseCase iCreateAttendanceUseCase;
    private final IRetrieveAttendanceUseCase iRetrieveAttendanceUseCase;

    @Override
    public Attendance save(Long affiliateId) {
        return iCreateAttendanceUseCase.createAttendance(affiliateId);
    }

    @Override
    public Attendance findById(Long id) {
        return iRetrieveAttendanceUseCase.getAttendanceById(id);
    }

    @Override
    public Page<Attendance> search(AttendanceSearchCriteria attendanceSearchCriteria, Pageable pageable) {
        return iRetrieveAttendanceUseCase.search(attendanceSearchCriteria, pageable);
    }
}
