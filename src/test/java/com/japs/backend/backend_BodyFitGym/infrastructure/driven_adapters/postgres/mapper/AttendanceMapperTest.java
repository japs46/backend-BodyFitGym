package com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.mapper;

import com.japs.backend.backend_BodyFitGym.domain.model.Attendance;
import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.entities.AttendanceEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class AttendanceMapperTest {

    @Test
    void toEntity_devuelveNullConEntradaNull() {
        assertThat(AttendanceMapper.toEntity(null)).isNull();
    }

    @Test
    void toModel_devuelveNullConEntradaNull() {
        assertThat(AttendanceMapper.toModel(null)).isNull();
    }

    @Test
    void mapeoIdaYVuelta_conservaTodosLosCampos() {
        Attendance original = Attendance.builder()
                .id(1L)
                .affiliateId(4L)
                .affiliateMembershipId(10L)
                .attendanceDate(LocalDateTime.of(2026, 9, 26, 8, 30))
                .build();

        Attendance result = AttendanceMapper.toModel(AttendanceMapper.toEntity(original));

        assertThat(result).usingRecursiveComparison().isEqualTo(original);
    }

    @Test
    void toEntity_mapeaLosCamposClave() {
        Attendance attendance = Attendance.builder()
                .id(2L)
                .affiliateId(7L)
                .affiliateMembershipId(3L)
                .build();

        AttendanceEntity entity = AttendanceMapper.toEntity(attendance);

        assertThat(entity.getId()).isEqualTo(2L);
        assertThat(entity.getAffiliateId()).isEqualTo(7L);
        assertThat(entity.getAffiliateMembershipId()).isEqualTo(3L);
    }
}
