package scrum.attendance_app.data.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttendanceDTO {

    @JsonProperty("attendanceId")
    private UUID id;
    @JsonProperty("studentId")
    private UUID studentId;
    @JsonProperty("lessonId")
    private UUID lessonId;
}
