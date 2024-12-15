package scrum.attendance_app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import scrum.attendance_app.data.dto.AttendanceDTO;
import scrum.attendance_app.data.entities.Attendance;

@Mapper(componentModel = "spring")
public interface AttendanceMapper {

    @Mapping(target = "studentId", source = "registration.student.id")
    @Mapping(target = "lessonId", source = "lesson.id")
    AttendanceDTO toDto(Attendance attendance);
}
