package scrum.attendance_app.data.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "registrations_t")
public class Registration {

    @Id
    @Column(name = "id")
    //@GeneratedValue(strategy = GenerationType.UUID)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", referencedColumnName = "ID", nullable = false)
    private Student student;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "registrationdate", nullable = false)
    private LocalDate registrationDate;

    public Registration() {}

    public Registration(Student student, Course course, LocalDate registrationDate) {
        this.student = student;
        this.course = course;
        this.registrationDate = registrationDate;
    }

}