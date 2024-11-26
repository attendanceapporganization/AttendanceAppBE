package scrum.attendance_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import scrum.attendance_app.data.entities.Course;
import scrum.attendance_app.data.entities.Professor;
import scrum.attendance_app.data.entities.Student;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {
    @Override
    <S extends Student> S save(S entity);

    Optional<Student> findByEmail(String email);

    @Query("SELECT s FROM Student s WHERE s.registrationNumber = :registrationNumber")
    Student findByRegistrationNumber(@Param("registrationNumber") String registrationNumber);
}
