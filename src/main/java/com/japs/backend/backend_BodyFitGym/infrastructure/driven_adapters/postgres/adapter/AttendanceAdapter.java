package com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.adapter;

import com.japs.backend.backend_BodyFitGym.application.dto.AttendanceSearchCriteria;
import com.japs.backend.backend_BodyFitGym.domain.model.Attendance;
import com.japs.backend.backend_BodyFitGym.domain.port.out.AttendanceRepositoryPort;
import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.entities.AttendanceEntity;
import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.helper.AttendanceSpecification;
import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.mapper.AttendanceMapper;
import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.repository.AttendanceEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class AttendanceAdapter implements AttendanceRepositoryPort {

    private final AttendanceEntityRepository attendanceEntityRepository;

    @Override
    public Attendance save(Attendance attendance) {
        AttendanceEntity entity = AttendanceMapper.toEntity(attendance);
        return AttendanceMapper.toModel(attendanceEntityRepository.save(entity));
    }

    @Override
    public Optional<Attendance> findById(Long id) {
        return attendanceEntityRepository.findById(id)
                .map(AttendanceMapper::toModel);
    }

    @Override
    public boolean existsByAffiliateIdOnDate(Long affiliateId, LocalDate date) {
        return attendanceEntityRepository.existsByAffiliateIdAndAttendanceDateBetween(
                affiliateId, date.atStartOfDay(), date.atTime(LocalTime.MAX));
    }

    @Override
    public Page<Attendance> search(AttendanceSearchCriteria attendanceSearchCriteria, Pageable pageable) {

        Specification<AttendanceEntity> specification = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        if (attendanceSearchCriteria.getAffiliateId() != null) {
            specification = specification.and(AttendanceSpecification.affiliateIdEquals(attendanceSearchCriteria.getAffiliateId()));
        }

        if (attendanceSearchCriteria.getDateFrom() != null) {
            specification = specification.and(AttendanceSpecification.attendanceDateFrom(attendanceSearchCriteria.getDateFrom()));
        }

        if (attendanceSearchCriteria.getDateTo() != null) {
            specification = specification.and(AttendanceSpecification.attendanceDateTo(attendanceSearchCriteria.getDateTo()));
        }

        return attendanceEntityRepository.findAll(specification, pageable).map(AttendanceMapper::toModel);
    }
}
