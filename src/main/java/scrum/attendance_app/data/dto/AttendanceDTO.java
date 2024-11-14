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

    private UUID id;
    private UUID registrationId;

    @JsonProperty("attendanceDate")
    private LocalDate attendanceDate;

    @JsonProperty("isPresent")
    private Boolean isPresent;

    @JsonProperty("hours")
    private String hours;
}
