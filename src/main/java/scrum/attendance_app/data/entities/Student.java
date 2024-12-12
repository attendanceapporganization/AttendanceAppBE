package scrum.attendance_app.data.entities;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "student_t")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class Student {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "registrationnumber", length = 20, nullable = false, unique = true)
    private String registrationNumber;

    @Column(name = "email", length = 100, nullable = false, unique = true)
    private String email;

    @EqualsAndHashCode.Exclude
    @Column(name = "password", length = 100, nullable = false)
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "ID")
    @EqualsAndHashCode.Exclude
    private Registry registry;

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Registration> registrations = new ArrayList<>();

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