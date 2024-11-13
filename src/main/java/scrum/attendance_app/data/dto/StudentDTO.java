package scrum.attendance_app.data.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class StudentDTO {

    private UUID id;

    @JsonProperty("registrationNumber")
    private String registrationNumber;

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
