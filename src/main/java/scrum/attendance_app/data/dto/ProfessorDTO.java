package scrum.attendance_app.data.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@Builder
@Data
public class ProfessorDTO {

    private UUID id;

    @JsonProperty("email")
    private String email;

    @JsonProperty("firstname")
    private String firstname;

    @JsonProperty("lastname")
    private String lastname;

    @JsonProperty("dateOfBirth")
    private LocalDate dateOfBirth;

    @JsonProperty("password")
    private String password;
}
