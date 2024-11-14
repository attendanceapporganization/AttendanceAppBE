package scrum.attendance_app.data.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "lesson_t")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Lesson {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "time_interval", length = 100, nullable = false)
    private String time_interval;

    @Column(name = "date", nullable = false)
    private Date date;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "digit_code_id")
    private DigitCode digitCode;

    @Override
    public String toString() {
        return "Lesson{" +
                "id=" + id +
                ", professorOwner=" + /*professorOwner*/ +
                '}';
    }
}
