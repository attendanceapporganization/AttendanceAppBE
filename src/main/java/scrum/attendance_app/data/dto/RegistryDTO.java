package scrum.attendance_app.data.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class RegistryDTO {

    private Long id;
    private String firstname;
    private String lastname;
    private LocalDate dateOfBirth;

    public RegistryDTO(Long id, String firstname, String lastname, LocalDate dateOfBirth) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.dateOfBirth = dateOfBirth;
    }
}
