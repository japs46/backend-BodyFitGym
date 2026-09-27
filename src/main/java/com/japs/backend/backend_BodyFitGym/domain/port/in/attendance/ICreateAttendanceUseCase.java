package com.japs.backend.backend_BodyFitGym.domain.port.in.attendance;

import com.japs.backend.backend_BodyFitGym.domain.model.Attendance;

public interface ICreateAttendanceUseCase {

    Attendance createAttendance(Long affiliateId);
}
