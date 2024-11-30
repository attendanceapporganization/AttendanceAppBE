package scrum.attendance_app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import scrum.attendance_app.data.dto.LessonDTO;
import scrum.attendance_app.data.entities.DigitCode;
import scrum.attendance_app.data.entities.Lesson;

@Mapper(componentModel = "spring")
public interface LessonMapper {
    @Mapping(target = "digitCode", source = "digitCode", qualifiedByName = "digitCodeToString")
    @Mapping(target = "courseId", source = "course.id")
    LessonDTO toDto(Lesson lessonEntity);
    //Lesson toEntity(LessonDTO lessonDTO);

    @Named("digitCodeToString")
    default String digitCodeToString(DigitCode digitCode) {
        return digitCode.formattedValue();
    }
}
