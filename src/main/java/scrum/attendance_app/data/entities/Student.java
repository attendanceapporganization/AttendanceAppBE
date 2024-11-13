package scrum.attendance_app.data.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.time.LocalDate;
import java.util.UUID;


@Entity
@Table(name = "student_t")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Student {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "registrationnumber", length = 20, nullable = false, unique = true)
    private String registrationNumber;

    @Column(name = "email", length = 100, nullable = false, unique = true)
    private String email;

    @Column(name = "password", length = 100, nullable = false)
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "ID")
    private Registry registry;

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", registrationNumber='" + registrationNumber + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", registry=" + registry +
                '}';
    }
}