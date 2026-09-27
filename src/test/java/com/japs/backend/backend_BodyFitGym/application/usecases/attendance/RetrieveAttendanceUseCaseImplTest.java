package com.japs.backend.backend_BodyFitGym.application.usecases.attendance;

import com.japs.backend.backend_BodyFitGym.application.dto.AttendanceSearchCriteria;
import com.japs.backend.backend_BodyFitGym.domain.model.Attendance;
import com.japs.backend.backend_BodyFitGym.domain.port.out.AttendanceRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RetrieveAttendanceUseCaseImplTest {

    @Mock
    private AttendanceRepositoryPort attendanceRepositoryPort;

    @InjectMocks
    private RetrieveAttendanceUseCaseImpl retrieveAttendanceUseCase;

    @Test
    void getAttendanceById_lanzaCuandoIdEsNull() {
        assertThatThrownBy(() -> retrieveAttendanceUseCase.getAttendanceById(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void getAttendanceById_lanzaCuandoIdNoEsPositivo() {
        assertThatThrownBy(() -> retrieveAttendanceUseCase.getAttendanceById(0L))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> retrieveAttendanceUseCase.getAttendanceById(-1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void getAttendanceById_lanzaNoSuchElementCuandoNoExiste() {
        when(attendanceRepositoryPort.findById(50L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> retrieveAttendanceUseCase.getAttendanceById(50L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("50");
    }

    @Test
    void getAttendanceById_retornaCuandoExiste() {
        Attendance attendance = Attendance.builder().id(1L).affiliateId(4L).build();
        when(attendanceRepositoryPort.findById(1L)).thenReturn(Optional.of(attendance));

        assertThat(retrieveAttendanceUseCase.getAttendanceById(1L)).isSameAs(attendance);
    }

    @Test
    void search_delegaAlPuerto() {
        AttendanceSearchCriteria criteria = AttendanceSearchCriteria.builder().affiliateId(4L).build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Attendance> page = new PageImpl<>(List.of(Attendance.builder().id(1L).build()));
        when(attendanceRepositoryPort.search(criteria, pageable)).thenReturn(page);

        Page<Attendance> result = retrieveAttendanceUseCase.search(criteria, pageable);

        assertThat(result).isSameAs(page);
    }
}
