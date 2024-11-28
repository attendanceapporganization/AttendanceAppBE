package scrum.attendance_app.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
@Builder
@Data
public class CourseDTO {

    private UUID id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("professorOwner")
    private String professorOwner;

    @JsonProperty("code")
    private String code;

    @JsonProperty("idProfessor")
    private UUID idProfessor;

    public CourseDTO() {}
}
