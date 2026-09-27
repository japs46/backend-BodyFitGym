package com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.mapper;

import com.japs.backend.backend_BodyFitGym.domain.model.Attendance;
import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.entities.AttendanceEntity;

public class AttendanceMapper {

    public static AttendanceEntity toEntity(Attendance attendance) {

        if (attendance == null)
            return null;

        return AttendanceEntity.builder()
                .id(attendance.getId())
                .affiliateId(attendance.getAffiliateId())
                .affiliateMembershipId(attendance.getAffiliateMembershipId())
                .attendanceDate(attendance.getAttendanceDate())
                .build();
    }

    public static Attendance toModel(AttendanceEntity attendanceEntity) {

        if (attendanceEntity == null)
            return null;

        return Attendance.builder()
                .id(attendanceEntity.getId())
                .affiliateId(attendanceEntity.getAffiliateId())
                .affiliateMembershipId(attendanceEntity.getAffiliateMembershipId())
                .attendanceDate(attendanceEntity.getAttendanceDate())
                .build();
    }
}
