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
    @Column(name = "lesson_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToOne(cascade = CascadeType.PERSIST)
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
