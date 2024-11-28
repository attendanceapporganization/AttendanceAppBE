package scrum.attendance_app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import scrum.attendance_app.data.dto.StudentDTO;
import scrum.attendance_app.data.entities.Student;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    @Mapping(target = "firstname", source = "registry.firstname")
    @Mapping(target = "lastname", source = "registry.lastname")
    @Mapping(target = "dateOfBirth", source = "registry.dateOfBirth")
    StudentDTO toDTO(Student student);
    Student toEntity(StudentDTO studentDTO);
}
