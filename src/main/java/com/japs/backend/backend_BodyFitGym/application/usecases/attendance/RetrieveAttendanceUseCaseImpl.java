package com.japs.backend.backend_BodyFitGym.application.usecases.attendance;

import com.japs.backend.backend_BodyFitGym.application.dto.AttendanceSearchCriteria;
import com.japs.backend.backend_BodyFitGym.domain.model.Attendance;
import com.japs.backend.backend_BodyFitGym.domain.port.in.attendance.IRetrieveAttendanceUseCase;
import com.japs.backend.backend_BodyFitGym.domain.port.out.AttendanceRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Component
public class RetrieveAttendanceUseCaseImpl implements IRetrieveAttendanceUseCase {

    private final AttendanceRepositoryPort attendanceRepositoryPort;

    @Override
    public Attendance getAttendanceById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID de la asistencia es obligatorio.");
        }

        if (id <= 0) {
            throw new IllegalArgumentException("El ID de la asistencia debe ser un número positivo.");
        }

        return attendanceRepositoryPort.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No existe ninguna asistencia con el ID: " + id));
    }

    @Override
    public Page<Attendance> search(AttendanceSearchCriteria attendanceSearchCriteria, Pageable pageable) {
        return attendanceRepositoryPort.search(attendanceSearchCriteria, pageable);
    }
}
