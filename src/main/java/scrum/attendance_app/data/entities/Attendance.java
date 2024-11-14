package scrum.attendance_app.data.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "attendance_t")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Attendance {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "registration_id", nullable = false)
    private Registration registration;

    @Column(name = "attendancedate", nullable = false)
    private java.time.LocalDate attendanceDate;

    @Column(name = "ispresent", nullable = false)
    private Boolean isPresent;

    @Column(name = "hours", length = 20)
    private String hours;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "digit_code_id", nullable = false)
    private DigitCode digitCode;

    @Override
    public String toString() {
        return "Attendance{" +
                "id=" + id +
//                ", registration=" + registration +
                ", attendanceDate=" + attendanceDate +
                ", isPresent=" + isPresent +
                ", hours='" + hours + '\'' +
                '}';
    }
}
