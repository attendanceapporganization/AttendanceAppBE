package scrum.attendance_app.mapper;

import org.mapstruct.Mapper;
import scrum.attendance_app.data.dto.CourseDTO;
import scrum.attendance_app.data.entities.Course;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    CourseDTO toDto(Course course);
    List<CourseDTO> toDtoList(List<Course> courses);
}
