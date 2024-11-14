package scrum.attendance_app.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LessonDTO {

    private int id;

    private String timeInterval;

    private Date date;

    private int courseId;

    private int digitCodeId;
}
