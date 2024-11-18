package scrum.attendance_app.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import scrum.attendance_app.data.entities.DigitCode;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LessonDTO {

    private UUID id;

    private UUID courseId;

    private DigitCode digitCode;
}
